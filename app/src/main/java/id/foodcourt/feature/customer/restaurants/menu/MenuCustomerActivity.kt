package id.foodcourt.feature.customer.restaurants.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import id.foodcourt.R
import id.foodcourt.data.response.Menu
import id.foodcourt.data.request.OrderMenu
import id.foodcourt.feature.customer.payment.OrderCustomerActivity
import kotlinx.android.synthetic.main.activity_menu_customer.*
import java.util.ArrayList

class MenuCustomerActivity : AppCompatActivity() {

    companion object {
        var itemOrder : MutableList<OrderMenu> = mutableListOf()
    }

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

        resultTv.text=nama
//        +"\n UID ="+uid "Restaurant  = "+

        btn_detail.setOnClickListener {
            val orderList = arrayListOf(itemOrder)
            val intent = Intent(this@MenuCustomerActivity, OrderCustomerActivity::class.java)
            intent.putParcelableArrayListExtra("ORDERLIST", ArrayList<Parcelable>(itemOrder))
            intent.putExtra("RestaurantName", nama)
            intent.putExtra("RestaurantUid", uid)
            startActivity(intent)


        }
    }
    private inner class MenuAdpater internal constructor(options: FirestoreRecyclerOptions<Menu>) : FirestoreRecyclerAdapter<Menu, MenuViewHolder>(options) {
        override fun onBindViewHolder(menuViewHolder: MenuViewHolder, position: Int, menu: Menu) {
            menuViewHolder.setMenu(menu)
            menuViewHolder.initListener()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
            return MenuViewHolder(view)
        }
    }

    private inner class MenuViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view) {

        lateinit var mMenu : Menu

        fun setMenu(menu : Menu){
            mMenu = menu
            val tvMenu = view.findViewById<TextView>(R.id.tv_menu)
            val tvPrice= view.findViewById<TextView>(R.id.tv_price)

            tvPrice.text = menu.price.toString()
            tvMenu.text = menu.name
        }


        fun initListener(){
            val btn_plus : TextView = view.findViewById(R.id.tv_plus)
            val btn_less : TextView = view.findViewById(R.id.tv_less)
            val qty : TextView = view.findViewById(R.id.tv_qty)


            btn_plus.setOnClickListener {
                var count = qty.text.toString().toInt()
                count++
                qty.text = count.toString()
                updateCart(count)
                Log.d("Menu total + ", "Total = ${itemOrder.size}")
            }
            btn_less.setOnClickListener {
                var count = qty.text.toString().toInt()
                if(count > 0){
                    count--
                    qty.text = count.toString()
                }
                updateCart(count)
                Log.d("Menu total - ", "Total = ${itemOrder.size}")

            }

        }

        fun updateCart(qty : Int){

            if(qty > 0) {

                var searchSameMenu = itemOrder.filter {
                    it.menu == mMenu.name
                }

                if (searchSameMenu.size == 1) {
                    itemOrder.find {
                        it.menu == mMenu.name
                    }?.qty = qty
                } else {
                    itemOrder.add(
                        OrderMenu(
                            mMenu.name,
                            mMenu.price,
                            qty
                        )
                    )
                }
            } else {
                var searchSameMenu = itemOrder.filter {
                    it.menu == mMenu.name
                }

                if (searchSameMenu.size == 1) {
                    itemOrder.removeIf {
                        it.menu == mMenu.name
                    }
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
