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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.Configuration;
import javax.servlet.ServletRequest;

/*
 * HTTP 1.1 protocol header's string
 * =====================================================
 * 
 * POST /servlet/default.jsp?userName=sky HTTP/1.1
 * 
 * Accept: text/plain; text/html
 * 
 * Accept-Language: en-gb
 * 
 * Connection: Keep-Alive
 * 
 * Host: localhost
 * 
 * Referer: http://localhost:8080/tomcat/index.jsp
 * 
 * User-Agent: Mozilla/4.0 (compatible; MSIE 4.01; Windows 98)
 * 
 * Content-Length: 33
 * 
 * Content-Type: text/html
 * 
 * Accept-Encoding: gzip, deflate
 * 
 * Cookie: name1=value1; name2=value2;
 * 
 * userName=abc&password=abc
 * =====================================================
 */
/**
 * @author WenQiang Wu
 * @version Jan 15, 2010
 */
public class HttpServletRequest implements ServletRequest {

    private InputStream is;

    private String path;
    private String method;
    private String protocol;

    private Properties headers;
    private Map<String, Object> parameters;
    private Map<String, Cookie> cookies;
    private Map<String, String> attributes;

    /**
     * 
     * @param is
     * @throws Exception
     */
    public HttpServletRequest(InputStream is) throws Exception {
        // init
        this.is = is;

        this.headers = new Properties();
        this.parameters = new HashMap<String, Object>();
        this.cookies = new HashMap<String, Cookie>();
        this.attributes = new HashMap<String, String>();

        // process reqeust headers string
        readRequestProtocol();
    }

    /**
     * 读取http协议头,http协议头上以[\r\n\r\n]结尾的.
     * 
     * @throws Exception
     */
    private void readRequestProtocol() throws Exception {
        int data = -1;
        StringBuffer protocol = new StringBuffer();

        // 读取协议头
        while ((data = is.read()) != -1) {
            protocol.append((char) data);
            if (protocol.indexOf("\r\n\r\n") >= 0) {
                break;
            }
        }

        System.out
                .println("================Http request protocol====================");
        System.out.println("request protocol : " + protocol);
        System.out
                .println("=========================================================");

        // 处理协议头
        parseRequestProtocol(protocol.toString());

        // 以POST请求的参数协议头是在协议头结束以后(\r\n\r\n)开始的.解析方式同GET请求
        long contentLength = getContentLength();
        if (contentLength > 0 && this.method.equalsIgnoreCase("POST")) {
            byte[] postParamData = new byte[(int) contentLength];
            String strPostParamData = new String(postParamData);

            // 处理参数协议
            parseParameters(strPostParamData);
        }
    }

    /**
     * 处理得到的HTTP协议头
     * 
     * @param protocol
     */
    private void parseRequestProtocol(String protocol) {
        protocol = protocol.replaceAll("\r\n\r\n", "");
        String[] lines = protocol.split("\r\n");

        // 处理协议头的第一行
        parseTopLine(lines[0]);

        // 处理协议头的其他行
        for (int i = 1; i < lines.length; i++) {
            parseOtherLine(lines[i]);
        }
    }

    /**
     * 处理协议头的第一行(POST /servlet/default.jsp?userName=sky HTTP/1.1) 协议头的第一行是一空格分割
     * 
     * @param topLine
     */
    private void parseTopLine(String topLine) {
        String[] values = topLine.split(" ");
        this.method = values[0];
        this.path = values[1];

        int position = this.path.indexOf("?");
        // 判断URL中是否带有参数
        if (position >= 0) {
            String strParameter = this.path.substring(position + 1);
            this.path = this.path.substring(0, position);

            // 处理带有参数的URL
            parseParameters(strParameter);
        }
        this.protocol = values[2];
    }

