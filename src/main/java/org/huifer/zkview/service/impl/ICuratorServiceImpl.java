package org.huifer.zkview.service.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.huifer.zkview.service.ICuratorService;

public class ICuratorServiceImpl implements ICuratorService {

  /**
   * 连接测试
   *
   * @param ipPort ip+端口
   * @return boolean
   */
  @Override
  public boolean connection(String ipPort) {
    CuratorFramework build = CuratorFrameworkFactory.builder().connectString(ipPort)
        .sessionTimeoutMs(3000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    try {
      build.start();
      build.create().forPath("/connection", "test".getBytes());
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      build.close();
      return false;
    }
  }

  @Override
  public boolean inCluster(String ipPort) {
    return false;
  }
}
