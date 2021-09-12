package com.example.applicationprova

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * TESTING THE MONEY DIVISION ALGORITM
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun dividi() {
        val spese = mutableMapOf<String,Float>()
        spese.put("Utente1", 0.00f)
        spese.put("Utente2", 1.50f)
        spese.put("Utente3", 45.00f)
        spese.put("Utente4", 23.50f)
        spese.put("Utente5", 6.00f)
        spese.put("Utente6", 1.25f)
        val divisione = divisione(spese)
        assertEquals(divisione, 12.875f, .0005f)

    }

    fun divisione(spese: Map<String, Float>): Float {
        val divisione = spese.values.sum()/spese.size
        return divisione
    }
}