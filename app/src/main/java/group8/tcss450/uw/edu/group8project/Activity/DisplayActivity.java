package group8.tcss450.uw.edu.group8project.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import group8.tcss450.uw.edu.group8project.Fragments.DisplayFragment;
import group8.tcss450.uw.edu.group8project.Fragments.FavoriteFragment;
import group8.tcss450.uw.edu.group8project.Fragments.HomeFragment;
import group8.tcss450.uw.edu.group8project.Fragments.SearchFragment;
import group8.tcss450.uw.edu.group8project.Fragments.SurveyFragment;
import group8.tcss450.uw.edu.group8project.R;

/**
 * This class display user's email and the feature "search recipes"
 * after successfully signing in
 */
public class DisplayActivity extends AppCompatActivity implements SurveyFragment.OnFragmentInteractionListener,
                                    DisplayFragment.OnFragmentInteractionListener2,
                                    SearchFragment.OnFragmentInteractionListener3,
                                    FavoriteFragment.OnFragmentInteractionListener4 {

    private TextView textViewName;
    private SurveyFragment survay;
    private HomeFragment home;
    private SearchFragment search;
    private FavoriteFragment favorite;
    private FirebaseAuth firebaseAuth;
    private Firebase.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        survay = new SurveyFragment();
        home = new HomeFragment();
        search = new SearchFragment();
        favorite = new FavoriteFragment();

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

                    case R.id.search:

                        FragmentTransaction transaction3 = getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.DisplayActivity, search)
                                .addToBackStack(null);
                        transaction3.commit();
                        break;

                    case R.id.favorite:

                        FragmentTransaction transaction4;
                        transaction4 = getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.DisplayActivity, favorite)
                                .addToBackStack(null);
                        transaction4.commit();
                        break;

                }

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DisplayActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle("Confirm Exit");

            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you want to LOG OUT?");

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    dialog.cancel();
                    finish();

                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(main);

                }
            });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String json, int num) {

        DisplayFragment f = new DisplayFragment();
        Bundle args = new Bundle();
        args.putSerializable("Json", json);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

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
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            Intent intent = new Intent(DisplayActivity.this, SingleRecipeActivity .class);
            startActivity(intent);


        }
    }


}