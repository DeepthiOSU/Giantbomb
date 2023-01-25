package com.example.giantbomb.network

data class GamesResponse(
    val error: String,
    val limit: Int,
    val offset: Int,
    val number_of_page_results: Int,
    val number_of_total_results: Int,
    val status_code: Int,
    val results: List<Results>?,
    val version: String
)