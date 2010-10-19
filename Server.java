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
package org.apache.catalina;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author WenQiang Wu
 * @version Jan 14, 2010
 */
public abstract class Server implements Runnable {
    private ServerSocket serverSocket;
    private Socket socket;

    private int port;
    private boolean running;

    /**
     * @param port
     *            the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * start server
     */
    public void start() {
        this.running = true;
        new Thread(this).start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            System.out.println("Listening to port : " + this.port);
            System.out.println("Server start success!");

            while (running) {
                this.socket = this.serverSocket.accept();
                System.out.println("Client connected : "
                        + this.socket.getInetAddress().getHostAddress());

                process(socket);
                System.out.println("Close client.\n\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (null != socket) {
                try {
                    this.socket.close();
                    this.socket = null;
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (null != this.serverSocket) {
                try {
                    this.serverSocket.close();
                    this.serverSocket = null;
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    /**
     * 
     * @param socket
     * @throws Exception
     */
    protected abstract void process(Socket socket) throws Exception;
   
    
}
