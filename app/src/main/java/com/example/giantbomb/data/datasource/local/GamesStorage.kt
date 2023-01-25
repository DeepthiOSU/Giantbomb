package com.example.giantbomb.data.datasource.local

import com.example.giantbomb.network.Results

interface GamesStorage {
    fun saveResults(results: List<Results>)
    fun findAll(): List<Results>
}