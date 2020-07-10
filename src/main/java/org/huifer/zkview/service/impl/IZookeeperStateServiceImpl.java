package org.huifer.zkview.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.zookeeper.client.FourLetterWordMain;
import org.apache.zookeeper.common.X509Exception.SSLContextException;
import org.huifer.zkview.model.ZookeeperState;
import org.huifer.zkview.service.IZookeeperStateService;
import org.springframework.util.StringUtils;

public class IZookeeperStateServiceImpl implements IZookeeperStateService {


  @Override
  public Map<String, String> mntr(String host, int port) throws IOException, SSLContextException {
    String mntr = FourLetterWordMain.send4LetterWord(host, port, "mntr");
    Scanner scanner = new Scanner(mntr);
    Map<String, String> map = new HashMap<>();

    while (scanner.hasNext()) {
      String line = scanner.nextLine();

      String[] split = line.split("\t");
      if (split.length == 2) {
        map.put(split[0], split[1]);
      }
    }
    return map;
  }

  @Override
  public Map<String, String> conf(String host, int port) throws IOException, SSLContextException {
    String conf = FourLetterWordMain.send4LetterWord(host, port, "conf");
    Scanner scanner = new Scanner(conf);
    Map<String, String> map = new HashMap<>();

    while (scanner.hasNext()) {
      String line = scanner.nextLine();

      String[] split = line.split("\t");
      if (split.length == 2) {
        map.put(split[0], split[1]);
      }
    }
    return map;
  }

  @Override
  public Map<String, String> envi(String host, int port) throws IOException, SSLContextException {
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
    return map;
  }

  private boolean ok(String host, int port) throws IOException, SSLContextException {
    String ruok = FourLetterWordMain.send4LetterWord(host, port, "ruok");
    return "imok".equals(ruok);
  }

  @Override
  public ZookeeperState srvr(String host, int port) throws IOException, SSLContextException {
    String stateText = FourLetterWordMain.send4LetterWord(host, port, "srvr");
    int minLatency = -1;
    BigDecimal avgLatency = new BigDecimal("-1");

    int maxLatency = -1;
    long received = -1, sent = -1;
    int outStanding = -1;
    long zxid = -1;
    String mode = "";

    int nodeCount = -1;
    if (!StringUtils.isEmpty(stateText)) {
      Scanner scanner = new Scanner(stateText);
      while (scanner.hasNext()) {
        String line = scanner.nextLine();

        if (line.startsWith("Latency min/avg/max:")) {
          String[] latencys = split(line).split("/");
          minLatency = Integer.parseInt(latencys[0]);
          avgLatency = new BigDecimal(latencys[1]);
          maxLatency = Integer.parseInt(latencys[2]);
        } else if (line.startsWith("Received:")) {
          received = Long.parseLong(split(line));
        } else if (line.startsWith("Sent:")) {
          sent = Long.parseLong(split(line));
        } else if (line.startsWith("Outstanding:")) {
          outStanding = Integer
              .parseInt(split(line));
        } else if (line.startsWith("Zxid:")) {
          zxid = Long.parseLong(split(line)
              .substring(2), 16);
        } else if (line.startsWith("Mode:")) {
          mode = split(line);
        } else if (line.startsWith("Node count:")) {
          nodeCount = Integer.parseInt(split(line));
        }

      }
      scanner.close();
    }

    int totalWatches = -1;
    String wchsText = FourLetterWordMain.send4LetterWord(host, port, "wchs");
    if (!StringUtils.isEmpty(wchsText)) {
      Scanner scanner = new Scanner(wchsText);
      while (scanner.hasNext()) {
        String line = scanner.nextLine();
        if (line.startsWith("Total watches:")) {
          totalWatches = Integer
              .parseInt(split(line));
        }
      }
      scanner.close();
    }

    int clientNumber = -1;
    String consText = FourLetterWordMain.send4LetterWord(host, port, "cons");

    if (!StringUtils.isEmpty(consText)) {
      Scanner scanner = new Scanner(consText);
      if (StringUtils.isEmpty(consText)) {
        clientNumber = 0;
      }
      while (scanner.hasNextLine()) {
        String s = scanner.nextLine();
        ++clientNumber;
      }
      scanner.close();

    }

    ZookeeperState zookeeperState = new ZookeeperState();
    zookeeperState.setMinLatency(minLatency);
    zookeeperState.setAvgLatency(avgLatency);
    zookeeperState.setMaxLatency(maxLatency);
    zookeeperState.setReceived(received);
    zookeeperState.setSent(sent);
    zookeeperState.setOutStanding(outStanding);
    zookeeperState.setZxid(zxid);
    zookeeperState.setMode(mode);
    zookeeperState.setNodeCount(nodeCount);
    zookeeperState.setTotalWatches(totalWatches);
    zookeeperState.setClientNumber(clientNumber);

    return zookeeperState;
  }

  /**
   * 去掉没用的空格
   *
   * @param line
   * @return
   */
  private String split(String line) {
    return line.substring(line.indexOf(":") + 1)
        .replaceAll(" ", "").trim();
  }
}
