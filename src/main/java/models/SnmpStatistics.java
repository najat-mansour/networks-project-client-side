package models;

public class SnmpStatistics {
    private final Long index;

    private final String name;

    private final String value;

    public SnmpStatistics(Long index, String name, String value) {
        this.index = index;
        this.name = name;
        this.value = value;
    }

    public Long getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
