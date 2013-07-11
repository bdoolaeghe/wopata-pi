
/**
 * a segment (a set of plots between 2 plots "a" and "b" )
 * @author Bruno DOOLAEGHE
 */
class Segment {
    
    private Plot p1;
    private Plot p2;

    // equation : m.x + p.y = k
    private double m; 
    private double p;
    private double k;
    
    public Segment(Plot p1, Plot p2) {
        super();
        this.p1 = p1;
        this.p2 = p2;
        if (p2.getX() == p1.getX()) {
            // vertical line
            this.m = 1; 
            this.p = 0;
            this.k = p1.getX();
        } else if (p2.getY() == p1.getY()){
            // horizontale line
            this.m = 0;
            this.p = 1;
            this.k = p1.getY();
        } else {
            // affine
            this.m = p2.getY() - p1.getY();
            this.p = p1.getX() - p2.getX();
            this.k = m * p1.getX() + p * p1.getY();
        }
    }
    
    /**
     * get the X of point on the line of the segment
     * @param y
     * @return the X
     * @throws UnsupportedOperationException if the line is horizontale  
     */
    public double getX(double y) {
        if (isHorizontale()) {
            throw new UnsupportedOperationException("Can not get X for Y = '" + y + "' with horizontale line : y = " + k);
        } else {
            return (k - p * y) / m;
        }
    }

    /**
     * @return true if line is horizontale
     */
    private boolean isHorizontale() {
        return m == 0;
    }

    /**
     * get the Y of point on the line of the segment
     * @param x
     * @return the Y
     * @throws UnsupportedOperationException if the line is verticale
     */
    public double getY(double x) {
        if (isVerticale()) {
            throw new UnsupportedOperationException("Can not get Y for X = '" + x + "' with line : x = " + k);
        } else {
            return (k - m * x ) / p;
        }
    }
    
    /**
     * @return true if line is verticale
     */
    public boolean isVerticale() {
        return p == 0;
    }
    
    /**
     * @return true if the line is linear, false if its affine
     */
    public boolean isLinear() {
        return k == 0;
    }
 
    /**
     * @return the min Y of the segment
     */
    public double getMinY() {
        return Math.min(p1.getY(), p2.getY());
    }
    
    /**
     * @return the max Y of the segment
     */
    public double getMaxY() {
        return Math.max(p1.getY(), p2.getY());
    }

    /**
     * @return the length (size) of the segment
     */
    public double getSize() {
        return Math.sqrt(
                Math.pow((p2.getX() - p1.getX()), 2) 
                + 
                Math.pow((p2.getY() - p1.getY()), 2)
               );
    }

    
    @Override
    public String toString() {
        if (isVerticale()) {
            return "(D) : x = " + k +  "  and  " + p1.toString() + " -> " + p2.toString() ;
        }
        if (isHorizontale()) {
            return "(D) : y = " + k + "  and  " + p1.toString() + " -> " + p2.toString() ;
        }
        
        return "(D) : " + m + "x + " + p + "y = " + k + "  and  " + p1.toString() + " -> " + p2.toString() ;
    }
}
