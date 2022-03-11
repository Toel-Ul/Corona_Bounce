package Fractale;


import java.awt.Color;

/**
 * Classe qui calcule une couleur à partir du modèle rgb ou hsb
 */
public final class ColorFunction {
    /**
     * true si le modèle est rgb, false si le modèle est hsb
     */
    private final boolean rgb;
    /**
     * si le modèle est RGB alors v1 correspond à la valeur du rouge sinon de la teinte
     */
    private final int v1;
    /**
     * si le modèle est RGB alors v2 correspond à la valeur du vert sinon de la saturation
     */
    private final int v2;
    /**
     * si le modèle est RGB alors v3 correspond à la valeur du bleu sinon de la lumière
     */
    private final int v3;

    /**
     * Constructeur par défaut de ColorFunction avec les valeurs:
     * rgb = false
     * v1 = -1
     * v2 = 70
     * v3 = 70
     */
    public ColorFunction(){
        this.rgb = false;
        this.v1 = -1;
        this.v2 = 70;
        this.v3 = 70;
    }

    /**
     * @param rgb true si le modèle est RGB et false si le modèle est HSB
     * @param v1 la valeur du premier composant du modèle
     * @param v2 la valeur du deuxième composant du modèle
     * @param v3 la valeur du troisième composant du modèle
     * Créer un ColorFunction selon les paramètres donnés
     */
    public ColorFunction(boolean rgb, int v1, int v2, int v3){
    	if (rgb && (v1 < -1 || v1 > 255 || v2 < -1 || v2 > 255 || v3 < -1 || v3 > 255))
            throw new IllegalArgumentException("pour le modèle RGB les valeurs doivent etre entre -1 et 255");
    	if (!rgb && (v1 < -1 || v1 > 360 || v2 < -1 || v2 > 100 | v3 < -1 || v3 > 100))
    		throw new IllegalArgumentException("pour le modèle HSB, v1 doit être entre -1 et 360 et v2 et v3 entre -1 et 100");
        this.rgb = rgb;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    /**
     * @param indice l'indice de divergence
     * @param maxIter le maximun d'itération pour le calcule de l'indice de divergence (pour la proportionnalité)
     * @return un entier correspondant à la couleur associée à un indice de divergence.
     */
    public int getColor(int indice, int maxIter){
        if (rgb) {
            int r = (v1 == -1) ? (255 * indice) / maxIter : v1;
            int g = (v2 == -1) ? (255 * indice) / maxIter : v2;
            int b = (v3 == -1) ? (255 * indice) / maxIter : v3;
            return (r << 16) | (g << 8) | b;
        }
        float h = (v1 == -1) ? (indice*1f / maxIter) : (v1 / 360f);
        float s = (v2 == -1) ? (indice*1f / maxIter) : (v2 / 100f);
        float b = (v3 == -1) ? (indice*1f / maxIter) : (v3 / 100f);
        return Color.HSBtoRGB(h,s,b);
    }

    /**
     * @return un booleen indiquant si la couleur est de type RGB
     */
    public boolean isRGB() {
        return rgb;
    }

    /**
     * @return la valeur de v1
     */
    public int getV1() {
        return v1;
    }

    /**
     * @return la valeur de v2
     */
    public int getV2() {
        return v2;
    }

    /**
     * @return la valeur de v3
     */
    public int getV3() {
        return v3;
    }
}
