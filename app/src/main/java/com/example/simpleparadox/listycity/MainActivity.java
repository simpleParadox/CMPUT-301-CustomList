package com.example.simpleparadox.listycity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Declare the variables so that you will be able to reference it later.
    ListView cityList;
    ArrayAdapter<City> cityAdapter;
    ArrayList<City> cityDataList;
    String TAG = "Sample";
    Button addCityButton;
    EditText addCityEditText;
    EditText addProvinceEditText;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addCityButton = findViewById(R.id.add_city_button);
        addCityEditText = findViewById(R.id.add_cty_field);
        addProvinceEditText = findViewById(R.id.add_province_edit_text);

//         Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();


        // Get a reference to the ListView and create an object for the city list.
        cityList = findViewById(R.id.city_list);
        cityDataList = new ArrayList<>();

        // Get a top-level reference to the collection.
        final CollectionReference collectionReference = db.collection("Cities");

        // Set the adapter for the listView to the CustomAdapter that we created in Lab 3.
        cityAdapter = new CustomList(MainActivity.this, cityDataList);
        cityList.setAdapter(cityAdapter);

// TODO: RMOVE THE FOLLOWING COMMENTS
        //Now using the collection reference, we will fetch the data (if any) and display it in the ListView.
//        collectionReference
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for (DocumentSnapshot doc : task.getResult()) {
//                    Log.d(TAG, String.valueOf(doc.getData().get("province_name")));
//                    String city = doc.getId();
//                    String province = (String) doc.getData().get("province_name");
//                    cityDataList.add(new City(city, province)); // Adding the cities and provinces from Firestore.
//                }
//                cityAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud.
//            }
//        });

        // Now listening to all the changes in the database and get notified, note that offline support is enabled by default
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                // clear the old list
                cityDataList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    Log.d(TAG, String.valueOf(doc.getData().get("province_name")));
                    String city = doc.getId();
                    String province = (String) doc.getData().get("province_name");
                    cityDataList.add(new City(city, province)); // Adding the cities and provinces from FireStore.
                }
                cityAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud.
            }
        });

        // Adding an onClickListener to the button.
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Retrieving the city name and the province name from the EditText fields.
                final String cityName = addCityEditText.getText().toString();
                final String provinceName = addProvinceEditText.getText().toString();

                // We use a HashMap to store a key-value pair in firestore. Can you guess why? Because it's a No-SQL database.
                HashMap<String, String> data = new HashMap<>();
                if(cityName.length()>0 && provinceName.length()>0){ // We do not add anything if either of the fields are empty.

                    // If there is some data in the EditText field, then we create a new key-value pair.
                    data.put("province_name",provinceName);

                    // The set method sets a unique id for the document.
                    collectionReference
                            .document(cityName)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is successful.
                                    Log.d(TAG, "Data addition successful");

// TODO: RMOVE THE FOLLOWING COMMENTS

//                                    cityDataList.add(new City(cityName, provinceName));
//                                    cityAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new values in the ListView.
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // This method gets executed if there is any problem.
                                    Log.d(TAG, "Data addition failed" + e.toString());
                                }
                            });

                    // Setting the fields to null so the user can add a new city.
                    addCityEditText.setText("");
                    addProvinceEditText.setText("");
                }
            }
        });
    }
}
