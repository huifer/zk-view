package org.huifer.zkview.service;

import java.util.List;
import org.huifer.zkview.model.req.CreateNodeReq;
import org.huifer.zkview.service.impl.INodeServiceImpl.Hc;

public interface INodeService {

  List<String> nodeList(String path) throws Exception;


  Object info(String path) throws Exception;

  Hc tree() throws Exception;

  boolean create(CreateNodeReq createNodeReq);
}
