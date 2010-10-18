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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletResponse;

import org.apache.catalina.common.util.CookieTools;
import org.apache.catalina.common.util.DateTool;

/*
 * HTTP 1.1 protocol header's string
 * =====================================================
 * 
 * HTTP/1.1 200 OK
 * 
 * Server: Apache-Coyote/1.1
 * 
 * ETag: W/"7857-1216663272000"
 * 
 * Last-Modified: Mon, 21 Jul 2008 18:01:12 GMT
 * 
 * Content-Type: text/html
 * 
 * Content-Length: 7857
 * 
 * Date: Fri, 22 Jan 2010 08:08:14 GMT
 * 
 * Connection: close
 * 
 * Set-Cookie: key=haha; expires=Sun, 31-Dec-2006 16:00:00 GMT; path=/
 * =====================================================
 */
/**
 * @author WenQiang Wu
 * @version Jan 15, 2010
 */
public class HttpServletResponse implements ServletResponse {

    private String protocol;
    private Properties headers;
    private List<Cookie> allCookies;
    private String description;
    private int states;

    private OutputStream out;

    /**
     * init
     * 
     * @param out
     */
    public HttpServletResponse(OutputStream out) {
        this.out = out;

        this.headers = new Properties();
        this.allCookies = new LinkedList<Cookie>();
    }

    /**
     * 响应浏览器的请求
     * 
     * @throws IOException
     */
    public void writeToClient() throws IOException {
        StringBuffer protocol = new StringBuffer();

        // 相应头格式的第一行
        protocol.append(this.protocol + " " + this.states + " "
                + this.description + "\r\n");

        // 相应头格式的其他(不)
        Enumeration<?> headerKeys = this.headers.keys();
        while (headerKeys.hasMoreElements()) {
            String header = (String) headerKeys.nextElement();
            String headerValue = this.headers.getProperty(header);
            protocol.append(header + ": " + headerValue + "\r\n");
        }

        // 解析cookies
        parseCookies(protocol);

        // 返回相应协议
        protocol.append("\r\n");

        System.out
                .println("================Http response protocol====================");
        System.out.println("request protocol : " + protocol);
        System.out
                .println("==========================================================");

        this.out.write(protocol.toString().getBytes());
    }

    /**
     * 处理Cookie的相应头
     * 
     * Cookie相应头格式:
     * 
     * Set-Cookie: key=value; expires=Sun, 31-Dec-2006 16:00:00 GMT; path=/
     */
    private void parseCookies(StringBuffer protocol) {

        for (int i = 0; i < allCookies.size(); i++) {
            Cookie cookie = allCookies.get(i);

            //
            String strCookieInProtocol = CookieTools
                    .processProtocolHeader(cookie);

            System.out.println(this.getClass().getName()
                    + " : parseCookies method =====>" + strCookieInProtocol);

            protocol.append(strCookieInProtocol + "\r\n");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletResponse#getProtocol()
     */
    public String getProtocol() {
        return protocol;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletResponse#setProtocol(java.lang.String)
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletResponse#getDescription()
     */
    public String getDescription() {
        return description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletResponse#setDescription(java.lang.String)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletResponse#getStates()
     */
    public int getStates() {
        return states;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletResponse#setStates(int)
     */
    public void setStates(int states) {
        this.states = states;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletResponse#getHeader(java.lang.String)
     */
    public String getHeader(String headerName) {
        return headers.getProperty(headerName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletResponse#setHeader(java.lang.String,
     *      java.lang.String)
     */
    public void setHeader(String headerName, String headerValue) {
        this.headers.setProperty(headerName, headerValue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletResponse#getOutputStream()
     */
    public OutputStream getOutputStream() {
        return out;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
     */
    public void setContentType(String contentType) {
        this.headers.setProperty("Content-Type", contentType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletResponse#getContentType()
     */
    public String getContentType() {
        return this.headers.getProperty("Content-Type");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletResponse#getContentLength()
     */
    public long getContentLength() {
        String contentLength = this.headers.getProperty("Content-Length");
        if (null != contentLength) {
            return Long.parseLong(contentLength);
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletResponse#setContentLength(long)
     */
    public void setContentLength(long contentLength) {
        this.headers.setProperty("Content-Length", contentLength + "");
    }

    /**
     * 添加COOKIE
     * 
     * @param cookie
     */
    public void addCookie(Cookie cookie) {
        this.allCookies.add(cookie);
    }

    /**
     * 
     * @param status
     * @author skyqiang
     */
    public void setConnection(String status) {
        this.headers.setProperty("Connection", status);
    }

    /**
     * 
     * @return
     * @author skyqiang
     */
    public String setConnection() {
        return this.headers.getProperty("Connection");
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#getDateHeader()
     */
    public String getDateHeader() {
        return this.headers.getProperty("Date");
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#setDateHeader(java.util.Date)
     */
    public void setDateHeader(Date date) {
        this.headers.setProperty("Date", DateTool.httpDateHeader.format(date));
    }

}
