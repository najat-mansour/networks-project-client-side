package models;

public class Tcp {
    private final Long index;

    private final String state;

    private final String sourceIp;

    private final String sourcePort;

    private final String destinationIp;

    private final String destinationPort;

    public Tcp(Long index, String state, String sourceIp, String sourcePort, String destinationIp, String destinationPort) {
        this.index = index;
        this.state = state;
        this.sourceIp = sourceIp;
        this.sourcePort = sourcePort;
        this.destinationIp = destinationIp;
        this.destinationPort = destinationPort;
    }

    public Long getIndex() {
        return index;
    }

    public String getState() {
        return state;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public String getDestinationPort() {
        return destinationPort;
    }
}
