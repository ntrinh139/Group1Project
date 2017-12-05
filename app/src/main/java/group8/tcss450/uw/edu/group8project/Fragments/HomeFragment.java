package group8.tcss450.uw.edu.group8project.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import group8.tcss450.uw.edu.group8project.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    View v;
    private OnFragmentInteractionListenerHome mListener;
    TextView textViewName;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.fragment_home, container, false);

        return v;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            textViewName = getActivity().findViewById(R.id.welcome);
            String nameFromIntent = getArguments().getString("EMAIL");
            textViewName.setText("Welcome " + nameFromIntent);
            textViewName.setBackgroundColor(Color.WHITE);


            AsyncTask<String, Void, String> task = new WebServiceTask();
            task.execute("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/food/videos/search?excludeingredients=mustard&includeingredients=chicken&maxLength=999&minLength=0&number=10&offset=0&query=chicken+soup");
        }



    }




    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                return event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }




    private class WebServiceTask extends AsyncTask<String, Void, String> {
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
                String s;
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
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            Log.d("TAG", result);
            //mListener.onFragmentInteractionHome(result);
            Log.d("TAG", "IIIIII");
            try {
                JSONObject jsonObject = new JSONObject(result);//.getJSONArray("videos").getJSONObject(0).getString("youTubeId");
                displayVideosListPrompts(jsonObject);
            }catch (Exception e){}

        }
    }

    private void displayVideosListPrompts(JSONObject jsonObject) throws Exception {

        LinearLayout linearLayout = getActivity().findViewById(R.id.home_fragment);
        linearLayout.setBackgroundColor(Color.WHITE);
        final JSONArray videos = jsonObject.getJSONArray("videos");

        for (int i = 0; i < jsonObject.getInt("totalResults"); i++) {

            WebView web = new WebView(getContext());

            web.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, 500));
            web.getSettings().setLoadWithOverviewMode(true);
            web.getSettings().setUseWideViewPort(true);
            //web.scrollTo(0, web.getChildAt(0).getHeight()/2);
            web.setId(i);
            web.loadUrl(videos.getJSONObject(i).getString("thumbnail"));


            final GestureDetector gestureDetector = new GestureDetector(getActivity(), new SingleTapConfirm());

            web.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d("TAG", event.getAction()+"");
                    if (gestureDetector.onTouchEvent(event) ) {
                        try {
                            watchYoutubeVideo(videos.getJSONObject(v.getId()).getString("youTubeId"));
                        } catch (Exception e) {
                        }
                        return true;
                    }
                    return false;
                }
            });



            TextView newTV = new TextView(getContext());
            newTV.setId(i);
            newTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        watchYoutubeVideo(videos.getJSONObject(v.getId()).getString("youTubeId"));
                    }catch(Exception e){}
                }
            });
            newTV.setId(i);

            newTV.setText(videos.getJSONObject(i).getString("shortTitle") + "\n");

            linearLayout.addView(web);
            linearLayout.addView(newTV);

        }

    }


    public void watchYoutubeVideo(String id){

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            getContext().startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            getContext().startActivity(webIntent);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerHome) {
            mListener = (OnFragmentInteractionListenerHome) context;
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
    public interface OnFragmentInteractionListenerHome {
        void onFragmentInteractionHome(String jsonObject);
    }


    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }
}
