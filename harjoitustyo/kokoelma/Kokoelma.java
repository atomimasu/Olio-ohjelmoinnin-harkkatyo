/*
 * Olio-ohjelmoinnin perusteet 2 Harjoitustyö
 *
 * Miikka Venäläinen 432165
 *
 * miikka.t.venalainen@tuni.fi
 *
 * Kokoelma.java
 *
 */

// Sijainti paketissa
package harjoitustyo.kokoelma;
// Otetaan muita luokkia/rajapintoja käyttöön
import harjoitustyo.dokumentit.Dokumentti;
import harjoitustyo.apulaiset.Kokoava;
import harjoitustyo.dokumentit.Uutinen;
import harjoitustyo.dokumentit.Vitsi;
import harjoitustyo.omalista.OmaLista;
// Otetaan käyttöön pakkauksia
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeMap;

public class Kokoelma extends Object implements Kokoava<Dokumentti> {

  /*
   * Attribuutit.
   *
   */

  /** Lista johon säilötään dokumentit */
  private OmaLista<Dokumentti> dokumentit;

  /*
   * Rakentajat.
   *
   */

  public Kokoelma(){
    dokumentit = new OmaLista<>(); // Liitetään attribuuttiin tyhjä listaolio
  }

  /*
   * Aksessorit.
   *
   */

  public OmaLista<Dokumentti> dokumentit() {
    return dokumentit;
  }

  /**
   * Lisää kokoelmaan käyttöliittymän kautta annetun dokumentin.
   *
   * @param uusi viite lisättävään dokumenttiin.
   * @throws IllegalArgumentException jos dokumentin vertailu Comparable-rajapintaa
   * käyttäen ei ole mahdollista, listalla on jo equals-metodin mukaan sama
   * dokumentti eli dokumentti, jonka tunniste on sama kuin uuden dokumentin
   * tai parametri on null.
   */

  @Override
  public void lisää(Dokumentti uusi) throws IllegalArgumentException {

    if (uusi == null) { // Tarkastetaan onko parametri null-arvoinen
      throw new IllegalArgumentException();
    }
    if (dokumentit.contains(uusi)) { // Tarkastetaan sisältääkö lista saman dokumentin
      throw new IllegalArgumentException();
    }
    dokumentit.lisää(uusi); // Lisätään uusi dokumentti listalle
  }

  /**
   * Hakee kokoelmasta dokumenttia, jonka tunniste on sama kuin parametrin
   * arvo.
   *
   * @param tunniste haettavan dokumentin tunniste.
   * @return viite löydettyyn dokumenttiin. Paluuarvo on null, jos haettavaa
   * dokumenttia ei löydetty.
   */

  @Override
  public Dokumentti hae(int tunniste) {

    for (int i = 0; i < dokumentit.size(); i++) {
      // Käydään jokainen dokumentin tunniste yksi kerrallaan läpi ja verrataan
      // sitä parametrina saatuun tunnisteen arvoon
      if (dokumentit.get(i).tunniste() == tunniste) {
        return dokumentit.get(i); // Vastaavuuden löytyessä, palautetaan kyseinen dokumentti
      }
    }
    return null; // Muutoin palautus on null
  }

  /**
   * Lajittelee dokumentin halutulla tavalla
   *
   * @param syote sisältää tiedon miten dokumentit lajitellaan
   * @return palauttaa vertailun onnistumisen
   */

  public boolean dokumentinLajittelija(String syote) {

    // Vertaillaan dokumentteja käyttäen lambdaa
    Comparator<Dokumentti> vertailija = ((Dokumentti d1, Dokumentti d2) -> {

      // Alustetaan vertailun lopputulokselle muuttuja
      int loppuTulos = 0;

      switch (syote) { // Verrataan syötteen haluttua vertailua
        case "id": // Tunnisteen mukaan

          loppuTulos = d1.compareTo(d2); // compareTo-metodikutsu

          break;
        case "date": // Uutiset päivämäärän mukaan

          Uutinen u1 = (Uutinen) d1;
          Uutinen u2 = (Uutinen) d2;

          loppuTulos = u1.päivämäärä().compareTo(u2.päivämäärä()); // compareTo-metodikutsu

          break;
        case "type": // Vitsit lajin mukaan

          Vitsi v1 = (Vitsi) d1;
          Vitsi v2 = (Vitsi) d2;

          loppuTulos = v1.laji().compareTo(v2.laji()); // compareTo-metodikutsu
          break;
      }

      return loppuTulos; // Palautetaan vertailun tulos
    });

    // Kutsutaan lajittele-metodia, joka lajittelee listan
    this.dokumentit().lajittele(vertailija);

    // Jos vertailu onnistui, palautetaan true
    return true;
  }


