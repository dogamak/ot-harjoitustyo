package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    private static void assertSaldo(int saldo, Maksukortti kortti) {
      assertEquals("saldo: " + (saldo/100) + "." + (saldo%100), kortti.toString());
    }

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }

    @Test
    public void luodullaKortillaOnOikeaSaldo() {
      assertSaldo(10, kortti);
    }

    @Test
    public void rahanLataaminenKasvattaaSaldoa() {
      kortti.lataaRahaa(100);
      assertSaldo(110, kortti);
    }

    @Test
    public void saldoVaheneeOikeinKunRahaaOnTarpeeksi() {
      kortti.otaRahaa(9);
      assertSaldo(1, kortti);
    }

    @Test
    public void saldoEiVaheneKunRahaaEiOleTarpeeksi() {
      kortti.otaRahaa(20);
      assertSaldo(10, kortti);
    }

    @Test
    public void otaRahaaPalauttaaTrueKunRahaRiittaa() {
      assertTrue(kortti.otaRahaa(5));
    }

    @Test
    public void otaRahaaPalauttaaFalseKunRahaEiRiita() {
      assertFalse(kortti.otaRahaa(15));
    }

    @Test
    public void saldoMetodiPalauttaaOikeanSaldon() {
      assertEquals(10, kortti.saldo());
    }
}
