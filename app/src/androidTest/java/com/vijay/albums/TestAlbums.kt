package com.vijay.albums

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import org.junit.Test


class TestAlbums {

    private val itemAlbum = R.id.itemAlbumCardView

    @Test
    fun testAlbums() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.moveToState(Lifecycle.State.RESUMED)

        assertDisplayed(itemAlbum)
    }

}