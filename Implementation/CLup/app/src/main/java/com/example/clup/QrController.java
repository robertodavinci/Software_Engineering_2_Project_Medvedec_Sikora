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

public class QrController extends AppCompatActivity {

    Button genButton;
    EditText qrText;
    ImageView qrImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_controller);

        qrText = (EditText)findViewById(R.id.editTextQR);
        genButton = (Button) findViewById(R.id.buttonqr);
        qrImage = (ImageView) findViewById(R.id.imageViewQR);

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