package pj_graphes;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

class GraphePanel extends JPanel {
    private Map<Integer, List<Integer>> graphe;
    private Map<Integer, Integer> affectationCouleurs;
    private static final Color[] COULEURS = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE,
            Color.MAGENTA, Color.PINK, Color.CYAN, Color.DARK_GRAY, Color.GRAY };
    private static final int RAYON = 20;
    private Map<Integer, Point> positions;

    public GraphePanel(Map<Integer, List<Integer>> graphe, Map<Integer, Integer> affectationCouleurs) {
        this.graphe = graphe;
        this.affectationCouleurs = affectationCouleurs;
        genererPositions();
    }

    private void genererPositions() {
        positions = new HashMap<>();
        
        // Vérifier que le panel a bien une taille définie
        int largeur = getWidth() > 0 ? getWidth() : 500;
        int hauteur = getHeight() > 0 ? getHeight() : 500;
        
        int rayon = (int) (Math.min(largeur, hauteur) * 0.4);
        int centerX = largeur / 2;
        int centerY = hauteur / 2;
        
        double angleStep = 2 * Math.PI / graphe.size();
        int index = 0;

        for (Integer sommet : graphe.keySet()) {
            int x = centerX + (int) (rayon * Math.cos(index * angleStep));
            int y = centerY + (int) (rayon * Math.sin(index * angleStep));
            positions.put(sommet, new Point(x, y));
            index++;
        }
        
        repaint(); // Force le redessin après mise à jour
    }

    private void ajusterParForceDirected() {
        int iterations = 200; // Plus d’itérations pour stabiliser
        double repulsion = 3000; // Ajusté pour éviter les superpositions
        double attraction = 0.02; // Ajusté pour éviter un graphe trop serré

        for (int it = 0; it < iterations; it++) {
            Map<Integer, Point> forces = new HashMap<>();

            for (Integer s : graphe.keySet()) {
                forces.put(s, new Point(0, 0));
            }

            // Forces de répulsion (évite le chevauchement)
            for (Integer s1 : graphe.keySet()) {
                for (Integer s2 : graphe.keySet()) {
                    if (!s1.equals(s2)) {
                        Point p1 = positions.get(s1);
                        Point p2 = positions.get(s2);
                        double dx = p1.x - p2.x;
                        double dy = p1.y - p2.y;
                        double distance = Math.max(Math.sqrt(dx * dx + dy * dy), 1);
                        double force = repulsion / (distance * distance);

                        forces.get(s1).translate((int) (force * dx / distance), (int) (force * dy / distance));
                        forces.get(s2).translate((int) (-force * dx / distance), (int) (-force * dy / distance));
                    }
                }
            }

            // Forces d’attraction (garde les arêtes lisibles)
            for (Integer s1 : graphe.keySet()) {
                for (Integer s2 : graphe.get(s1)) {
                    Point p1 = positions.get(s1);
                    Point p2 = positions.get(s2);
                    double dx = p1.x - p2.x;
                    double dy = p1.y - p2.y;
                    double distance = Math.max(Math.sqrt(dx * dx + dy * dy), 1);
                    double force = attraction * (distance * distance);

                    forces.get(s1).translate((int) (-force * dx / distance), (int) (-force * dy / distance));
                    forces.get(s2).translate((int) (force * dx / distance), (int) (force * dy / distance));
                }
            }

            // Appliquer les forces
            for (Integer s : graphe.keySet()) {
                Point p = positions.get(s);
                Point force = forces.get(s);
                p.translate(force.x / 15, force.y / 15); // Stabilisation lente
                positions.put(s, p);
            }
        }
    }


    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        genererPositions(); // Recalcule les positions avec la nouvelle taille
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        // Dessiner les arÃªtes
        for (Map.Entry<Integer, List<Integer>> entry : graphe.entrySet()) {
            int sommet = entry.getKey();
            Point p1 = positions.get(sommet);
            for (Integer voisin : entry.getValue()) {
                Point p2 = positions.get(voisin);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // Dessiner les sommets
        for (Map.Entry<Integer, Point> entry : positions.entrySet()) {
            int sommet = entry.getKey();
            Point p = entry.getValue();
            int couleurIndex = affectationCouleurs.getOrDefault(sommet, -1) - 1;
            Color couleur = (couleurIndex >= 0 && couleurIndex < COULEURS.length) ? COULEURS[couleurIndex]
                    : Color.BLACK;
            g.setColor(couleur);
            g.fillOval(p.x - RAYON / 2, p.y - RAYON / 2, RAYON, RAYON);
            g.setColor(Color.BLACK);
            g.drawOval(p.x - RAYON / 2, p.y - RAYON / 2, RAYON, RAYON);
            g.drawString(String.valueOf(sommet), p.x - 5, p.y - 10);
        }
    }
}

