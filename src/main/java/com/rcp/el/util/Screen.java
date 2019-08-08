package com.rcp.el.util;

public class Screen {
  public void clear() {
    for (int i = 0; i < 80; i++) {
      System.out.println();
    }
  }
}
