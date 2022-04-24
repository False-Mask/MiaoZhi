package com.example.module_main.bean


import com.google.gson.annotations.SerializedName

data class ObjLocation(
    @SerializedName("lat_ble")
    val latBle: Double,
    @SerializedName("lon_ble")
    val lonBle: Double,
    @SerializedName("services_id")
    val servicesId: String
)