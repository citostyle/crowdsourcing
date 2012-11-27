
package tuwien.aic.crowdsourcing.persistence.entities;

public enum Gender implements java.io.Serializable {
    MALE(0), FEMALE(1), UNKNOWN(-1);

    private final Integer value;

    private Gender(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
