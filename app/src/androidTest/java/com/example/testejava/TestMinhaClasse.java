package com.example.testejava;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestMinhaClasse {


    @Test
    public void testWelcomeMessageDisplayed() {
        MinhaClasse minhaAtv = Mockito.mock(MinhaClasse.class);
        when(minhaAtv.getWelcomeMessage("World")).thenReturn("Mocked Welcome, World!");

        String expectedMessage = "Mocked Welcome, World!";
        String actualMessage = minhaAtv.getWelcomeMessage("World");
        assertEquals(expectedMessage, actualMessage);
    }
}