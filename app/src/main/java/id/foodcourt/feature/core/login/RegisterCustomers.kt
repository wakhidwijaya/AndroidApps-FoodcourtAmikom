package id.foodcourt.feature.core.login

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import id.foodcourt.R
import id.foodcourt.feature.customer.dashboard.DashboardCustomerActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register_customers.*

class RegisterCustomers : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_customers)

        auth = FirebaseAuth.getInstance()
        val database = FirebaseFirestore.getInstance()

        btn_register.setOnClickListener {
            val nama = et_name_register.text.toString()
            val email = et_email_register.text.toString()
            val password = et_password_register.text.toString()

            if (email.isBlank()){
                Toast.makeText(applicationContext, "Email Required ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isBlank()){
                Toast.makeText(applicationContext, "Password Required ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult>{task ->
                if(task.isSuccessful){
                    val currentuser = FirebaseAuth.getInstance().currentUser!!.uid

                    val customers = hashMapOf(
                        "nama" to nama,
                        "email" to email,
                        "uid" to currentuser
                    )
                    database.collection("customers").document().set(customers)
                        .addOnSuccessListener {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener { e -> Log.w("Register", "Error writing document", e) }
                }
                else{
                    Log.d("Error", "error register")
                }
            }
            )
        }


//        btn_register.setOnClickListener{
//            auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("SUCCESS", "createUserWithEmail:success");
//                            FirebaseUser user = mAuth . getCurrentUser ();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("ERROR", "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(
//                                EmailPasswordActivity.this, "Authentication failed.",
//                                Toast.LENGTH_SHORT
//                            ).show();
//                            updateUI(null);
//                        }
//
//                    }
//                }
//    }
}
}