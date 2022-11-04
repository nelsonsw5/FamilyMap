package Results;

public class Result {
    /**
     * saying whether an error occurred, and what it is
     */
    private String message;
    /**
     * boolean of whether request was successfully completed
     */
    private boolean success;

    /**
     *
     * @param message saying whether an error occurred, and what it is
     * @param success boolean of whether request was successfully completed
     */
    public Result(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() { return message; }

    public boolean isSuccess() { return success; }
}
