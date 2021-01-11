/*
 * Olio-ohjelmoinnin perusteet 2 Harjoitustyö
 *
 * Miikka Venäläinen 432165
 *
 * miikka.t.venalainen@tuni.fi
 *
 * Kayttoliittyma.java
 *
 */

//Sijainti paketissa
package harjoitustyo.kayttoliittyma;
// Otetaan muita luokkia/rajapintoja käyttöön
import harjoitustyo.dokumentit.Dokumentti;
import harjoitustyo.kokoelma.Kokoelma;
// Otetaan käyttöön pakkauksia
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeMap;

public class Kayttoliittyma{

  /*
   * Luokkavakiot
   *
   */

  private static final String TULOSTA = "print";
  private static final String RIVITYS = "pprint";
  private static final String POISTU = "quit";
  private static final String ESIKASITTELY = "polish";
  private static final String LISAA = "add";
  private static final String ETSI = "find";
  private static final String POISTA = "remove";
  private static final String PERU = "reset";
  private static final String FREKV = "freqs";
  private static final String LAJITTELE = "sort";
  private static final String KAIKU = "echo";

  /**
   * On vuorovaikutuksessa käyttäjän kanssa.
   *
   * @param args sisältää tiedon tiedostoista
   */

  public static void vuorovaikutus(String[] args) {

    // Tarkastetaan, että tiedostoja on kysytty määrä
    if (args.length != 2 || !args[0].split("_")[0].equals("jokes") && !args[0].split("_")[0].equals("news")
    || !args[1].split("_")[0].equals("stop")) {
      System.out.println("Wrong number of command-line arguments!");
      System.out.println("Program terminated.");
      System.exit(0);
    }

    // Luodaan Kokoelma-tyyppinen kokoelmaolio ja liitetään siihe viite
    Kokoelma kokoelmaOlio = new Kokoelma();

    // Luodaan uusi LinkedList-tyyppinen olio ja liitetään siihen viite joka on sulkusanalista
    // Samalla avataan tiedosto Kokoelma-luokan metodissa
    LinkedList<String> sulkusanalista = kokoelmaOlio.tiedostonAvaaja(args);

    // Esitellään viite, luodaan standardisyötevirtaa lukeva olio
    // ja liitetään viite olioon.
    Scanner lukija = new Scanner(System.in);

    // Tervehditään käyttäjää.
    System.out.println("Welcome to L.O.T.");

    // Annetaan echo komennon suorittavalle muuttujalle alkuarvoksi false
    boolean kaiutus = false;
    // Alustetaan polishia varten muuttujat aluksi arvolla false
    boolean esikäsitelty = false;
    boolean uusiLisäys = false;

    while (true) {

      try {

        // Kysytään käyttäjältä komentoa.
        System.out.println("Please, enter a command:");
        // Luetaan syöte käyttäjältä.
        String syote = lukija.nextLine();

        // Kun echo komento on käynnissä, toistetaan komento
        if (kaiutus && !syote.equals(KAIKU)) {
          System.out.println(syote);
        }

        // Alustetaan muuttuja ja asetetaan alkuarvoksi 0
        int tunnisteNumero = 0;

        // Jos käyttäjältä saatu syöte on kaksiosainen, joista komennon jälkimmäisen osan tulisi olla numero,
        // tallennetaan numero aiemmin alustettuun muuttujaan
        if ((syote.split(" ")[0].equals(TULOSTA)
            || syote.split(" ")[0].equals(POISTA) || syote.split(" ")[0].equals(RIVITYS)
            || syote.split(" ")[0].equals(FREKV)) && syote.split(" ").length == 2) {
          tunnisteNumero = Integer.parseInt(syote.split(" ")[1]);
        } else if ((syote.split(" ")[0].equals(POISTU) || syote.split(" ")[0].equals(KAIKU)
        || syote.split(" ")[0].equals(PERU)) && syote.split(" ").length != 1) {
          syote = "Error";
        }

        // Verrataan käyttäjältä saatua syötettä saatavilla oleviin komentoihin
        switch (syote.split(" ")[0]) {
          case POISTU: // quit-komento

            System.out.println("Program terminated.");
            System.exit(0); // Suljetaan ohjelma

          case LAJITTELE: // sort-komento

            String laji = syote.split(" ")[1]; // Pilkotaan komento
            boolean lajittelu = true; // Alustetaan muuttuja

            // Tarkistetaan syötteen oikeellisuus
            if (syote.split(" ").length != 2 || (!laji.equals("id") && !laji.equals("type")
                && !laji.equals("date"))) {
              lajittelu = false; // Virhe
            } else {
              lajittelu = kokoelmaOlio.dokumentinLajittelija(laji); // Kutsutaan metodia
            }
            if (!lajittelu) { // Virhe lajittelussa palauttaa falsen
              System.out.println("Error!");
            }

            break;
          case KAIKU: // echo-komento

            if (kaiutus) { // Jos kaiku on aktiivinen ja käytössä
              kaiutus = false; // Poistetaan se käytöstä
            } else { // Muutoin kaiutus otetaan käyttöön
              kaiutus = true;
            }
            System.out.println("echo");

            break;
          case ETSI: // find-komento

            String[] hakusanat = syote.split(" "); // Pilkotaan komennosta hakusanat

            int[] tulosta = kokoelmaOlio.dokumentinEtsija(hakusanat); // Kutsutaan metodia

            for (int i = 0; i < tulosta.length; i++) {
              if (tulosta[i] != 0) { // Jos 0, kaikkia hakusanoja ei löytynyt
                System.out.println(tulosta[i]); // Tulostetaan tunnisteet, joista hakusanat löytyi
              }
            }

            break;
          case FREKV: // freqs-komento

            // Kutsutaan frekvensoinnista vastaavaa metodia
            TreeMap<String, Integer> avainPari = kokoelmaOlio.dokumentinFrekvensoija(syote);

            if (avainPari != null) { // TreeMap ei saa olla tyhjä
              // Alustetaan muuttuja
              int lukumäärä = 0;

              Iterator<String> avaimet = avainPari.keySet().iterator(); // Luodaan iteraattori
              Iterator<Integer> arvot = avainPari.values().iterator(); // Luodaan iteraattori
              while (avaimet.hasNext()) {
                System.out.print(avaimet.next() + " "); // Tulostetaan sana
                int seuraavaArvo = arvot.next();
                System.out.println(seuraavaArvo); // Ja sitä vastaava määrä

                lukumäärä = lukumäärä + seuraavaArvo; // Lisätään kokonaislukumäärään
              }
              System.out.println("A total of " + lukumäärä + " words."); // Tulostetaan sanojen yhteismäärä
            } else {
              System.out.println("Error!"); // Virheilmoitus
            }

            break;
          case LISAA: // add-komento

            // Poistetaan komennossa mukana tullut "add " sana
            String uusi = syote.replaceFirst("add ", "");

            uusiLisäys = kokoelmaOlio.dokumentinLisaaja(uusi, args); // Kutsutaan metodia

            break;
          case POISTA: // remove-komento

            kokoelmaOlio.dokumentinPoistaja(tunnisteNumero); // Kutsutaan metodia

            break;
          case PERU: // reset-komento

            kokoelmaOlio.dokumentit().clear(); // Tyhjennetään nykyinen lista
            sulkusanalista = kokoelmaOlio.tiedostonAvaaja(args); // Ladataan tiedoston tiedot uudestaan

            uusiLisäys = false; // Arvo palautetaan alkuperäiseen
            esikäsitelty = false; // Arvo palautetaan alkuperäiseen

            break;
          case RIVITYS: // pprint-komento

            LinkedList<String> lause = kokoelmaOlio.dokumentinRivittäjä(tunnisteNumero);
            for (String s : lause) {
              System.out.println(s); // Tulostetaan lista
            }

            break;
          case TULOSTA: // print-komento

            if (syote.equals(TULOSTA)) { // Komento pelkkä "print"
              for (int i = 0; i < kokoelmaOlio.dokumentit().size(); i++) {
                System.out.println(kokoelmaOlio.dokumentit().get(i)); // Tulostetaan kaikki dokumentit
              }
            } else {
              Dokumentti tuloste = kokoelmaOlio.hae(tunnisteNumero); // Kutsutaan komennon käsittelevää metodia
              if (tuloste != null) { // Jos tuloste ei ole null-arvoinen, tulostetaan tuloste
                System.out.println(tuloste);
              } else {
                System.out.println("Error!");
              }
            }

            break;
          case ESIKASITTELY: // polish-komento

            // Varmistutaan, ettei esikäsittelyä tehdä jo valmiiksi esikäsitellylle ilman muutoksia
            if (esikäsitelty && !uusiLisäys && syote.equals(ESIKASITTELY)) {
              System.out.println("Error!"); // Virheen löytyessä tulostetaan virheilmoitus
            } else { // Muutoin käsitellään kokoelma normaalisti
              for (int i = 0; i < kokoelmaOlio.dokumentit().size(); i++) {
                kokoelmaOlio.dokumentit().get(i).siivoa(sulkusanalista, syote);
              }
              uusiLisäys = false; // Viimeisin lisäys on esikäsitelty
              esikäsitelty = true; // Kokoelma on jo valmiiksi esikäsitelty
            }
            break;
          default: // Vääränlainen komento johtaa virheilmoitukseen

            System.out.println("Error!");

            break;
        }
      } // Virheen sattuessa tulostetaan virheilmoitus
      catch(Exception e){
      System.out.println("Error!");
      }
    }
  }
}
