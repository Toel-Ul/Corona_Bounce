package Fractale;

/**
 * Créer une configuration pour l'image d'une fractale
 */
public class Configuration {
    /**
     * le nombre max d'itération
     */
    private int maxIter;
    /**
     * le pas de discrétisation
     */
    private double pas;
    /**
     * le minimum de l'intervalle des réels
     */
    private double minRe;
    /**
     * le maximum de l'intervalle des réels
     */
    private double maxRe;
    /**
     * le minimum de l'intervalle des imaginaires
     */
    private double minIm;
    /**
     * le maximum de l'intervalle des imaginaires
     */
    private double maxIm;
    /**
     * la longueur de l'image
     */
    private int longueur;
    /**
     * la largeur de l'image
     */
    private int largeur;
    /**
     * la couleur des points convergents
     */
    private int convergentColor;
    /**
     * la couleur des points en fonciton de l'indice de divergence
     */
    private ColorFunction color;

    /**
     * Classe interne qui permet de construire un configuration
     */
    public static class Builder {
        /**
         * le nombre max d'itération de la configuration
         */
        private int maxIter;
        /**
         * le pas de discrétisation de la configuration
         */
        private double pas;
        /**
         * le minimum des réels de la configuration
         */
        private double minRe;
        /**
         * le maximum des réels de la configuration
         */
        private double maxRe;
        /**
         * le minimum des imaginaires de la configuration
         */
        private double minIm;
        /**
         * le maximum des imaginaires de la configuration
         */
        private double maxIm;
        /**
         * la longueur de l'image de la configuration
         */
        private int longueur;
        /**
         * la largeur de l'image de la configuration
         */
        private int largeur;
        /**
         * la valeur de la couleur des points convergents
         */
        private int convergentColor;
        /**
         * la couleur des points en fonciton de l'indice de divergence
         */
        private ColorFunction color;
        
        /**
         * Constructeur par défaut de Builder avec les valeurs:
         * maxIter = 50
         * pas = 0.05
         * minRe = -2
         * maxRe = 1.995
         * minIm = -2
         * maxIm = 1.995
         * longueur = 800
         * largueur = 800
         * color = ColorFunction par défaut
         */
        public Builder() {
            this.maxIter = 50;
            this.pas = 0.005;
            this.minRe = -2;
            this.maxRe = 1.995;
            this.minIm = -2;
            this.maxIm = 1.995;
            this.longueur = 800;
            this.largeur = 800;
            this.convergentColor = 0;
            this.color = new ColorFunction();
        }
        
        /**
         * @param maxIter le maximum d'itération
         * @return l'objet courant avec la valeur de son attribut maxIter égale à la valeur du paramètre
         */
        public Builder maxIter(int maxIter) {
            this.maxIter = maxIter;
            return this;
        }

        /**
         * Modifie le plan complexe avec les valeurs données en paramètre et adapte la taille de l'image
         * @param minRe le minimum des réels
         * @param maxRe le maximum des réels
         * @param minIm le minimum des imaginaires
         * @param maxIm le maximum des imaginaires
         * @return l'objet courant avec les nouvelles valeurs
         */
        public Builder planComplexe(double minRe, double maxRe, double minIm, double maxIm){
            this.minRe = minRe;
            this.maxRe = maxRe;
            this.minIm = minIm;
            this.maxIm = maxIm;
            longueur = (int) Math.ceil((maxRe - minRe) / pas) + 1;
            largeur = (int) Math.ceil((maxIm - minIm) / pas) + 1;
            return this;
        }

        /**
         * @param pas la pas de discrétisation
         * @return l'objet courant avec la valeur de son attribut pas égale à la valeur du paramètre
         */
        public Builder pas(double pas) {
            this.pas = pas;
            return this;
        }

