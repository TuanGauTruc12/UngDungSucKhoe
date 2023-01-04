/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.doanlaptrinhmang;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * @author ANH TUAN
 */
public class Server{
    static final int PORT = 8888;
    private ServerSocket server = null;
    
    public Server(){
        try{
            server = new ServerSocket(PORT);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void action() throws IOException{
        Socket socket = null;
        System.out.println("Server listening.........");
        try {
            while((socket = server.accept()) != null)
            {
                new ServerThread(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        new Server().action();
    }
    
}
