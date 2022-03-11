package Fractale;


import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.complex.ComplexFormat;

/**
 * Classe représentant l'ensemble de Julia
 */
public final class Julia extends Fractale {
    /**
     * la fonction associée à la fractale
     */
    private Function<Complexe,Complexe> f;
    /**
     * la fonction en chaine de caractère
     */
    private final String fonction;

    /**
     * Construit une fractale Julia à partir d'une fonction
     * @param fonction la fonction de le fractale
     */
    public Julia(String fonction) {
        if (fonction == null)
            throw new IllegalArgumentException();
        this.fonction = fonction.replaceAll("\\s", "");
        f = parseFonction();
    }

    /**
     * @return une Function représentant la fonction
     * @exception IllegalArgumentException si le format de la fonction n'est pas valide
     */
    private Function<Complexe,Complexe> parseFonction(){
        Function<Complexe,Complexe> f = z -> Complexe.ZERO;
        String s = "+" + fonction.replaceAll("\\.", ",");
        ComplexFormat format = new ComplexFormat();
        Matcher m = Pattern.compile("(\\(([0-9.+-i]*)\\)z\\^([0-9]*))?").matcher(s);
        int i = 0;
        while (i != s.length()) {
            if (s.charAt(i++) == '+') {
                if (m.find(i) && m.start() == i) {
                    Complexe c = new Complexe(format.parse(m.group(2)));
                    int p = Integer.parseInt(m.group(3));
                    Function<Complexe,Complexe> g = f;
                    f = z -> g.apply(z).plus(c.fois(z.puissance(p)));
                    i = m.end();
                }
                else break;
            }
            else break;
        }
        if (i != s.length())
            throw new IllegalArgumentException(fonction + " n'est pas reconnu comme une fonction");
        return f;
    }

    /**
     * @return la valeur de l'attribut fonction
     */
    public String getFonction() {
        return fonction;
    }

    @Override
    public int divergenceIndex(Complexe z0, int maxIter){
        int ite = 0;
        Complexe zn = z0;
        while (ite < maxIter && zn.module() <= 2) {
            zn = f.apply(zn);
            ite ++;
        }
        return ite;
    }

    @Override
    public Julia copy(){
        return new Julia(fonction);
    }
}
