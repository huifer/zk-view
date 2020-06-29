package org.huifer.zkview.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNodeReq {

  /**
   * 节点地址
   */
  private String path;
  /**
   * 节点类型
   */
  private String type;
  /**
   * 节点数据
   */
  private String data;

}
