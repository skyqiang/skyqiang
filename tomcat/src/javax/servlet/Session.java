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

import javax.servlet.http.ServletContext;

/**
 * @author WenQiang Wu
 * @version Jul 14, 2010
 */
public interface Session {

    /**
     * this method is get create time for session object
     * 
     * @return long millisecond
     * @author skyqiang
     */
    public long getCreationTime();

    /**
     * 
     * @return
     * @author skyqiang
     */
    public String getId();

    /**
     * 
     * @return
     * @author skyqiang
     */
    public long getLastAccessedTime();

    /**
     * 
     * @return
     * @author skyqiang
     */
    public ServletContext getServletContext();

    /**
     * 
     * @param interval
     * @author skyqiang
     */
    public void setMaxInactiveInterval(int interval);

    /**
     * 
     * @return
     * @author skyqiang
     */
    public int getMaxInactiveInterval();

    /**
     * 
     * @param name
     * @return
     * @author skyqiang
     */
    public Object getAttribute(String name);

    /**
     * 
     * @param name
     * @param value
     * @author skyqiang
     */
    public void setAttribute(String name, Object value);

    /**
     * 
     * @param name
     * @author skyqiang
     */
    public void removeAttribute(String name);
}
