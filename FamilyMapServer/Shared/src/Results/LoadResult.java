package Results;

public class LoadResult extends Result {
    // gets all its data from the result mother package.

    /**
     *
     * @param message saying whether an error occurred
     * @param success boolean of whether request was successfully completed
     */

    public LoadResult(String message, boolean success) {
        super(message, success);
    }
}
