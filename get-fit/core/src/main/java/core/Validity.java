package core;

/**
 * Validity of a EntryBuilder.
 */
public class Validity {
    /**
     * Whether the builder is valid.
     */
    private final boolean isValid;
    /**
     * The reason for the validity of the builder.
     */
    private final String validityReason;

    /**
     * Object representing the validity of an EntryBuilder.
     *
     * @param valid  whether the builder is valid.
     * @param reason "ok" if valid,
     *               otherwise the reason the builder is not valid.
     */
    public Validity(final boolean valid, final String reason) {
        this.isValid = valid;
        this.validityReason = reason;
    }

    /**
     * Whether the builder is valid.
     *
     * @return the validity
     */
    public boolean invalid() {
        return !this.isValid;
    }

    /**
     * The reason for the validity of the builder.
     *
     * @return the reason.
     */
    public String reason() {
        return this.validityReason;
    }
}
