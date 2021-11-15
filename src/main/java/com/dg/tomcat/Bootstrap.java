package com.dg.tomcat;

import cn.hutool.core.net.NetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: DG
 * @Date: 2021/11/5 00:54
 * @Description:
 */
public class Bootstrap {

    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(Bootstrap.class.getName());

        try {
            // 设置端口，默认端口 7777
            int port = 7777;

            // 判断端口是否被占用
            if (!NetUtil.isUsableLocalPort(port)) {
                System.out.println();
                logger.error(port +" 端口已被占用，无法启动！");
                return;
            }
            ServerSocket ss = new ServerSocket(port);

            while (true) {
                Socket s = ss.accept();
                InputStream is = s.getInputStream();
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                is.read(buffer);
                String requestString = new String(buffer, "utf-8");
                logger.info("浏览器的输入信息： \r\n" + requestString);

                OutputStream os = s.getOutputStream();
                String responseHead = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n\r\n";
                String responseContent = "Hello DIY Tomcat from DG";
                String responseString = responseHead + responseContent;
                os.write(responseString.getBytes());
                os.flush();
                s.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
