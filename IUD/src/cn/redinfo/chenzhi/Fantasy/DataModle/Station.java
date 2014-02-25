package cn.redinfo.chenzhi.Fantasy.DataModle;

public final class Station {

  private Terminal terminal = null;

  private Sites station = null;

  public Terminal getTerminal() {
    return terminal;
  }

  public void setTerminal(Terminal terminal) {
    this.terminal = terminal;
  }

  public Sites getSite() {
    return station;
  }

  public void setSite(Sites site) {
    this.station = site;
  }
}
