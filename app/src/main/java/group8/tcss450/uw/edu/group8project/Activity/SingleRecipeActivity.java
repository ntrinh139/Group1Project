package group8.tcss450.uw.edu.group8project.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import group8.tcss450.uw.edu.group8project.R;

public class SingleRecipeActivity extends AppCompatActivity {

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


        FloatingActionButton savebutton = (FloatingActionButton) findViewById(R.id.Savebutton);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SingleRecipeActivity.this, "Recipe Saved",
                        Toast.LENGTH_SHORT).show();
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

            TextView titleView = (TextView) findViewById(R.id.vTitle);
            TextView ingrediantsView = (TextView) findViewById(R.id.vIngredinats);
            TextView instructionView = (TextView) findViewById(R.id.vInstructions);

            titleView.setText("Recipe Detail");
            ingrediantsView.setText(sb.toString());
            instructionView.setText(sbInstructions.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
