/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.doanlaptrinhmang;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author ANH TUAN
 */
public class ServerThread implements Runnable {

    private Socket socket;
    private PrintWriter out = null;
    private Scanner in = null;
    private FirebaseConnection connect = null;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new Scanner(this.socket.getInputStream());
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.connect = new FirebaseConnection();
        new Thread(this).start();
    }

    private String checkNongDo(int nongDo) {
        String strNongDo = "";

        if (nongDo >= 97 && nongDo <= 99) {
            strNongDo = "Chỉ số oxy trong máu tốt";
        } else if (nongDo >= 94 && nongDo <= 96) {
            strNongDo = "Chỉ số oxy trong máu trung bình, cần thở thêm oxy";
        } else if (nongDo >= 90 && nongDo <= 93) {
            strNongDo = "Chỉ số oxy trong máu thấp, cần xin ý kiến của bác sĩ chủ trị";
        } else if (nongDo < 90) {
            strNongDo = "Canh bao nguy hiem";
        }
        return strNongDo;
    }

    private String checkNhpTim(double nhipTim) {
        String strNhipTim = "";
        if (nhipTim >= 60 && nhipTim <= 90) {
            strNhipTim = "Nhip tim của bạn bình thường";
        } else if (nhipTim > 90) {
            strNhipTim = "Nhịp tim của bạn đang nhanh";
        } else if (nhipTim < 60) {
            strNhipTim = "Nhịp tim của bạn đang chậm";
        }
        return strNhipTim;
    }

    @Override
    public void run() {
        try {
            StringBuffer sb = new StringBuffer();
            LapTrinhMang lapTrinhMang = connect.getData();
            double nhipTim = lapTrinhMang.getBPM();
            int nongDo = lapTrinhMang.getSpO2();
            sb.append(nhipTim);
            sb.append("@");
            sb.append(nongDo);
            sb.append("@");
            sb.append(checkNhpTim(nhipTim));
            sb.append("@");
            sb.append(checkNongDo(nongDo));
            out.println(sb.toString());

        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

}
