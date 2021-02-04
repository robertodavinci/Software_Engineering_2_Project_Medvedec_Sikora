package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;

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

    Button genButton, cancelButton;
    EditText qrText;
    ImageView qrImage;
    TextView storeInfo;
    TextView ticketNum;
    TextView ticketStatus;
    String qrkey;
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


    EncryptionService es = new EncryptionService();
    StrongAES sa = new StrongAES();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_controller);

        ticketNum = (TextView) findViewById(R.id.ticketNum);
        storeInfo = (TextView) findViewById(R.id.storeInfo);
        ticketStatus = (TextView) findViewById(R.id.ticketStatus);
        genButton = (Button) findViewById(R.id.buttonqr);
        cancelButton = (Button) findViewById(R.id.buttoncan);
        qrImage = (ImageView) findViewById(R.id.imageViewQR);
        Director director = new Director();

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
        });


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
            BitMatrix bitMatrix = multiFormatWriter.encode(cipherstring, BarcodeFormat.QR_CODE,350, 350 );
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrImage.setImageBitmap(bitmap);

        } catch (Exception e) {
            System.out.println("NOOOOOOOOOU");
        }

        genButton.setOnClickListener(new View.OnClickListener() {
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
            });
        };
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                director.getRequestManager().cancelTicket(((ApplicationState) getApplication()).getTicket().getStore(), ((ApplicationState) getApplication()).getTicket(), new OnTaskCompleteListener() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Ticket deleted");
                    }

                    @Override
                    public void onFailure() {
                        System.out.println("Ticket not deleted");
                    }
                });
            };
        });
    }

}