  /**
   * Metodi pilkkoo dokumentin halutun pituisiksi
   *
   * @param tunnisteNumero kertoo pilkottujen osien maksimipituuden
   * @return palauttaa listan, jonka alkioina on on oikein pilkotut dokumentin palaset
   */

  public LinkedList<String> dokumentinRivittäjä(int tunnisteNumero) {

    // Alustetaan oliot
    LinkedList<String> rivit = new LinkedList<>();
    StringBuilder uusiRivi = new StringBuilder();

    for (int i = 0; i < dokumentit().size(); i++) {
      // Kiinnitetään dokumentti olioon, jotta voidaan tarkastella dokumentti kerrallaan
      String lause = String.valueOf(dokumentit().get(i));
      // Alustetaan laskuri
      int laskuri = 0;
      // Pilkotaan dokumentti sanoiksi
      String[] sanat = lause.split(" ");

      for (int j = 0; j < sanat.length; j++) {

        if (j == 0) { // Ensimmäinen termi
          uusiRivi.append(sanat[j]); // Lisätään sana
          laskuri = laskuri + sanat[j].length(); // Laskuriin lisätään sanan pituus
        } else {
          // Jos edeltävät sanat + uuden sanan pituus on alle määrätyn pituuden
          if (laskuri + sanat[j].length() < tunnisteNumero) {
            uusiRivi.append(" ").append(sanat[j]); // Lisätään välilyönti ja sana
            laskuri = laskuri + sanat[j].length() + 1; // Laskuriin lisätään sanan ja välilyönnin pituus
          }
          else {
            rivit.addLast(String.valueOf(uusiRivi)); // Lisätään sanat listaan
            uusiRivi = new StringBuilder(); // Nollataan
            uusiRivi.append(sanat[j]); // Lisätään sana
            laskuri = sanat[j].length()
            ; // Lisätään laskuriin

          }
        }
      }
      rivit.addLast(String.valueOf(uusiRivi)); // Lisätään jäljellä olevat
      uusiRivi = new StringBuilder(); // Nollataan, jotta uusi dokumentti lisätään tyhjälle
    }
    return rivit; // Palautetaan lista, joka tulostetaan käyttöliittymässä
  }

  /**
   * Metodi poistaa halutun dokumentin
   *
   * @param tunnisteNumero on poistettavan dokumentin tunniste
   */

  public void dokumentinPoistaja(int tunnisteNumero) {

    // Tyhjästä on paha nyhjäistä
    if (dokumentit().isEmpty()) {
      System.out.println("Error!"); // Ilmoitetaan virheestä
    }

    for (int i = 0; i < dokumentit().size(); i++) {

      // Tyhjästä on paha nyhjäistä
      if (dokumentit().isEmpty()) {
        System.out.println("Error!"); // Ilmoitetaan virheestä
      } else {
        // Verrataan jokaista listan tunnistetta annettuun numeroarvoon
        if (dokumentit().get(i).tunniste() == tunnisteNumero) {
          dokumentit().remove(i); // Vastaavuus poistetaan listalta
          break;
        } else if (i == dokumentit().size() - 1) {
          System.out.println("Error!"); // Jos dokumenttia ei löydy, ilmoitetaan virheesstä
        }
      }
    }
  }

  /**
   * Metodi lisää parametrina annetun dokumentin jos sellaista ei valmiiksi jo löydy
   *
   * @param syote sisältää komennon ja uuden lisäyksen
   * @param args sisältää tiedot millainen dokumentti on kyseessä
   * @return palauttaa tiedon onnistuiko dokumentin lisäys
   */

