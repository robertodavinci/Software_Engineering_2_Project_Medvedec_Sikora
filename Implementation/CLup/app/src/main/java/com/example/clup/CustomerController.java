package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clup.Entities.ApplicationState;
import com.example.clup.Entities.Store;
import com.example.clup.Services.Implementation.Director;
import com.google.firebase.database.DataSnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.sql.Timestamp;

public class CustomerController extends AppCompatActivity {

    TextView storeInfo4, storeStatus3, scanStatus;
    Button scanTicket, storeExit;
    Director director = new Director();
    int LAUNCH_SECOND_ACTIVITY = 1;
    StrongAES sa = new StrongAES();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_controller);
        storeInfo4 = findViewById(R.id.storeInfo4);
        storeStatus3 = findViewById(R.id.storeStatus3);
        scanTicket = findViewById(R.id.scanTicket);
        storeExit = findViewById(R.id.storeExit);
        scanStatus = findViewById(R.id.scanStatus);
        storeInfo4.setText(((ApplicationState) getApplication()).getStoreName() + ", " + ((ApplicationState) getApplication()).getAddress() + ", " + ((ApplicationState) getApplication()).getStoreCity());

        director.getDatabaseManager().getStore(new Store(((ApplicationState) getApplication()).getStoreID(), ((ApplicationState) getApplication()).getStoreName(), ((ApplicationState) getApplication()).getStoreCity()), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
               //Integer maxCustomers =  Integer.valueOf(String.valueOf(dataSnapshot.child("maxNoCustomers").getValue()));
               String maxCus = (String.valueOf(dataSnapshot.child("maxNoCustomers").getValue()));
               String occ = (String.valueOf(dataSnapshot.child("occupancy").getValue()));
                ((ApplicationState) getApplication()).setStoreKey(String.valueOf(dataSnapshot.child("key").getValue()));
               Integer maxCustomers = Integer.valueOf(maxCus);
               Integer occupancy = Integer.valueOf(occ);
               Integer availability = maxCustomers - occupancy;
               System.out.println(maxCustomers);
               System.out.println(occupancy);
               storeStatus3.setText(availability.toString());
            }
        });

        scanTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                System.out.println("GO TO SCAN");

                startActivityForResult((new Intent(view.getContext(), ScannerController.class)), LAUNCH_SECOND_ACTIVITY);
            };
        });

        storeExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                System.out.println("EXIT STORE 1");
            };
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                String qrcode = data.getStringExtra("result");
               // Toast.makeText(CustomerController.this, qrcode, Toast.LENGTH_SHORT).show();

                try {
                    String[] splitqr = qrcode.split("w");
                    byte[] plaintext = new byte[splitqr.length];;



                    Integer i = 0;
                    for(String ac : splitqr){
                        //System.out.println(ac);
                        byte a = (byte) Integer.parseInt(ac);
                        plaintext[i] = a;
                        i++;
                    }
                    String decrypted = sa.AESDecrypt(plaintext ,((ApplicationState) getApplication()).getStoreKey());
                    System.out.println(decrypted);
                    Toast.makeText(CustomerController.this, decrypted, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    System.out.println("NOOOOOOOOOU");
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                scanStatus.setText("Bad scan - try again");
            }
        }
    }//onActivityResult

}