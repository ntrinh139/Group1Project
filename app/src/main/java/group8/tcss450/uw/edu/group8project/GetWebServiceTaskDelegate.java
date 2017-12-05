package group8.tcss450.uw.edu.group8project;

/**
 * Created by DavidMkrtychyan on 11/5/17.
 */

public interface GetWebServiceTaskDelegate {

    void handleSuccess();

    void handleFailure(int status);
}
