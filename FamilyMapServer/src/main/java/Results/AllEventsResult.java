package Results;

import Models.EventModel;

public class AllEventsResult extends Result {
    /**
     * array of event objects
     */
    private EventModel[] data;

    /**
     *
     * @param message saying whether an error occurred
     * @param success boolean of whether request was successfully completed
     * @param data array of event objects to pass as result
     */

    public AllEventsResult(String message, boolean success, EventModel[] data) {
        super(message, success);
        this.data = data;
    }

    public EventModel[] getData() { return data; }
}
