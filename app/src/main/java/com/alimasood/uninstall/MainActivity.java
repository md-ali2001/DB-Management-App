package com.alimasood.uninstall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.internal.framed.FrameReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db ;
    EditText fn,sn,rollno,updateroll;
    EditText df;
    Button reg,updateshow,clearb;
    TextView firestoredata;
    private FrameReader.Handler snapshot;
    String a="";
    boolean h=true;
    FirebaseFirestoreSettings settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();

        clearb=findViewById(R.id.clearb);
        fn=findViewById(R.id.fname);
        sn=findViewById(R.id.sname);
        df=findViewById(R.id.dob);
        rollno=findViewById(R.id.roll);
        firestoredata=findViewById(R.id.data);
        updateroll=findViewById(R.id.updateroll);
        updateshow=findViewById(R.id.updateshow);
        reg=findViewById(R.id.button);
        firestoredata.setMovementMethod(new ScrollingMovementMethod());
        settings=new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();
        db.setFirestoreSettings(settings);












        // Create a new user with a first, middle, and last name


    }
    public void register(View v) {
        if(updateshow.getText().toString()=="UPDATE")
            updateshow.setText("SHOW");
        else
        {}

        String firstname = fn.getText().toString();
        String secondname = sn.getText().toString();
        String dateofbirth = df.getText().toString();
        String rol = rollno.getText().toString();
        Map<String, Object> user = new HashMap<>();
        user.put("first", firstname);
        user.put("last", secondname);
        user.put("born", dateofbirth);
        DocumentReference doc=db.collection("user").document(rol);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                fn.getText().clear();
                fn.setHint("FIRST NAME");

                sn.getText().clear();
                sn.setHint("SECOND NAME");
                df.getText().clear();
                df.setHint("D.O.B ");
                rollno.getText().clear();
                rollno.setHint("ROLL NO.");


                if(documentSnapshot.exists() && h)
                {
                    Toast.makeText(MainActivity.this,"sorry , record already exists",Toast.LENGTH_LONG).show();
                    h=true;


                }
                else {
                    if(firstname==""|| secondname==""||dateofbirth==""||rol=="")
                    {
                        Toast.makeText(MainActivity.this,"please provide complete data",Toast.LENGTH_LONG).show();
                    }
                    else


                        db.collection("user").document(rol).set(user);
                    if(h==true)

                        Toast.makeText(MainActivity.this, "record added successfully", Toast.LENGTH_LONG).show();
                    else
                    {
                        h=true;

                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });









    }


    public void updateshowfunc(View s)
    {
        if (updateshow.getText().toString()=="UPDATE")
        {
            updateshow.setText("SHOW");

            Toast.makeText(MainActivity.this,"UPDATED SUCCESSFULLY",Toast.LENGTH_LONG).show();
            h=false;
            register(s);
        }
        else{



            String b=updateroll.getText().toString();

            DocumentReference doc=db.collection("user").document(b);
            doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists())
                    {
                        fn.setText(documentSnapshot.getString("first"));
                        sn.setText(documentSnapshot.getString("last"));
                        df.setText(documentSnapshot.getString("born"));
                        rollno.setText(b);

                    }
                    else{
                        Toast.makeText(MainActivity.this, "sorry , no record exists", Toast.LENGTH_SHORT).show();
                        updateshow.setText("SHOW");}


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


            updateshow.setText("UPDATE");


        }}

    public void clearf(View v)
    {
        fn.getText().clear();
        fn.setHint("FIRST NAME");

        sn.getText().clear();
        sn.setHint("SECOND NAME");
        df.getText().clear();
        df.setHint("D.O.B ");
        rollno.getText().clear();
        rollno.setHint("ROLL NO.");
        firestoredata.setText("");

    }

    public void delfunc(View v)
    {
        String b=updateroll.getText().toString();
        DocumentReference doc=db.collection("user").document(b);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    doc.delete();
                    Toast.makeText(MainActivity.this, "record deleted successfully", Toast.LENGTH_SHORT).show();}
                else
                {
                    Toast.makeText(MainActivity.this, "sorry , no record exists", Toast.LENGTH_SHORT).show();
                }

            }
        });
        doc.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }


// Add a new document with a generated ID







    public void fetch(View v)
    {
        firestoredata.setText("");
        a="";
        db.collection("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        a+=document.getId()+" "+document.getString("first")+" "+document.getString("last")+" "+document.getString("born")+"\n";


                    }
                    firestoredata.setText(a);

                } else {
                    Toast.makeText(MainActivity.this, "SORRRRRY", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}