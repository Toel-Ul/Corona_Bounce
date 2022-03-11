package Fractale;

/**
 * Classe abstraite représentant une fractale
 */
public abstract class Fractale {

    /**
     * @param z0 le point surlequel on calcule l'indice de divergence
     * @param maxIter le nombre maximum d'itération pour le calcul
     * @return l'indicence de divergence au point z0
     */
    public abstract int divergenceIndex(Complexe z0, int maxIter);

    /**
     * @return une copie d'une Fractale
     */
    public abstract Fractale copy();

}
