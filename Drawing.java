import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

public class Drawing extends JPanel {
    private ArrayList<Point> points;
    private JTextArea logger;
    private Object[] hullObjects;
    private Algorithm alg;

    private final Color GRID_COLOR = new Color(224, 224, 224);
    private final Color AXIS_COLOR = new Color(100, 100, 100);
    private final Color POINT_COLOR = new Color(85, 156, 186);
    private final Color HULL_COLOR = new Color(50, 90, 151);
    private final Stroke HULL_STROKE = new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    public Drawing(JTextArea logger) {
        this.logger = logger;
        this.points = new ArrayList<>();
        this.alg = new Algorithm(points);

        this.setPreferredSize(new Dimension(501, 501));
        this.setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                processMouseClick(e.getX(), e.getY());
            }
        });
    }

    private void processMouseClick(int mouseX, int mouseY) {
        int logicX = mouseX - 250;
        int logicY = 250 - mouseY;

        Point newPoint = new Point(logicX, logicY);

        if (!points.contains(newPoint)) {
            points.add(newPoint);
            alg.setPoints(points);

            hullObjects = alg.solve();

            logger.append("New point: " + newPoint + "\n");
            logger.append("Convex Hull consists of " + hullObjects.length + " nodes" + "\n\n");
            repaint();
        }
    }

    public void reset() {
        points.clear();
        hullObjects = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2);
        drawAxes(g2);
        drawPoints(g2);
        drawHull(g2);
    }

    private void drawHull(Graphics2D g2) {
        if (hullObjects == null || hullObjects.length < 2) return;

        g2.setColor(HULL_COLOR);
        g2.setStroke(HULL_STROKE);

        for (int i = 0; i < hullObjects.length; i++) {
            Point p1 = (Point) hullObjects[i];
            Point p2 = (Point) hullObjects[(i + 1) % hullObjects.length];

            g2.drawLine(calcX(p1.getX()), calcY(p1.getY()),
                    calcX(p2.getX()), calcY(p2.getY()));
        }
    }

    private void drawPoints(Graphics2D g2) {
        for (Point p : points) {
            g2.setColor(POINT_COLOR);
            g2.fillOval(calcX(p.getX()) - 4, calcY(p.getY()) - 4, 8, 8);
        }
    }

    private void drawGrid(Graphics2D g2) {
        g2.setColor(GRID_COLOR);
        for(int i = -25; i <= 25; i++) {
            g2.drawLine(calcX(i * 10), 0, calcX(i * 10), 500);
            g2.drawLine(0, calcY(i * 10), 500, calcY(i * 10));
        }
    }

    private void drawAxes(Graphics2D g2) {
        g2.setColor(AXIS_COLOR);
        g2.drawLine(0, 250, 500, 250);
        g2.drawLine(250, 0, 250, 500);
    }

    private int calcX(int logicX) { return logicX + 250; }
    private int calcY(int logicY) { return 250 - logicY; }
}