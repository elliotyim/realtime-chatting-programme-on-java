package com.rcp.el.proxy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.rcp.el.Identifier;

public class ChattingProxy<T> implements Proxy<T>, Identifier {

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
  public void send(T message) {

    try(Socket socket = new Socket(host, port);
        PrintWriter out = new PrintWriter(
            new ObjectOutputStream(socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(
            new ObjectInputStream(socket.getInputStream())))) {

      out.println(CLIENT_SERIALNO);
      out.println(name);
      out.println(message);
      out.flush();

    } catch (Exception e) {
      System.out.println("데이터 전송 중 오류 발생!");
      e.printStackTrace();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public T receive() {
    T message = null;

    try(Socket socket = new Socket(host, port);
        PrintWriter out = new PrintWriter(
            new ObjectOutputStream(socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(
            new ObjectInputStream(socket.getInputStream())))) {

      out.println(MONITOR_SERIALNO);
      out.flush();
      
      message = (T)in.readLine();

    } catch (Exception e) {
      System.out.println("데이터 수신 중 오류 발생! ");
      e.printStackTrace();
      System.exit(0);
    }

    return message;
  }

}
