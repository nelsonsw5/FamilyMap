package Results;

public class ClearResult extends Result {
    // gets everything from the result mother package

    /**
     *
     * @param message saying whether an error occurred
     * @param success boolean of whether request was successfully completed
     */
    public ClearResult(String message, boolean success) {
        super(message, success);
    }
}
