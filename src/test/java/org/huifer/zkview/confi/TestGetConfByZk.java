package org.huifer.zkview.confi;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.Test;

public class TestGetConfByZk {


  @Test
  public void conf() {
    CuratorFramework curatorFramework = CuratorFrameworkFactory
        .newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
    curatorFramework.start();

  }

}
