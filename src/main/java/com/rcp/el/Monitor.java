// 파워쉘에서 모니터 실행시 커맨드
// => java -cp bin/main com.rcp.el.Monitor
package com.rcp.el;

import java.util.Scanner;

import com.rcp.el.proxy.ChattingProxy;
import com.rcp.el.util.Input;
import com.rcp.el.util.Screen;

public class Monitor {
  
  Scanner keyScan;
  String host;
  int port;
  String name;
  
  private void execute() {
    keyScan = new Scanner(System.in);
    Input input = new Input(keyScan);
    
    Screen screen = new Screen();
    
    screen.clear();
    
    System.out.println("이 프로그램은 베타버전이며, 파워쉘에서 구동 해야 한글이 깨지지 않습니다.");
    System.out.println("프로젝트 경로인 [realtime-chatting-programme-on-java]에서 실행하세요.");
    System.out.println("실행 커맨드 : java -cp bin/main com.rcp.el.Monitor\n\n");
    
    System.out.println("연결하려는 호스트 서버의 IP 주소를 입력해주세요. (ex: 192.168.0.1)");
    this.host = input.getStringValue(">> ");
    
    System.out.println("포트번호를 입력해주세요. (8888)");
    this.port = input.getIntValue(">> ");
    
    screen.clear();
    
    ChattingProxy chattingProxy = new ChattingProxy(host, port);
    System.out.println("서버에 연결됨!");

    while (true) {
      
      System.out.println(chattingProxy.receive0());
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
  }
  
  public static void main(String[] args) {
    Monitor monitor = new Monitor();
    monitor.execute();
  }

}
