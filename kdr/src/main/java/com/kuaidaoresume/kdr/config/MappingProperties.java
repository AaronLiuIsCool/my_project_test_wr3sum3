package com.kuaidaoresume.kdr.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.builder.ToStringStyle.NO_CLASS_NAME_STYLE;

@Getter
@Setter
public class MappingProperties {
  /**
   * Name of the mapping
   */
  private String name;
  /**
   * Path for mapping incoming HTTP requests URIs.
   */
  private String host = "";
  /**
   * List of destination hosts where HTTP requests will be forwarded.
   */
  private List<String> destinations = new ArrayList<>();
  /**
   * Properties responsible for timeout while forwarding HTTP requests.
   */
  private TimeoutProperties timeout = new TimeoutProperties();

  /**
   * Custom properties placeholder.
   */
  private Map<String, Object> customConfiguration = new HashMap<>();

  public MappingProperties copy() {
    MappingProperties clone = new MappingProperties();
    clone.setName(name);
    clone.setHost(host);
    clone.setDestinations(destinations == null ? null : new ArrayList<>(destinations));
    clone.setTimeout(timeout);
    clone.setCustomConfiguration(customConfiguration == null ? null : new HashMap<>(customConfiguration));
    return clone;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, NO_CLASS_NAME_STYLE)
      .append("name", name)
      .append("host", host)
      .append("destinations", destinations)
      .append("timeout", timeout)
      .append("customConfiguration", customConfiguration)
      .toString();
  }


  public static class TimeoutProperties {
    /**
     * Connect timeout for HTTP requests forwarding.
     */
    private int connect = 10000; // temp increase KDR to 10000
    /**
     * Read timeout for HTTP requests forwarding.
     */
    private int read = 40000; // temp increase KDR to 40000

    public int getConnect() {
      return connect;
    }

    public void setConnect(int connect) {
      this.connect = connect;
    }

    public int getRead() {
      return read;
    }

    public void setRead(int read) {
      this.read = read;
    }

    @Override
    public String toString() {
      return new ToStringBuilder(this, NO_CLASS_NAME_STYLE)
        .append("connect", connect)
        .append("read", read)
        .toString();
    }
  }
}

