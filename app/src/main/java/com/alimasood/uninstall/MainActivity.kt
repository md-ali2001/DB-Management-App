package com.alimasood.uninstall

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestoreSettings
import android.os.Bundle
import com.alimasood.uninstall.R
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.squareup.okhttp.internal.framed.FrameReader
import java.util.*

class MainActivity : AppCompatActivity() {
    var db: FirebaseFirestore? = null
    var fn: EditText? = null
    var sn: EditText? = null
    var rollno: EditText? = null
    var updateroll: EditText? = null
    var df: EditText? = null
    var reg: Button? = null
    var updateshow: Button? = null
    var clearb: Button? = null
    var firestoredata: TextView? = null
    private val snapshot: FrameReader.Handler? = null
    var a = ""
    var h = true
    var settings: FirebaseFirestoreSettings? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = FirebaseFirestore.getInstance()
        clearb = findViewById(R.id.clearb)
        fn = findViewById(R.id.fname)
        sn = findViewById(R.id.sname)
        df = findViewById(R.id.dob)
        rollno = findViewById(R.id.roll)
        firestoredata = findViewById(R.id.data)
        updateroll = findViewById(R.id.updateroll)
        updateshow = findViewById(R.id.updateshow)
        reg = findViewById(R.id.button)
        //firestoredata.setMovementMethod(ScrollingMovementMethod())
        settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        db!!.firestoreSettings = settings!!


        // Create a new user with a first, middle, and last name
    }

    fun register(v: View?) {
        if (updateshow!!.text.toString() === "UPDATE") updateshow!!.text = "SHOW" else {
        }
        val firstname = fn!!.text.toString()
        val secondname = sn!!.text.toString()
        val dateofbirth = df!!.text.toString()
        val rol = rollno!!.text.toString()
        val user: MutableMap<String, Any> = HashMap()
        user.put("first", firstname)
        user.put("last", secondname)
        user.put("born", dateofbirth)
        val doc = db!!.collection("user").document(rol)
        doc.get().addOnSuccessListener { documentSnapshot ->
            fn!!.text.clear()
            fn!!.hint = "FIRST NAME"
            sn!!.text.clear()
            sn!!.hint = "SECOND NAME"
            df!!.text.clear()
            df!!.hint = "D.O.B "
            rollno!!.text.clear()
            rollno!!.hint = "ROLL NO."
            if (documentSnapshot.exists() && h) {
                Toast.makeText(this@MainActivity, "sorry , record already exists", Toast.LENGTH_LONG).show()
                h = true
            } else {
                if (firstname === "" || secondname === "" || dateofbirth === "" || rol === "") {
                    Toast.makeText(this@MainActivity, "please provide complete data", Toast.LENGTH_LONG).show()
                } else db!!.collection("user").document(rol).set(user)
                if (h == true) Toast.makeText(this@MainActivity, "record added successfully", Toast.LENGTH_LONG).show() else {
                    h = true
                }
            }
        }.addOnFailureListener { }
    }

    fun updateshowfunc(s: View?) {
        if (updateshow!!.text.toString() === "UPDATE") {
            updateshow!!.text = "SHOW"
            Toast.makeText(this@MainActivity, "UPDATED SUCCESSFULLY", Toast.LENGTH_LONG).show()
            h = false
            register(s)
        } else {
            val b = updateroll!!.text.toString()
            val doc = db!!.collection("user").document(b)
            doc.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    fn!!.setText(documentSnapshot.getString("first"))
                    sn!!.setText(documentSnapshot.getString("last"))
                    df!!.setText(documentSnapshot.getString("born"))
                    rollno!!.setText(b)
                } else {
                    Toast.makeText(this@MainActivity, "sorry , no record exists", Toast.LENGTH_SHORT).show()
                    updateshow!!.text = "SHOW"
                }
            }.addOnFailureListener { }
            updateshow!!.text = "UPDATE"
        }
    }

    fun clearf(v: View?) {
        fn!!.text.clear()
        fn!!.hint = "FIRST NAME"
        sn!!.text.clear()
        sn!!.hint = "SECOND NAME"
        df!!.text.clear()
        df!!.hint = "D.O.B "
        rollno!!.text.clear()
        rollno!!.hint = "ROLL NO."
        firestoredata!!.text = ""
    }

    fun delfunc(v: View?) {
        val b = updateroll!!.text.toString()
        val doc = db!!.collection("user").document(b)
        doc.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                doc.delete()
                Toast.makeText(this@MainActivity, "record deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "sorry , no record exists", Toast.LENGTH_SHORT).show()
            }
        }
        doc.get().addOnFailureListener { }
    }

    // Add a new document with a generated ID
    fun fetch(v: View?) {
        firestoredata!!.text = ""
        a = ""
        db!!.collection("user").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    a += """${document.id} ${document.getString("first")} ${document.getString("last")} ${document.getString("born")}
"""
                }
                firestoredata!!.text = a
            } else {
                Toast.makeText(this@MainActivity, "SORRRRRY", Toast.LENGTH_SHORT).show()
            }
        }
    }
}