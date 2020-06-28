package org.huifer.zkview.conf;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ZkConfig {

  private static String IP_PORT = "127.0.0.1:2181";

  public static String getIP_PORT() {
    return IP_PORT;
  }

  public static void setIP_PORT(String ipPort) {
    IP_PORT = ipPort;
  }
}
