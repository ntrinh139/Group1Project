package group8.tcss450.uw.edu.group8project;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class display user's email and the feature "search recipes"
 * after successfully signing in
 */
public class DisplayActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener, DisplayFragment.OnFragmentInteractionListener2{

    private TextView textViewName;
    private SearchFragment survay;
    private HomeFragment home;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        survay = new SearchFragment();
        home = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable("EMAIL", getIntent().getStringExtra("EMAIL"));
        home.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.DisplayActivity, home)
                .addToBackStack(null);
        transaction.commit();

        BottomNavigationView bar = (BottomNavigationView) findViewById(R.id.navigation);
        bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               ;

                switch (item.getItemId()){
                    case R.id.survey:

                        FragmentTransaction transaction = getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.DisplayActivity, survay)
                                .addToBackStack(null);
                        transaction.commit();
                        break;


                    case R.id.home:

                        Bundle args = new Bundle();
                        args.putSerializable("EMAIL", getIntent().getStringExtra("EMAIL"));
                        home.setArguments(args);
                        FragmentTransaction transaction2 = getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.DisplayActivity, home)
                                .addToBackStack(null);
                        transaction2.commit();
                        break;

                }




                return true;
            }
        });



    }


    @Override
    public void onFragmentInteraction(String json, int num) {
//        textViewName.setText("Please click on the recipe to display instructions:");

        DisplayFragment f = new DisplayFragment();
        Bundle args = new Bundle();
        args.putSerializable("Json", json.toString());
        args.putSerializable("num", num);
        f.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.DisplayActivity, f)
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction2(JSONObject jsonObject, int recipeIndex) {
        try {
            int id = Integer.parseInt(jsonObject.getJSONArray("results").getJSONObject(recipeIndex).getString("id"));
            AsyncTask<String, Void, String> task = new WebServiceTask();
            task.execute("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/"+id+"/analyzedInstructions?stepBreakdown=false");
        } catch (Exception e) {

        }
    }

    public android.support.v4.app.Fragment getVisibleFragment(){
        FragmentManager fragmentManager = DisplayActivity.this.getSupportFragmentManager();
        AtomicReference<List<Fragment>> fragments = new AtomicReference<List<Fragment>>();
        fragments.set(fragmentManager.getFragments());
        if(fragments.get() != null){
            for(android.support.v4.app.Fragment fragment : fragments.get()){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getVisibleFragment() instanceof  SearchFragment) {
            String nameFromIntent = getIntent().getStringExtra("EMAIL");
            textViewName.setText("Welcome " + nameFromIntent+"\n\nRecipe Search:");
        } else if (getVisibleFragment() instanceof DisplayFragment) {
            textViewName.setText("Please click on the recipe to display instructions:");
        }else {
            Toast.makeText(getApplicationContext(), "else", Toast.LENGTH_LONG)
                    .show();
        }
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
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            DisplaySingleRecipe f = new DisplaySingleRecipe();
            Bundle args = new Bundle();
            args.putSerializable("recipeDetails", result);
            f.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.DisplayActivity, f)
                    .addToBackStack(null);
            // Commit the transaction
            transaction.commit();


        }
    }

}