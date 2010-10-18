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

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.ServletContext;

import org.apache.catalina.common.Constant;

/**
 * @author WenQiang Wu
 * @version Jan 15, 2010
 */
public class Configuration {
    public static final String WEB_ROOT_PATH = "webRootPath";
    public static final String PORT = "port";
    public static final String MIME_PRFEX = "mime.";

    private Properties prop;
    private Map<String, ServletConfig> servletConfigMap;

    private static Configuration configuration = null;

    /**
     * 
     */
    private Configuration() {
        this.prop = new Properties();
        servletConfigMap = new HashMap<String, ServletConfig>();
        loadFromConfigFile();
    }

    /**
     * 
     * @return
     */
    public static Configuration getInstance() {
        if (null == configuration) {
            configuration = new Configuration();
        }
        return configuration;
    }

    /**
     * read configuration.properties file
     */
    private void loadFromConfigFile() {
        InputStream is = null;
        try {
            is = this.getClass().getResourceAsStream(
                    "/org/apache/catalina/properties/configuration.properties");
            this.prop.load(is);

            Enumeration<?> en = this.prop.keys();
            while (en.hasMoreElements()) {
                String name = (String) en.nextElement();

                if (Constant.CONFIG_SERVLET_NAME.equals(name)) {
                    String servletName = this.prop.getProperty(name);
                    String clazzName = this.prop
                            .getProperty(Constant.SERVLET_CLAZZ_PRFEX
                                    + servletName
                                    + Constant.SERVLET_CLAZZ_SUFIX);
                    String urlPattern = this.prop
                            .getProperty(Constant.SERVLET_CLAZZ_PRFEX
                                    + servletName
                                    + Constant.SERLVET_CLAZZ_URL_SUFIX);

                    //
                    ServletConfig config = new ServletConfig(
                            new ServletContext());
                    config.setName(servletName);
                    config.setClazz(clazzName);
                    config.setUrlPattern(urlPattern);

                    this.servletConfigMap.put(urlPattern, config);
                }
            }

        } catch (IOException e) {
            System.out
                    .println("Load configuration.properties file happend error!");
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 
     * @param name
     * @return
     */
    public String getConfigItem(String name) {
        return this.prop.getProperty(name);
    }

    /**
     * get servlet config object
     * 
     * @param urlpattern
     * @return ServletConfig
     */
    public ServletConfig getServletConfig(String urlpattern) {
        return this.servletConfigMap.get(urlpattern);
    }
}
