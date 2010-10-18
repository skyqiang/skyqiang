package org.apache.catalina.tomcat.demo;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.ServletContext;

public class MyFirstServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see org.tomcat.server.http.Servlet#service(org.tomcat.server.http.HttpRequest,
     *      org.tomcat.server.http.HttpResponse)
     */
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String[] parameters = request.getParameterValues("item");
        String str = "";
        for (int i = 0; parameters != null && i < parameters.length; i++) {
            str += (parameters[i] + "<br>");
        }

        Cookie[] cookies = request.getCookies();
        for (int i = 0; cookies != null && i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            System.out.println(cookie.getName() + "=" + cookie.getValue());
        }

        Cookie cookie = new Cookie("UserName", "Jacky");
        cookie.setMaxAge(60000);
        response.addCookie(cookie);

        ServletContext context = sc.getServletContext();
        context.setAttribute("UserName", "Jacky");

        response.setStates(200);
        response.setContentType("text/html");
        response.setContentLength(str.length());
        response.setDateHeader(new Date());
        response.setDescription("OK");
        response.setConnection("close");
        response.writeToClient();
        response.getOutputStream().write(str.getBytes());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.tomcat.server.http.Servlet#destory()
     */
    public void destory() {

    }
}
