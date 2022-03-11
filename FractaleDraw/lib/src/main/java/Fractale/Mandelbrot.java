package Fractale;


import java.util.function.BiFunction;

/**
 * Classe représentant l'ensemble de Mandelbrot
 */
public final class Mandelbrot extends Fractale {
    /**
     * la fonction associée à Mandelbrot
     */
    private final static BiFunction<Complexe,Complexe,Complexe> f =
            (c,z) -> c.plus(z.puissance(2));
    /**
     * l'unique instance de Mandelbrot
     */
    private final static Mandelbrot instance = new Mandelbrot();

    /**
     * @return l'attribut instance
     */
    public static Mandelbrot getInstance(){
        return instance;
    }

    @Override
    public int divergenceIndex(Complexe c, int maxIter){
        int ite = 0;
        Complexe zn = Complexe.ZERO;
        while (ite < maxIter && zn.module() <= 2) {
            zn = f.apply(c,zn);
            ite ++;
        }
        return ite;
    }
    
    @Override
    public Mandelbrot copy(){
        return this;
    }
}
