# Vaatimysmäärittely

## Sovelluksen tarkoitus

Käyttäjä voi sovelluksen avulla pitää kirjaa IT-laitteistostaan, sekä
laitteille asennetuista ohjelmistoista.

## Käyttäjähallinta

Sovellus tukee useita käyttäjätunnuksia, jotka ovat inventaariotiedostokohtaisia.
Jokaisella inventaariotiedostolla on pääkäyttäjä, sekä mahdollisia muita käyttäjiä.
Pääkäyttäjällä on oikeus tehdä mitä tahansa muutoksia kirjanpitoon, sekä oikeus
luoda uusia käyttäjiä. Muilla käyttäjillä on oikeus lisätä uusia laitteita ja ohjelmistoja
kirjanpitoon, sekä muokata ja poistaa itse lisäämiään laitteita ja ohjelmistoja.
Muilla kuin pääkäyttäjällä ei ole oikeutta luoda toisia käyttäjiä.

### Käyttäjäluokat

 - **Pääkäyttäjä**
   - Oikeus luoda, muokata ja poistaa käyttäjiä.
   - Oikeus luoda, muokata ja poistaa laitteita.
   - Oikeus luoda, muokata ja poistaa ohjelmistoja.
 - **Normaalikäyttäjä**
   - Oikeus luoda, muokata ja poistaa omia laitteita.
   - Oikeus luoda, muokata ja poistaa omia ohjelmistoja.

## Toiminnallisuus

Seuraavassa vaatimuslistauksessa merkinnällä **(MVP)** varustetut ominaisuudet ovat
ne ominaisuudet, jotka tulee minimissään toteuttaa, jotta sovelluksesta olisi
hyötyä käyttäjälleen.  Loput ominaisuuksista ovat lisäarvoa tuottavia ominaisuuksia.

### Käyttäjähallinta
 - [ ] Käyttäjä voi luoda uuden inventaariotiedoston, joka toimii sovelluksen tietokantana. **(MVP)**
   - Tiedoston luonnin yhteydessä luodaan pääkäyttäjä.
 - [ ] Käyttäjä voi avata jo olemassa olevan inventaariotiedoston. **(MVP)**
 - [ ] Käyttäjä voi kirjautua pääkäyttäjän tunnuksilla. **(MVP)**

### Laitteistokirjanpito
 - [ ] Käyttäjä voi lisätä kirjanpitoon uusia laitteita, joilla on seuraavia tietueita: (**MVP)**
   - Valmistaja
   - Malli **(MVP)**
   - Käyttöönottoajankohta
   - Sarjanumero
   - Käyttöjärjestelmä
   - Käyttöjärjestelmän versio
   - IP-osoite
   - MAC-osoite
   - Vapaamuotoinen kuvaus
 - [ ] Käyttäjä voi poistaa kirjanpidosta laitteita. **(MVP)**
 - [ ] Käyttäjä voi muokata kirjanpidossa olevan laitteen tietoja. **(MVP)**

### Ohjelmistokirjanpito
 - [ ] Käyttäjä voi kirjata laitekohtaisesti käytössä olevia ohjelmistoja. **(MVP)**
   + Ohjelmiston nimi **(MVP)**
   + Ohjelmiston versio
   + Ohjelmiston valmistaja
   + Ohjelmiston käyttämät portit tai URL, josta sovellus on käytettävissä.
   + Vapaamuotoinen kuvaus
 - [ ] Käyttäjä voi poistaa ohjelmistoja kirjanpidosta. **(MVP)**
 - [ ] Käyttäjä voi muokata kirjanpidossa olevien ohjelmistojen tietoja. **(MVP)**
