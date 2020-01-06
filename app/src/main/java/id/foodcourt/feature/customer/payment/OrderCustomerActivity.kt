package id.foodcourt.feature.customer.payment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import id.foodcourt.R
import id.foodcourt.data.request.OrderMenu
import id.foodcourt.feature.customer.dashboard.DashboardCustomerActivity
import id.foodcourt.utils.Config.TABLE
import kotlinx.android.synthetic.main.activity_order_customer.*
import java.lang.Integer.sum


class OrderCustomerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_customer)
        val intent = intent
        var list = intent.getParcelableArrayListExtra<OrderMenu>("ORDERLIST")
        val nameresto = intent.getStringExtra("RestaurantName")
        val uidresto = intent.getStringExtra("RestaurantUid")

        val adapterMenu = OrderAdapter(list)
        rv_order.layoutManager = LinearLayoutManager(this)
        rv_order.adapter = adapterMenu

        val db = FirebaseAuth.getInstance().currentUser
        val database = FirebaseFirestore.getInstance()
        val email = db!!.email
        val name = db!!.displayName
        val uid = db!!.uid
        val tablenum = intent.getStringExtra("table")
        var menus : MutableList<HashMap<String, Any?>> = mutableListOf()


        val pricetotal = findViewById<TextView>(R.id.tv_total_price)


        for (i in list){
            val order = hashMapOf(
                "menu" to i.menu,
                "name" to nameresto,
                "price" to i.price * i.qty,
                "qty" to i.qty,
                "uid" to uidresto
            )
            val price = i.price * i.qty
            menus.add(order)
        }

        btn_checkout.setOnClickListener {
            val customer = hashMapOf(
                "email" to email,
                "name" to name,
                "status" to 0,
                "table" to tablenum.toInt(),
                "uid" to uid
            )

            val timestamp = Timestamp.now().seconds.toString()
            database.collection("transactions").document(timestamp).set(customer)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        menus.forEachIndexed { i, it ->
                            database.collection("transactions").document(timestamp)
                                .collection("orders")
                                .document(i.toString())
                                .set(it)
                                .addOnSuccessListener {
                                    val intent = Intent(this, DashboardCustomerActivity::class.java)
                                    startActivity(intent)
                                    Log.d("Dokumen", "Menu berhasil document")
                                }
                                .addOnFailureListener { e -> Log.w("Dokumen", "Menu Error writing document", e) }
                        }
                    }

                }
                .addOnFailureListener { e -> Log.w("Dokumen", "Error writing document", e) }

        }

    }


    private inner class OrderAdapter (private val listdata : List<OrderMenu>) : RecyclerView.Adapter<OrderViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
            return OrderViewHolder(view)
        }

        override fun getItemCount() = listdata.size


        override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
            holder.setOrder(listdata.get(position))
        }
    }

    private inner class OrderViewHolder (orderview : View) : RecyclerView.ViewHolder(orderview){

        val tvmenu = orderview.findViewById<TextView>(R.id.tv_menu)
        val tvqty = orderview.findViewById<TextView>(R.id.tv_qty)
        val tvprice = orderview.findViewById<TextView>(R.id.tv_price)

        fun setOrder(order : OrderMenu){
            tvmenu.text = order.menu
            tvqty.text = order.qty.toString()
            var price = order.qty * order.price
            tvprice.text = price.toString()
        }
    }

}
