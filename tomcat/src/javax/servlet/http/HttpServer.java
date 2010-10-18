// -------------------------------------------------------------------------
// Copyright (c) 2000-2010 Ufinity. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Ufinity
//
// Original author:WenQiang Wu
//
// -------------------------------------------------------------------------
// UFINITY MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
// THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
// TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE, OR NON-INFRINGEMENT. UFINITY SHALL NOT BE
// LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
// MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
//
// THIS SOFTWARE IS NOT DESIGNED OR INTENDED FOR USE OR RESALE AS ON-LINE
// CONTROL EQUIPMENT IN HAZARDOUS ENVIRONMENTS REQUIRING FAIL-SAFE
// PERFORMANCE, SUCH AS IN THE OPERATION OF NUCLEAR FACILITIES, AIRCRAFT
// NAVIGATION OR COMMUNICATION SYSTEMS, AIR TRAFFIC CONTROL, DIRECT LIFE
// SUPPORT MACHINES, OR WEAPONS SYSTEMS, IN WHICH THE FAILURE OF THE
// SOFTWARE COULD LEAD DIRECTLY TO DEATH, PERSONAL INJURY, OR SEVERE
// PHYSICAL OR ENVIRONMENTAL DAMAGE ("HIGH RISK ACTIVITIES"). UFINITY
// SPECIFICALLY DISCLAIMS ANY EXPRESS OR IMPLIED WARRANTY OF FITNESS FOR
// HIGH RISK ACTIVITIES.
// -------------------------------------------------------------------------
package javax.servlet.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import javax.servlet.Configuration;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;

import org.apache.catalina.Server;

/**
 * @author WenQiang Wu
 * @version Jan 15, 2010
 */
public class HttpServer extends Server {

    private static final String DEFAULT_PORT_STR = "8080";

    private InputStream in;
    private OutputStream out;

    /**
     * 设置服务器监听的端口
     */
    public HttpServer() {
        String strPort = Configuration.getInstance().getConfigItem(
                Configuration.PORT);
        if (null == strPort) {
            strPort = DEFAULT_PORT_STR;
        }
        setPort(Integer.parseInt(strPort));
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.tomcat.server.Server#process(java.net.Socket)
     */
    @Override
    protected void process(Socket socket) throws Exception {
        in = socket.getInputStream();
        out = socket.getOutputStream();

        // init request and response objects
        HttpServletRequest request = new HttpServletRequest(in);
        HttpServletResponse response = new HttpServletResponse(out);
        response.setProtocol("HTTP/1.1");

        String path = request.getPath();
        System.out.println("HttpServet[path] : " + path);

        ServletConfig sc = Configuration.getInstance().getServletConfig(path);
        // 处理动态请求的数据
        if (null != sc) {
            String className = sc.getClazz();
            Servlet servlet = (Servlet) Class.forName(className).newInstance();
            servlet.init(sc);
            servlet.service(request, response);
            servlet.destory();
        } else {
            // 处理静态文本的请求
            processStaticRequest(request, response);
        }
    }

    /**
     * 处理静态文本的请求
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    private void processStaticRequest(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String filePath = request.getAbsolutePath();
        System.out.println("static request file path : " + filePath);

        File file = new File(filePath);

        if (!file.exists()) {
            // 如果请求的静态页面没有找到
            System.out.println("404 ERROR!File not found!");

            response.setStates(404);
            response.setContentType("text/html");
            response.setDateHeader(new Date());
            response.setDescription("File not found!");
            file = new File("E:/TOMCAT/HttpServer/common/404.html");
            response.setContentLength(file.length());
        } else {
            // 找到请求页面
            System.out.println("Response protocol : 200 OK");
            response.setStates(200);
            response.setDescription("OK");
            response.setDateHeader(new Date());
            response.setContentLength(file.length());
            
            String fileNameExtension = file.getName().substring(
                    file.getName().lastIndexOf(".") + 1);
            String mime = Configuration.getInstance().getConfigItem(
                    Configuration.MIME_PRFEX + fileNameExtension);
            if (null != mime) {
                response.setContentType(mime);
                System.out.println("Response file's content type : " + mime);
            }
        }
        
        response.setConnection("close");
        // 输出相应头
        response.writeToClient();
        System.out.println("Write http resposne protocol to client!");
        // 输出响应的静态文件
        writeStaticFile(response.getOutputStream(), file);
        System.out.println("Send file to client!");
    }

    /**
     * 输出静态文件
     * 
     * @param out
     * @param file
     */
    private void writeStaticFile(OutputStream out, File file) {
        FileInputStream in = null;

        try {
            in = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int readLength = 0;

            while ((readLength = in.read(buffer)) != -1) {
                out.write(buffer, 0, readLength);
            }
        } catch (FileNotFoundException e) {
            System.out
                    .println("File not found when execute write static file method!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
