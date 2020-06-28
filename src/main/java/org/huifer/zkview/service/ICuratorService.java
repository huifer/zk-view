package org.huifer.zkview.service;

public interface ICuratorService {

  /**
   * 连接测试
   *
   * @param ipPort ip+端口
   * @return boolean
   */
  boolean connection(String ipPort);

  boolean inCluster(String ipPort);

}
