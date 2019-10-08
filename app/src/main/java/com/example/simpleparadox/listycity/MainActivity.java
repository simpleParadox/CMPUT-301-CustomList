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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Declare the variables so that you will be able to reference it later.
    ListView cityList;
    ArrayAdapter<City> cityAdapter;
    ArrayList<City> cityDataList;
    String TAG = "Sample";
    Button addCityButton;
    EditText addCityText;
    EditText addProvinceText;
    CustomList customList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityList = findViewById(R.id.city_list);
        addCityButton = findViewById(R.id.add_city_button);
        addCityText = findViewById(R.id.add_city_field);
        addProvinceText = findViewById(R.id.add_province_edit_text);

//        String []cities ={"Edmonton", "Vancouver", "Toronto", "Hamilton", "Denver", "Los Angeles"};
//        String []provinces = {"AB", "BC", "ON", "ON", "CO", "CA"};
        db = FirebaseFirestore.getInstance();
        final CollectionReference citiesRef = db.collection("cities");
        cityDataList = new ArrayList<>();


        cityAdapter = new CustomList(this, cityDataList);

        cityList.setAdapter(cityAdapter);


        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String cityName = addCityText.getText().toString();
                final String provinceName = addProvinceText.getText().toString();

                HashMap<String, String> data = new HashMap<>();
                if (cityName.length() > 0 && provinceName.length() > 0){
                    data.put("province", provinceName);

                    citiesRef.document(cityName)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "data is added to firestore");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Error: "+e.getMessage());
                                }
                    });

                    addCityText.setText("");
                    addProvinceText.setText("");
                }
            }
        });

        citiesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                cityDataList.clear();

                for (DocumentSnapshot city: queryDocumentSnapshots){
                    Log.d(TAG, String.valueOf(city.getData().get("province")));
                    String cityName = city.getId();
                    String provinceName = (String) city.getData().get("province");
                    cityDataList.add(new City(cityName, provinceName));
                }
                cityAdapter.notifyDataSetChanged();
            }
        });
//        dataList = new ArrayList<>();
//        dataList.addAll(Arrays.asList(cities));
//
//        cityAdapter = new ArrayAdapter<>(this, R.layout.content, dataList);
//
//        cityList.setAdapter(cityAdapter);



    }


}