    /**
     * 处理除了第一行以外的其他协议头,协议头的格式是以[: ]进行风格
     * 
     * @param otherLine
     */
    private void parseOtherLine(String otherLine) {
        String[] header = otherLine.split(": ");
        this.headers.setProperty(header[0], header[1]);

        if ("Cookie".equals(header[0])) {
            String strCookie = header[1];
            // 处理Cookie的协议头
            parseCookies(strCookie);
        }

    }

    /**
     * 处理Cookie的http请求协议头
     * 
     * @param strCookie
     */
    private void parseCookies(String strCookie) {
        String[] cookies = strCookie.split(": ");

        for (int i = 0; i < cookies.length; i++) {
            String[] cookieArray = cookies[i].split("=");
            Cookie cookie = new Cookie(cookieArray[0], cookieArray[1]);
            this.cookies.put(cookieArray[0], cookie);
        }
    }

    /**
     * 处理请求的URL中带有参数 (userName=sky&password=111)
     * 
     * @param strParameter
     */
    private void parseParameters(String parameterStr) {
        String[] strParameters = parameterStr.split("&");

        for (int i = 0; i < strParameters.length; i++) {
            String[] strParameter = strParameters[i].split("=");

            Object oldValue = this.parameters.get(strParameter[0]);

            // 第一次时候直接加入
            if (null == oldValue) {
                this.parameters.put(strParameter[0], strParameter[1]);

                // 第二次的时候判断该参数是否存在
            } else if (oldValue instanceof String) {
                String[] newData = new String[2];
                newData[0] = (String) oldValue;
                newData[1] = strParameter[1];
                this.parameters.put(strParameter[0], newData);

                // 第三次判断存在的该参数是否是一个数组
            } else if (oldValue instanceof String[]) {
                String[] oldArray = (String[]) oldValue;
                String[] newData = new String[oldArray.length + 1];
                System.arraycopy(oldArray, 0, newData, 0, oldArray.length);

                newData[newData.length - 1] = strParameter[1];
                this.parameters.put(strParameter[0], newData);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#getContentLength()
     */
    public long getContentLength() {
        String strContentLength = getHeader("Content-Length");
        if (null != strContentLength && !"".equals(strContentLength.trim())) {
            return Long.parseLong(strContentLength);
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#getCookies()
     */
    public Cookie[] getCookies() {
        Collection<Cookie> collection = this.cookies.values();
        Cookie[] cookies = new Cookie[collection.size()];
        return collection.toArray(cookies);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#getInputStream()
     */
    public InputStream getInputStream() {
        return is;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#getReader()
     */
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(is));
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#getPath()
     */
    public String getPath() {
        return path;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#getAbsolutePath()
     */
    public String getAbsolutePath() {
        return Configuration.getInstance().getConfigItem(
                Configuration.WEB_ROOT_PATH)
                + this.path;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#getMethod()
     */
    public String getMethod() {
        return method;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#getProtocol()
     */
    public String getProtocol() {
        return protocol;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#setAttributes(java.lang.String,
     *      java.lang.String)
     */
    public void setAttributes(String name, String value) {
        this.attributes.put(name, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#getHeader(java.lang.String)
     */
    public String getHeader(String headerName) {
        return this.headers.getProperty(headerName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#getHeaderNames()
     */
    public Enumeration<Object> getHeaderNames() {
        return this.headers.keys();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#getAccept()
     */
    public String getAccept() {
        return this.headers.getProperty("Accept");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
     */
    public String getParameter(String name) {
        Object obj = this.parameters.get(name);
        if (null == obj) {
            return null;
        } else if (obj instanceof String) {
            return (String) obj;
        }
        return ((String[]) obj)[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
     */
    public String[] getParameterValues(String name) {
        Object obj = this.parameters.get(name);
        if (null == obj) {
            return null;
        } else if (obj instanceof String) {
            return new String[] { (String) obj };
        }
        return (String[]) obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequest#getDateHeader()
     */
    public String getDateHeader() {
        return this.headers.getProperty("Date");
    }

}
