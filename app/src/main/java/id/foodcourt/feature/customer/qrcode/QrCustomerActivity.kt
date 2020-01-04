package id.foodcourt.feature.customer.qrcode

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import id.foodcourt.R
import id.foodcourt.feature.customer.restaurants.RestaurantListActivity
import id.foodcourt.utils.Config.PREF_NAME
import id.foodcourt.utils.Config.PRIVATE_MODE
import id.foodcourt.utils.Config.TABLE
import me.dm7.barcodescanner.zxing.ZXingScannerView


class   QrCustomerActivity : AppCompatActivity(),  ZXingScannerView.ResultHandler {

    lateinit var scanView: ZXingScannerView
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_customer)

        sharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        scanView = ZXingScannerView(this)
        setContentView(scanView)
    }
    override fun onResume() {
        super.onResume()
        scanView.setResultHandler(this)
        scanView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scanView.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {
        Log.v("TAG", rawResult!!.getText()); // Prints scan results
        Log.v("TAG", rawResult!!.getBarcodeFormat().toString());
        val builder :  AlertDialog.Builder = AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        val alert1: AlertDialog = builder.create()
        if(rawResult.text.length == 10){
            val tableNum = rawResult.text.toString().takeLast(2)
            val editor = sharedPreferences.edit()
            editor.putInt(TABLE, tableNum.toInt())
            editor.apply()
            val intent = Intent(this, RestaurantListActivity::class.java)
            startActivity(intent)

        }
        alert1.show();

        scanView.resumeCameraPreview(this);
    }
}
