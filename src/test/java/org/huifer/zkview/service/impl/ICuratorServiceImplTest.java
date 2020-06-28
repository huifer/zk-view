package org.huifer.zkview.service.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetConfigBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.huifer.zkview.service.ICuratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

class ICuratorServiceImplTest {

  ICuratorService curatorService;

  @BeforeEach
  public void f() {
    curatorService = new ICuratorServiceImpl();
  }

  @Test
  public void testConnection() {
    boolean connection = curatorService.connection("127.0.0.1:2181");
    Assert.isTrue(connection);
  }

  @Test
  public void hh() throws Exception {
    CuratorFramework build = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
        .sessionTimeoutMs(30000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
    build.start();
    build.create().forPath("/ac", "demo".getBytes());
    GetConfigBuilder config = build.getConfig();
    System.out.println();
  }

}