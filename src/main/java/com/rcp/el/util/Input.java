package com.rcp.el.util;

import java.util.Scanner;

public class Input {
  private Scanner keyScan;
  
  public Input(Scanner keyScan) {
    this.keyScan = keyScan;
  }
  
  public String getStringValue(String message) {
    System.out.print(message);
    return keyScan.nextLine();
  }
  
  public int getIntValue(String message) {
    while (true) {
      try {
        System.out.print(message);
        return Integer.parseInt(keyScan.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("숫자를 입력하세요.");
      }
    }
  }
  
}
