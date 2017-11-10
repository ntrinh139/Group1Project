package group8.tcss450.uw.edu.group8project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import static group8.tcss450.uw.edu.group8project.R.id.edittextEmail;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView next=(TextView) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
//
//                    Intent accountsIntent = new Intent(getApplicationContext(), DisplayActivity.class);
//                    accountsIntent.putExtra("EMAIL", mAuth.getCurrentUser().getEmail());
//                    startActivity(accountsIntent);
//
//                } else {
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginIntent);
//                }
            }
        });
    }

}
