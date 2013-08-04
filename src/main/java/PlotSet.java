import java.util.Arrays;
import java.util.HashSet;

/**
 * a Set of plots  
 * 
 * @author bdoolaeghe
 * 
 */
class PlotSet extends HashSet<Plot> {
    
    private static final long serialVersionUID = 1L;

    public PlotSet() {
        super();
    }
    
    public PlotSet(Plot...plots) {
        super();
        for (Plot plot : plots) {
            add(plot);
        }
    }

    /**
     * create a new Plot and add it to the {@link PlotSet}
     * @param x of the plot to add
     * @param y of the plot to add
     */
    public void addPlot(double x, double y) {
        this.add(new Plot(x, y));
    }

    /**
     * create a new sub plot set, without the black plot
     * @param blackPlot
     * @return a new subset equals to this subset minus blackplot
     */
    public PlotSet subSetWithout(Plot blackPlot) {
        PlotSet subSet = new PlotSet();
        subSet.addAll(this);
        subSet.remove(blackPlot);
        return subSet;
    }
    
    /**
     * compute the baricenter of plots
     * @return the baricenter
     */
    public Plot baricenter() {
        double sumX = 0d;
        double sumY = 0d;
        for(Plot p : this) {
            sumX = sumX + p.getX();
            sumY = sumY + p.getY();
        }
        return new Plot(sumX / (double) size(), sumY / (double) size());
    }
    
    /**
     * @param center
     * @param range
     * @return true if all plots are closer than "range" from the center
     */
    public boolean isFullyReachableFrom(Plot center, double range) {
        for(Plot p : this) {
            if (length(center, p) > range) {
                return false;
            }
        }
        // else, all plots have been reached
        return true;
    }
    
    /**
     * get the gap between 2 plots
     * @param p1
     * @param p2
     * @return
     */
    private double length(Plot p1, Plot p2) {
        return Math.sqrt(
                Math.pow(p2.getX() - p1.getX(), 2)
                + 
                Math.pow(p2.getY() - p1.getY(), 2)
                );
    }

    @Override
    public String toString() {
        String s = super.toString();
        String[] splits = s.split("\\),");
        Arrays.sort(splits);
        s = ""; 
        for (String split : splits) {
            s = s + split + ")\n";
        }        
        
        return s;
    }
    
};
