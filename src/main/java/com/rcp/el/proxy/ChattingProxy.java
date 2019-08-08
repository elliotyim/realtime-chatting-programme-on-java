package com.rcp.el.proxy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.rcp.el.Identifier;

public class ChattingProxy implements Proxy<String>, Identifier {

  String host;
  int port;
  String name;

  public ChattingProxy(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public ChattingProxy(String host, int port, String name) {
    this.host = host;
    this.port = port;
    this.name = name;
  }

  @Override
  public void send(String message) {

    try(Socket socket = new Socket(host, port);
        PrintWriter out = new PrintWriter(
            new ObjectOutputStream(socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(
            new ObjectInputStream(socket.getInputStream())))) {

      out.println("client-ifjosdhf3/" + name);
      out.println(message);
      out.flush();

    } catch (Exception e) {
      System.out.println("데이터 전송 중 오류 발생!");
      e.printStackTrace();
    }
  }

  @Override
  public String[] receive() {
    // 이 메소드는 서버가 받은 message들을 배열로 리턴한다.
    //
    // log[0] : 메세지를 보낸 사람 IP
    // log[1] : 메세지를 보낸 사람 ID
    // log[2] : 메세지 내용

    String[] log = new String[3];

    return log;
  }

  public String receive0() {

    String message = null;

    try(Socket socket = new Socket(host, port);
        PrintWriter out = new PrintWriter(
            new ObjectOutputStream(socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(
            new ObjectInputStream(socket.getInputStream())))) {

      out.println("monitor-dsfie23r98");
      out.flush();

      message = in.readLine();

    } catch (Exception e) {
      System.out.println("데이터 전송 중 오류 발생! ");
      e.printStackTrace();
    }

    return message;
  }
  
}
