package com.dg.tomcat.util;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: DG
 * @Date: 2021/11/14 21:05
 * @Description: 迷你浏览器，获取HTTP内容与HTTP响应头信息
 */
@Slf4j
public class MiniBrowser {

    public static void main(String[] args) {
        String url = "http://static.how2j.cn/diytomcat.html";
        String contentString = getContentString(url, false);
        System.out.println(contentString);
        String httpString = getHttpString(url, false);
        System.out.println(httpString);
    }

    public static byte[] getContentBytes(String url) {
        return getContentBytes(url, false);
    }


    public static String getContentString(String url) {
        return getContentString(url, false);
    }

    public static String getContentString(String url, boolean gzip) {
        byte[] result = getContentBytes(url, gzip);
        if (result == null) {
            return null;
        }
        try {
            return new String(result, "utf-8").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getContentBytes(String url, boolean gzip) {
        byte[] response = getHttpBytes(url, gzip);
        byte[] doubleReturn = "\r\n\r\n".getBytes();

        int pos = -1;
        for (int i = 0; i < response.length - doubleReturn.length; i++) {
            byte[] temp = Arrays.copyOfRange(response, i, i + doubleReturn.length);

            if (Arrays.equals(temp, doubleReturn)) {
                pos = i;
                break;
            }
        }
        if (pos == -1) {
            return null;
        }

        pos += doubleReturn.length;

        byte[] result = Arrays.copyOfRange(response, pos, response.length);
        return result;
    }

    public static String getHttpString(String url, boolean gzip) {
        byte[] bytes = getHttpBytes(url, gzip);
        return new String(bytes).trim();
    }

    public static String getHttpString(String url) {
        return getHttpString(url, false);
    }

    public static byte[] getHttpBytes(String url, boolean gzip) {
        byte[] result = null;

        try {
            URL u = new URL(url);
            Socket client = new Socket();
            int port = u.getPort();
            if (port == -1)
                port = 80;
            InetSocketAddress inetSocketAddress = new InetSocketAddress(u.getHost(), port);
            client.connect(inetSocketAddress, 1000);
            Map<String, String> requestHeaders = new HashMap<>();

            requestHeaders.put("Host", u.getHost() + ":" + port);
            requestHeaders.put("Accept", "text/html");
            requestHeaders.put("Connection", "close");
            requestHeaders.put("User-Agent", "how2j mini brower / java1.8");

            if (gzip)
                requestHeaders.put("Accept-Encoding", "gzip");

            String path = u.getPath();
            if (path.length() == 0)
                path = "/";

            String firstLine = "GET " + path + " HTTP/1.1\r\n";

            StringBuffer httpRequestString = new StringBuffer();
            httpRequestString.append(firstLine);
            Set<String> headers = requestHeaders.keySet();
            for (String header : headers) {
                String headerLine = header + ":" + requestHeaders.get(header) + "\r\n";
                httpRequestString.append(headerLine);
            }

            PrintWriter pWriter = new PrintWriter(client.getOutputStream(), true);
            pWriter.println(httpRequestString);
            InputStream is = client.getInputStream();

            int buffer_size = 1024;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte buffer[] = new byte[buffer_size];
            while (true) {
                int length = is.read(buffer);
                if (-1 == length)
                    break;
                baos.write(buffer, 0, length);
                if (length != buffer_size)
                    break;
            }

            result = baos.toByteArray();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                result = e.toString().getBytes("utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }

        return result;
    }

}