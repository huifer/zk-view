package org.huifer.zkview.controller;

import java.io.IOException;
import org.apache.zookeeper.common.X509Exception.SSLContextException;
import org.huifer.zkview.model.ResultVO;
import org.huifer.zkview.model.ZookeeperState;
import org.huifer.zkview.model.req.IpPortReq;
import org.huifer.zkview.service.IZookeeperStateService;
import org.huifer.zkview.service.impl.IZookeeperStateServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/state")
public class ZookeeperStateController {

  IZookeeperStateService zookeeperStateService = new IZookeeperStateServiceImpl();

  @PostMapping("/zk")
  public ResultVO zkState(
      @RequestBody IpPortReq req
  ) throws IOException, SSLContextException {
    ZookeeperState state = zookeeperStateService.state(req.getIp(), req.getPort());
    return new ResultVO("ok", state, 200);
  }
}
