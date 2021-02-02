package com.example.clup.Services.Implementation;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Entities.TicketState;
import com.example.clup.Entities.Timeslot;
import com.example.clup.Services.DatabaseManagerService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatabaseManager implements DatabaseManagerService {
    static private DatabaseManager singleton = null;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference storeReference;
    private FirebaseAuth mAuth;


    private boolean loginSuccess;
    private int temp, maxId;
    private Ticket tempTicket;

    @Override
    public List<String> getStoreCities(){
        List<String> res = new ArrayList<String>();
        storeReference = firebaseDatabase.getReference("Stores");
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot i : dataSnapshot.getChildren()) {
                    res.add(i.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
        return res;
    }
    @Override
    public List<String> getStores(String city){
        List<String> res = new ArrayList<String>();
        storeReference = firebaseDatabase.getReference("Stores/"+city);
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot i : dataSnapshot.getChildren()) {
                    res.add(i.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
        return res;
    }
    @Override
    public List<Integer> getStoreOcupancy(Store store) {
        List<Integer> occupancy = new ArrayList<>();
        storeReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId());
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot i : dataSnapshot.getChildren()) {
                    if(i.getKey().equals("occupancy")){
                        occupancy.add(((Long)i.getValue()).intValue());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
        return occupancy;
    }

    @Override
    public List<String> getStoreAddresses(String city, String name){
        List<String> res = new ArrayList<String>();
        storeReference = firebaseDatabase.getReference("Stores/"+city+"/"+name);
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Store store = dataSnapshot.getValue(Store.class);
                for(DataSnapshot i : dataSnapshot.getChildren()) {
                    res.add(i.getKey());
                    System.out.println(i.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
        return res;
    }

    @Override
    public List<Ticket> getTickets(Store store) {
        List<Ticket> res = new ArrayList<Ticket>();
        storeReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId()+"/Tickets");
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot i : dataSnapshot.getChildren()) {
                    res.add(getTicket(store, i.getKey()));
                    System.out.println(i.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
        return res;
    }

    @Override
    public List<Integer> getMaxTicketId(Store store) {
        List<Integer> maxTicket = new ArrayList<>();
        maxId = 0;
        storeReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId()+"/Tickets");
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot i : dataSnapshot.getChildren()) {
                    if(Integer.parseInt(i.getKey())>maxId){
                        maxId = Integer.parseInt(i.getKey());
                    }
                }
                maxTicket.add(maxId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
        return maxTicket;
    }

    @Override
    public Boolean checkCredentials(String email, String password){
        Boolean b = false;
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        temp = 0;
                    } else{
                        user.sendEmailVerification();
                        //res = false;
                    }
                } else{
                    //res = false;
                }
            }
        });
        return b;
    }

    @Override
    public Ticket getTicket(Store store, String ticketId) {
        storeReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId()+"/Tickets"+ticketId);
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                    System.out.println(i.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
        tempTicket.setStore(store);
        tempTicket.setId(Integer.getInteger(ticketId));
        return tempTicket;
    }

    static public DatabaseManager getInstance() {
        if(singleton==null){
            singleton = new DatabaseManager();
        }
        return singleton;
    }


    // TODO: delete all tickets at store closing time
}
