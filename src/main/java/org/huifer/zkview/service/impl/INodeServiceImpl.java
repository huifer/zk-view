package org.huifer.zkview.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.huifer.zkview.conf.ZkConfig;
import org.huifer.zkview.model.NodeInfo;
import org.huifer.zkview.service.INodeService;
import org.springframework.stereotype.Service;

@Service
public class INodeServiceImpl implements INodeService {


  private static List<Hc> child(List<String> strings, String path) {
    List<Hc> hc = new ArrayList<>();

    for (String string : strings) {
      if (path.equals("/")) {
        hc.add(new Hc(path + string, string, null));
      } else {

        Hc hc1 = new Hc(path + "/" + string, string, null);
        hc.add(hc1);
      }
    }
    return hc;
  }

  public static void calcTree(CuratorFramework curatorFramework, String path, Hc c) throws Exception {
    List<String> strings = curatorFramework.getChildren().forPath(path);
    c.setPath(path);
    c.setChild(child(strings, path));

    for (Hc hc : c.getChild()) {
      calcTree(curatorFramework, hc.getPath(), hc);
    }

  }

  @Override
  public List<String> nodeList(String path) throws Exception {
    CuratorFramework build = CuratorFrameworkFactory.builder().connectString(ZkConfig.getIP_PORT())
        .sessionTimeoutMs(30000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();

    build.start();
    List<String> strings = build.getChildren().forPath(path);
    build.close();
    return strings;
  }

  private String nodeType(Stat stat) {
    return stat.getEphemeralOwner() > 0 ? "临时节点" : "持久化节点";
  }

  @Override
  public Object info(String path) throws Exception {
    CuratorFramework build = CuratorFrameworkFactory.builder().connectString(ZkConfig.getIP_PORT())
        .sessionTimeoutMs(30000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
    build.start();
    // 数据
    Stat stat = new Stat();
    byte[] bytes = build.getData().storingStatIn(stat).forPath(path);

    String data;
    if (bytes != null && bytes.length > 0) {

      data = new String(bytes);
    } else {
      data = "";
    }
    build.close();
    return new NodeInfo(stat, data, nodeType(stat));
  }

  @Override
  public Hc tree() throws Exception {

    CuratorFramework build = CuratorFrameworkFactory.builder().connectString(ZkConfig.getIP_PORT())
        .sessionTimeoutMs(30000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
    build.start();
    Hc hc = new Hc(null, "/", null);
    calcTree(build, "/", hc);
    build.close();
    return hc;
  }

  @Data
  @AllArgsConstructor
  public static class Hc {

    private String path;
    private String showName;
    private List<Hc> child;

  }
}
