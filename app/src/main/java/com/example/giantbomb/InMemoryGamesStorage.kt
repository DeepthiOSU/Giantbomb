package com.example.giantbomb

import com.example.giantbomb.network.GamesStorage
import com.example.giantbomb.network.Results

open class InMemoryGamesStorage : GamesStorage {
    private var inMemoryResults: List<Results> = listOf()
    override fun saveResults(results: List<Results>) {
        inMemoryResults = results
    }

    override fun findAll(): List<Results> {
        return inMemoryResults
    }
}