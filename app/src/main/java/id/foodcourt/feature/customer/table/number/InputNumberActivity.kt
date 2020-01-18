package id.foodcourt.feature.customer.table.number

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.foodcourt.R
import id.foodcourt.feature.customer.restaurants.RestaurantListActivity
import kotlinx.android.synthetic.main.activity_input_number.*

class InputNumberActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_number)

        btn_addnumber.setOnClickListener {
            val tableNum = et_number.text.toString()
            val intent = Intent(this, RestaurantListActivity::class.java)
            intent.putExtra("table", tableNum)
            startActivity(intent)
        }
    }
}
