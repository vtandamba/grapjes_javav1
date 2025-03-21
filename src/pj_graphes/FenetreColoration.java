package pj_graphes;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List; 

public class FenetreColoration extends JFrame {
    private ColorationGraphe coloration;
    private GraphePanel panel;
    private Map<Integer, List<Integer>> graphe;

    public FenetreColoration() {
        setTitle("Coloration de Graphe");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Barre de boutons
        JPanel boutonsPanel = new JPanel();

        JButton genererBtn = new JButton("Générer Graphe");
        JButton backtrackingBtn = new JButton("Backtracking");
        JButton firstFitBtn = new JButton("First Fit");
        JButton largestFirstBtn = new JButton("Largest First");
        JButton smallestLastBtn = new JButton("Smallest Last");
        JButton dsaturBtn = new JButton("DSatur");
        List<JButton> boutons = Arrays.asList(backtrackingBtn, firstFitBtn, largestFirstBtn, smallestLastBtn, dsaturBtn);

        boutonsPanel.add(genererBtn);
        boutonsPanel.add(backtrackingBtn);
        boutonsPanel.add(firstFitBtn);
        boutonsPanel.add(largestFirstBtn);
        boutonsPanel.add(smallestLastBtn);
        boutonsPanel.add(dsaturBtn);

        add(boutonsPanel, BorderLayout.NORTH);

        // Création initiale d’un graphe aléatoire
        graphe = ColorationGraphe.genererGrapheAleatoire(5);
        coloration = new ColorationGraphe(graphe);
        coloration.colorationDSatur();

        panel = new GraphePanel(graphe, coloration.getAffectationCouleurs());
        add(panel, BorderLayout.CENTER);

        // Actions des boutons
        genererBtn.addActionListener(e -> {
            graphe = ColorationGraphe.genererGrapheAleatoire(5 + new Random().nextInt(5));
            coloration = new ColorationGraphe(graphe);
            panel = new GraphePanel(graphe, coloration.getAffectationCouleurs());
            mettreAJourBoutons(genererBtn, boutons);
            refreshPanel();

        });

        backtrackingBtn.addActionListener(e -> {
            if (coloration.colorationBacktracking(0, 4)) {
                panel.repaint();
                mettreAJourBoutons(backtrackingBtn, boutons);

            } else {
                JOptionPane.showMessageDialog(this, "Aucune solution trouvée !");
            }
        });

        firstFitBtn.addActionListener(e -> {
            coloration.colorationFirstFit();
            panel.repaint();
            mettreAJourBoutons(firstFitBtn, boutons);
           
        });

        largestFirstBtn.addActionListener(e -> {
            coloration.colorationLargestFirst();
            panel.repaint();
            mettreAJourBoutons(largestFirstBtn, boutons);

        });

        smallestLastBtn.addActionListener(e -> {
            coloration.colorationSmallestLast();
            panel.repaint();
            mettreAJourBoutons(smallestLastBtn, boutons);

        });

        dsaturBtn.addActionListener(e -> {
            coloration.colorationDSatur();
            mettreAJourBoutons(dsaturBtn, boutons);
            panel.repaint();
        });
        panel.setPreferredSize(new Dimension(500, 500));

    }
    private void mettreAJourBoutons(JButton boutonActif, List<JButton> boutons) {
        for (JButton bouton : boutons) {
            if (bouton == boutonActif) {
                bouton.setBackground(Color.ORANGE);  // Active -> Orange
            } else {
                bouton.setBackground(UIManager.getColor("Button.background")); // Inactif -> Couleur par défaut
            }
        }
    }

    private void refreshPanel() {
        getContentPane().remove(panel);
        panel = new GraphePanel(graphe, coloration.getAffectationCouleurs());
        getContentPane().add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetreColoration app = new FenetreColoration();
            app.setVisible(true);
        });
    }
    
}
