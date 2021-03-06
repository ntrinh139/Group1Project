package group8.tcss450.uw.edu.group8project.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import group8.tcss450.uw.edu.group8project.R;

/**
 * This activity is appeared at beggining
 * when first running the class.
 * There is "click to continue" button
 * If the button is clicked -> LoginActivity will launch
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView next=(TextView) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("FoodRecipes", MODE_PRIVATE);
                String currentUserEmail = settings.getString("email", null);
                boolean isLoggedIn = settings.getBoolean("isLoggedIn", false);
                if (currentUserEmail != null && isLoggedIn) {
                    Intent accountsIntent = new Intent(MainActivity.this, DisplayActivity.class);
                    accountsIntent.putExtra("EMAIL",currentUserEmail);
                    startActivity(accountsIntent);
                } else {
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginIntent);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {

    }

}
