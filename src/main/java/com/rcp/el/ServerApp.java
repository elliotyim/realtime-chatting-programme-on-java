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
import java.util.LinkedList;
import java.util.Queue;

public class ServerApp implements Identifier {

  Queue<String> messageQueue = new LinkedList<>();
  String[] name = new String[2];

  public void execute() {

    try (ServerSocket serverSocket = new ServerSocket(8888)) {
      System.out.println("서버 시작!");

      while (true) {
        Socket socket = serverSocket.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(
            new ObjectInputStream(socket.getInputStream())));
        PrintWriter out = new PrintWriter(new ObjectOutputStream(
            socket.getOutputStream()));

        System.out.println("클라이언트 요청을 기다리는 중...");

        String identifier = in.readLine();
        System.out.println("id " + identifier);
        name = identifier.split("/");

        if (identifier.startsWith("client-ifjosdhf3")) {
          new Thread(new ClientMessageReader(socket, in, name[1])).start();
        } else if (identifier.equals("monitor-dsfie23r98")) {
          new Thread(new MonitorMessagePrinter(out)).start();
        }
        
        //socket.close(); // 소켓이 먼저 닫혀서 Printer가 출력을 못함
      }
    } catch (Exception e) {
      System.out.println("오류!");
      e.printStackTrace();
    }

  }

  private class ClientMessageReader implements Runnable {

    BufferedReader in;
    Socket socket;
    String name;

    public ClientMessageReader(Socket socket, BufferedReader in, String name) {
      this.socket = socket;
      this.in = in;
      this.name = name;
    }

    @Override
    public void run() {
      try (BufferedReader in = this.in) {
        String ipAddress = String.format("%s",
            ((InetSocketAddress)socket.getRemoteSocketAddress())
            .getAddress()).substring(1);
        
        messageQueue.offer(String.format("[%s (%s)] %s",
            name,
            ipAddress,
            in.readLine()
            ));

      } catch (Exception e) {
        System.out.println("메시지를 읽는 도중 오류가 발생!" + e.getMessage());
        e.printStackTrace();
      }

    }
  }

  private class MonitorMessagePrinter implements Runnable {

    PrintWriter out;

    public MonitorMessagePrinter(PrintWriter out) {
      this.out = out;
    }

    @SuppressWarnings("static-access")
    @Override
    public void run() {
      try (PrintWriter out = this.out) {
        while (true) {
          if (!messageQueue.isEmpty()) {
            out.println(messageQueue.poll());
            break;
          }
          Thread.sleep(100);
        }
      } catch (Exception e) {
        System.out.println("모니터로 출력 중 에러 발생!");
        e.printStackTrace();
      }

    }

  }



  public static void main(String[] args) {
    ServerApp serverApp = new ServerApp();
    serverApp.execute();
  }


}