  public boolean dokumentinLisaaja(String syote, String[] args) {

    // Pilkotaan dokumentti paloiksi
    int tunniste = Integer.parseInt(syote.split("///")[0]); // Tunnisteen arvo
    String tyyppiEro = syote.split("///")[1]; // Vitsin laji tai uutisen päivämäärä
    String teksti = syote.split("///")[2]; // Teksti

    if (!tyyppiEro.isEmpty() && args[0].split("_")[0].equals("jokes") && tyyppiEro.matches("[a-öA-Ö ]+")) {

      Dokumentti vitsi = new Vitsi(tunniste, tyyppiEro, teksti); // Luodaan uusi vitsi
      lisää(vitsi); // Lisätään vitsi kokoelmaan
      return true; // Onnistunut lisääminen palauttaa true

    } else if (!tyyppiEro.isEmpty() && args[0].split("_")[0].equals("news")) {

      DateTimeFormatter pvm = DateTimeFormatter.ofPattern("d.M.yyyy");
      LocalDate päivämäärä = LocalDate.parse(tyyppiEro, pvm); // Luodaan päivämäärä

      Dokumentti uutinen = new Uutinen(tunniste, päivämäärä, teksti); // Muodostetaan uusi uutinen
      lisää(uutinen); // Lisätään uutinen kokoelmaan
      return true; // Onnistunut lisääminen palauttaa true
    }
    System.out.println("false");
    return false; // Jos lisäys ei onnistu, paluuarvo false
  }

  /**
   * Metodi etsii halutun sanan esiintymän
   *
   * @param syote etsittävät sanat
   * @return palauttaa mahdollisen osuman sijainnin
   */

  public int[] dokumentinEtsija(String[] syote) {

    // Alustetaan muuttujat/oliot
    LinkedList<String> lista = new LinkedList<>();
    int[] tuloste = new int[dokumentit().size()];

    for (int i = 1; i < syote.length; i++) {
      lista.add(syote[i]); // Lisätään haettavat sanat listalle vertailua varten
    }
    for (int j = 0; j < dokumentit().size(); j++) {

      boolean mätsi = dokumentit().get(j).sanatTäsmäävät(lista); // Etsitään osumaa

      if (mätsi) {
        tuloste[j] = dokumentit().get(j).tunniste(); // Osuman löytyessä lisätään tunniste taulukkoon
      }
    }
    return tuloste; // Palautetaan taulukko tulostusta varten
  }

  /**
   * Metodi tulostaa jokaista sanaa vastaavan lukumäärän dokumentissa
   *
   * @param syote on saatu komento, joka sisältää tiedon mitä dokumenttia käsitellään
   * @return palauttaa TreeMapin, joka sisältää sanat ja niitten lukumäärän dokumentissa
   */

  public TreeMap<String, Integer> dokumentinFrekvensoija(String syote) {

    if (syote.equals("freqs")) {

      // Alustetaan muuttujat
      int lukumäärä = 0;
      TreeMap<String, Integer> yhteisPari = new TreeMap<>();

      // Käydään frekvensointi jokaisella dokumentilla läpi
      for (int i = 0; i < dokumentit().size(); i++) {

        // Kutsutaan metodia
        TreeMap<String, Integer> avainPari = dokumentit().get(i).laskeFrekvenssit();

        Iterator<String> avaimet = avainPari.keySet().iterator(); // Luodaan iteraattori
        Iterator<Integer> arvot = avainPari.values().iterator(); // Luodaan iteraattori

        while (avaimet.hasNext()) {

          int seuraavaArvo = arvot.next(); // Liitetään arvo muuttujaan
          String seuraavaAvain = avaimet.next(); // Liitetään avain muuttujaan

          if (!yhteisPari.containsKey(seuraavaAvain)) { // Jos avainta ei löydy, se lisätään
            yhteisPari.put(seuraavaAvain, seuraavaArvo);
          } else {
            // Muutoin etsitään aiempi arvo
            int uusiArvo = yhteisPari.get(seuraavaAvain);

            // Ja lisätään avain aiemman ja uuden arvon kanssa
            yhteisPari.put(seuraavaAvain, uusiArvo + seuraavaArvo);

          }
          lukumäärä = lukumäärä + seuraavaArvo; // Lisätään kokonaislukumäärään
        }
      }
      return yhteisPari; // Palautetaan TreeMap
    } else if (syote.split(" ").length == 2) { // Muutoin frekvensoidaan tietty dokumentti

      int tunnisteNumero = Integer.parseInt(syote.split(" ")[1]); // Dokumentin tunniste

      return hae(tunnisteNumero).laskeFrekvenssit(); // Metodikutsu ja TreeMapin palautus samassa
    }
    return null; // Virhe palauttaa nullin, joka tarkistetaa
  }

