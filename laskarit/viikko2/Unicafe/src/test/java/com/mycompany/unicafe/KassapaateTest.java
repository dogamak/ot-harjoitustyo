package com.mycompany.unicafe;

import org.junit.*;
import static org.junit.Assert.*;

public class KassapaateTest {
  Kassapaate paate;

  @Before
  public void setupKassapaate() {
    paate = new Kassapaate();
  }

  @Test
  public void luodunKassapaatteenRahamaaraOnOikea() {
    assertEquals(1000, paate.kassassaRahaa());
  }

  @Test
  public void luodunKassapaatteenMyyntiMaaraOnOikea() {
    assertEquals(0, paate.maukkaitaLounaitaMyyty());
    assertEquals(0, paate.edullisiaLounaitaMyyty());
  }

  @Test
  public void maukkaanLounaanKateisostoToimii() {
    int vaihtoraha = paate.syoMaukkaasti(450);

    assertEquals(
      "maukkaiden lounaiden myyntimäärä on virheellinen",
      1, paate.maukkaitaLounaitaMyyty());
    assertEquals(
      "edullisten lounaiden myyntimäärä on virheellinen",
      0, paate.edullisiaLounaitaMyyty());
    assertEquals(
      "kassan rahamäärä on virheellinen",
      1400, paate.kassassaRahaa());
    assertEquals(
      "vaihtorahan määrä on virheellinen",
      50, vaihtoraha);
  }

  @Test
  public void edullisenLounaanKateistostoToimii() {
    int vaihtoraha = paate.syoEdullisesti(450);

    assertEquals(
      "maukkaiden lounaiden myyntimäärä on virheellinen",
      0, paate.maukkaitaLounaitaMyyty());
    assertEquals(
      "edullisten lounaiden myyntimäärä on virheellinen",
      1, paate.edullisiaLounaitaMyyty());
    assertEquals(
      "kassan rahamäärä on virheellinen",
      1240, paate.kassassaRahaa());
    assertEquals(
      "vaihtorahan määrä on virheellinen",
      210, vaihtoraha);
  }

  @Test
  public void edullisenLounaanKateistostoKunRahaEiRiita() {
    int vaihtoraha = paate.syoEdullisesti(200);

    assertEquals(
      "maukkaiden lounaiden myyntimäärä on virheellinen",
      0, paate.maukkaitaLounaitaMyyty());
    assertEquals(
      "edullisten lounaiden myyntimäärä on virheellinen",
      0, paate.edullisiaLounaitaMyyty());
    assertEquals(
      "kassan rahamäärä on virheellinen",
      1000, paate.kassassaRahaa());
    assertEquals(
      "vaihtorahan määrä on virheellinen",
      200, vaihtoraha);
  }

  @Test
  public void maukkaanLounaanKateistostoKunRahaEiRiita() {
    int vaihtoraha = paate.syoMaukkaasti(200);

    assertEquals(
      "maukkaiden lounaiden myyntimäärä on virheellinen",
      0, paate.maukkaitaLounaitaMyyty());
    assertEquals(
      "edullisten lounaiden myyntimäärä on virheellinen",
      0, paate.edullisiaLounaitaMyyty());
    assertEquals(
      "kassan rahamäärä on virheellinen",
      1000, paate.kassassaRahaa());
    assertEquals(
      "vaihtorahan määrä on virheellinen",
      200, vaihtoraha);
  }

  @Test
  public void maukkaanLounaanKorttiostoToimiiKunRahaRiittaa() {
    Maksukortti kortti = new Maksukortti(1000);

    assertTrue("metodi syuMaukkaasti palauttaa False", paate.syoMaukkaasti(kortti));
    assertEquals("kortin saldo on virheellinen", 600, kortti.saldo());
    assertEquals("kassan rahamäärä on virheellinen", paate.kassassaRahaa(), 1000);
    assertEquals("myytyjen edullisten lounaiden määrä on virheellinen", 0, paate.edullisiaLounaitaMyyty());
    assertEquals("myytyjen maukkaiden lounaiden määrä on virheellinen", 1, paate.maukkaitaLounaitaMyyty());
  }

  @Test
  public void edullisenLounaanKorttiostoToimiiKunRahaRiittaa() {
    Maksukortti kortti = new Maksukortti(1000);

    assertTrue("metodi syuMaukkaasti palauttaa False", paate.syoEdullisesti(kortti));
    assertEquals("kortin saldo on virheellinen", 760, kortti.saldo());
    assertEquals("kassan rahamäärä on virheellinen", paate.kassassaRahaa(), 1000);
    assertEquals("myytyjen edullisten lounaiden määrä on virheellinen", 1, paate.edullisiaLounaitaMyyty());
    assertEquals("myytyjen maukkaiden lounaiden määrä on virheellinen", 0, paate.maukkaitaLounaitaMyyty());
  }

  @Test
  public void maukkaanLounaanKorttiostoToimiiKunRahaEiRiita() {
    Maksukortti kortti = new Maksukortti(100);

    assertFalse("metodi syuMaukkaasti palauttaa True", paate.syoMaukkaasti(kortti));
    assertEquals("kortin saldo on virheellinen", 100, kortti.saldo());
    assertEquals("kassan rahamäärä on virheellinen", paate.kassassaRahaa(), 1000);
    assertEquals("myytyjen edullisten lounaiden määrä on virheellinen", 0, paate.edullisiaLounaitaMyyty());
    assertEquals("myytyjen maukkaiden lounaiden määrä on virheellinen", 0, paate.maukkaitaLounaitaMyyty());
  }

  @Test
  public void edullisenLounaanKorttiostoToimiiKunRahaEiRiita() {
    Maksukortti kortti = new Maksukortti(100);

    assertFalse("metodi syuMaukkaasti palauttaa True", paate.syoEdullisesti(kortti));
    assertEquals("kortin saldo on virheellinen", 100, kortti.saldo());
    assertEquals("kassan rahamäärä on virheellinen", paate.kassassaRahaa(), 1000);
    assertEquals("myytyjen edullisten lounaiden määrä on virheellinen", 0, paate.edullisiaLounaitaMyyty());
    assertEquals("myytyjen maukkaiden lounaiden määrä on virheellinen", 0, paate.maukkaitaLounaitaMyyty());
  }
}
