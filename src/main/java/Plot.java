
/**
 * a 2D plot
 * 
 * @author bdoolaeghe
 */
class Plot {

    private double x;
    private double y;
    private int hashCode;

    public Plot(double x, double y) {
        super();
        this.x = x;
        this.y = y;
        // immuable object
        this.hashCode = computeHashcode();
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
        return hashCode;
    }

    private int computeHashcode() {
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
    
    
}
