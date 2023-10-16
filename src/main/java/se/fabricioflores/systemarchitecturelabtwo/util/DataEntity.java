package se.fabricioflores.systemarchitecturelabtwo.util;

public class DataEntity {
    public DataMeta meta;
    public Object data;

    public DataEntity(Object data, String info) {
        this.meta = new DataMeta(info);
        this.data = data;
    }

    public DataEntity() {}
}

