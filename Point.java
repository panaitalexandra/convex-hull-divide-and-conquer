public class Point implements Comparable {
    private int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }


    // pentru compararea  în liste
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    // pentru sortarea inițială după X
    @Override
    public int compareTo(Object o) {
        Point other = (Point) o;
        if (this.x != other.x) {
            return this.x - other.x;
        }
        return this.y - other.y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}