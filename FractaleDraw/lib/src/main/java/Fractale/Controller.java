package Fractale;

import javax.swing.*;

/**
 * Controleur de la Frame et de la FractaleImage
 * membre de la modelisation MVC
 */
public class Controller {
    /**
     * représente un modèle de Fractale par défaut
     */
    private final FractaleImage defaut;
    /**
     * représente le modèle courant de la Fractale
     */
    private final FractaleImage fImage;
    /**
     * représente la vue courante
     */
    private Frame frame;

    /**
     * @param fImage
     * Constructeur prenant une FractaleImage en parametre pour
     * l'attribuer a fImage
     */
    public Controller(FractaleImage fImage){
        defaut = fImage.copy();
        this.fImage = fImage;
    }

    /**
     * @param frame
     * setteur de l'attribut frame
     */
    public  void setFrame(Frame frame){
        this.frame = frame;
    }

    /**
     * @param text
     * @return booleen
     * verifie que la fonction entrer en parametre pour la fractale Julia est valide
     */
    public boolean testFonction(String text) {
        try {
            Julia julia = new Julia(text);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * @param pas
     * setteur du pas et met a jour les couples minimum/maximum Reel et Imaginaire de frame
     */
    public void setPas(String pas){
        try {
            double value = Double.parseDouble(pas);
            fImage.getConfiguration().updatePas(value);
            frame.updateMinRe();
            frame.updateMaxRe();
            frame.updateMinIm();
            frame.updateMaxIm();
        } catch (Exception e) {
            frame.updatePas();
        }
    }

    /**
     * @param minRe
     * setteur du minimum Reel
     */
    public void setMinRe(String minRe){
        Configuration config = fImage.getConfiguration();
        try {
            double value = Double.parseDouble(minRe);
            config.updateMinRe(value);
            frame.updateMaxRe();
        } catch (Exception e){
            frame.updateMinRe();
        }
    }

    /**
     * @param maxRe
     * setteur du maximum Reel
     */
    public void setMaxRe(String maxRe){
        Configuration config = fImage.getConfiguration();
        try {
            double value = Double.parseDouble(maxRe);
            config.updateMaxRe(value);
            frame.updateMinRe();
        } catch (Exception e){
            frame.updateMaxRe();
        }
    }

    /**
     * @param minIm
     * setteur du minimum Imaginaire
     */
    public void setMinIm(String minIm){
        Configuration config = fImage.getConfiguration();
        try {
            double value = Double.parseDouble(minIm);
            config.updateMinIm(value);
            frame.updateMaxIm();
        } catch (Exception e){
            frame.updateMinIm();
        }
    }

    /**
     * @param maxIm
     * setteur du maximum Imaginaire
     */
    public void setMaxIm(String maxIm){
        Configuration config = fImage.getConfiguration();
        try {
            double value = Double.parseDouble(maxIm);
            config.updateMaxIm(value);
            frame.updateMinIm();
        } catch (Exception e){
            frame.updateMaxIm();
        }
    }

    /**
     * @param longueur
     * setteur de la longueur de l'image
     */
    public void setLongueur(int longueur){
        fImage.getConfiguration().updateLongueur(longueur);
        frame.updateMaxRe();
    }

    /**
     * @param largeur
     * setteur de la largeur de l'image
     */
    public void setLargeur(int largeur){
        fImage.getConfiguration().updateLargeur(largeur);
        frame.updateMinIm();
    }

    /**
     * @param mandelbrot
     * @param fonction
     * @param iteration
     * @param convergent
     * @param rgb
     * @param v1
     * @param v2
     * @param v3
     * affiche l'image de la fractale correspondant au parametre donné dans la vue
     */
    public void affiche(boolean mandelbrot, String fonction, int iteration,
            String convergent, boolean rgb, int v1, int v2, int v3){

        Fractale fractale = Mandelbrot.getInstance();
        if (! mandelbrot){
            try {
                fractale = new Julia(fonction);
            } catch (Exception e){
                return;
            }
        }
        fImage.setFractale(fractale);
        Configuration config = fImage.getConfiguration();
        config.setMaxIter(iteration);
        if (convergent == null)
            config.setConvergentColor(-1);
        else
            config.setConvergentColor(Integer.decode("0x" + convergent));
        ColorFunction color = new ColorFunction(rgb, v1, v2, v3);
        config.setColor(color);
        afficheImage();
    }

    /**
     * calcule et affiche l'image correspondant à fImage dans frame
     */
    private void afficheImage(){
        Thread t = new Thread(() -> {
            fImage.calculImage();
            frame.affiche();
        });
        t.start();
    }
    
    /**
     * @param direction
     * @param value
     * calcule un décalage selon une direction et une value pour ensuite recalculer et réafficher l'image avec ce décalage
     * par rapport à l'image précédente
     */
    public void deplace(int direction, int value){
        Configuration config = fImage.getConfiguration();
        double decalage = value * config.getPas();
        switch (direction){
            case 0:
                config.updateMaxIm(config.getMaxIm() + decalage); break;
            case 1: 
                config.updateMinIm(config.getMinIm() - decalage); break;
            case 2:
                config.updateMinRe(config.getMinRe() - decalage); break;
            case 3:
                config.updateMaxRe(config.getMaxRe() + decalage); break;
        }
        if (direction == 0 || direction == 1){
            frame.updateMaxIm();
            frame.updateMinIm();
        }
        else {
            frame.updateMaxRe();
            frame.updateMinRe();
        }
        afficheImage();
    }

    /**
     * @param zoom
     * @param value
     * calcule un zoom ou dezoom selon un boolean et une value
     * met à jour le pas, le minimum et maximum des intervalles réels et imaginaire de la frame
     * et recalcule et reaffiche l'image en conséquence
     */
    public void zoom(boolean zoom, String value){
        Configuration config = fImage.getConfiguration();
        double pas = config.getPas();
        try {
            double x = Double.parseDouble(value);
            if (x > 0){
                double newPas = zoom ? pas / x : pas * x;
                config.updatePas(newPas);
                frame.updatePas();
                frame.updateMinRe();
                frame.updateMaxRe();
                frame.updateMinIm();
                frame.updateMaxIm();
                afficheImage();
            }
        } catch (Exception e){
        }
    }

    /**
     *remet les valeurs des differents composants au valeur par defaut
     */
    public void defaut(){
        fImage.setConfiguration(defaut.getConfiguration().copy());
        fImage.setFractale(defaut.getFractale().copy());
        frame.initValues();
    }

    /**
     *créer une fenetre pop-up pour rentrer le nom du fichier sous lequel enregistrer l'image
     * si la demande est faite ou le nom donné n'est pas valide la sauvegarde est annulé
     */
    public void sauvegarde() {
        JFrame popPup = new JFrame();
        popPup.setTitle("Sauvegarde");
        popPup.setLocationRelativeTo(null);
        String getMessage = JOptionPane.showInputDialog(popPup, "Entrez le nom du fichier");
        if(getMessage != null && !getMessage.equals("")){
            SaveImage.save(getMessage,fImage);
            JOptionPane.showMessageDialog(popPup,"Sauvergarde effectué");
        }else JOptionPane.showMessageDialog(popPup,"Sauvegarde annulée");
    }

    /**
     *annule le calcule en cours de l'image
     */
    public void annule() {
        fImage.annule();
    }
}
