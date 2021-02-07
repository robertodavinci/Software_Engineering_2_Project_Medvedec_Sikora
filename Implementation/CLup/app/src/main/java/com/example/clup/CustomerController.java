package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clup.Entities.ApplicationState;
import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Services.Implementation.Director;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CustomerController extends AppCompatActivity {

    private TextView storeInfo4, storeStatus3, scanStatus;
    private Button scanTicket, storeExit;
    private AnimationDrawable animation;
    private FrameLayout loadscreen;
    private Director director = new Director();
    private int LAUNCH_SECOND_ACTIVITY = 1;
    private StrongAES sa = new StrongAES();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_controller); // Setting activity view to the controller
        storeInfo4 = findViewById(R.id.storeInfo4);
        storeStatus3 = findViewById(R.id.storeStatus3);
        scanTicket = findViewById(R.id.scanTicket);
        storeExit = findViewById(R.id.storeExit);
        scanStatus = findViewById(R.id.scanStatus);            // end setting
        ImageView loading = (ImageView) findViewById(R.id.loading2);  // Loading animation
        loadscreen = (FrameLayout) findViewById(R.id.loadscreen);     //
        loading.setBackgroundResource(R.drawable.loading);            //
        animation = (AnimationDrawable)loading.getBackground();       //
        animation.start();                                            // Loading animation code start
        storeInfo4.setText(((ApplicationState) getApplication()).getStoreName() + ", " + ((ApplicationState) getApplication()).getAddress() + ", " + ((ApplicationState) getApplication()).getStoreCity());

        // Scheduler that checks and updates store queue every 10 seconds
        ScheduledExecutorService scheduler;
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        director.getStoreManager().updateQueue(new Store(((ApplicationState) getApplication()).getStoreID(), ((ApplicationState) getApplication()).getStoreName(), ((ApplicationState) getApplication()).getStoreCity()), new OnTaskCompleteListener() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onFailure(int errCode) {
                            }
                        });
                        updateStoreStatus(); // used for printing store status on the display
                    }
                }, 0, 10, TimeUnit.SECONDS);

        // sets button listener
        scanTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // goes to scanner controller and waits for the return value (scanned ticket)
                startActivityForResult((new Intent(view.getContext(), ScannerController.class)), LAUNCH_SECOND_ACTIVITY);
            };
        });

        storeExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                director.getStoreManager().manageExit(new Store(((ApplicationState) getApplication()).getStoreID(), ((ApplicationState) getApplication()).getStoreName(), ((ApplicationState) getApplication()).getStoreCity()), new OnTaskCompleteListener() {
                    @Override
                    public void onSuccess() {
                        director.getStoreManager().updateQueue(new Store(((ApplicationState) getApplication()).getStoreID(), ((ApplicationState) getApplication()).getStoreName(), ((ApplicationState) getApplication()).getStoreCity()), new OnTaskCompleteListener() {
                            @Override
                            public void onSuccess() {
                                updateStoreStatus();
                                scanStatus.setText("Exit confirmed");
                            }

                            @Override
                            public void onFailure(int errCode) {

                            }
                        });
                    }
                    @Override
                    public void onFailure(int err) {
                        scanStatus.setText("Exit not confirmed - error");
                    }
                });
            }
        });

    }
    // return value from the scanner - return string is decrypted and used to validate ticket
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                String qrcode = data.getStringExtra("result");

                try {
                    String[] splitqr = qrcode.split("w");
                    byte[] plaintext = new byte[splitqr.length];;
                    Integer i = 0;
                    for(String ac : splitqr){
                        byte a = (byte) Integer.parseInt(ac);
                        plaintext[i] = a;
                        i++;
                    }
                    String decrypted = sa.AESDecrypt(plaintext ,((ApplicationState) getApplication()).getStoreKey());
                    String[] ticketFields = decrypted.split(";");
                    Ticket ticket = new Ticket(Integer.parseInt(ticketFields[3]), new Store(Integer.parseInt(ticketFields[2]), ticketFields[1], ticketFields[0]));
                    director.getStoreManager().manageEntrance(decrypted, new OnTaskCompleteListener() {
                        @Override
                        public void onSuccess() {
                            scanStatus.setText("Ticket valid - entrance allowed");
                            director.getStoreManager().updateQueue(new Store(((ApplicationState) getApplication()).getStoreID(), ((ApplicationState) getApplication()).getStoreName(), ((ApplicationState) getApplication()).getStoreCity()), new OnTaskCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    updateStoreStatus();
                                }

                                @Override
                                public void onFailure(int errCode) {

                                }
                            });
                        }
                        @Override
                        public void onFailure(int err) {
                            if(err == 1)
                                scanStatus.setText("Invalid QR code");
                            else if (err == 2)
                                scanStatus.setText("Invalid ticket - not active yet");
                            else
                                scanStatus.setText("Scan error");
                        }
                    });

                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                scanStatus.setText("Bad scan - try again");
            }
        }
    }
    // updates UI regarding the store status
    public void updateStoreStatus(){
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
                //System.out.println(maxCustomers);
                //System.out.println(occupancy);
                storeStatus3.setText(availability.toString());
                animation.stop();
                loadscreen.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(DatabaseError databaseError){
            }
        });
    }
    // Sets the action of a back button pressed from Android
    @Override
    public void onBackPressed () {
        startActivity(new Intent(CustomerController.this, StoreManagerController.class));
        finish();
    }

}