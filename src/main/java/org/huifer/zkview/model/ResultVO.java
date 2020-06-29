package org.huifer.zkview.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO {

  private String msg;
  private Object data;
  private Integer code;

}
