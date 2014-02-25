package cn.redinfo.chenzhi.Fantasy;

import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: orinchen
 * Date: 12-12-13
 * Time: 下午4:08
 */
public class Sessions {
  private HashMap<String, Object> sessions = null;
  private static Sessions instance = null;

  public static Sessions getInstance() {
    if (instance == null)
      instance = new Sessions();
    return instance;
  }

  private Sessions() {
    this.sessions = new HashMap<String, Object>(16);
  }

  public void set(String name, Object value) {
    this.sessions.put(name, value);
  }

  public void set(String name, String value) {
    this.sessions.put(name, value);
  }

  public void set(String name, int value) {
    this.sessions.put(name, value);
  }

  public void set(String name, Date value) {
    this.sessions.put(name, value);
  }

  public void set(String name, float value) {
    this.sessions.put(name, value);
  }

  public Object getObject(String name) {
    if (!this.sessions.containsKey(name))
      return null;
    return this.sessions.get(name);
  }

//    public <T> T get(String name, TypeToken<T> typeToken){
//        Object o = this.getObject(name);
//        Type t = TypeToken.get(o.getClass()).getType();
//        token.equals(o.getClass());
//    }

  public <T> T getSession(String name, Class<T> clazz) {
    Object o = this.getObject(name);
    if (o == null || o.getClass() != clazz)
      return null;
    clazz.isAssignableFrom(o.getClass());
    return clazz.cast(o);
  }

  public String getString(String name) {
    return this.getSession(name, String.class);
  }

  public Float getFloat(String name) {
    return this.getSession(name, Float.class);
  }

  public Integer getInt32(String name) {
    return this.getSession(name, Integer.class);
  }

  public Date getDate(String name) {
    return this.getSession(name, Date.class);
  }
}
