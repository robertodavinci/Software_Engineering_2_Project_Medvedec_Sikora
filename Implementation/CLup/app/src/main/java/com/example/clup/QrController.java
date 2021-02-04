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
import java.util.Base64;

public class QrController extends AppCompatActivity {

    Button genButton;
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
        //genButton = (Button) findViewById(R.id.buttonqr);
        qrImage = (ImageView) findViewById(R.id.imageViewQR);
        Director director = new Director();

        storeID = ((ApplicationState) getApplication()).getStoreID();
        storeCity = ((ApplicationState) getApplication()).getStoreCity();
        storeName = ((ApplicationState) getApplication()).getStoreName();
        address = ((ApplicationState) getApplication()).getAddress();
        storeKey = ((ApplicationState) getApplication()).getStoreKey();
        ticketID = ((ApplicationState) getApplication()).getTicketID();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        ticketNum.setText(ticketID.toString());
        storeInfo.setText(storeName + ", " + address + ", " + storeCity);
        ticketStatus.setText("You're up!");

        qrString = storeCity + ";" + storeName + ";" + storeID;
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

    /*    genButton.setOnClickListener(new View.OnClickListener() {
        @Override
         public void onClick(View view){

        };
    });
    }
    */
    }
}