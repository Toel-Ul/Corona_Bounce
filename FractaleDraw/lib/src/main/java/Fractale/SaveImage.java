package Fractale;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Classe qui permet de sauvegarder l'image d'une FractaleImage dans un fichier PNG
 * et sa configuration dans un fichier TXT
 */
public class SaveImage {

    /**
     * Sauvegarde l'image et la configuration d'une FractaleImage dans un fichier PNG et TXT
     * sous un nom filename
     * @param filename le nom du fichier
     * @param modele le modèle
     */
    public static void save(String filename, FractaleImage modele) {
        BufferedImage image = modele.getImage();
        if (image == null) {
            modele.calculImage();
        }
        image = modele.getImage();

        File file = new File(filename + ".png");
        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        file = new File(filename + ".txt");
        try (FileWriter fw = new FileWriter(file)) {
            Configuration config = modele.getConfiguration();
            if (modele.getFractale() instanceof Mandelbrot) {
                fw.write("-m");
            } else {
                fw.write("-j " + ((Julia) modele.getFractale()).getFonction());
            }
            fw.write(" -i " + config.getMaxIter());
            fw.write(" -p " + config.getPas());
            double minRe = config.getMinRe();
            double maxRe = config.getMaxRe();
            double minIm = config.getMinIm();
            double maxIm = config.getMaxIm();
            fw.write(" -P " + minRe + " " + maxRe + " " + minIm + " " + maxIm);
            fw.write(" -t " + config.getLongueur() + " " + config.getLargeur());
            fw.write(" -c " + (config.getConvergentColor() == -1 ? -1 : Integer.toHexString(config.getConvergentColor())));
            if (config.getColor().isRGB()) {
                fw.write(" -R " + config.getColor().getV1() + " " + config.getColor().getV2() + " " + config.getColor().getV3());
            } else {
                fw.write(" -H " + config.getColor().getV1() + " " + config.getColor().getV2() + " " + config.getColor().getV3());
            }
            fw.write("\n");
            fw.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

    }

    /**
     * @param filename le nom du fichier
     * @return un tableau des arguments de la configuration dans filename
     */
    public static String[] readConfig(String filename) {
        try (BufferedReader rd = new BufferedReader(new FileReader(filename))) {
            String config = rd.readLine();
            return config.split(" ");
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(-1);
        }
        return null;
    }
}