  /**
   * Lukee tiedoston rivi kerrallaan ja lisää yksittäiset dokumentit listalle.
   *
   * @param args lista joka sisältää saatujen tiedostojen nimet
   * @return listan, joka sisältää sulkusanat.
   */

  public LinkedList<String> tiedostonAvaaja(String[] args) {

    try {

      // Määritellään muuttujille tyyppi
      String lause;
      String sana;

      // Luokkavakiot
      final String STOP = "stop";
      final String UUTISET = "news";
      final String VITSIT = "jokes";

      /*
       * Avataan tiedosto.
       *
       */

      String tiedostoTyyppiNimi = args[0].split("_")[0]; // jokes vai news?
      String sulkusanalistaNimi = args[1].split("_")[0]; // stop?

      if (tiedostoTyyppiNimi == null || sulkusanalistaNimi == null) {
        System.out.println("Missing file!");
        System.out.println("Program terminated.");
        System.exit(0);
      }

      // Luodaan tiedostoon liittyvät tiedosto-oliot
      File tiedostoDokumentti = new File(
          "C:\\Users\\venal\\IdeaProjects\\harjoitustyo\\src\\esimerkit\\jokes_oldies.txt");
      File tiedostoSulkusanat = new File(
          "C:\\Users\\venal\\IdeaProjects\\harjoitustyo\\src\\stop_words.txt");

      // Liitetään tiedosto-olioihin lukijat
      Scanner tiedostonLukija = new Scanner(tiedostoDokumentti);
      Scanner sulkusanaLukija = new Scanner(tiedostoSulkusanat);

      // Luodaan uusi LinkedList-tyyppinen olio ja liitetään siihen viite.
      // Huomaa geneeriset määreet, joilla kokoelman alkioiden tyypiksi
      // kiinnitetään String.
      LinkedList<String> sulkusanalista = new LinkedList<>();

      // Luetaan niin kauan kun löytyy uusi tekstirivi
      while (tiedostonLukija.hasNextLine()) {

        // Kiinnitetään uusi rivi olioon
        lause = tiedostonLukija.nextLine();

        // Pilkotaan rivi eli dokumentti paloiksi
        int tunniste = Integer.parseInt(lause.split("///")[0]); // Tunnisteen arvo
        String tyyppiEro = lause.split("///")[1]; // Vitsin laji tai uutisen päivämäärä
        String teksti = lause.split("///")[2]; // Teksti

        // Verrataan tiedoston nimeä; uutinen vai vitsi
        if (tiedostoTyyppiNimi.equals(UUTISET)) {
          // Muunnetaan päivämäärän tyyppi LocalDate
          DateTimeFormatter pvm = DateTimeFormatter.ofPattern("d.M.yyyy");
          LocalDate päivämäärä = LocalDate.parse(tyyppiEro, pvm);
          // Luodaan Dokumentti-tyyppinen uutisolio annetuilla arvoilla
          Dokumentti uusi = new Uutinen(tunniste, päivämäärä, teksti);
          // Hyödynnetään lisää-metodia dokumentin lisäyksessä
          lisää(uusi);
        } else if (tiedostoTyyppiNimi.equals(VITSIT)) {
          // Luodaan Dokumentti-tyyppinen vitsiolio annetuilla arvoilla
          Dokumentti uusi = new Vitsi(tunniste, tyyppiEro, teksti);
          // Hyödynnetään lisää-metodia dokumentin lisäyksessä
          lisää(uusi);
        }
      }

      // Suljetaan lukija, kun kaikki rivit on käyty läpi
      tiedostonLukija.close();

      // Luetaan niin kauan kuin löytyy uusi tekstirivi
      while (sulkusanaLukija.hasNextLine()) {

        // Kiinnitetään uusi rivi olioon
        sana = sulkusanaLukija.nextLine();

        // Verrataan tiedoston nimeä; onko stop?
        if (sulkusanalistaNimi.equals(STOP)) {
          sulkusanalista.add(sana); // Myönteisessä tapauksessa lisätään sana listalle
        }
      }

      // Suljetaan lukija, kun kaikki rivit on käyty läpi
      sulkusanaLukija.close();

      // Palautetaan sulkusanalista
      return sulkusanalista;

    } catch (IOException h) {
      System.out.println("Missing file!");
      System.out.println("Program terminated.");
      System.exit(0);
    }
    return null;
  }
}