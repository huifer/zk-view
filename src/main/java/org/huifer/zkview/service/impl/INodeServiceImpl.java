package org.huifer.zkview.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.huifer.zkview.conf.ZkConfig;
import org.huifer.zkview.model.NodeInfo;
import org.huifer.zkview.model.req.CreateNodeReq;
import org.huifer.zkview.model.req.UpdateNodeReq;
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

  public static void calcTree(CuratorFramework curatorFramework, String path, Hc c)
      throws Exception {
    List<String> strings = curatorFramework.getChildren().forPath(path);
    c.setPath(path);
    c.setChild(child(strings, path));

    for (Hc hc : c.getChild()) {
      calcTree(curatorFramework, hc.getPath(), hc);
    }

  }

  public static void main(String[] args) throws Exception {
    CuratorFramework build = CuratorFrameworkFactory.builder().connectString(ZkConfig.getIP_PORT())
        .sessionTimeoutMs(30000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();

    build.start();

    Stat stat = build.checkExists().forPath("/acd");
    System.out.println();
  }

  @Override
  public List<String> nodeList(String path) throws Exception {
    List<String> list = new ArrayList<>();
    ls("/", list);
    return list;
  }

  private void ls(String path, List<String> sc) throws Exception {
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

  @Override
  public List<String> childList(String path) throws Exception {
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

  @Override
  public boolean create(CreateNodeReq createNodeReq) {
    CuratorFramework build = CuratorFrameworkFactory.builder().connectString(ZkConfig.getIP_PORT())
        .sessionTimeoutMs(30000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
    build.start();
    switch (createNodeReq.getType()) {
      case "PERSISTENT":
        // 持久化节点
        return createPersistent(build, createNodeReq);
      case "EPHEMERAL":
        // 临时节点
        return createEphemeral(build, createNodeReq);

    }

    return false;
  }

  private boolean checkPath(String p) {
    String[] split = p.split("/");
    return split.length > 2;
  }

  private boolean createEphemeral(CuratorFramework build,
      CreateNodeReq createNodeReq) {
    try {
      if (checkPath(createNodeReq.getPath())) {
        throw new IllegalArgumentException("临时节点不允许有子节点");
      }
      if (!exists(build, createNodeReq.getPath())) {

        build.create().withMode(CreateMode.EPHEMERAL)
            .forPath(createNodeReq.getPath(), createNodeReq.getData().getBytes());

        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean createPersistent(CuratorFramework build,
      CreateNodeReq createNodeReq) {
    try {
      if (!exists(build, createNodeReq.getPath())) {
        build.create().creatingParentsIfNeeded()
            .withMode(CreateMode.PERSISTENT)
            .forPath(createNodeReq.getPath(), createNodeReq.getData().getBytes());
        return true;
      } else {

        return false;

      }
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }

  private boolean exists(CuratorFramework build, String path) {
    try {
      return build.checkExists().forPath(path) != null;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

  }

  @Override
  public boolean update(UpdateNodeReq updateNodeReq) {
    CuratorFramework build = CuratorFrameworkFactory.builder().connectString(ZkConfig.getIP_PORT())
        .sessionTimeoutMs(30000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();

    build.start();

    // 是否存在
    if (exists(build, updateNodeReq.getPath())) {

      try {
        build.setData().forPath(updateNodeReq.getPath(), updateNodeReq.getData().getBytes());
        return true;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    } else {
      throw new IllegalArgumentException("当前节点地址不存在,path=[ " + updateNodeReq.getPath() + " ]");
    }
  }

  @Override
  public boolean del(String path) {
    CuratorFramework build = CuratorFrameworkFactory.builder().connectString(ZkConfig.getIP_PORT())
        .sessionTimeoutMs(30000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();

    build.start();
    if (exists(build, path)) {

      try {
        build.delete().forPath(path);
        return true;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }
    return false;
  }

  @Data
  @AllArgsConstructor
  public static class Hc {

    private String path;
    private String showName;
    private List<Hc> child;

  }
}
