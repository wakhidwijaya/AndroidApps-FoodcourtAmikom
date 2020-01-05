package id.foodcourt.feature.customer.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import id.foodcourt.R
import id.foodcourt.data.request.OrderMenu
import id.foodcourt.data.response.Menu
import id.foodcourt.data.response.Restaurant
import id.foodcourt.feature.customer.restaurants.RestaurantListActivity
import id.foodcourt.feature.customer.restaurants.menu.MenuCustomerActivity
import kotlinx.android.synthetic.main.activity_order_customer.*

class OrderCustomerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_customer)

        var list = intent.getParcelableArrayListExtra<OrderMenu>("ORDERLIST")

        val adapterMenu = OrderAdapter(list)
        rv_order.layoutManager = LinearLayoutManager(this)
        rv_order.adapter = adapterMenu

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
