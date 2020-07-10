package org.huifer.zkview.service;

import java.io.IOException;
import org.apache.zookeeper.common.X509Exception.SSLContextException;
import org.huifer.zkview.model.ZookeeperState;

public interface IZookeeperStateService {

  ZookeeperState state(String host, int port) throws IOException, SSLContextException;

}
