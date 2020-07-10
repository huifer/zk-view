package org.huifer.zkview.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableEntity {

  private List<String> head;
  private List<List<String>> body;

}