        /**
         * @param longueur la longueur de l'image
         * @param largeur la largeur de l'image
         * @return l'objet courant avec la valeur de ses attributs longueur et largeur égale aux valeurs des paramètres correspondant
         */
        public Builder taille(int longueur, int largeur) {
            this.longueur = longueur;
            this.largeur = largeur;
            return this;
        }

        /**
         * @param convergentColor la couleur pour les points convergents
         * @return l'objet courant avec la valeur de son attribut convergentColor égale à la valeur du paramètre
         */
        public Builder convergentColor(int convergentColor) {
            this.convergentColor = convergentColor;
            return this;
        }

        /**
         * @param rgb true si la couleur est selon le modèle RGB et false si elle est selon le modèle HSB
         * @param v1 la premiere valeur du modèle
         * @param v2 la deuxième valeur du modèle
         * @param v3 la troisième valeur du modèle
         * @return l'objet courant avec les valeurs de ses attributs rgb, v1, v2 et v3 égalent aux valeurs des paramètres correspondant
         */
        public Builder rgb(boolean rgb, int v1, int v2, int v3) {
            this.color = new ColorFunction(rgb, v1, v2, v3);
            return this;
        }

        /**
         * @return une Configuration à partir des attributs du Builder
         */
        public Configuration build() {
            if (maxIter <= 0)
                throw new IllegalArgumentException("le nombre d'itération maximum doit etre strictement positif");
            if (pas <= 0)
                throw new IllegalArgumentException("le pas doit etre strictement positif");
            if (longueur <= 0)
                throw new IllegalArgumentException("la longueur doit etre strictement positif");
            if (largeur <= 0)
                throw new IllegalArgumentException("la largeur doit etre strictement positif");
            return new Configuration(this);
        }
    }

    /**
     * @param b un Configuration.Builder
     * Créer une configuration avec les mêmes valeur que le builder
     */
    private Configuration(Builder b) {
        this.maxIter = b.maxIter;
        this.minRe = b.minRe;
        this.maxRe = b.maxRe;
        this.minIm = b.minIm;
        this.maxIm = b.maxIm;
        this.pas = b.pas;
        this.largeur = b.largeur;
        this.longueur = b.longueur;
        this.convergentColor = b.convergentColor;
        this.color = b.color;
    }

    /**
     * @return la valeur de l'attribut maxIter
     */
    public int getMaxIter() {
        return maxIter;
    }

    /**
     * @return la valeur de l'attribut minRe
     */
    public double getMinRe(){
        return minRe;
    }

    /**
     * @return la valeur de l'attribut maxRe
     */
    public double getMaxRe(){
        return maxRe;
    }

    /**
     * @return la valeur de l'attribut minIm
     */
    public double getMinIm(){
        return minIm;
    }

    /**
     * @return  la valeur de l'attribut maxIm
     */
    public double getMaxIm(){
        return maxIm;
    }

    /**
     * @return la valeur de l'attribut pas
     */
    public double getPas() {
        return pas;
    }

    /**
     * @return  la valeur de l'attribut longueur
     */
    public int getLongueur() {
        return longueur;
    }

    /**
     * @return la valeur de l'attribut largeur
     */
    public int getLargeur() {
        return largeur;
    }

    /**
     * @return  la valeur de l'attribut convergentColor
     */
    public int getConvergentColor() {
        return convergentColor;
    }

    /**
     * @return la valeur de l'attribut color
     */
    public ColorFunction getColor() {
        return color;
    }

    /**
     * Met à jour la valeur de l'attribut maxIter selon celle du paramètre
     * @param maxIter le nouveau nombre maximum d'itérations
     */
    public void setMaxIter(int maxIter){
        this.maxIter = maxIter;
    }

    /**
     * Met à jour la valeur de l'attribut convergentColor selon celle du paramètre
     * @param color la nouvelle couleur pour les points convergents
     */
    public void setConvergentColor(int color){
        this.convergentColor = color;
    }

