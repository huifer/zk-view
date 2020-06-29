package org.huifer.zkview.model.req;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNodeReq {

  @NotEmpty
  private String path;
  @NotEmpty
  private String data;
}
