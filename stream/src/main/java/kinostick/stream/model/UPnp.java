package kinostick.stream.model;

public enum UPnp {
    ADD("add"),
    DELETE("delete");

    private String operation;

    UPnp(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