    /**
     * Met à jour la valeur de l'attribut color selon celle du paramètre
     * @param color la nouvelle ColorFunction
     */
    public void setColor(ColorFunction color){
        this.color = color;
    }

    /**
     * Met à jour la valeur de l'attribut pas selon celle du paramètre
     * en vérifiant que celle-ci est valide et met à jour la valeur des
     * attributs minRe, maxRe, minIm et maxIm en conséquence
     * @param pas le nouveau pas
     */
    public void updatePas(double pas) {
        if (pas <= 0)
            throw new IllegalArgumentException("le pas doit être positif");
        this.pas = pas;
        double mRe = (minRe + maxRe) / 2;
        double mIm = (minIm + maxIm) / 2;
        double lRe = (longueur / 2) * pas;
        double lIm = (largeur / 2) * pas;
        minRe = mRe - lRe;
        maxRe = mRe + lRe;
        minIm = mIm - lIm;
        maxIm = mIm + lIm;
    }

    /**
     * Met à jour la valeur de l'attribut minRe selon celle du paramètre
     * et met à jour la valeur du paramètre maxRe en conséquence
     * @param minRe le nouveau minimum des réels
     */
    public void updateMinRe(double minRe) {
        double d = minRe - this.minRe;
        this.minRe = minRe;
        this.maxRe += d;
    }

    /**
	 * Met à jour la valeur de l'attribut maxRe selon celle du paramètre
     * et met à jour la valeur du paramètre minRe en conséquence
     * @param maxRe le nouveau maximum des réels
     */
    public void updateMaxRe(double maxRe) {
        double d = maxRe - this.maxRe;
        this.maxRe = maxRe;
        this.minRe += d;
    }

    /**
     * Met à jour la valeur de l'attribut minIm selon celle du paramètre
     * et met à jour la valeur du paramètre maxIm en conséquence
     * @param minIm le nouveau minimum des imaginaires
     */
    public void updateMinIm(double minIm) {
        double d = minIm - this.minIm;
        this.minIm = minIm;
        this.maxIm += d;
    }

    /**
     * Met à jour la valeur de l'attribut maxIm selon celle du paramètre
     * et met à jour la valeur du paramètre minIm en conséquence
     * @param maxIm le nouveau maximum des imaginaires
     */
    public void updateMaxIm(double maxIm) {
        double d = maxIm - this.maxIm;
        this.maxIm = maxIm;
        this.minIm += d;
    }

    /**
     * Met à jour la valeur de l'attribut longueur selon celle du paramètre
     * si celle-ci est valide et met à jour celle de maxRe en conséquence
     * @param longueur la nouvelle longueur
     */
    public void updateLongueur(int longueur) {
        if (longueur <= 0)
            throw new IllegalArgumentException("la longueur doit être positive");
        this.longueur = longueur;
        maxRe = (longueur - 1) * pas + minRe;
    }

    /**
     * Met à jour la valeur de l'attribut largeur selon celle du paramètre
     * si celle-ci est valide et met à jour celle de minIm en conséquence
     * @param largeur
     */
    public void updateLargeur(int largeur) {
        if (largeur <= 0)
            throw new IllegalArgumentException("la largeur doit être positive");
        this.largeur = largeur;
        minIm = maxIm - (largeur - 1) * pas;
    }

    /**
     * Créer une configuration avec les mêmes valeurs que la configuration c
     * @param c une configuration
     */
    private Configuration(Configuration c){
        this.maxIter = c.maxIter;
        this.minRe = c.minRe;
        this.maxRe = c.maxRe;
        this.minIm = c.minIm;
        this.maxIm = c.maxIm;
        this.pas = c.pas;
        this.largeur = c.largeur;
        this.longueur = c.longueur;
        this.convergentColor = c.convergentColor;
        this.color = c.color;
    }

    /**
     * @return une configuration qui est une copie de l'objet courant
     */
    public Configuration copy(){
        return new Configuration(this);
    }
}
