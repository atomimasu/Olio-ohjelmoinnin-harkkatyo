/*
 * Olio-ohjelmoinnin perusteet 2 Harjoitustyö
 *
 * Miikka Venäläinen 432165
 *
 * miikka.t.venalainen@tuni.fi
 *
 * OmaLista.java
 *
 */

// Sijainti paketissa
package harjoitustyo.omalista;
// Otetaan muita luokkia/rajapintoja käyttöön
import harjoitustyo.apulaiset.Ooperoiva;
// Otetaan käyttöön pakkauksia
import java.util.Comparator;
import java.util.LinkedList;


public class OmaLista<E> extends LinkedList<E> implements Ooperoiva<E> {

  /**
   * Lisää listalle uuden alkion sen suuruusjärjestyksen mukaiseen paikkaan.
   *
   * @param uusi viite olioon, jonka luokan tai luokan esivanhemman oletetaan
   * toteuttaneen Comparable-rajapinnan.
   * @throws IllegalArgumentException jos lisäys epäonnistui, koska uutta alkiota
   * ei voitu vertailla. Vertailu epäonnistuu, kun parametri on null-arvoinen
   * tai siihen liittyvän olion luokka tai luokan esivanhempi ei vastoin oletuksia
   * toteuta Comparable-rajapintaa.
   */

  @SuppressWarnings({"unchecked"})
  @Override
  public void lisää(E uusi) throws IllegalArgumentException {

    // Varmistutaan, ettei parametri ole null-arvoinen
    if (uusi == null) {
      throw new IllegalArgumentException();
    }
    try {

      // Asetetaan nykyiseen alkioon compareTo-metodiin perustuvan
      // vertailun mahdollistava apuviite. Tämä on kääntäjän mielestä
      // vaarallista, koska kieliopillisesti ei voida päätellä onko
      // lisättävän olion luokalla Comparable-toteutus. Kääntäjän
      // mutinat estetään annotaatiolla.

      Comparable<E> uusiLisäys = (Comparable<E>) uusi;

      if (size() == 0) { // Jos lista on tyhjä, voidaan lisää suoraan
        add(uusi);
      } else if (size() == 1) { // Jos koko on yksi, verrataan lisäystä jo listassa olevaan
        if (uusiLisäys.compareTo(get(0)) >= 0) { // Suurempi lisätään listalle normaalisti viimeseksi
          add(uusi);
        } else { // Pienempi lisätään ensimmäiseksi
          addFirst(uusi);
        }
      } else {
        for (int i = 1; i < size(); i++) {

          if (uusiLisäys.compareTo(get(0)) == -1) { // Jos lisäys on pienempi kuin listan ensimmäinen alkio
            addFirst(uusi); // Lisätään se ensimmäiseksi
            break;
          }
          // Koitetaan löytää lisättävälle kolo, jossa sitä aiempi alkio on pienempi ja sitä seuraava alkio on suurempi
          else if (uusiLisäys.compareTo(get(i - 1)) >= 0 && uusiLisäys.compareTo(get(i)) < 0) {
            add(i, uusi);
            break;
          } else if (i == size() - 1) { // Muutoin, jos päästään listan päähän
            add(uusi); // Lisätään normaalisti listalle viimeiseksi
            break;
          }
        }
      }
    } catch (Exception e) { // Tarkastellaan virheitä
      throw new IllegalArgumentException();
    }
  }

  /**
   *
   *
   * @param vertailija viite vertailijaan, joka on Comparator-rajapinnan toteuttava
   * metodi eli lambda.
   * @throws IllegalArgumentException jos parametri on null-arvoinen tai lajittelun
   * aikana tapahtuu mikä tahansa poikkeus.
   */

  @Override
  public void lajittele(Comparator<? super E> vertailija) throws IllegalArgumentException {

    // Varmistetaan, ettei vertailla null-arvoa
    if (vertailija == null) {
      throw new IllegalArgumentException();
    }

    // Suoritetaan dokumentin järjestely sort-metodia hyväksi käyttäen
    this.sort(vertailija);
  }
}
