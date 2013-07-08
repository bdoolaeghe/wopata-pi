import java.util.Comparator;

/**
 * a 2D plot
 * 
 * @author bdoolaeghe
 */
public class Plot {

    private double x;
    private double y;

    public Plot(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     * @return the X of plot
     */
    public double getX() {
        return x;
    }

    /**
     * @return the Y of plot
     */
    public double getY() {
        return y;
    }
    
    @Override
    public String toString() {
        return "P(" + x +" , " + y + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * 2 plots are equals if  x1 = x2 and y1 = y2
     * @see linked with #hashCode() 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Plot other = (Plot) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
            return false;
        return true;
    }
    
    /**
     * compare 2 plots on their X. if X are equals, compare the Y
     */
    public static Comparator<Plot> ByXComparator = new Comparator<Plot>() {
        
        public int compare(Plot p1, Plot p2) {
            if (p1.getX() == p2.getX()) {
                return Double.compare(p1.getY(), p2.getY());
            } else {
                return Double.compare(p1.getX(), p2.getX());
            }
        }
        
    };
    

    /**
     * compare 2 plots on their Y. if Y are equals, compare the X
     */
    public static Comparator<Plot> ByYComparator = new Comparator<Plot>() {

        public int compare(Plot p1, Plot p2) {
            if (p1.getY() == p2.getY()) {
                return Double.compare(p1.getX(), p2.getX());
            } else {
                return Double.compare(p1.getY(), p2.getY());
            }
        }
        
    };
    
    /**
     * compare 2 Plots on the angle (P0, i, P1) vs. (P0, i, P2) where P0 is a specified pivot plot 
     */
    public static class ByAngleComparator implements Comparator<Plot> {

        private Plot pivot;

        /**
         * @param pivoPLot, wich must be the most down plot of compared plots
         */
        public ByAngleComparator(Plot pivoPLot) {
            super();
            this.pivot = pivoPLot;
        }
        
        public int compare(Plot o1, Plot o2) {
            if (pivot.getY() > o1.getY() || pivot.getY() > o2.getY()) {
                throw new UnsupportedOperationException("Can not compare " + o1 + " against " + o2 + " with the pivot " + pivot + " because pivot is not the most down plot" );
            }
            
            // vector1
            double x1 = o1.getX() - pivot.getX();  
            double y1 = o1.getY() - pivot.getY();
            double size1 = Math.sqrt(x1 * x1 + y1 * y1); 
            double scalar1 = x1; // o1.i
            double costheta1 = scalar1 / size1;
            
            // vector2
            double x2 = o2.getX() - pivot.getX();  
            double y2 = o2.getY() - pivot.getY();
            double size2 = Math.sqrt(x2 * x2 + y2 * y2);
            double scalar2 = x2; // o2.i
            double costheta2 = scalar2 / size2;
            
            // cos(theta) in same order as angles
            return Double.compare(costheta2, costheta1);
        }
        
    };
    
}
