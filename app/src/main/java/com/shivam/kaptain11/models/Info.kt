package com.shivam.kaptain11.models

data class Info(
    val amount: Double,
    val code: String,
    val created_by: String,
    val display_picture: Any?,
    val dynamic_config_id: String,
    val email: String,
    val expiry_in_days: Int,
    val followers_count: Int,
    val guru_id: String,
    val guru_name: String,
    val last_updated_by: String,
    val last_updated_date: Any?,
    val mobile: String,
    val static_config_id: String,
    val status: String,
    val type: String,
    val user_basic_id: String,
    val username: String
)