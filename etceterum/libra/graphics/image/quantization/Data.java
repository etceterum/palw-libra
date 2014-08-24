package etceterum.libra.graphics.image.quantization;

import java.util.ArrayList;
import java.util.Iterator;

public final class Data implements Iterable<Point> {
    final ArrayList<Point> points;
    
    public Data() {
        points = new ArrayList<Point>();
    }
    
    public Data(int cap) {
        points = new ArrayList<Point>(cap);
    }
    
    public void clear() {
        points.clear();
    }
    
    public void addPoint(Point p) {
        points.add(p);
    }
    
    public Point getPoint(int i) {
        return points.get(i);
    }
    
    public int getSize() {
        return points.size();
    }

    @Override
    public Iterator<Point> iterator() {
        return points.iterator();
    }
    
}
