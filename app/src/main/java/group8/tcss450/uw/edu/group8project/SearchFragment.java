package group8.tcss450.uw.edu.group8project;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchFragment extends Fragment {


    protected OnFragmentInteractionListener mListener;
    private int numOfRecipes;

    private static final String PARTIAL_URL
            = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/searchComplex?";

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        Button search = (Button) v.findViewById(R.id.button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncTask<String, Void, String> task = new TestWebServiceTask();


                StringBuffer sb = new StringBuffer(PARTIAL_URL);

                TextView tv1 = (TextView) getActivity().findViewById(R.id.editTextCuisine);
                if (!tv1.getText().toString().isEmpty()) {
                    sb.append("cuisine="+tv1.getText().toString());
                }

                TextView tv2 = (TextView) getActivity().findViewById(R.id.editTextDiet);
                if (!tv2.getText().toString().isEmpty()) {
                    sb.append("&diet="+tv2.getText().toString());
                }

                TextView tv3 = (TextView) getActivity().findViewById(R.id.editTextExcludeIngredients);
                if (!tv3.getText().toString().isEmpty()) {
                    String [] ex = tv3.getText().toString().split(",");
                    sb.append("&excludeIngredients="+ex[0]);
                    for (int i = 1; i < ex.length; i++){
                        sb.append("%2C+"+ex[i]);
                    }
                }

                TextView tv4 = (TextView) getActivity().findViewById(R.id.editTextIncludeIngredients);
                if (!tv4.getText().toString().isEmpty()) {
                    String [] in = tv4.getText().toString().split(",");
                    sb.append("&includeIngredients="+in[0]);
                    for (int i = 1; i < in.length; i++){
                        sb.append("%2C+"+in[i]);
                    }
                }

                sb.append("&limitLicense=false");

                TextView tv5 = (TextView) getActivity().findViewById(R.id.editTextNumber);
                if (!tv5.getText().toString().isEmpty()) {
                    sb.append("&number="+tv5.getText().toString());
                    try {
                        numOfRecipes = Integer.parseInt(tv5.getText().toString());
                        sb.append("&offset=0");

                        TextView tv6 = (TextView) getActivity().findViewById(R.id.editTextQuery);
                        if (!tv6.getText().toString().isEmpty()) {
                            sb.append("&query="+tv6.getText().toString());
                        }

                        sb.append("&ranking=1");


                        task.execute(sb.toString().replaceAll(" ", ""));
                    } catch (Exception e) {
                        tv5.setError("Number of Recipes have to be numeric");
                    }

                }else {
                    tv5.setError("Number of Recipes is missing");
                }

            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String json, int num);
    }

    private class TestWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = null;
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            try {
                URL urlObject = new URL(url);

                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-Mashape-Key", "V0va8PPFQMmshrhuRsVxtq8RDzR9p1saIT5jsnuzvhjsSxaZRl");
                urlConnection.setRequestProperty("Accept", "application/json");


                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            if(mListener != null) {
                mListener.onFragmentInteraction(result, numOfRecipes);
            }


        }
    }

}