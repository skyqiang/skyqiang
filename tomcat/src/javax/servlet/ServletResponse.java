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

import java.io.OutputStream;
import java.util.Date;

/**
 * @author WenQiang Wu
 * @version Jul 14, 2010
 */
public interface ServletResponse {

    /**
     * 
     * @param contentLength
     */
    public void setContentLength(long contentLength);

    /**
     * 
     * @return
     */
    public long getContentLength();

    /**
     * 
     * @return
     * @author skyqiang
     */
    public String getContentType();

    /**
     * 
     * @param contentType
     * @author skyqiang
     */
    public void setContentType(String contentType);

    /**
     * 
     * @return
     * @author skyqiang
     */
    public OutputStream getOutputStream();

    /**
     * 
     * @param headerName
     * @return
     * @author skyqiang
     */
    public String getHeader(String headerName);

    /**
     * 
     * @param headerName
     * @param headerValue
     * @author skyqiang
     */
    public void setHeader(String headerName, String headerValue);

    /**
     * @return the protocol
     */
    public String getProtocol();

    /**
     * @param protocol
     *            the protocol to set
     */
    public void setProtocol(String protocol);

    /**
     * @return the description
     */
    public String getDescription();

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description);

    /**
     * @return the states
     */
    public int getStates();

    /**
     * @param states
     *            the states to set
     */
    public void setStates(int states);

    /**
     * 
     * @return
     * @author skyqiang
     */
    public String getDateHeader();

    /**
     * 
     * @param date
     * @author skyqiang
     */
    public void setDateHeader(Date date);
}
