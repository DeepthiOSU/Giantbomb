package com.example.giantbomb.network

interface GamesStorage {
    fun saveResults(results: List<Results>)
    fun findAll(): List<Results>
}