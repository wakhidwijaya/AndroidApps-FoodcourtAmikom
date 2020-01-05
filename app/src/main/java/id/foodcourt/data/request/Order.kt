package id.foodcourt.data.request

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderMenu(
    val menu: String? = "",
    val price: Int = 0,
    var qty: Int = 0
) : Parcelable