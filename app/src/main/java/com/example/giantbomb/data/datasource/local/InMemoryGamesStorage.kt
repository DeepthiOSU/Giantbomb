package com.example.giantbomb.data.datasource.local

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