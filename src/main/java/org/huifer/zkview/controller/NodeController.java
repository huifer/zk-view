package org.huifer.zkview.controller;

import org.huifer.zkview.service.INodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/node")
public class NodeController {

  @Autowired
  private INodeService nodeService;

  @GetMapping("/get/child")
  public ResponseEntity child(
      @RequestParam(value = "path") String path
  ) throws Exception {
    return ResponseEntity.ok(nodeService.nodeList(path));
  }

  @GetMapping("/get/info")
  public ResponseEntity info(
      @RequestParam(value = "path") String path
  ) throws Exception {
    return ResponseEntity.ok(nodeService.info(path));
  }
}
