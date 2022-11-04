package Results;

import Models.PersonModel;

public class AllPersonsResult extends Result {
    /**
     * array of all person objects
     */
    private PersonModel[] data;

    /**
     *
     * @param message saying whether there's an error
     * @param success boolean whether request was successful or not
     * @param data array of all person objects
     */
    public AllPersonsResult(String message, boolean success, PersonModel[] data) {
        super(message, success);
        this.data = data;
    }

    public PersonModel[] getData() {
        return data;
    }
}
