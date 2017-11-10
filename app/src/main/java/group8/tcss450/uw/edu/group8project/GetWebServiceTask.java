package group8.tcss450.uw.edu.group8project;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by DavidMkrtychyan on 11/5/17.
 */

public class GetWebServiceTask extends AsyncTask<String, Void, Integer> {
    public GetWebServiceTaskDelegate delegate = null;
        @Override
        protected Integer doInBackground(String... strings) {
            if (strings.length != 3) {
                throw new IllegalArgumentException("Three String arguments required.");
            }
            Integer response = null;
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            String emailQuery = "?email=" + strings[1];
            String passwordQuery = "&password=" + strings[2];
            try {
                URL urlObject = new URL(url + emailQuery + passwordQuery);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                int responseCode = urlConnection.getResponseCode();
                response = responseCode;
            } catch (Exception e) {
                //Toast to the user that there was a network error.
                response = 500;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }

    @Override
    protected void onPostExecute(Integer result) {
        // Something wrong with the network or the URL.
        if(result < 300) {
            delegate.handleSuccess();
        } else {
            delegate.handleFailure("Error");
        }
    }
}

