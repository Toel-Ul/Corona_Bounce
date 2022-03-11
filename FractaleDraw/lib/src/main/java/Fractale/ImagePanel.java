package Fractale;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Classe héritant de la classe JPanel pour afficher des images de fractale
 */
public class ImagePanel extends javax.swing.JPanel{
    /**
     * l'image à afficher
     */
    private BufferedImage image;

    /**
     * Modifie l'image à afficher
     * @param image la nouvelle image
     */
    public void setImage(BufferedImage image){
        this.image = image;
    }

    /**
     * Dessine l'image dans le panel
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponents(g);
        if (image != null){
            g.drawImage(image, 0, 0, null);
        }
    }
}
