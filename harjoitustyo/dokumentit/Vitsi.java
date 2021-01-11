/*
 * Olio-ohjelmoinnin perusteet 2 Harjoitustyö
 *
 * Miikka Venäläinen 432165
 *
 * miikka.t.venalainen@tuni.fi
 *
 * Vitsi.java
 *
 */

package harjoitustyo.dokumentit;

public class Vitsi extends Dokumentti {

  /*
   * Ilmentymäattribuutit.
   *
   */

  /** Vitsin laji */
  private String laji;

  /*
   * Rakentajat.
   *
   */

  public Vitsi(int tunniste, String l, String teksti) throws IllegalArgumentException{

    super(tunniste, teksti);
    laji(l);
  }

  /*
   * Aksessorit.
   *
   */

  public String laji() {
    return laji;
  }

  public void laji (String l) {
    if (l != null && !l.equals("")) { // Laji on oikeaa muotoa?
      laji = l;
    } else{
      throw new IllegalArgumentException();
    }
  }

  @Override
  public String toString() {
    return tunniste() + "///" + laji + "///" + teksti();
  }
}