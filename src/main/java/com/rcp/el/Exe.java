package com.rcp.el;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.rcp.el.proxy.ChattingProxy;

public class Exe {

  private String checkingHost = "연결하려는 호스트 서버의 IP 주소를 입력해주세요. (ex:192.168.0.1)\n";
  private String checkingPort = "포트번호를 입력해주세요. (8888)\n";
  private String checkingName = "닉네임을 입력해주세요. (최대 8자)\n";

  private JTextArea inputText;
  private JTextField outputText;

  String host;
  int port;
  String clientName;

  ChattingProxy chattingProxy;

  private void createInputPanel(JPanel inputPanel) {
    inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

    inputText = new JTextArea();
    inputText.setEditable(false);
    inputText.setText(checkingHost);

    JScrollPane scroll = new JScrollPane(inputText);
    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    inputPanel.add(scroll);
  }


  private void createOutputPanel(JPanel outputPanel) {
    outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.X_AXIS));

    outputText = new JTextField();
    JButton bt = new JButton("submit");

    outputText.addKeyListener(new OutputTextListener());
    bt.addActionListener(new ButtonActionListener());

    outputPanel.add(outputText);
    outputPanel.add(bt);
  }

  private void createFrame(JPanel inputPanel, JPanel outputPanel, JFrame frame) {
    frame.getContentPane().add(BorderLayout.CENTER,inputPanel);
    frame.add(BorderLayout.SOUTH, outputPanel);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(640, 480);
    frame.setVisible(true);
  }

  private class OutputTextListener implements KeyListener {

    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == 10) {
        if (chattingProxy != null)
          chattingProxy.send(outputText.getText());
        checkingInitialInformations();
        outputText.setText(null);
      }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

  }

  private class ButtonActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (chattingProxy != null)
        chattingProxy.send(outputText.getText());
      checkingInitialInformations();
      outputText.setText(null);
    }
  }

  private void checkingInitialInformations() {
    if (inputText.getText().contains(checkingHost)) {
      if (isValidHost(outputText.getText())) {
        this.host = outputText.getText();
        inputText.setText(checkingPort);

      } else {
        inputText.append(outputText.getText()+"\n");
        inputText.append("유효하지 않은 IP주소입니다.\n");

      }

    } else if (inputText.getText().contains(checkingPort)) {
      if (isValidPortNumber(outputText.getText())) {
        this.port = Integer.parseInt(outputText.getText());
        inputText.setText(checkingName);

      } else {
        inputText.append(outputText.getText()+"\n");
        inputText.append("유효하지 않은 포트번호입니다.\n");

      }

    } else if (inputText.getText().contains(checkingName)) {
      if (isValidClientName(outputText.getText())) {
        this.clientName = outputText.getText();
        inputText.setText("서버에 연결됨!\n");
        
      } else {
        inputText.append(outputText.getText()+"\n");
        inputText.append("유효하지 않은 닉네임입니다.\n");
      }
    }

  }

  private boolean isValidHost(String host) {
    if (host == null)
      return false;
    if (host.equals("localhost"))
      return true;

    String ipClass = "25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?";
    Pattern ipPattern = Pattern.compile(String.format("\\b(?:(?:%s)\\.){3}(?:%s)\\b", ipClass, ipClass));
    
    Matcher matcher = ipPattern.matcher(host);
    return matcher.matches();
    
  }

  private boolean isValidPortNumber(String portNo) {
    try {
      if (portNo == null || portNo.length() > 4)
        return false;
      this.port = Integer.parseInt(portNo);

    } catch (NumberFormatException e) {
      return false;
    }

    return true;
  }

  private boolean isValidClientName(String name) {
    if (name == null || name.length() > 8) {
      return false;
    }
    return true;
  }

  private void execute() throws InterruptedException {

    JFrame frame = new JFrame("Realtime Chatting Programme");
    JPanel inputPanel = new JPanel();
    JPanel outputPanel = new JPanel();

    createInputPanel(inputPanel);
    createOutputPanel(outputPanel);
    createFrame(inputPanel, outputPanel, frame);
    outputText.requestFocus();

    
    while (true) {
      if (host != null && port != 0
          && clientName != null) {
        chattingProxy = new ChattingProxy(host, port, clientName);
        break;
      }
      Thread.sleep(100);
    }

    while (true) {
      if (chattingProxy != null)
        inputText.append(chattingProxy.receive()+"\n");
      inputText.setCaretPosition(inputText.getDocument().getLength());
    }

  }

  public static void main(String[] args) throws InterruptedException {
    Exe programme = new Exe();
    programme.execute();
  }

}
