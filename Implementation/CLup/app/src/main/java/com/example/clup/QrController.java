package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Timeslot;
import com.example.clup.Services.Implementation.Director;
import com.google.firebase.database.DataSnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.example.clup.EncryptionService;
import com.example.clup.StrongAES;
import javax.crypto.Cipher;
import com.example.clup.Entities.ApplicationState;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;

public class QrController extends AppCompatActivity {

    private Button checkButton, cancelButton;
    private EditText qrText;
    private ImageView qrImage;
    private TextView storeInfo;
    private TextView ticketNum;
    private TextView ticketStatus;
    private String qrkey;
    private String cipherstring  = new String();
    private String qrString  = new String();
    private String storeCity  = new String();
    private String storeName  = new String();
    private String address  = new String();
    private String storeKey  = new String();
    private String ticketState = new String();
    private Integer peopleAhead;
    private Integer storeID;
    private Integer ticketID;


    StrongAES sa = new StrongAES();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_controller);
        // CHECK APP STATE
        // ((ApplicationState) getApplication()).printAppState();

        ticketNum = (TextView) findViewById(R.id.ticketNum);
        storeInfo = (TextView) findViewById(R.id.storeInfo);
        ticketStatus = (TextView) findViewById(R.id.ticketStatus);
        checkButton = (Button) findViewById(R.id.buttonqr);
        cancelButton = (Button) findViewById(R.id.buttoncan);
        qrImage = (ImageView) findViewById(R.id.imageViewQR);
        Director director = new Director();
        // gets all the Applicatio state data for easier use
        storeID = ((ApplicationState) getApplication()).getStoreID();
        storeCity = ((ApplicationState) getApplication()).getStoreCity();
        storeName = ((ApplicationState) getApplication()).getStoreName();
        address = ((ApplicationState) getApplication()).getAddress();
        storeKey = ((ApplicationState) getApplication()).getStoreKey();
        ticketID = ((ApplicationState) getApplication()).getTicket().getId();
        ticketState = ((ApplicationState) getApplication()).getTicket().getTicketState().toString();

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        ticketNum.setText("Ticket number " + ticketID.toString());
        storeInfo.setText(storeName + ", " + address + ", " + storeCity);
        director.getRequestManager().checkTicket(((ApplicationState) getApplication()).getTicket(), new OnCheckTicketListener() {
            @Override
            public void onWaiting(int peopleAhead) {
                ((ApplicationState) getApplication()).setPeopleAhead(peopleAhead);
                ticketStatus.setText("In queue with " + peopleAhead + " customer"+(peopleAhead==1?"":"s")+" ahead");
            }

            @Override
            public void onActive(Timestamp expireTime) {
                ((ApplicationState) getApplication()).getTicket().setTimeslot(new Timeslot(expireTime));
                ticketStatus.setText("You're up - ticket expires at " + expireTime.toString());
            }
            @Override
            public void onBadStore(String string){
                ticketStatus.setText("Bad store information - reload app");
            }
        });
        // QR string is consisted of storeCity, storeName, StoreID and ticketID
        // after they have been encrpyted, we get several signed integers, which we
        // interleave wtih a character "w"
        // when decoding, this is split based on regex "w"
        qrString = storeCity + ";" + storeName + ";" + storeID + ";" + ticketID.toString();
        try {
            byte[] ciphertext = sa.AESEncrypt(qrString, storeKey);
            for (byte b : ciphertext) {
                //System.out.println(b);
                cipherstring += b;
                cipherstring += "w";
            }
            System.out.println(cipherstring);
           /* System.out.println("KEY");
            System.out.println(sa.AESDecrypt(finalcipher, storeKey));
            System.out.println("KEYEND");
            //System.out.println(plainText);
            */
            // generation of QR code
            BitMatrix bitMatrix = multiFormatWriter.encode(cipherstring, BarcodeFormat.QR_CODE,400 , 400 );
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrImage.setImageBitmap(bitmap);

        } catch (Exception e) {
            System.out.println("Error");
        }
        // Check the current state of the ticket
        checkButton.setOnClickListener(new View.OnClickListener() {
        @Override
         public void onClick(View view){
            director.getRequestManager().checkTicket(((ApplicationState) getApplication()).getTicket(), new OnCheckTicketListener() {
                @Override
                public void onWaiting(int peopleAhead) {
                    ((ApplicationState) getApplication()).setPeopleAhead(peopleAhead);
                    ticketStatus.setText("In queue with " + peopleAhead + " customer"+(peopleAhead==1?"":"s")+" ahead");
                }

                @Override
                public void onActive(Timestamp expireTime) {
                    ((ApplicationState) getApplication()).getTicket().setTimeslot(new Timeslot(expireTime));
                    ticketStatus.setText("You're up - ticket expires at " + expireTime.toString());
                }
                @Override
                public void onBadStore(String string){
                    ticketStatus.setText(string);
                }

            });
        }
        });
        // cancel and delete the ticket
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                director.getRequestManager().cancelTicket(((ApplicationState) getApplication()).getTicket().getStore(), ((ApplicationState) getApplication()).getTicket(), new OnTaskCompleteListener() {
                    @Override
                    public void onSuccess() {
                        ((ApplicationState) getApplication()).clearAppState();
                        System.out.println("Ticket deleted");
                        startActivity((new Intent(view.getContext(), HomeController.class)));
                    }

                    @Override
                    public void onFailure(int err) {
                        System.out.println("Ticket not deleted");
                        startActivity((new Intent(view.getContext(), TicketController.class)));
                    }
                });
            };
        });
    }
    // Sets the action of a back button pressed from Android
    @Override
    public void onBackPressed () {
        startActivity(new Intent(QrController.this, TicketController.class));
        finish();
    }

}