package group8.tcss450.uw.edu.group8project.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import group8.tcss450.uw.edu.group8project.R;

public class AllRecipesActivity extends AppCompatActivity {

    String products;
    int id;
    JSONArray object;

    boolean allRecipes;
    JSONArray recipesJsonArray;

    private List<String> recipeArray = new ArrayList<String>();
    private ListView recipes;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_all_recipes);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            products = bundle.getString("products");
            System.out.println(products);
        }

        recipes = (ListView) findViewById(R.id.recipes);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, recipeArray);
        recipes.setAdapter(arrayAdapter);

        try {
            allRecipes = true;
            new FoodAPI().execute("/recipes/findByIngredients?number=100&ingredients=" + URLEncoder.encode(products, "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        recipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

//                JSONObject jsonObject = new JSONObject();
                try {
//                    id = Integer.parseInt(jsonObject.getJSONArray("results").getJSONObject(position).getString("id"));
                    AsyncTask<String, String, String> task = new AllRecipesActivity.FoodAPI();
                    Log.d("TAG", "baller");
                    allRecipes = false;

                    JSONObject result = (JSONObject) recipesJsonArray.get(position);
                    id = result.getInt("id");
                    Log.d("TAG", id+"");
                    task.execute("/recipes/"+id+"/analyzedInstructions?stepBreakdown=false");
                } catch (Exception e) {

                }


            }
        });

    }

    void reloadList(JSONArray array) {
        recipeArray.clear();

        try {
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject result = (JSONObject) array.get(i);
                    recipeArray.add(result.getString("title"));
                    //id = result.getInt("id");
                    arrayAdapter.notifyDataSetChanged();
                }
            } else {
                recipeArray.add("No results");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // https://developer.android.com/reference/android/os/AsyncTask.html
    public class FoodAPI extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
Log.d("Tag", params[0]);
                URL url = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com" + params[0]);
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestProperty("X-Mashape-Key", "V0va8PPFQMmshrhuRsVxtq8RDzR9p1saIT5jsnuzvhjsSxaZRl");
                connection.setRequestProperty("Accept", "application/json");

                try {
                    connection.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }

                connection.connect();

                InputStream stream = null;
                try {
                    stream = connection.getInputStream();
                } catch (IOException ioe) {
                    if (connection instanceof HttpURLConnection) {
                        int statusCode = connection.getResponseCode();
                        if (statusCode != 200) {
                            stream = connection.getErrorStream();
                        }
                    }
                }

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }


                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.out.println("MalFunc");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IO");
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (allRecipes) {
                try {
                    recipesJsonArray = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AllRecipesActivity.this.reloadList(recipesJsonArray);
            } else {


                Log.d("TAG2", result);
                Intent intent = new Intent(AllRecipesActivity.this, SingleRecipeActivity.class);
                intent.putExtra("json", result);
                intent.putExtra("id", id);
                startActivity(intent);
            }


        }
    }
}
