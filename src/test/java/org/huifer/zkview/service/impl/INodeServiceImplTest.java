package org.huifer.zkview.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.huifer.zkview.conf.ZkConfig;
import org.junit.jupiter.api.Test;

class INodeServiceImplTest {


  public static void hch(CuratorFramework curatorFramework, String path, Hc c) throws Exception {
    List<String> strings = curatorFramework.getChildren().forPath(path);
    c.setPath(path);
    c.setChild(og(strings, path));

    for (Hc hc : c.getChild()) {
      hch(curatorFramework, hc.getPath(), hc  );
    }

  }

  private static List<Hc> og(List<String> strings, String path) {
    List<Hc> hc = new ArrayList<>();

    for (String string : strings) {
      if (path.equals("/")) {
        hc.add(new Hc(path + string, null));
      } else {

      Hc hc1 = new Hc(path + "/" + string, null);
      hc.add(hc1);
      }
    }
    return hc;
  }

  @Test
  public void hh() throws Exception {
    CuratorFramework build = CuratorFrameworkFactory.builder().connectString(ZkConfig.getIP_PORT())
        .sessionTimeoutMs(30000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();

    build.start();
    Hc hc = new Hc(null, null);
    hch(build, "/", hc);
    System.out.println();
  }

  @Data
  @AllArgsConstructor
  public static class Hc {

    private String path;
    private List<Hc> child;

  }

}