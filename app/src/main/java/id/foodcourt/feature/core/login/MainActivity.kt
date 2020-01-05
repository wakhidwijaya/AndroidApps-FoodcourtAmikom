package id.foodcourt.feature.core.login

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import id.foodcourt.R
import id.foodcourt.feature.customer.dashboard.DashboardCustomerActivity
import id.foodcourt.feature.customer.restaurants.RestaurantListActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()


        btn_login.setOnClickListener {
//            val intent = Intent(this, RestaurantListActivity::class.java)
//            startActivity(intent)
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if (email.isBlank()){
                Toast.makeText(applicationContext, "Email Required ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isBlank()){
                Toast.makeText(applicationContext, "Password Required ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signIn(it, et_email.text.toString(), et_password.text.toString())
            finish()

        }
    }

    fun signIn(view: View, email: String, password: String){
        showMessage(view,"Authenticating...")

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
            if(task.isSuccessful){
                val currentuser = FirebaseAuth.getInstance().currentUser!!.uid
                checkRule(uid = currentuser )
                } else{
                showMessage(view,"Error: ${task.exception?.message}")
            }
        })
     }

    fun checkRule(uid: String){

       val db = FirebaseFirestore.getInstance()

        db.collection("customers")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { documents ->
                when (documents.size()) {
                    1 -> {
                        val intent = Intent(this, DashboardCustomerActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            .addOnFailureListener { exception ->
//                Log.w(Tag, "Error getting documents: ", exception)
            }

//        db.collection("restaurants")
//            .whereEqualTo("uid", uid)
//            .get()
//            .addOnSuccessListener { documents ->
//                when (documents.size()) {
//                    1 -> {
//                        val intent = Intent(this, DashboardCustomerActivity::class.java)
//                        startActivity(intent)
//                    }
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents: ", exception)
//            }

    }

    fun showMessage(view:View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
    }
}
