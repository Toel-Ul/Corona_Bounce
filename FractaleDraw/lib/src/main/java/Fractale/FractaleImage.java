package Fractale;


import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


/**
 * Modèle de l'interface graphique
 */
public class FractaleImage {
    /**
     * la fractale à afficher
     */
    private Fractale fractale;
    /**
     * la configuration de la fractale
     */
    private Configuration configuration;
    /**
     * l'image de la fractale
     */
    private BufferedImage image;
    /**
     * la liste des thread en cours
     */
    private List<ForkJoinPool> threads;

    /**
     * Construit une FractaleImage à partir d'une fractale et d'une configuration
     * @param fractale la fractale à afficher
     * @param configuration la configuration de la fractale
     */
    public FractaleImage(Fractale fractale, Configuration configuration) {
        this.fractale = fractale;
        this.configuration = configuration;
        threads = new ArrayList<>();
    }

    /**
     * @return l'attribut image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @return l'attribut fractale
     */
    public Fractale getFractale() {
        return fractale;
    }

    /**
     * Remplace la fractale par celle donnée en paramètre
     * @param fractale la nouvelle fractale
     */
    public void setFractale(Fractale fractale) {
        this.fractale = fractale;
    }

    /**
     * @return l'attribut configuration
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Remplace la configuration par celle donnée en paramètre
     * @param configuration une nouvelle configuration
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Calcul l'image selon la configuration associée
     */
    public void calculImage() {
        BufferedImage result = new BufferedImage(configuration.getLongueur(), configuration.getLargeur(), BufferedImage.TYPE_INT_RGB);
        Calcul work = new Calcul(result, 0, configuration.getLargeur());
        ForkJoinPool pool = new ForkJoinPool();
        threads.add(pool);
        try {
            pool.invoke(work);
            threads.remove(pool);
            image = result;
        } catch (Exception e){
        	// si le calcul a été interrompu
        }
    }

    /**
     * Interrompt les calculs des threads en cours
     */
    public void annule(){
        threads.forEach(f -> {
            f.shutdownNow();
        });
        threads = new ArrayList<>();
    }

    /**
     * Classe qui calcule l'image d'une fractale
     */
    private class Calcul extends RecursiveAction {
        /**
         * la fractale
         */
        private final Fractale fractale = FractaleImage.this.fractale;
        /**
         * le pas de discrétisation
         */
        private final double pas = configuration.getPas();
        /**
         * le maximum d'itération
         */
        private final int maxIter = configuration.getMaxIter();
        /**
         * la couleur des points convergents
         */
        private final int convergentColor = configuration.getConvergentColor();
        /**
         * la couleur des points en fonciton de l'indice de divergence
         */
        private final ColorFunction color = configuration.getColor();
        /**
         * la longueur de l'image
         */
        private final int longueur = configuration.getLongueur();
        /**
         * le minimum de l'intervalle des réels
         */
        private final double minRe = configuration.getMinRe();
        /**
         * le maximum de l'intervalle des imaginaires
         */
        private final double maxIm = configuration.getMaxIm();
        /**
         * le numéro de la première ligne de l'image à calculer
         */
        private final int fromLigne;
        /**
         * le nombre de ligne à calculer
         */
        private final int toLigne;
        /**
         * l'image qui doit être calculée
         */
        private final BufferedImage result;

        /**
         * Construit un Calcul selon une image et un intervalle de calcul
         * @param result l'image qui doit être calculée
         * @param fromLigne la première ligne à calculer
         * @param toLigne le nombre de ligne à calculer
         */
        private Calcul(BufferedImage result, int fromLigne, int toLigne) {
            this.result = result;
            this.fromLigne = fromLigne;
            this.toLigne = toLigne;
        }

        /**
         * Calcule la portion d'image définie par la première ligne et le nombre de ligne
         */
        private void run() {
            for (int x = 0; x < longueur; x++) {
                double re = minRe + (pas * x);
                for (int y = fromLigne; y < toLigne; y++) {
                    double im = maxIm - (pas * y);
                    int indice = fractale.divergenceIndex(new Complexe(re,im), maxIter);
                    int c = (indice == maxIter && convergentColor != -1) ? convergentColor : color.getColor(indice, maxIter);
                    result.setRGB(x, y, c);
                }
            }
        }

        @Override
        protected void compute() {
            if (toLigne - fromLigne <= 100)
                run();
            else{
                int middle = (fromLigne + toLigne) / 2;
                invokeAll(new Calcul(result, fromLigne, middle), new Calcul(result, middle, toLigne));
            }
        }
    }


    /**
     * @return une copie d'une FractaleImage
     */
    public FractaleImage copy(){
        return new FractaleImage(fractale.copy(), configuration.copy());
    }
}
