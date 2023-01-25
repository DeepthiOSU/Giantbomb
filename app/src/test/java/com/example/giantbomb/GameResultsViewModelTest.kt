package com.example.giantbomb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.giantbomb.network.GamesStorage
import com.example.giantbomb.network.Results
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GameResultsViewModelTest {

    private lateinit var target: GamesResultsViewModel

    @RelaxedMockK
    private lateinit var gamesStorageMock: GamesStorage

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = GamesResultsViewModel(gamesStorageMock)
        target.gameResults.observeForever { }
    }

    @Test
    fun testGameResults() {
        val results = emptyList<Results>()
        every { gamesStorageMock.findAll() } returns results
        assertTrue(target.gameResults.value!!.isEmpty())
    }
}