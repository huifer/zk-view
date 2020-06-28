package org.huifer.zkview.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.zookeeper.data.Stat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeInfo {

  private Stat stat;
  private String data;

  /**
   * 持久化节点,临时节点
   */
  private String type;

}
