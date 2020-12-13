# Testaukseen käytetyt menetelmät

Tämä repositorio sisältää kattavan kokoelman automatisoituja yksikkötestejä, sekä
korkeamman tason integraatiotestejä, jotka nekin on automatisoitu. Lisäksi sovelluksen
järjestelmätason testausta on suoritettu manuaalisesti koko kehityksen ajan. Testien
automaatioon on käytetty luotettavaksi todettua JUnit-kirjastoa.

## Yksikkö- ja integraatiotestaus

### Sovelluslogiikka

Sovelluksen sovelluslogiikka sijaitsee `ohte.domain` pakkauksessa ja sen testauksesta
vastaavat luokat `ApplicationTest`, `AccountTest` ja `ÀssetTest`. `ApplicationTest` luokka
sisältää `Application`-luokkaa koskevat testit, joissa `Application`-luokan riippuvuus
`Storage` on korvattu valekomponentilla käyttäen Mockito-kirjastoa. Luokat `AssetTest` ja
`AccountTest` kattavat nimiensä mukaiset luokat ja niiden testit ovat huomattavasti yksinkertaisempia.

### Persistenssikerros

Luokka `Storage` vastaa sovelluksen käyttäjien ja assettien varastoimisesta välimuistissa.
Välimuistissa olevat objektit synkronoidaan levyllä olevaan SQLite-tietokantaan käyttän
persisteriluokkia.

Luokka `Storage` on itsessään hyvin yksinkertainen ja lähes kaikki sen metodit ovat yksirivisiä
kääreitä muiden luokkien metodeille. Tästä syystä `Storage`-luokkaa ei ole yksikkötestattu erikseen
vaan sen testaaminen on jätetty integraatiotestin varaan.

Persisteriluokat `AccountSqlitePersister` ja `AssetSqlitePersister` ovat sen sijaan kattavasti
yksikkötestattuja, ja niiden testeissä käytetään välimuistissa sijaitsevaa SQLite-tietokantaa
joten testit vastaavan hyvin luokkien oikeaa käyttötapausta.

## Järjestelmätestaus

Sovelluksen järjestelmätestaus on suoritettu manuaalisesti kehitystyön yhteydessä.
