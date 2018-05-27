package ru.discloud.shared.web;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UtmLabel {
  String campaign;
  String content;
  String source;
  String term;
}
