package org.huifer.zkview.service.impl;

import java.util.List;
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


  @Override
  public List<String> nodeList(String path) throws Exception {
    CuratorFramework build = CuratorFrameworkFactory.builder().connectString(ZkConfig.getIP_PORT())
        .sessionTimeoutMs(30000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();

    build.start();
    return build.getChildren().forPath(path);
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
    String data = new String(bytes);
    return new NodeInfo(stat, data, nodeType(stat));
  }

  private String nodeType(Stat stat) {
    return stat.getEphemeralOwner() > 0 ? "临时节点" : "持久化节点";
  }

}
