package id.foodcourt.feature.customer.restaurants.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import id.foodcourt.R
import id.foodcourt.data.Menu
import id.foodcourt.data.Restaurant
import id.foodcourt.feature.customer.restaurants.RestaurantListActivity
import kotlinx.android.synthetic.main.activity_menu_customer.*
import kotlinx.android.synthetic.main.activity_restaurant_list.*

class MenuCustomerActivity : AppCompatActivity() {
    lateinit var db : FirebaseFirestore
    private var adapterMenu: MenuAdpater? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_customer)

        val intent = intent
        val nama = intent.getStringExtra("RestaurantName")
        val uid = intent.getStringExtra("RestaurantUid")

        rv_menu.layoutManager = LinearLayoutManager(this)

        db = FirebaseFirestore.getInstance()
        val query = db!!.collection("restaurants").document(uid).collection("menus")

        val options = FirestoreRecyclerOptions.Builder<Menu>().setQuery(query, Menu::class.java).build()

        adapterMenu = MenuAdpater(options)

        rv_menu.adapter = adapterMenu

        val resultTv = findViewById<TextView>(R.id.tv_menu)

        resultTv.text="Restaurant  = "+nama+"\n UID ="+uid
    }
    private inner class MenuAdpater internal constructor(options: FirestoreRecyclerOptions<Menu>) : FirestoreRecyclerAdapter<Menu, MenuViewHolder>(options) {
        override fun onBindViewHolder(menuViewHolder: MenuViewHolder, position: Int, menu: Menu) {
            menuViewHolder.setMenuName(menu.name)
            menuViewHolder.initListener()

            var user = FirebaseAuth.getInstance().currentUser
            btn_detail.setOnClickListener {
                Toast.makeText(applicationContext, user!!.uid, Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
            return MenuViewHolder(view)
        }
    }

    private inner class MenuViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view) {
        internal fun setMenuName(menuName: String) {
            val textView = view.findViewById<TextView>(R.id.tv_menu)
            textView.text = menuName
        }

        fun initListener(){
            val btn_plus : TextView = view.findViewById(R.id.tv_plus)
            val btn_less : TextView = view.findViewById(R.id.tv_less)
            val qty : TextView = view.findViewById(R.id.tv_qty)
            btn_plus.setOnClickListener {
                var count = qty.text.toString().toInt()
                count++
                qty.text = count.toString()
            }
            btn_less.setOnClickListener {
                var count = qty.text.toString().toInt()
                if(count > 0){
                    count--
                    qty.text = count.toString()
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        adapterMenu!!.startListening()
    }

    override fun onStop() {
        super.onStop()

        if (adapterMenu != null) {
            adapterMenu!!.stopListening()
        }
    }

}
