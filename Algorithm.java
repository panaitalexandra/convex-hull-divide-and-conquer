import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Algorithm {
    private ArrayList<Point> allPoints;

    public Algorithm(ArrayList<Point> p) {
        this.allPoints = new ArrayList<>(p);
    }

    public void setPoints(ArrayList<Point> p) {
        this.allPoints = new ArrayList<>(p);
    }

    // Metoda principală apelată din interfață
    public Object[] solve() {
        if (allPoints.size() < 3) return allPoints.toArray();

        // Sortăm inițial după X pentru Divide et Impera
        allPoints.sort(Comparator.comparingInt(Point::getX));

        List<Point> result = divideEtImpera(allPoints);
        return result.toArray();
    }

    private List<Point> divideEtImpera(List<Point> s) {
        if (s.size() <= 5) {
            return grahamScan(new ArrayList<>(s));
        }

        int mid = s.size() / 2;
        List<Point> s1 = divideEtImpera(s.subList(0, mid));
        List<Point> s2 = divideEtImpera(s.subList(mid, s.size()));

        return mergeHulls(s1, s2);
    }

    private List<Point> mergeHulls(List<Point> ch1, List<Point> ch2) {
        // 1. Alegem O ca fiind centrul de greutate al primelor 3 puncte din CH(S1)
        Point o = new Point(
                (ch1.get(0).getX() + ch1.get(1).getX() + ch1.get(2).getX()) / 3,
                (ch1.get(0).getY() + ch1.get(1).getY() + ch1.get(2).getY()) / 3
        );

        // 2. Verificăm dacă O este în interiorul CH(S2)
        boolean oInS2 = isInside(o, ch2);

        List<Point> l1 = new ArrayList<>(ch1);
        List<Point> l2;

        if (oInS2)
            // Cazul 1: O este în interior, ambele liste sunt direct interclasabile
            l2 = new ArrayList<>(ch2);
        else
            // Cazul 2: O este în exterior, trebuie să găsim dreptele de suport
            l2 = reorderForExternalPoint(o, ch2);


        // 3. Interclasare în O(n) bazată pe unghiul față de O
        List<Point> combined = mergeByAngle(o, l1, l2);

        // 4. Un singur pas Graham Scan în O(n)
        return fastGraham(combined);
    }


    private boolean isInside(Point p, List<Point> hull) {
        // Metoda penelor simplificată (O(n) sau O(log n))
        for (int i = 0; i < hull.size(); i++)
            if (calcDet(hull.get(i), hull.get((i + 1) % hull.size()), p) < 0)
                return false;
        return true;
    }

    private List<Point> mergeByAngle(Point o, List<Point> l1, List<Point> l2) {
        List<Point> merged = new ArrayList<>();
        int i = 0, j = 0;

        // Asigurăm că ambele liste încep de la cel mai mic unghi față de O
        sortPointsByAngle(o, l1);
        sortPointsByAngle(o, l2);

        while (i < l1.size() && j < l2.size())
            if (compareAngles(o, l1.get(i), l2.get(j)) <= 0)
                merged.add(l1.get(i++));
            else
                merged.add(l2.get(j++));

        while (i < l1.size())
            merged.add(l1.get(i++));
        while (j < l2.size())
            merged.add(l2.get(j++));

        return merged;
    }

    private List<Point> fastGraham(List<Point> points) {
        if (points.size() < 3) return points;
        List<Point> hull = new ArrayList<>();
        for (Point p : points) {
            while (hull.size() >= 2 && calcDet(hull.get(hull.size() - 2), hull.get(hull.size() - 1), p) <= 0)
                hull.remove(hull.size() - 1);
            hull.add(p);
        }
        return hull;
    }

    public double calcDet(Point p1, Point p2, Point p3) {
        return (double)(p2.getX() - p1.getX()) * (p3.getY() - p1.getY()) -
                (double)(p2.getY() - p1.getY()) * (p3.getX() - p1.getX());
    }

    private void sortPointsByAngle(Point o, List<Point> pts) {
        pts.sort((p1, p2) -> compareAngles(o, p1, p2));
    }

    private int compareAngles(Point o, Point a, Point b) {
        double angleA = Math.atan2(a.getY() - o.getY(), a.getX() - o.getX());
        double angleB = Math.atan2(b.getY() - o.getY(), b.getX() - o.getX());
        return Double.compare(angleA, angleB);
    }

    private List<Point> grahamScan(ArrayList<Point> pts) {
        // Implementare standard pentru baza recursivității
        if (pts.size() < 3) return pts;
        Point pivot = pts.stream().min(Comparator.comparingInt(Point::getY).thenComparingInt(Point::getX)).get();
        pts.sort((p1, p2) -> {
            double d = calcDet(pivot, p1, p2);
            if (d == 0) return Double.compare(distSq(pivot, p1), distSq(pivot, p2));
            return d > 0 ? -1 : 1;
        });
        return fastGraham(pts);
    }

    private double distSq(Point p1, Point p2) {
        return Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2);
    }

    private List<Point> reorderForExternalPoint(Point o, List<Point> hull) {
        // În acest caz, trebuie doar să ne asigurăm că punctele din hull2 
        // sunt tratate ca o listă ordonată unghiular față de O.
        List<Point> res = new ArrayList<>(hull);
        sortPointsByAngle(o, res);
        return res;
    }
}