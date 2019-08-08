// 파워쉘에서 클라이언트 실행시 커맨드
// => java -cp bin/main com.rcp.el.App
package com.rcp.el;

import java.util.Scanner;

import com.rcp.el.proxy.ChattingProxy;
import com.rcp.el.util.Input;
import com.rcp.el.util.Screen;

public class App {
  
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
    System.out.println("실행 커맨드 : java -cp bin/main com.rcp.el.App\n\n");
    
    
    System.out.println("연결하려는 호스트 서버의 IP 주소를 입력해주세요. (ex: 192.168.0.1)");
    this.host = input.getStringValue(">> ");
    
    System.out.println("포트번호를 입력해주세요. (8888)");
    this.port = input.getIntValue(">> ");
    
    while (true) {
      System.out.println("이름을 입력해주세요. (최대 8자)");
      if ((name = input.getStringValue(">> ")).length() <= 8) {
        break;
      }
      System.out.println("너무 긴 이름입니다.");
    }

    screen.clear();
    
    ChattingProxy chattingProxy = new ChattingProxy(host, port, name);
    System.out.println("채팅을 시작합니다!");
    
    String message = null;
    while (true) {
      message = input.getStringValue(">> ");
      
      chattingProxy.send(message);
    }
    
  }
  
  public static void main(String[] args) {
    App app = new App();
    app.execute();
  }
}
