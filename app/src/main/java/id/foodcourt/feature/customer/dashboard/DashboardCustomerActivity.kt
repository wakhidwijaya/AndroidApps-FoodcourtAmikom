package id.foodcourt.feature.customer.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.foodcourt.R
import id.foodcourt.feature.customer.table.TableNumberActivity
import id.foodcourt.feature.order.active.OrderActiveActivity
import kotlinx.android.synthetic.main.activity_dashboard_customer.*

class DashboardCustomerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_customer)

        btn_resto.setOnClickListener {
            val intent = Intent(this, TableNumberActivity::class.java )
            startActivity(intent)
        }

        btn_ordercustomer.setOnClickListener {
            val intent = Intent(this, OrderActiveActivity::class.java)
            startActivity(intent)
        }
    }
}
