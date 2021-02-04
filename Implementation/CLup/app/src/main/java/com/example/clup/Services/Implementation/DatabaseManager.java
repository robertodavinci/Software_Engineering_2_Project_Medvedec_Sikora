package com.example.clup.Services.Implementation;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Entities.TicketState;
import com.example.clup.Entities.Timeslot;
import com.example.clup.OnCredentialCheckListener;
import com.example.clup.OnGetDataListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class DatabaseManager implements DatabaseManagerService {
    static private DatabaseManager singleton = null;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference storeReference;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    private boolean loginSuccess;
    private int temp, maxId;
    private Ticket tempTicket;

    @Override
    public void getStoreCities(OnGetDataListener onGetDataListener){
        storeReference = firebaseDatabase.getReference("Stores");
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onGetDataListener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }
    @Override
    public void getStores(String city, OnGetDataListener onGetDataListener){
        storeReference = firebaseDatabase.getReference("Stores/"+city);
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onGetDataListener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }
    @Override
    public void getStore(Store store, OnGetDataListener onGetDataListener) {
        storeReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId());
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onGetDataListener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }
    @Override
    public void getStoreOcupancy(Store store, OnGetDataListener onGetDataListener) {
        storeReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId()+"/occupancy/");
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onGetDataListener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }

    @Override
    public void getStoreOpen(Store store, OnGetDataListener onGetDataListener) {
        storeReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId()+"/open/");
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onGetDataListener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }

    @Override
    public void getStoreAddresses(String city, String name, OnGetDataListener onGetDataListener){
        storeReference = firebaseDatabase.getReference("Stores/"+city+"/"+name);
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onGetDataListener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }

    @Override
    public void getTickets(Store store, OnGetDataListener onGetDataListener) {
        storeReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId()+"/Tickets");
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onGetDataListener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }

    @Override
    public void getMaxTicketId(Store store, OnGetDataListener onGetDataListener) {
        storeReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId()+"/maxId");
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onGetDataListener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }

    @Override
    public void getStoreMaxNoCustomers(Store store, OnGetDataListener onGetDataListener) {
        storeReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId()+"/maxNoCustomers/");
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onGetDataListener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }

    @Override
    public void checkCredentials(String email, String password, OnCredentialCheckListener onCredentialCheckListener){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        onCredentialCheckListener.onSuccess();
                    } else{
                        user.sendEmailVerification();
                        onCredentialCheckListener.onFailure();
                    }
                } else{
                    onCredentialCheckListener.onFailure();
                }
            }
        });
    }

    @Override
    public void getTicket(Store store, String ticketId, OnGetDataListener onGetDataListener) {
        storeReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId()+"/Tickets/"+ticketId);
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onGetDataListener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }

    static public DatabaseManager getInstance() {
        if(singleton==null){
            singleton = new DatabaseManager();
        }
        return singleton;
    }

    @Override
    public void persistTicketQrCodeString(Ticket ticket) {
        DatabaseReference updateReference = firebaseDatabase.getReference("Stores/"+ticket.getStore().getCity()+"/"+ticket.getStore().getName()+"/"+ticket.getStore().getId() + "/Tickets/"+ticket.getId()+"/qrurl");
        updateReference.setValue(ticket.getQrCode());
    }

    @Override
    public void persistExit(Store store){
        storeReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId());
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference updateReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId() + "/occupancy");
                if(((Long)dataSnapshot.child("occupancy").getValue()).intValue()>0) updateReference.setValue(((Long)dataSnapshot.child("occupancy").getValue()).intValue()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }

    @Override
    public void persistEnter(Store store, Ticket ticket) {
        DatabaseReference updateReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId() + "/Tickets/"+ticket.getId());
        updateReference.setValue(null);
        storeReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId());
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference updateReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId() + "/occupancy");
                updateReference.setValue(((Long)dataSnapshot.child("occupancy").getValue()).intValue()+1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }

    @Override
    public void getStoreKey(Store store, OnGetDataListener onGetDataListener) {
        storeReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId()+"/key/");
        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onGetDataListener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }

    @Override
    public void persistTicket(Ticket ticket) {
        System.out.println("Persist ticket db");
        Store store = ticket.getStore();
        DatabaseReference updateReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId() + "/Tickets/");
        Map<String, Object> updateParent = new HashMap<>();
        Map<String, Object> updateChildren = new HashMap<>();
        updateChildren.put("expires", ticket.getTimeslot().getExpectedEnter().toString());
        updateChildren.put("qrurl", ticket.getQrCode());
        updateChildren.put("ticketState", ticket.getTicketState());
        updateParent.put(String.valueOf(ticket.getId()), updateChildren);
        System.out.println("Parent "+updateParent);
        System.out.println(updateReference);
        updateReference.updateChildren(updateParent);
        getMaxTicketId(store, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                DatabaseReference updateReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId() + "/maxId");
                updateReference.setValue(Integer.parseInt(dataSnapshot.getValue().toString())+1);
            }
        });
    }

    // TODO: delete all tickets at store closing time
    @Override
    public void openStore(Store store) {
        DatabaseReference updateReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId() + "/open");
        updateReference.setValue(1);
        updateReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId() + "/occupancy");
        updateReference.setValue(0);
        updateReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId() + "/maxId");
        updateReference.setValue(0);
    }
    @Override
    public void closeStore(Store store) {
        DatabaseReference updateReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId() + "/open");
        updateReference.setValue(0);
        updateReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId() + "/occupancy");
        updateReference.setValue(0);
        updateReference = firebaseDatabase.getReference("Stores/"+store.getCity()+"/"+store.getName()+"/"+store.getId() + "/Tickets/");
        updateReference.setValue(null);
    }
}
