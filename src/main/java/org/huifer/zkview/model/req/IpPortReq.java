package org.huifer.zkview.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpPortReq {

  private String ip;
  private Integer port;

}
