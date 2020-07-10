package org.huifer.zkview.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.apache.zookeeper.common.X509Exception.SSLContextException;
import org.huifer.zkview.model.ZookeeperState;
import org.huifer.zkview.service.IZookeeperStateService;
import org.junit.jupiter.api.Test;

class IZookeeperStateServiceImplTest {

  IZookeeperStateService zookeeperStateService = new IZookeeperStateServiceImpl();
  @Test
  public void client() throws IOException, SSLContextException {
    ZookeeperState localhost = zookeeperStateService.state("localhost", 2181);
    System.out.println();
  }

}