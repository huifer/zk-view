package org.huifer.zkview.utils;

public class QpsUtils {

  private double qps(int req, int sec) {
    return req / sec;
  }

}
