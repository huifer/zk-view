package org.huifer.zkview.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.zookeeper.common.X509Exception.SSLContextException;
import org.huifer.zkview.service.IZookeeperStateService;
import org.junit.jupiter.api.Test;

class IZookeeperStateServiceImplTest {

  IZookeeperStateService zookeeperStateService = new IZookeeperStateServiceImpl();

  @Test
  public void client() throws IOException, SSLContextException {
    List<String> hosts = new ArrayList<>();
    hosts.add("localhost");
    hosts.add("127.0.0.1");
    List<Map<String, String>> query = new ArrayList<>();
    List<String> head = new ArrayList<>();
    head.add("配置");

    for (String host : hosts) {
      Map<String, String> localhost1 = zookeeperStateService.mntr(host, 2181);
      query.add(localhost1);
      head.add(host);
    }

    Map<String, List<String>> body = new HashMap<>();
    Map<String, String> map = query.get(0);
    for (String s : map.keySet()) {
      List<String> one = new ArrayList<>();
      one.add(s);
      body.put(s, one);
    }

    for (Map<String, String> stringStringMap : query) {
      stringStringMap.forEach(
          (k,v)->{
            boolean b = body.containsKey(k);
            if (b) {
              body.get(k).add(v);
            }
          }
      );
    }

    Collection<List<String>> values = body.values();
    List<List<String>> res = new ArrayList<>();
    res.add(head);

    for (List<String> value : values) {
      res.add(value);
    }
    System.out.println();
  }


}