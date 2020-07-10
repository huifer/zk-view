package org.huifer.zkview.zkclient;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

public class ZkClientTest {

  private static final int CLIENT_PORT = 2181;
  private static final String ROOT_PATH = "/root";
  private static final String CHILD_PATH = "/root/childPath";
  private static final String CHILD_PATH_2 = "/root/childPath2";
  static ZooKeeper zk = null;

  public static void main(String[] args) throws Exception {
    try {
      zk = new ZooKeeper("localhost:" + CLIENT_PORT, 3000, (watchedEvent) -> {
        System.out.println(
            watchedEvent.getPath() + "触发了" + watchedEvent.getType() + "事件!" + "data:" + watchedEvent
                .getPath());
      }
      );

      // 创建根目录

      Thread.sleep(1000000L);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String getData(String path) {
    if (path == null) {
      return null;
    }
    try {
      return new String(zk.getData(path, false, null));
    } catch (KeeperException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }
}
