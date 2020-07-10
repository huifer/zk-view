package org.huifer.zkview.model;

import java.math.BigDecimal;

public class ZookeeperState {


  private int minLatency = -1;

  private BigDecimal avgLatency = new BigDecimal("-1");
  private int maxLatency = -1;
  private long received = -1;
  private long sent = -1;
  private int outStanding = -1;
  private long zxid = -1;
  private String mode = null;
  private int nodeCount = -1;
  private int totalWatches = -1;
  private int clientNumber = -1;

  public int getClientNumber() {
    return clientNumber;
  }

  public void setClientNumber(int clientNumber) {
    this.clientNumber = clientNumber;
  }

  public int getMinLatency() {
    return minLatency;
  }

  public void setMinLatency(int minLatency) {
    this.minLatency = minLatency;
  }

  public BigDecimal getAvgLatency() {
    return avgLatency;
  }

  public void setAvgLatency(BigDecimal avgLatency) {
    this.avgLatency = avgLatency;
  }

  public int getMaxLatency() {
    return maxLatency;
  }

  public void setMaxLatency(int maxLatency) {
    this.maxLatency = maxLatency;
  }

  public long getReceived() {
    return received;
  }

  public void setReceived(long received) {
    this.received = received;
  }

  public long getSent() {
    return sent;
  }

  public void setSent(long sent) {
    this.sent = sent;
  }

  public int getOutStanding() {
    return outStanding;
  }

  public void setOutStanding(int outStanding) {
    this.outStanding = outStanding;
  }

  public long getZxid() {
    return zxid;
  }

  public void setZxid(long zxid) {
    this.zxid = zxid;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public int getNodeCount() {
    return nodeCount;
  }

  public void setNodeCount(int nodeCount) {
    this.nodeCount = nodeCount;
  }

  public int getTotalWatches() {
    return totalWatches;
  }

  public void setTotalWatches(int totalWatches) {
    this.totalWatches = totalWatches;
  }
}
