// 파워쉘에서 클라이언트 실행시 커맨드
// => java -cp bin/main com.rcp.el.App
package com.rcp.el.pre_version;

import java.util.Scanner;

import com.rcp.el.proxy.ChattingProxy;
import com.rcp.el.util.Input;
import com.rcp.el.util.Screen;

public class App {
  private void execute() {
    Scanner keyScan = new Scanner(System.in);
    Input input = new Input(keyScan);
    String clientName;

    Screen screen = new Screen();
    screen.clear();
    
    System.out.println("이 프로그램은 베타버전이며, 파워쉘에서 구동 해야 한글이 깨지지 않습니다.");
    System.out.println("프로젝트 경로인 [realtime-chatting-programme-on-java]에서 실행하세요.");
    System.out.println("실행 커맨드 : java -cp bin/main com.rcp.el.App\n\n");
    
    String host = input.getStringValue("연결하려는 호스트 서버의 IP 주소를 입력해주세요. (ex: 192.168.0.1)\n>> ");
    int port = input.getIntValue("포트번호를 입력해주세요. (8888)\n>> ");
    
    while (true) {
      if ((clientName = input.getStringValue("이름을 입력해주세요. (최대 8자)\n>> ")).length() <= 8)
        break;
      System.out.println("너무 긴 이름입니다.");
    }
    screen.clear();
    
    ChattingProxy chattingProxy = new ChattingProxy(host, port, clientName);
    System.out.println("채팅방에 접속했습니다!");
    
    while (true) {
      chattingProxy.send(input.getStringValue(">> "));
    }
    
  }
  
  public static void main(String[] args) {
    App app = new App();
    app.execute();
  }
}
