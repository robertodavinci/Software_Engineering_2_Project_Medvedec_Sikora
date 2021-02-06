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

import com.example.clup.Entities.ApplicationState;
import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Services.Implementation.DatabaseManager;
import com.example.clup.Services.Implementation.Director;
import com.example.clup.Services.Implementation.RequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LoginController extends AppCompatActivity implements View.OnClickListener {

    private EditText editEmail, editPassword;

    private TextView forgottenPassword;
    //private TextView register:
    private Button loginButton;
    private ProgressBar progressBar;
    private List<String> temp;
    private List<Integer> occupancy;
    Director director = new Director();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_controller);
        // CHECK APP STATE
        ((ApplicationState) getApplication()).printAppState();

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
        RequestManager requestManager = new RequestManager();
        String qrCodeText = "Milano, Italy; Carrefour; Viale Ungheria 3; 0; 8";
        Store store = new Store(0, "Carrefour", "Viale Ungheria 3", "Milano, Italy");
        Ticket ticket = new Ticket(10, store);
        //director.getStoreManager().closeStore(store);
        //director.getStoreManager().openStore(store);
        //director.getStoreManager().updateQueue(store);
       /* director.getRequestManager().cancelTicket(store, ticket, new OnTaskCompleteListener() {
            @Override
            public void onSuccess() {
                System.out.println("Successful cancel");
            }

            @Override
            public void onFailure() {
                System.out.println("Faaaaaail");
            }
        });
        */

        /*StoreManager storeManager = new StoreManager();
        storeManager.openStore(store);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                storeManager.closeStore(store);
            }
        }, 5000);   //5 seconds
        */
        /*requestManager.getTicket(store, new OnGetTicketListener() {
            @Override
            public void onSuccess(Ticket ticket) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        db.persistEnter(store, ticket);
                    }
                }, 5000);   //5 seconds
            }

            @Override
            public void onFailure() {
                System.out.println("Fail to get ticket");
            }
        });*/

       /* db.getStoreCities(new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<String> res = new ArrayList<String>();
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    res.add(i.getKey());
                }
            }
        });
        director.getStoreSelectionManager().getStores("Milano, Italy", new OnGetDataListener(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<String> res = new ArrayList<String>();
                for(DataSnapshot i : dataSnapshot.getChildren()) {
                    res.add(i.getKey());
                }
                System.out.println("bla "+res.size());
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

        */
        /*int ticketId = 0;
        db.getTickets(store, new OnGetDataListener(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<Ticket> res = new ArrayList<Ticket>();
                for(DataSnapshot i : dataSnapshot.getChildren()) {
                    int ticketId = Integer.parseInt(i.getKey());
                    String ticketId_str = String.valueOf(Integer.parseInt(i.getKey()));
                    db.getTicket(store, ticketId_str, new OnGetDataListener(){
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
                            res.add(tempTicket);
                        }
                    });
                }
                //res
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
        director.getLoginManager().manageLogin("toma.petar.sikora@gmail.com", "Toma123", UserType.STORE_MANAGER, new OnCredentialCheckListener() {
            @Override
            public void onSuccess() {
                System.out.println("Success");
            }
            @Override
            public void onFailure() {
                System.out.println("Fail");
            }
        });
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
        });*/
        //Ticket ticket = new Ticket(6, store, new Timeslot(Timestamp.valueOf("2018-09-01 09:01:16")));
        //System.out.println("Persist ticket");
        //db.persistTicket(ticket);
        //ticket.setId(7);
        //db.persistTicket(ticket);
        /*db.getTickets(store, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<Ticket> tickets = new ArrayList<>();
                for(DataSnapshot ticket : dataSnapshot.getChildren()){
                    int ticketId = Integer.parseInt(ticket.getKey());
                    Timeslot timeslot = new Timeslot(Timestamp.valueOf(ticket.child("expectedEnter").getValue().toString()));
                    Ticket t = new Ticket(ticketId, store, timeslot);
                    switch (ticket.child("ticketState").getValue().toString()){
                        case "WAITING":
                            t.setTicketState(TicketState.WAITING);
                            break;
                        case "IN_STORE":
                            t.setTicketState(TicketState.IN_STORE);
                            break;
                        case "EXPIRED":
                            t.setTicketState(TicketState.EXPIRED);
                            break;
                        default:
                            System.err.println("Error with ticketstate"+ticket.child("ticketState").toString());
                    }
                    tickets.add(t);
                }
            }
        });*/

        /*requestManager.getTicket(store, new OnGetTicketListener() {
            @Override
            public void onSuccess(Ticket ticket) {

            }
        });*/
        //storeManager.manageExit(store);
        /*storeManager.manageEntrance(qrCodeText, new OnTaskCompleteListener() {
            @Override
            public void onSuccess() {
                System.out.println("Qr code check result is: Success");
            }

            @Override
            public void onFailure() {
                System.out.println("Qr code check result is: Fail");
            }
        });*/
        /*System.out.println("Get ticket");
        requestManager.getTicket(store, new OnGetTicketListener() {
            @Override
            public void onSuccess(Ticket ticket) {
                System.out.println(ticket.getId()+ " " + ticket.getTimeslot().toString());
                db.persistEnter(store, ticket);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        db.persistExit(store);
                    }
                }, 5000);   //5 seconds
            }
        });*/

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
            /*case R.id.registerLink:
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


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        startActivity(new Intent(LoginController.this, StoreManagerController.class));
                    }
                    else{
                        user.sendEmailVerification();
                        Toast.makeText(LoginController.this, "Check your mail to verify the account!", Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toast.makeText(LoginController.this, "Failed to log in!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}