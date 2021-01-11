/*
 * Olio-ohjelmoinnin perusteet 2 Harjoitustyö
 *
 * Miikka Venäläinen 432165
 *
 * miikka.t.venalainen@tuni.fi
 *
 * Dokumentti.java
 *
 */

// Sijainti paketissa
package harjoitustyo.dokumentit;
// Otetaan muita luokkia/rajapintoja käyttöön
import harjoitustyo.apulaiset.Tietoinen;
// Otetaan käyttöön pakkauksia
import java.util.LinkedList;
import java.util.TreeMap;

public abstract class Dokumentti implements Tietoinen<Dokumentti>, Comparable <Dokumentti> {

  /*
   * Ilmentymäattribuutit.
   *
   */

  /** Dokumentin tunniste */
  private int tunniste;
  /** Dokumentin tekstisisältö */
  private String teksti;

  /*
   * Rakentajat
   *
   */

   // Parametrillinen rakentaja, jonka parametreilla määritellään tunnisteen arvo
   // ja tekstin sisältö. Arvot asetetaan vain, jos annettu arvo on sallittu
  public Dokumentti(int tu, String te) throws IllegalArgumentException{

    // Kutsutaan aksessoreita
    tunniste(tu);
    teksti(te);
  }

  /*
   * Aksessorit.
   *
   */

  public int tunniste() {
    return tunniste;
  }

  public void tunniste(int tu) {
    // Varmistetaan, että tunniste on 0 suurempaa
    if (tu > 0) {
      tunniste = tu;
    } else {
      throw new IllegalArgumentException();
    }
  }

  public String teksti() {
    return teksti;
  }

