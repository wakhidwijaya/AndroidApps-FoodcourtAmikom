package id.foodcourt.feature.core.login

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
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

        }
    }

    fun signIn(view: View, email: String, password: String){
        showMessage(view,"Authenticating...")

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
            if(task.isSuccessful){
                val intent = Intent(this, DashboardCustomerActivity::class.java)
                startActivity(intent)
            } else{
                showMessage(view,"Error: ${task.exception?.message}")
            }
        })

    }

    fun showMessage(view:View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
    }
}
