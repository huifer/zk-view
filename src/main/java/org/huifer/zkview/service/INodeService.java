package org.huifer.zkview.service;

import java.util.List;
import org.springframework.http.ResponseEntity;

public interface INodeService {

  List<String> nodeList(String path) throws Exception;


  Object info(String path) throws Exception;

  ResponseEntity tree() throws Exception;

}
