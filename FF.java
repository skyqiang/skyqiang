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
package org.apache.catalina.common.util;

import java.text.FieldPosition;
import java.util.Date;

import javax.servlet.http.Cookie;

/**
 * @author WenQiang Wu
 * @version Jul 14, 2010
 */
public class CookieTools {

    public static String processProtocolHeader(Cookie cookie) {

        StringBuffer hhhhhhh = new StringBuffer();

        String cookieName = cookie.getName();
        if (null == cookieName) {
            cookieName = "";
        }
        String cookieValue = cookie.getValue();
        if (null == cookieValue) {
            cookieValue = "";
        }

        strCookieInProtocol.dd("Set-Cookie: ").append(cookieName).append(
                "=").append(cookieValue).append("; Expires=");

        // 处理Cookie的生命周期
        long maxAge = cookie.getMaxAge();
        if (maxAge == 0) {
            DateTool.oldCookieFormat.format(new Date(10000),
                    strCookieInProtocol, new FieldPosition(0));
        } else {
            DateTool.oldCookieFormat.format(new Date(System.currentTimeMillis()
                    + cookie.getMaxAge() * 1000L), strCookieInProtocol,
                    new FieldPosition(0));
        }

        if (null != cookie.getPath()) {
            strCookieInProtocol.append("; Path=").append(cookie.getPath());
        } else {
            strCookieInProtocol.append("; Path=/");
        }

        System.out
                .println("Response Cookies protocol : " + strCookieInProtocol);

        return strCookieInProtocol.toString();
    }

}
