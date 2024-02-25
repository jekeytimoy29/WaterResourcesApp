package com.example.waterresourcesapp.model

import java.io.Serializable

data class WaterResource(
    val name: String,
    val location: String,
    val type: String,
    var capacity: Double
) : Serializable
