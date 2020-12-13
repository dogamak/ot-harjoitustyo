# Käyttöopas

## Asennus

Sovelluksen asennus on äärimmäisen yksinkertaista. Asennukseen riittä JAR-tiedoston
lataaminen GitHubin Releases-välilehden alta.

## Käynnistäminen

Sovellus voidaan JAR-tiedoston lataamisen jälkeen käynnistää komentoriviltä seuraavasti:

```shell
$ java -jar AssetManager.jar
```

Sovellukselle voidaan halutessa antaa myös sauraavia komentoriviargumentteja, joista kaikki
ovat vapaaehtoisia:

```shell
$ java -jar AssetManager.jar --operation create --username root --password root path/to/inventory.file
```

## Käyttö

### Inventaariotiedoston luominen

Sovellus kysyy käynnistämisen yhteydessä, että haluatko luoda uuden inventaariotiedoston.
Vastaa painamalla "Create Inventory File"-painiketta.
Tämän jälkeen avautuu tiedostoselain, jossa voit navigoida haluaamaasi kansioon
ja antaa inventaariotiedostollesi haluamasi nimen. Paina tämän jälkeen "Save"-painiketta.
Seuraavaksi avautaa kirjautumistietodialogi, johon voit syöttää haluamasi pääkäyttäjän
kirjautumistiedot. Painettuasi tässä dialogissa OK-painiketta, tulisi päänäkymän avautua
ja inventaariotiedoston olla onnistuneesti luotu.

### Olemassaolevan inventaariotiedoston avaaminen

Sovellus kysyy käynnistämisen yhteydessä, että haluatko avata olemassa olevan inventaariotiedoston.
Vastaa painamalla "Open Inventory File"-painiketta.
Tämän jälkeen avautuu tiedostoselain, jossa voit valita inventaariotiedostosi.
"Open"-painikkeen painamisen jälkeen aukeaa kirjautumistietodialog, johon voit syöttää
inventaariotidoston pääkäyttäjän kirjautumistiedot. "Unlock"-painikkeen painamisen jälkeen
avautuu päänäkymä, mikäli olet syöttänyt oikeat kirjautumistiedot. Jos tiedot olivat virheelliset,
avautuu kirjautumistietodialogi uudestaan virheviestin kera.

### Assettien kirjaaminen järjestelmään

Assetteja voidaan luoda valitsemalla päänäkymässä "Assets"-välilehti ja painamalla
alanurkan plus-ikonilla varustettua painiketta. Tämä luo uuden assetin, jonka kaikki
kentät on alustettu tyhjiin arvoihin.

### Assettien tietojen muokkaaminen

Assetteja voidaan muokata valitsemalla hiirellä assetti "Assets"-välilehden taulukosta.
Tämän jälkeen assetin tiedot avautuvat oikealla puolella olevaan paneeliin.
Tästä paneelista voidaan muokata assetin kenttiä valitsemalla kentän vierestä kynällä varustettu painike.
Muokattuasi kentän arvon mieleiseksesi voit tallentaa kentän arvon painamalla OK-merkinnällä varustettua painiketta.
Kaikki muokkaukset tallentuvat välittömästi levylle.
