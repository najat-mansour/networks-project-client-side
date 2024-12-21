package models;

public class Arp {
    private final Long index;
    private final String mac;

    private final String ip;

    private final String ttl;

    public Arp(Long index, String mac, String ip, String ttl) {
        this.index = index;
        this.mac = mac;
        this.ip = ip;
        this.ttl = ttl;
    }

    public Long getIndex() {
        return index;
    }

    public String getMac() {
        return mac;
    }

    public String getIp() {
        return ip;
    }

    public String getTtl() {
        return ttl;
    }
}
