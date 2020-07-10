package org.huifer.zkview.zkclient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.zookeeper.client.FourLetterWordMain;
import org.apache.zookeeper.common.X509Exception.SSLContextException;

public class ZkClientTest {
// 4lw.commands.whitelist=*
  public static void main(String[] args) throws IOException, SSLContextException {
    String host = "127.0.0.1";
    int port = 2181;
//    String srvr = FourLetterWordMain.send4LetterWord(host, port, "srvr");
//    String stat = FourLetterWordMain.send4LetterWord(host, port, "stat");
    String mntr = FourLetterWordMain.send4LetterWord(host, port, "mntr");
//    String ruok = FourLetterWordMain.send4LetterWord(host, port, "ruok");
    //


    String envi = FourLetterWordMain.send4LetterWord(host, port, "envi");
    Scanner scanner = new Scanner(envi);
    Map<String, String> map = new HashMap<>();

    while (scanner.hasNext()) {
      String line = scanner.nextLine();

      String[] split = line.split("=");
      if (split.length == 2) {
        map.put(split[0], split[1]);
      }
    }
    System.out.println( );

  }

  private static String split(String line) {
    return line.substring(line.indexOf(":") + 1)
        .replaceAll(" ", "").trim();
  }

}
