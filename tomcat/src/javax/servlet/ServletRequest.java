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
package javax.servlet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.Enumeration;

import javax.servlet.http.Cookie;

/**
 * @author WenQiang Wu
 * @version Jul 14, 2010
 */
public interface ServletRequest {

    /**
     * this method is get absolute path for current file in tomcat
     * 
     * @return String is absolute path string
     * @author skyqiang
     */
    public String getAbsolutePath();

    /**
     * this method is get the browser can accept MIME types
     * 
     * @return String
     * @author skyqiang
     */
    public String getAccept();

    /**
     * this method is get parameter's value
     * 
     * @param name
     * @return
     * @author skyqiang
     */
    public String getParameter(String name);

    /**
     * this method is get parameter value's collection
     * 
     * @param name
     *            parameter's name
     * @return
     * @author skyqiang
     */
    public String[] getParameterValues(String name);

    /**
     * 
     * @param name
     * @return
     * @author skyqiang
     */
    public Object getAttribute(String name);

    /**
     * this method is get browser's protocal
     * 
     * @return
     * @author skyqiang
     */
    public String getProtocol();

    /**
     * this method is get input stream
     * 
     * @return InputStream
     * @author skyqiang
     */
    public InputStream getInputStream();

    /**
     * this method is request message content length
     * 
     * @return long
     * @author skyqiang
     */
    public long getContentLength();

    /**
     * 
     * @return
     * @author skyqiang
     */
    public Cookie[] getCookies();

    /**
     * 
     * @param headerName
     * @return
     * @author skyqiang
     */
    public String getHeader(String headerName);

    /**
     * 
     * @return
     * @author skyqiang
     */
    public Enumeration<Object> getHeaderNames();

    /**
     * this method is get current file's path
     * 
     * @return
     * @author skyqiang
     */
    public String getPath();

    /**
     * this method is get browser's request type
     * 
     * @return
     * @author skyqiang
     */
    public String getMethod();

    /**
     * 
     * @param name
     * @param value
     * @author skyqiang
     */
    public void setAttributes(String name, String value);

    /**
     * 
     * @return
     * @author skyqiang
     */
    public BufferedReader getReader();

    /**
     * 
     * @return
     * @author skyqiang
     */
    public String getDateHeader();
}
