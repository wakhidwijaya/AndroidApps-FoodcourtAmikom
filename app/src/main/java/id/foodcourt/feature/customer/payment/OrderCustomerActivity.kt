package id.foodcourt.feature.customer.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import id.foodcourt.R
import id.foodcourt.data.request.OrderMenu

class OrderCustomerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_customer)

        var list = intent.getParcelableArrayListExtra<OrderMenu>("ORDERLIST")

        Log.d("ORDER", "order = ${list.get(0).menu}")
    }
}
