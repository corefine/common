package org.corefine.common.cache.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * cache config
 */
@Configuration
@ConfigurationProperties("spring.cache")
public class CacheProperties {
    private String host;
    private Integer port;
    private String password;
    private Integer maxIdle;
    private Integer maxTotal;
    private Integer database;
    private CacheL2Properties l2;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }

    public CacheL2Properties getL2() {
        return l2;
    }

    public void setL2(CacheL2Properties l2) {
        this.l2 = l2;
    }
}
