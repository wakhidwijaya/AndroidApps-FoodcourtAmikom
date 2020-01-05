package id.foodcourt.feature.customer.restaurants

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import id.foodcourt.data.response.Restaurant
import id.foodcourt.feature.customer.restaurants.menu.MenuCustomerActivity
import kotlinx.android.synthetic.main.activity_restaurant_list.*


class RestaurantListActivity : AppCompatActivity() {

    lateinit var db : FirebaseFirestore
    private var adapterRest: RestaurantAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)
        rv_Restaurant.layoutManager = LinearLayoutManager(this)
        db = FirebaseFirestore.getInstance()

        val query = db!!.collection("restaurants")
        val options = FirestoreRecyclerOptions.Builder<Restaurant>().setQuery(query, Restaurant::class.java).build()

        adapterRest = RestaurantAdapter(options)

        rv_Restaurant.adapter = adapterRest
    }

    private inner class RestaurantAdapter internal constructor(options: FirestoreRecyclerOptions<Restaurant>) : FirestoreRecyclerAdapter<Restaurant, RestaurantViewHolder>(options) {
        override fun onBindViewHolder(productViewHolder: RestaurantViewHolder, position: Int, restaurant: Restaurant) {
            productViewHolder.setProductName(restaurant.name)
            productViewHolder.itemView.setOnClickListener {
                val tablenum = intent.getStringExtra("table")
                Toast.makeText(this@RestaurantListActivity, restaurant.name, Toast.LENGTH_SHORT)
                val intent = Intent(this@RestaurantListActivity, MenuCustomerActivity::class.java)
                intent.putExtra("RestaurantName", restaurant.name)
                intent.putExtra("RestaurantUid", getSnapshots().getSnapshot(position).id)
                intent.putExtra("table", tablenum)
                startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
            return RestaurantViewHolder(view)
        }
    }

    private inner class RestaurantViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view) {
        internal fun setProductName(productName: String) {
            val textView = view.findViewById<TextView>(R.id.tv_list_restaurant)
            textView.text = productName
        }
    }

    override fun onStart() {
        super.onStart()
        adapterRest!!.startListening()
    }

    override fun onStop() {
        super.onStop()

        if (adapterRest != null) {
            adapterRest!!.stopListening()
        }
    }

}
