package pj_graphes;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;

public class VisualisationGraphe {
    public static void main(String[] args) {
        Random random = new Random();
        int rand = 5 + random.nextInt(5); // pour modifier le nb de sommets 
        Map<Integer, List<Integer>> graphe = ColorationGraphe.genererGrapheAleatoire(rand);
        ColorationGraphe coloration = new ColorationGraphe(graphe);
        //coloration.colorationBacktracking(0,4);
        //coloration.colorationFirstFit();
        //coloration.colorationLargestFirst();
        //coloration.colorationSmallestLast();
        coloration.colorationDSatur();

        JFrame frame = new JFrame("Visualisation du Graphe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.add(new GraphePanel(graphe, coloration.getAffectationCouleurs()));
        frame.setVisible(true);
    }
}