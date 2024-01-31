package com.example.cypherassignment.model

import com.google.gson.annotations.SerializedName

data class RegisterDetails(
    @SerializedName("node_id_") var nodeId: String? = null,
    @SerializedName("network_speed_") var networkSpeed: Long = 0L,
    @SerializedName("battery_status") var batteryStatus: String = "",
    @SerializedName("free_bytes") var freeBytes: Long = 0L,
    @SerializedName("used_bandwidth") var usedBandwidth: Long = 0L,
    @SerializedName("last_chunk_download_time") var lastChunkDownloadTime: String = ""
)
