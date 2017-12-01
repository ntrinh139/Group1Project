package group8.tcss450.uw.edu.group8project.Activity;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import group8.tcss450.uw.edu.group8project.R;

public class SingleRecipeActivity extends AppCompatActivity {

    private Button Savebutton;
    private TextView Title;
    private TextView Instructions;
    private ListView Ingredients;
    private ArrayAdapter arrayAdapter;
    private List<String> arrayList = new ArrayList<String>();
    private String sourceUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recipe);

        final Bundle bundle = getIntent().getExtras();
        String json = "";
        if (bundle != null) {
            json = bundle.getString("json");
        }
        try{
            Log.d("parsed", json);
            JSONArray jsonArray = new JSONArray(json);
            Log.d("parsed", jsonArray.toString());
            DisplayFirstStep(jsonArray);
        } catch (Exception e) {}




        FirebaseAuth Auth = FirebaseAuth.getInstance();
        FirebaseUser user = Auth.getCurrentUser();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Users").child(user.getUid());



        Savebutton = (Button) findViewById(R.id.Savebutton);
        Savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.push().setValue(bundle.getInt("id"));
            }
        });
    }

    private void DisplayFirstStep(JSONArray jsonArray) {


        try {
            StringBuffer sb = new StringBuffer();
            JSONArray ingredients = jsonArray.getJSONObject(0).getJSONArray("steps").getJSONObject(0).getJSONArray("ingredients");
            for (int i = 0; i < ingredients.length(); i++) {
                sb.append("-"+ingredients.getJSONObject(i).getString("name")+"\n\n");

            }
            String step = jsonArray.getJSONObject(0).getJSONArray("steps").getJSONObject(0).getString("step");
            String[] array = step.split("\\.");

            StringBuffer sbInstructions = new StringBuffer();
            for (int i = 0; i < array.length; i++) {
                sbInstructions.append("-"+array[i]+".\n\n");
            }
            TextView ingrediantsPrompt = (TextView) findViewById(R.id.ingredinatsPrompt);
            TextView ingrediantsView = (TextView) findViewById(R.id.vIngredinats);

            TextView instructionPrompt = (TextView) findViewById(R.id.instructionsPrompt);
            TextView instructionView = (TextView) findViewById(R.id.vInstructions);


            StringBuffer result = new StringBuffer();
            ingrediantsPrompt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
            ingrediantsPrompt.setTypeface(null, Typeface.BOLD_ITALIC);
            ingrediantsPrompt.setText("Ingrediendts: \n\n");

            ingrediantsView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
            ingrediantsView.setTypeface(null, Typeface.NORMAL);
            ingrediantsView.setText(sb.toString());

            instructionPrompt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
            instructionPrompt.setTypeface(null, Typeface.BOLD_ITALIC);
            instructionPrompt.setText("Instructions: \n\n");

            instructionView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
            instructionView.setTypeface(null, Typeface.NORMAL);
            instructionView.setText(sbInstructions.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

//    public class FoodAPI extends AsyncTask<String, String, String> {
//
//        protected String type = "";
//
//        protected String doInBackground(String... params) {
//
//            type = params[1];
//
//            HttpURLConnection connection = null;
//            BufferedReader reader = null;
//
//            try {
//
//                URL url = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com" + params[0]);
//                connection = (HttpURLConnection) url.openConnection();
//
//                connection.setRequestProperty("X-Mashape-Key", "V0va8PPFQMmshrhuRsVxtq8RDzR9p1saIT5jsnuzvhjsSxaZRl");
//                connection.setRequestProperty("Accept", "application/json");
//
//                try {
//                    connection.setRequestMethod("GET");
//                } catch (ProtocolException e) {
//                    e.printStackTrace();
//                }
//
//                connection.connect();
//
//                InputStream stream = null;
//                try {
//                    stream = connection.getInputStream();
//                } catch (IOException ioe) {
//                    if (connection instanceof HttpURLConnection) {
//                        int statusCode = connection.getResponseCode();
//                        if (statusCode != 200) {
//                            stream = connection.getErrorStream();
//                        }
//                    }
//                }
//
//                reader = new BufferedReader(new InputStreamReader(stream));
//
//                StringBuffer buffer = new StringBuffer();
//                String line = "";
//
//                while ((line = reader.readLine()) != null) {
//                    buffer.append(line+"\n");
//                }
//
//
//                return buffer.toString();
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                System.out.println("MalFunc");
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println("IO");
//            } finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//                try {
//                    if (reader != null) {
//                        reader.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            if (type.equals("")) {
//
//            }
//            else if (type.equals("ingredients")) {
//                SingleRecipeActivity.this.getIngredients(result);
//            }
//            else if (type.equals("instructions")) {
//                SingleRecipeActivity.this.processInstructions(result);
//            }
//            else if (type.equals("title")) {
//                SingleRecipeActivity.this.getTitle(result);
//            }
//        }
//    }
//
//    private void processInstructions (String result) {
//
//        JSONArray array;
//        try {
//            array = new JSONArray(result);
//            JSONObject object = (JSONObject) array.get(0);
//            JSONArray instr = object.getJSONArray("steps");
//            int i;
//            StringBuilder stringBuilder = new StringBuilder();
//            for (i = 0; i < instr.length(); i++) {
//                JSONObject jeej = (JSONObject) instr.get(i);
//                stringBuilder.append("Step " + jeej.getString("number") + ": " + jeej.getString("step") + System.getProperty("line.separator"));
//            }
//            String instructions = stringBuilder.toString();
//            Instructions.setText(instructions);
//
//        } catch (JSONException e) {
//
//            e.printStackTrace();
//        }
//    }
//
//    private void getTitle (String result) {
//
//        try {
//            JSONObject object = new JSONObject(result);
//            Title.setText(object.getString("title"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getIngredients(String result) {
//        arrayList.clear();
//
//        try {
//            JSONObject object = new JSONObject(result);
//            JSONArray array = object.getJSONArray("extendedIngredients");
//
//            for (int i = 0; i < array.length(); i++) {
//                JSONObject list = (JSONObject) array.get(i);
//                arrayList.add(list.getString("originalString"));
//            }
//            arrayAdapter.notifyDataSetChanged();
//
//        } catch (JSONException e) {
//            arrayList.add("No ingredients found");
//            e.printStackTrace();
//        }
//    }
}
