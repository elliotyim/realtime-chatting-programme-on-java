package com.rcp.el.proxy;

public interface Proxy<T> {
  void send(T obj);
  T receive();
}
