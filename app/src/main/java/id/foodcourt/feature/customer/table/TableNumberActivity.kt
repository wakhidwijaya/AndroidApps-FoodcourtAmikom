package id.foodcourt.feature.customer.table

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.foodcourt.R
import id.foodcourt.feature.customer.table.number.InputNumberActivity
import id.foodcourt.feature.customer.table.qrcode.QrCustomerActivity
import kotlinx.android.synthetic.main.activity_table_number.*

class TableNumberActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_number)

        btn_input.setOnClickListener {
            val intent = Intent (this, InputNumberActivity::class.java)
            startActivity(intent)
        }
        btn_barcode.setOnClickListener {
            val intent = Intent (this, QrCustomerActivity::class.java)
            startActivity(intent)
        }
    }
}
