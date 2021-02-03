package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
    static String algorithm = "DESede";

    EncryptionService es = new EncryptionService();
    StrongAES sa = new StrongAES();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_controller);

        qrText = (EditText)findViewById(R.id.editTextQR);
        genButton = (Button) findViewById(R.id.buttonqr);
        qrImage = (ImageView) findViewById(R.id.imageViewQR);
        String qrstring = new String();
        qrstring =  ((ApplicationState) getApplication()).getStoreCity() + ((ApplicationState) getApplication()).getStoreName() + ((ApplicationState) getApplication()).getStoreID().toString();
        try{
            System.out.println(sa.AESDecrypt(sa.AESEncrypt(qrstring, "Bar12345Bar12345"), "Bar12345Bar12345"));
            byte[] by = sa.AESEncrypt(qrstring,"Bar12345Bar12345");
            String byy = new String();
            for(byte b : by){
                System.out.println(b);
                byy += b;
                byy += " ";
            }
            System.out.println(byy);
            String[] splitted = byy.split(" ");
            byte finalbb[] = new byte[splitted.length];
            Integer i = 0;
            for(String ac : splitted){
                System.out.println(ac);
                byte a = (byte) Integer.parseInt(ac);
                finalbb[i] = a;
                i++;
            }
            /*for(byte ba : splitted){
                System.out.println(ba);
            }*/
            System.out.println(sa.AESDecrypt(finalbb, "Bar12345Bar12345"));
            //System.out.println(plainText);


        }
        catch (Exception e){
            System.out.println("NOOOOOOOOOU");
        }

        ///////////////////////////

        /////////////////////////



        genButton.setOnClickListener(new View.OnClickListener() {
        @Override
         public void onClick(View view){
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

            try{
                BitMatrix bitMatrix = multiFormatWriter.encode(qrText.getText().toString(), BarcodeFormat.QR_CODE,500, 500 );
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                qrImage.setImageBitmap(bitmap);

            }
            catch (Exception e){}
        };
    });
    }
}