package org.huifer.zkview.controller;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.huifer.zkview.model.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conf")
public class ConfigController {

  private static final NumberFormat fmtI = new DecimalFormat("###,###",
      new DecimalFormatSymbols(Locale.CHINA));
  private static final NumberFormat fmtD = new DecimalFormat("###,##0.000",
      new DecimalFormatSymbols(Locale.CHINA));

  public static String toDuration() {
    RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
    double uptime = runtime.getUptime();
    uptime /= 1000;
    if (uptime < 60) {
      return fmtD.format(uptime) + "秒";
    }
    uptime /= 60;
    if (uptime < 60) {
      long minutes = (long) uptime;
      String s = fmtI.format(minutes) + "分";
      return s;
    }
    uptime /= 60;
    if (uptime < 24) {
      long hours = (long) uptime;
      long minutes = (long) ((uptime - hours) * 60);
      String s = fmtI.format(hours) + "小时";
      if (minutes != 0) {
        s += " " + fmtI.format(minutes) + "分";
      }
      return s;
    }
    uptime /= 24;
    long days = (long) uptime;
    long hours = (long) ((uptime - days) * 24);
    String s = fmtI.format(days) + "天";
    if (hours != 0) {
      s += " " + fmtI.format(hours) + "小时";
    }
    return s;
  }

  @GetMapping("/jvm")
  public ResultVO jvm() {
    long totalMem = Runtime.getRuntime().totalMemory();
    long maxMem = Runtime.getRuntime().maxMemory();
    long freeMem = Runtime.getRuntime().freeMemory();

    DecimalFormat df = new DecimalFormat("0.00");

    Map<String, String> map = new HashMap<>();
    map.put("totalMem", df.format(totalMem / 1000000F) + "MB");
    map.put("maxMem", df.format(maxMem / 1000000F) + "MB");
    map.put("freeMem", df.format(freeMem / 1000000F) + "MB");
    map.put("runTime", toDuration());
    return new ResultVO("ok", map, 200);
  }
}
