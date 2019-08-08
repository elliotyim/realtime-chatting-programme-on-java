package com.rcp.el.listener;

import java.util.List;

public interface InitEventListener <T>{
  default void eventInitialized(List<T> list) {};
}
