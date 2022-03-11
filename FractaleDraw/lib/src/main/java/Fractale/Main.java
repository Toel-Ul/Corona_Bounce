package Fractale;

import java.awt.EventQueue;

import org.apache.commons.cli.*;

/**
 * Classe qui contient le main du programme
 */
public class Main {
    /**
     * Analyse la ligne de commande l'exécute
     * @param args les arguments de la ligne de commande
     */
    public static void main(String[] args) {

        Options help = helpOptions();
        Options graphic = graphicOptions();
        Options config = configOptions();
        config.addOption(help.getOption("h")).addOption(graphic.getOption("g"));

        CommandLineParser parser = new DefaultParser();
        
        try {
            CommandLine line = parser.parse(help, args, true);
            if (line.hasOption("h")) {
                printHelp(config.addOption(fileOption()));
                System.exit(0);
            }
            line = parser.parse(graphic, args, true);
            
            Fractale fractale = null;
            Configuration configuration = null;
            if (line.hasOption("g")) {
                if (line.hasOption("l")){
                    args = SaveImage.readConfig(line.getOptionValue("l"));
                    line = parser.parse(config, args, true);
                    fractale = parseFractale(line);
                    configuration = parseConfiguration(line);
                }
                else {
                    fractale = Mandelbrot.getInstance();
                    configuration = new Configuration.Builder().build();
                }
                launchIG(new FractaleImage(fractale, configuration));
            }
            else {
                config.addOption(fileOption());
                line = parser.parse(config, args, true);
                String filename = line.getOptionValue("f");
                fractale = parseFractale(line);
                configuration = parseConfiguration(line);
                SaveImage.save(filename, new FractaleImage(fractale, configuration));
            }
        } catch (IllegalArgumentException | ParseException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Affiche l'aide pour la ligne de commande
     * @param options les options possibles de la ligne de commande
     */
    private static void printHelp(Options options) {
        String header = "Créer des images de fractales.\n";
        String footer = "Si les options -P, -t et -p sont données, le plan complexe est modifié selon le pas et la taille de l'image."
                + "Par défaut la couleur des points divergents correspond à -H -1 70 70.";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Fractale", header, options, footer, true);
    }

    /**
     * Lance l'interface graphique à partir du modèle fImage
     * @param fImage le modèle initial
     */
    private static void launchIG(FractaleImage fImage) {
        EventQueue.invokeLater( () ->
        {
            Controller controleur = new Controller(fImage);
            Frame frame = new Frame(fImage, controleur);
            frame.setVisible(true);
        });
    }

    /**
     * @param line la ligne de commande à analyser
     * @return une fractale associée à la ligne de commande line
     * @throws ParseException
     */
    public static Fractale parseFractale(CommandLine line) throws ParseException {
        if (line.hasOption("j"))
            return new Julia(line.getOptionValue("j"));
        return new Mandelbrot();
    }

    /**
     * @param line la ligne de commande à analyser
     * @return une configuration associée à la ligne de commande line
     * @throws ParseException
     */
    private static Configuration parseConfiguration(CommandLine line) throws ParseException {
        Configuration.Builder config = new Configuration.Builder();
        
        if (line.hasOption("i"))
            config.maxIter(((Number) line.getParsedOptionValue("i")).intValue());

        if (line.hasOption("p"))
            config.pas(((Number) line.getParsedOptionValue("p")).doubleValue());

        if (line.hasOption("P")) {
            String[] P = line.getOptionValues("P");
            double minRe = Double.parseDouble(P[0]);
            double maxRe = Double.parseDouble(P[1]);
            double minIm = Double.parseDouble(P[2]);
            double maxIm = Double.parseDouble(P[3]);
            config.planComplexe(minRe, maxRe, minIm, maxIm);
        }
        
        if (line.hasOption("t")) {
            String[] t = line.getOptionValues("t");
            int longueur = Integer.parseInt(t[0]);
            int largeur = Integer.parseInt(t[1]);
            config.taille(longueur, largeur);
        }

        if (line.hasOption("c"))
            try {
                config.convergentColor(Integer.decode("0x" + line.getOptionValue("c")));
            } catch (NumberFormatException e){
                System.err.println(line.getOptionValue("c") + " n'est pas un héxadécimal");
                System.exit(-1);
            }

        int v1, v2, v3;
        if (line.hasOption("R")) {
            String[] R = line.getOptionValues("R");
            v1 = Integer.parseInt(R[0]);
            v2 = Integer.parseInt(R[1]);
            v3 = Integer.parseInt(R[2]);
            config.rgb(true, v1, v2, v3);
        }
        else if (line.hasOption("H")) {
            String[] H = line.getOptionValues("H");
            v1 = Integer.parseInt(H[0]);
            v2 = Integer.parseInt(H[1]);
            v3 = Integer.parseInt(H[2]);
            config.rgb(false, v1, v2, v3);
        }
        return config.build();
    }

    /**
     * @return l'option help
     */
    private static Options helpOptions() {
        Options res = new Options();
        Option help = Option.builder("h")
                .longOpt("help")
                .desc("Affiche l'aide")
                .build();
        res.addOption(help);
        return res;
    }

    /**
     * @return les options de l'interface graphique
     */
    private static Options graphicOptions() {
        Options res = new Options();
        Option graphic = Option.builder("g")
                .longOpt("graphic")
                .desc("Lance l'interface graphique")
                .build();
        res.addOption(graphic);
        Option load = Option.builder("l")
                .longOpt("load")
                .desc("Lance l'interface graphique à partir de la configuration d'un fichier")
                .hasArg(true)
                .argName("nom")
                .build();
        res.addOption(load);
        return res;
    }

    /**
     * @return l'option du nom de fichier
     */
    private static Option fileOption(){
         Option filename = Option.builder("f")
                .longOpt("filename")
                .desc("Nom du fichier de l'image")
                .hasArg(true)
                .argName("nom")
                .required(true)
                .build();
        return filename;
    }

    /**
     * @return les options de la fractale et de la configuration
     */
    private static Options configOptions() {
        Options res = new Options();

        OptionGroup fractale = new OptionGroup();
        Option mandelbrot = Option.builder("m")
                .longOpt("mandelbrot")
                .desc("Ensemble de Mandelbrot (choisi par défaut)")
                .build();
        fractale.addOption(mandelbrot);
        Option julia = Option.builder("j")
                .longOpt("julia")
                .desc("Ensemble de Julia. La fonction doit être une somme de (c_i)z^i ou i est la puissance et c_i le coefficient associé.")
                .hasArg(true)
                .argName("fonction")
                .build();
        fractale.addOption(julia);
        res.addOptionGroup(fractale);

        Option maxIter = Option.builder("i")
                .longOpt("maxIter")
                .desc("Nombre maximum d'itération (50 par defaut)")
                .hasArg(true)
                .argName("itération maximum")
                .type(Number.class)
                .build();
        res.addOption(maxIter);

        Option pas = Option.builder("p")
                .longOpt("pas")
                .desc("Pas de discrétisation (0.005 par défaut)")
                .hasArg(true)
                .argName("pas")
                .type(Number.class)
                .build();
        res.addOption(pas);

        Option planComplexe = Option.builder("P")
                .longOpt("plan")
                .desc("Le plan complexe des valeurs à calculer (-2 1.995 -2 1.995 par défaut)")
                .hasArg(true)
                .numberOfArgs(4)
                .valueSeparator(' ')
                .argName("minimum réel> <maximum réel> <minimum imaginaire> <maximum imaginaire")
                .type(Number.class)
                .build();
        res.addOption(planComplexe);

        Option taille = Option.builder("t")
                .longOpt("taille")
                .desc("Taille de l'image (800 800 par défaut)")
                .hasArg(true)
                .numberOfArgs(2)
                .valueSeparator(' ')
                .argName("longueur> <largeur")
                .type(Number.class)
                .build();
        res.addOption(taille);

        Option couleurConvergent = Option.builder("c")
                .longOpt("convergent")
                .desc("Couleur des points convergents en RGB en héxadécimal. -1 s'il y en a pas (-1 par défaut)")
                .hasArg(true)
                .argName("couleur")
                .build();
        res.addOption(couleurConvergent);

        OptionGroup couleur = new OptionGroup();
        Option rgb = Option.builder("R")
                .longOpt("rgb")
                .desc("Couleur des points divergents en RGB. -1 si propotionnelle à l'indice de divergence")
                .hasArg(true)
                .numberOfArgs(3)
                .valueSeparator(' ')
                .argName("rouge> <vert> <bleu")
                .type(Number.class)
                .build();
        couleur.addOption(rgb);
        Option hsb = Option.builder("H")
                .longOpt("hsb")
                .desc("Couleur des points divergents en HSB. -1 si propotionnelle à l'indice de divergence")
                .hasArg(true)
                .numberOfArgs(3)
                .valueSeparator(' ')
                .argName("teinte> <saturation> <luminosite")
                .type(Number.class)
                .build();
        couleur.addOption(hsb);
        res.addOptionGroup(couleur);
        return res;
    }
}
