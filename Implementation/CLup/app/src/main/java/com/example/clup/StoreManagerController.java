package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.budiyev.android.codescanner.CodeScannerView;
import com.example.clup.Entities.ApplicationState;
import com.example.clup.Entities.Store;
import com.example.clup.Services.Implementation.Director;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

public class StoreManagerController extends AppCompatActivity {

    private Button closeStore, openStore;
    private TextView storeInfo3, storeOpenText;
    private Integer one = 1;
    private Boolean storeOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manager_controller);
        Director director = new Director();
        closeStore = findViewById(R.id.closeStore);
        openStore = findViewById(R.id.openStore);
        storeInfo3 = findViewById(R.id.storeInfo3);
        storeOpenText = findViewById(R.id.storeOpen);
        System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());
        System.out.println(((ApplicationState) getApplication()).getStoreName());
        System.out.println(((ApplicationState) getApplication()).getStoreID());
        System.out.println(((ApplicationState) getApplication()).getStoreCity());
        storeInfo3.setText(((ApplicationState) getApplication()).getStoreName() + ", " + ((ApplicationState) getApplication()).getAddress() + ", " + ((ApplicationState) getApplication()).getStoreCity());

        director.getDatabaseManager().getStoreOpen(new Store(((ApplicationState) getApplication()).getStoreID(), ((ApplicationState) getApplication()).getStoreName(), ((ApplicationState) getApplication()).getStoreCity()), new OnGetDataListener() {

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String op = (dataSnapshot.getValue().toString());
                if (op.equals("1")) ((ApplicationState) getApplication()).setStoreOpened(true);
                else ((ApplicationState) getApplication()).setStoreOpened(false);
                storeOpen = ((ApplicationState) getApplication()).getStoreOpened();
                //System.out.println(String.valueOf(dataSnapshot.getValue()));
                //System.out.println(dataSnapshot.getValue().toString());
                //System.out.println(storeOpen.toString());
                if (storeOpen == false) {
                    openStore.setBackgroundColor(ContextCompat.getColor(StoreManagerController.this, R.color.white));
                    // openStore.setTextColor(ContextCompat.getColor(StoreManagerController.this, R.color.colorPrimaryDark));
                    openStore.setClickable(true);
                    closeStore.setBackgroundColor(ContextCompat.getColor(StoreManagerController.this, R.color.greyed_out));
                    closeStore.setClickable(false);
                    storeOpenText.setText("The store is closed");
                }
                else {
                    closeStore.setBackgroundColor(ContextCompat.getColor(StoreManagerController.this, R.color.white));
                    closeStore.setClickable(true);
                    openStore.setBackgroundColor(ContextCompat.getColor(StoreManagerController.this, R.color.greyed_out));
                    openStore.setClickable(false);
                    storeOpenText.setText("The store is open");
                }
            }
        });



    }
}