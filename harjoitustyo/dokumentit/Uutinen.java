/*
 * Olio-ohjelmoinnin perusteet 2 Harjoitustyö
 *
 * Miikka Venäläinen 432165
 *
 * miikka.t.venalainen@tuni.fi
 *
 * Uutinen.java
 *
 */

package harjoitustyo.dokumentit;

import java.time.LocalDate;

public class Uutinen extends Dokumentti {

  /*
   * Ilmentymäattribuutit.
   *
   */

  /** Uutisen päivämäärä */
  private LocalDate päivämäärä;

  /*
   * Rakentajat.
   *
   */

  public Uutinen(int tunniste, LocalDate p, String teksti) throws IllegalArgumentException{

    super(tunniste, teksti);

    päivämäärä(p);
  }

  /*
   * Aksessorit.
   *
   */

  public LocalDate päivämäärä() {
    return päivämäärä;
  }

  public void päivämäärä(LocalDate p) {
    if (p != null) { // Päivämäärä on oikeanlainen?
      päivämäärä = p;
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public String toString() {

    return tunniste() + "///" + päivämäärä.getDayOfMonth() + "." + päivämäärä.getMonthValue()
        + "." + päivämäärä.getYear() + "///" + teksti();

  }
}
