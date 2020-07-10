package org.huifer.zkview.service;

import java.io.IOException;
import java.util.Map;
import org.apache.zookeeper.common.X509Exception.SSLContextException;
import org.huifer.zkview.model.ZookeeperState;

public interface IZookeeperStateService {


  Map<String, String> mntr(String host, int port) throws IOException, SSLContextException;


  ZookeeperState srvr(String host, int port) throws IOException, SSLContextException;

  Map<String, String> conf(String host, int port) throws IOException, SSLContextException;

  Map<String, String> envi(String host, int port) throws IOException, SSLContextException;
}
