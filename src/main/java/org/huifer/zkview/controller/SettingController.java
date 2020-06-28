package org.huifer.zkview.controller;

import org.huifer.zkview.conf.ZkConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/setting")
public class SettingController {

  @PostMapping("/port/{ip}/{port}")
  public ResponseEntity setIpPort(
      @PathVariable(value = "ip", required = true) String ip,
      @PathVariable(value = "port", required = true) String port
  ) {
    ZkConfig.setIP_PORT(ip + ":" + port);
    return ResponseEntity.ok().body("ok");
  }

  @GetMapping("/info")
  public ResponseEntity info() {
    return ResponseEntity.ok(ZkConfig.getIP_PORT());
  }
}
