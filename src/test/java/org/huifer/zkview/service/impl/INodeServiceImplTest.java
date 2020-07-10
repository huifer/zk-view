package org.huifer.zkview.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooKeeper;
import org.huifer.zkview.conf.ZkConfig;
import org.huifer.zkview.service.INodeService;
import org.junit.jupiter.api.Test;

class INodeServiceImplTest {


  INodeService nodeService = new INodeServiceImpl();

  public static void hch(CuratorFramework curatorFramework, String path, Hc c) throws Exception {
    List<String> strings = curatorFramework.getChildren().forPath(path);
    c.setPath(path);
    c.setChild(og(strings, path));

    for (Hc hc : c.getChild()) {
      hch(curatorFramework, hc.getPath(), hc);
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

  @Test
  public void qps() throws Exception {
    CuratorFramework build = CuratorFrameworkFactory.builder().connectString(ZkConfig.getIP_PORT())
        .sessionTimeoutMs(30000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
    build.start();
    List<String> list = new ArrayList<>();
    ls("/", list);
    System.out.println();
  }

  public void ls(String path, List<String> sc) throws Exception {
    System.out.println(path);
    sc.add(path);
    ZooKeeper zk = new ZooKeeper("localhost:2181", 5000, null);
    List<String> list = zk.getChildren(path, null);
    //判断是否有子节点
    if (list.isEmpty() || list == null) {
      return;
    } else {

      for (String s : list) {
        //判断是否为根目录
        if (path.equals("/")) {
          ls(path + s, sc);
        } else {
          ls(path + "/" + s, sc);
        }
      }
    }
  }

  @Data
  @AllArgsConstructor
  public static class Hc {

    private String path;
    private List<Hc> child;

  }

}