package Fractale;


import org.apache.commons.math3.complex.Complex;

/**
 * Classe représentant des nombres complexes
 */
public final class Complexe {
    /**
     * la partie réelle du complexe
     */
    public final double re;
    /**
     * la partie imaginaire du complexe
     */
    public final double im;
    /**
     * le complexe zéro
     */
    public static final Complexe ZERO = new Complexe(0,0);
    /**
     * le complexe un
     */
    public static final Complexe UN = new Complexe(1,0);

    /**
     * @param re la partie réelle
     * @param im la partie imaginaire
     * Créer un complexe avec les valeurs en paramètre
     */
    public Complexe(double re, double im){
        this.re = re;
        this.im = im;
    }

    /**
     * @param c un complexe
     * Créer un Complexe à partir d'un Complex de la bibliothèque commons.math
     */
    public Complexe(Complex c) {
        this(c.getReal(), c.getImaginary());
    }

    /**
     * @return une chaine de caractère représentant le complexe
     */
    @Override
    public String toString() {
        return "(" + re + "," + im + ")";
    }

    /**
     * @param c un complexe
     * @return renvoie un complexe qui résulte de la somme de c et du complexe courant
     */
    public Complexe plus(Complexe c) {
        return new Complexe(re + c.re, im + c.im);
    }

    /**
     * @param c un complexe
     * @return renvoie un complexe qui résulte du produit de c et du complexe courant
     */
    public Complexe fois(Complexe c) {
        return new Complexe(re * c.re - im * c.im, re * c.im + im * c.re);
    }

    /**
     * @param n un entier
     * @return renvoie un complexe correspondant au complexe courant à la puissance n
     */
    public Complexe puissance(int n) {
        if (n == 0)
            return Complexe.UN;
        if (n == 1)
            return this;
        if (n % 2 == 0) {
            Complexe c = puissance(n / 2);
            return c.fois(c);
        }
        else {
            Complexe c = puissance((n - 1) / 2);
            return fois(c.fois(c));
        }
    }

    /**
     * @return le module du complexe
     */
    public double module() {
        return Math.sqrt(re * re + im * im);
    }

}
