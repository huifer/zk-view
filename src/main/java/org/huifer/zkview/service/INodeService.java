package org.huifer.zkview.service;

import java.util.List;

public interface INodeService {

  List<String> nodeList(String path) throws Exception;


  Object info(String path) throws Exception;

}
