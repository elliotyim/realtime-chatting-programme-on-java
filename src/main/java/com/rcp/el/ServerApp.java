// 파워쉘에서 서버 실행시 커맨드
// => java -cp bin/main com.rcp.el.ServerApp
package com.rcp.el;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.rcp.el.pre_version.Identifier;

public class ServerApp implements Identifier {

  String clientName;
  ArrayList<PrintWriter> monitorList = new ArrayList<>();

  public void execute() {

    try (ServerSocket serverSocket = new ServerSocket(8888)) {
      System.out.println("서버 시작!");

      while (true) {
        Socket socket = serverSocket.accept();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(
            new ObjectInputStream(socket.getInputStream())));
        PrintWriter out = new PrintWriter(new ObjectOutputStream(
            socket.getOutputStream()));

        long identifier = Long.parseLong(in.readLine());

        if (identifier == CLIENT_SERIALNO) {
          clientName = in.readLine();
          new Thread(new ClientMessageReader(socket, in)).start();
        } else if (identifier == MONITOR_SERIALNO) {
          monitorList.add(out);
        }
        
      }
      
    } catch (Exception e) {
      System.out.println("오류!");
      e.printStackTrace();
    }
  }

  private class ClientMessageReader implements Runnable {

    BufferedReader in;
    Socket socket;

    public ClientMessageReader(Socket socket, BufferedReader in) {
      this.socket = socket;
      this.in = in;
    }

    @Override
    public void run() {
      try (BufferedReader in = this.in) {
        String clientIpAddress = String.format("%s",
            ((InetSocketAddress)socket.getRemoteSocketAddress())
            .getAddress()).substring(1);
        
        String str = in.readLine();
        for (PrintWriter out : monitorList) {
          out.println("[" + clientName +
              " ("+ clientIpAddress +")"+ "] "+ str);
          out.flush();
        }

      } catch (Exception e) {
        System.out.println("메시지를 읽는 도중 오류가 발생!");
        e.printStackTrace();
      }

    }
  }

  public static void main(String[] args) {
    ServerApp serverApp = new ServerApp();
    serverApp.execute();
  }
}
