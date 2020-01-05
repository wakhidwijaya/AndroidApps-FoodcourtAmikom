package id.foodcourt.feature.order.active

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import id.foodcourt.R
import id.foodcourt.data.response.OrderActive
import java.lang.Integer.sum

class OrderActiveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_active)

        val db = FirebaseFirestore.getInstance()
        val query = db!!.collection("transactions").document()

    }

    private inner class OrderActiveViewHoder internal constructor(private val view : View) : RecyclerView.ViewHolder(view){
        internal fun setOrderActive(orderactive : OrderActive){
            val nameresto = orderactive.name
            val total = orderactive.total_price
            val time = orderactive.time
        }
    }
}


