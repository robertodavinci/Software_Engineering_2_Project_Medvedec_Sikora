package com.example.clup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Entities.TicketState;
import com.example.clup.Entities.Timeslot;
import com.example.clup.Services.Implementation.DatabaseManager;
import com.example.clup.Services.Implementation.Director;
import com.example.clup.Services.Implementation.StoreSelectionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editEmail, editPassword;

    private TextView forgottenPassword;
    private Button loginButton;
    private ProgressBar progressBar;
    private List<String> temp;
    private List<Integer> occupancy;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        editEmail = (EditText) findViewById(R.id.editEmailAddress);
        editPassword = (EditText) findViewById(R.id.editPassword);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        loginButton = (Button) findViewById(R.id.button);
        loginButton.setOnClickListener(this);

        //register = (TextView) findViewById(R.id.registerLink);
        //register.setOnClickListener(this);
        forgottenPassword = (TextView) findViewById(R.id.forgottenPassword);
        forgottenPassword.setOnClickListener(this);

        DatabaseManager db = new DatabaseManager();
        System.out.println("begin");
        db.getStoreCities(new OnGetDataListener(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<String> res = new ArrayList<String>();
                for(DataSnapshot i : dataSnapshot.getChildren()) {
                    res.add(i.getKey());
                }
            }
        });
        db.getStores("Milano, Italy", new OnGetDataListener(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<String> res = new ArrayList<String>();
                for(DataSnapshot i : dataSnapshot.getChildren()) {
                    res.add(i.getKey());
                }
            }
        });
        db.getStoreOcupancy(new Store(0, "Carrefour", "Viale Ungheria 3", "Milano, Italy"), new OnGetDataListener(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                int occupancy = ((Long)dataSnapshot.getValue()).intValue();
            }
        });
        db.getStoreAddresses("Milano, Italy", "Carrefour", new OnGetDataListener(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<String> res = new ArrayList<String>();
                for(DataSnapshot i : dataSnapshot.getChildren()) {
                    res.add(i.getKey());
                }
            }
        });
        db.getTickets(new Store(0, "Carrefour", "Viale Ungheria 3", "Milano, Italy"), new OnGetDataListener(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<String> res = new ArrayList<String>();
                for(DataSnapshot i : dataSnapshot.getChildren()) {
                    //res.add(getTicket(store, i.getKey())); TODO
                    System.out.println(i.getKey());
                }
            }
        });
        db.getMaxTicketId(new Store(0, "Carrefour", "Viale Ungheria 3", "Milano, Italy"), new OnGetDataListener(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                int maxId = 0;
                for(DataSnapshot i : dataSnapshot.getChildren()) {
                    if(Integer.parseInt(i.getKey())>maxId){
                        maxId = Integer.parseInt(i.getKey());
                    }
                }
                System.out.println(maxId);
            }
        });
        /*db.checkCredentials("a@a.a", "a123", new OnCredentialCheckListener() {
            @Override
            public void onSuccess() {
                System.out.println("Success");
            }
            @Override
            public void onFailure() {
                System.out.println("Fail");
            }
        });TODO*/
        Store store = new Store(0, "Carrefour", "Viale Ungheria 3", "Milano, Italy");
        int ticketId = 0;
        db.getTicket(store, String.valueOf(ticketId), new OnGetDataListener(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Ticket tempTicket = new Ticket(ticketId, store, null); // ticketId the same as in new Store, just int instead of string
                for(DataSnapshot i : dataSnapshot.getChildren()) {
                    if(i.getKey().equals("expectedEnter")){
                        tempTicket.setTimeslot(new Timeslot(Timestamp.valueOf(i.getValue().toString())));
                    } else if(i.getKey().equals("ticketState")){
                        switch (i.getValue().toString()){
                            case "WAITING":
                                tempTicket.setTicketState(TicketState.WAITING);
                                break;
                            case "IN_STORE":
                                tempTicket.setTicketState(TicketState.IN_STORE);
                                break;
                            case "EXPIRED":
                                tempTicket.setTicketState(TicketState.EXPIRED);
                                break;
                            default:
                                break;
                        }
                    }
                }
                System.out.println(tempTicket.getId()+ " "+tempTicket.getTicketState());
            }
        });
        //System.out.println("Temp size "+temp.size());
        //db.getStores("Milano, Italy");
        //db.getStoreAddresses("Milano, Italy", "Esselunga");

        //StoreSelectionManager ssm = new StoreSelectionManager();
        //temp = ssm.getStores("Milano, Italy");
        //occupancy = db.getStoreOcupancy(new Store(0, "Carrefour", "Viale Ungheria 3", "Milano, Italy"));
        //occupancy = db.getMaxTicketId(new Store(0, "Carrefour", "Viale Ungheria 3", "Milano, Italy"));
        //System.out.println("Temp size "+temp.size());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
           /* case R.id.registerLink:
                startActivity(new Intent(this, RegisterScreen.class));
                break;*/
            case R.id.button:
                userLogin();
                break;
            case R.id.forgottenPassword:
                startActivity((new Intent(this, ForgotPassword.class)));
                break;
        }
    }

    private void userLogin() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if(email.isEmpty()){
            editEmail.setError("Email must be provided!");
            editEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Email address not valid!");
            editEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editPassword.setError("Password is required!");
            editPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            editPassword.setError("Password has to have at least 6 characters!");
            editPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this, UserProfile.class));
                    } else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your mail to verify the account!", Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toast.makeText(MainActivity.this, "Failed to log in!", Toast.LENGTH_LONG).show();
                }
            }
        });

        progressBar.setVisibility(View.GONE);
    }
}