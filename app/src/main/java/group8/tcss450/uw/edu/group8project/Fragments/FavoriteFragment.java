package group8.tcss450.uw.edu.group8project.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
import java.util.List;

import group8.tcss450.uw.edu.group8project.R;

public class FavoriteFragment extends Fragment {

    int id;
    private ListView favorites;
    private List<String> recipeArray = new ArrayList<>();

    private ArrayAdapter arrayAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;


    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_favorite, container, false);

        favorites = v.findViewById(R.id.favorites);
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, (ArrayList) recipeArray);
        favorites.setAdapter(arrayAdapter);


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        myRef = database.getReference("Users").child(user.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recipeArray.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String value = String.valueOf(child.getValue());
                    new FoodAPI().execute("/recipes/" + value + "/summary");
                    arrayAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed to read value." + databaseError.toException());
            }
        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (!recipeArray.contains(ds.getKey()) && ds.child("Favorited By")
                            .child(user.getUid()).exists()) {
                        recipeArray.add(ds.getKey());
                        arrayAdapter.notifyDataSetChanged();
                    }
                }

                final ListView lv_fav_recipes = v.findViewById(R.id.favorites);
                lv_fav_recipes.setAdapter(arrayAdapter);
                lv_fav_recipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        try {

                        } catch (Exception e) {

                        }

                    }
                });
            }

            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });

        return v;
    }

    void reloadList(JSONObject object) {

        try {
            if (object != null) {
                String result = object.getString("title");
                recipeArray.add(result);
                arrayAdapter.notifyDataSetChanged();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public interface OnFragmentInteractionListener4 {
    }

    // https://developer.android.com/reference/android/os/AsyncTask.html
    public class FoodAPI extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

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
                    buffer.append(line + "\n");
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


            JSONObject object = null;
            try {
                object = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            FavoriteFragment.this.reloadList(object);
        }
    }
}