  public void teksti(String te) {
    // Varmistetaan, ettei teksti ole null-arvoinen tai tyhjä merkkijono
    if (te != null && !te.equals("")) {
      teksti = te;
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Tutkii sisältääkö dokumentin teksti kaikki siitä haettavat sanat. Kunkin
   * listan sanan Li on vastattava yhtä tai useampaa dokumentin sanan Dj
   * esiintymää täysin (Li.equals(Dj) == true), jotta sanat täsmäävät. Jos parametrin
   * arvo on esimerkiksi ["cat, "dog"], niin hakusanat ja dokumentti täsmäävät
   * vain ja ainostaan, jos dokumentissa esiintyy vähintään kerran sanat "cat"
   * ja "dog". Dokumentin sanan osajonon ei katsota vastaavan hakusanaa.
   *
   * @param hakusanat lista dokumentin tekstistä haettavia sanoja.
   * @return true jos kaikki listan sanat löytyvät dokumentista. Muuten palautetaan
   * false.
   * @throws IllegalArgumentException jos sanalista on tyhjä tai se on null-arvoinen.
   */

  @Override
  public boolean sanatTäsmäävät(LinkedList<String> hakusanat) throws IllegalArgumentException {

    // Tarkastetaan parametrina saatu hakusanat-lista virheiden varalta
    if (hakusanat == null || hakusanat.isEmpty() || teksti == null) {
      throw new IllegalArgumentException();
    }

    // Käytetään hyödyksi split-metodia, jolle on tarvetta myöhemmin
    String[] lause = teksti.split(" ");
    // Alustetaan laskuri vastaavuuksien laskua varten
    int vastaavuus = 0;

    for (int i = 0; i < hakusanat.size(); i++) {
      for (int j = 0; j < lause.length; j++) {

        // Varmistutaan, ettei vertailla virheellisiä arvoja
        if (hakusanat.get(i) == null || lause[j] == null || lause[j].equals("")) {
          throw new IllegalArgumentException();
        }
        // Muutoin verrataan hakusanaa dokumentin jokaiseen sanaan tai kunnes tulee vastaavuus
        else if (hakusanat.get(i).equals(lause[j])) {
          // Kun löydetään vastaava sana, lisätään laskuriin +1 ja siirrytään seuraavaan sanaan
          vastaavuus++;
          break;
        }
      }
    }
    // Jos laskurin laskemat vastaavuudet vastaavat vertailtavien hakusanojen määrää, palautetaan true
    // muutoin false
    return vastaavuus == hakusanat.size();
  }

  /**
   * Muokkaa dokumenttia siten, että tiedonhaku on helpompaa ja tehokkaampaa.
   * <p>
   * Metodi poistaa ensin dokumentin tekstistä kaikki annetut välimerkit ja
   * muuntaa sitten kaikki kirjainmerkit pieniksi ja poistaa lopuksi kaikki
   * sulkusanojen esiintymät.
   *
   * @param sulkusanat lista dokumentin tekstistä poistettavia sanoja.
   * @param syote dokumentin tekstistä poistettavat välimerkit merkkijonona.
   * @throws IllegalArgumentException jos jompikumpi parametri on tyhjä tai null-
   * arvoinen.
   */

  @Override
  public void siivoa(LinkedList<String> sulkusanat, String syote) throws IllegalArgumentException {

    // Varmistutaan, ettei parametrit ole null-arvoisia tai tyhjiä merkkijonoja
    if (sulkusanat == null || syote == null || sulkusanat.isEmpty() || syote.equals("") || syote.split(" ").length > 2) {
      throw new IllegalArgumentException();
    }

    if (!syote.equals("polish")) {

      String valimerkit = syote.split(" ")[1];

      for (int i = 0; i < teksti.length(); i++) {
        // Alustetaan merkki c jota voidaan käyttää vertailuun.
        // c käy läpi kaikki kokoelman merkit
        char c = teksti.charAt(i);
        // Tehdään teksti-attribuutille pieni jippo, jotta siitä on helpompi poistaa
        // tietty merkki
        StringBuffer testi = new StringBuffer(teksti);

        // Kokeillaan löytyykö saaduista välimerkeistä merkkiä c.
        // Jos merkkiä ei löydy, indeksiarvo sille on -1
        if (valimerkit.indexOf(c) != -1) {
          // Jos vastaavuus löydetään, poistetaan kyseisen kohdan i merkki
          teksti = String.valueOf(testi.deleteCharAt(i));
          /*
           *Jotta ei hypättäisi yhdenkään merkin ylitse i = i - 1, i:n arvo ei muutu, kun siihen
           *lisätään +1 for loopin toimesta. Verrataan siis poistetun indeksin tilalle tullutta
           *merkkiä seuraavaksi.
           */
          i = i - 1;
        }
      }
    }

    // Muutetaan teksti pieniksi kirjaimiksi
    teksti = teksti.toLowerCase();

    // Pilkotaan dokumentti sanoiksi
    String[] sana = teksti.split(" ");
    // Luodaan uusi StringBuilder-tyyppinen olio ja liitetään siihen viite.
    StringBuilder uusiLause = new StringBuilder();

    for (int j = 0; j < sana.length; j++) {
      // Jos sana löytyy sulkusanalistasta, se jätetään huomiotta
      if (!sulkusanat.contains(sana[j]) && !sana[j].equals("")) {
        // Jos kyseessä on ensimmäinen sana, joka ei löydy listasta, lisätään sana
        if (uusiLause.length() == 0) {
          uusiLause.append(sana[j]);
        } else { // Muutoin lisätään välilyönti ja sen perään sana
          uusiLause.append(" ").append(sana[j]);
        }
      }
    }
    // Korvataan aiempi versio esikäsitellyllä versiolla
    teksti = uusiLause.toString();
  }

  /**
   * Laskee sanojen esiintymien lukumäärät dokumentissa. Sana - frekvenssi -parit
   * palautetaan avaimen (sanan) mukaan järjestetyssä sanakirjassa.
   *
   * @return sanojen frekvenssit sanojen mukaan järjestetyssä sanakirjassa.
   * @throws UnsupportedOperationException jos tapahtuu jotain laitonta
   */

  @Override
  public TreeMap<String, Integer> laskeFrekvenssit() throws UnsupportedOperationException {

    // Pilkotaan dokumentti sanoiksi
    String[] sanat = teksti.split(" ");
    // Alustetaan laskuri, jolla lasketaan kunkin sanan osumat
    int laskuri = 0;

    // Luodaan uusi TreeMap-tyyppinen olio ja liitetään siihen viite.
    TreeMap<String, Integer> avainPari = new TreeMap<>();

    for (int i = 0; i < teksti.split(" ").length; i++) {
      for (int j = 0; j < teksti.split(" ").length; j++) {

        // Verrataan jokaista sanaa jokaiseen dokumentin sanaan
        // ja osuman löytyessä lisätään sana ja sen moninkerta TreeMappiin
        if (sanat[i].equals(sanat[j])) {
          laskuri++;
          avainPari.put(sanat[i], laskuri); // Lisätään sana ja sitä vastaava määrä avainpariksi
        }
      }
      laskuri = 0; // Nollataan laskuri seuraavaa sanaa varten
    }
    return avainPari; // Palautetaan lopputulema
  }

  /*
   *Korvatut metodit
   *
   */

  @Override
  public String toString() {
    return tunniste + "///" + teksti;
  }

  @Override
  public int compareTo(Dokumentti dokumentti) {
    // Tämä olio < parametrina saatu olio.
    if (tunniste < dokumentti.tunniste()) {
      return -1;
    }
    // Tämä olio == parametrina saatu olio.
    else if (tunniste == dokumentti.tunniste()) {
      return 0;
    }
    // Tämä olio > parametrina saatu olio.
    else {
      return 1;
    }
  }

  @Override
  public boolean equals(Object obj) {
    try {
      if (obj == this) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (obj instanceof Dokumentti) {
        // Asetetaan olioon Dokumentti-luokan viite, jotta voidaan kutsua
        // Dokumentti-luokan aksessoreita.
        Dokumentti testi = (Dokumentti) obj;

        // Oliot ovat samat, jos attribuuttien arvot ovat samat.
        return testi.tunniste() == this.tunniste();
      }
    } catch(Exception e){
      return false;
    }
    return false;
  }
}
