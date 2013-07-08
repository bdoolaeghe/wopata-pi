import java.util.Arrays;

/**
 * a triangle 
 * @author Bruno DOOLAEGHE
 */
public class Triangle {

    private Plot pLeft;
    private Plot pMiddle;
    private Plot pRight;
    
    private Segment leftSegment;
    private Segment rightSegment;
    private Segment lastSegment;

    public Triangle(final Plot p0, final Plot p1, final Plot p2) {
        super();
        // order plots
        Plot[] plots = new Plot[] {p0, p1, p2};
        Arrays.sort(plots, Plot.ByXComparator);
        pLeft = plots[0];
        pMiddle = plots[1];
        pRight = plots[2];
        
        // setup 3 segments
        this.leftSegment = new Segment(pLeft, pMiddle);
        this.rightSegment = new Segment(pMiddle, pRight);
        this.lastSegment = new Segment(pLeft, pRight);
    }


    /**
     * get the area contained in the triangle 
     * (more precisly, all plots on the grid and into or on the triangle)
     * @return the plots of grid in or on the triangle
     */
    public PlotSet getGridContainedPlots() {
        PlotSet area = new PlotSet();
        double x;
        double y1;
        double y2;
        PlotSet coloredPlots = null;

        if (leftSegment.isVerticale() && rightSegment.isVerticale()
                && isOnGrid(pLeft)) {
            // degenrated triangle : vertical flat triangle on the grid 
            x = pLeft.getX();
            y1 = Math.min(leftSegment.getMinY(), Math.min(rightSegment.getMinY(), lastSegment.getMinY()));
            y2 = Math.max(leftSegment.getMaxY(), Math.max(rightSegment.getMaxY(), lastSegment.getMaxY()));
            coloredPlots = new VerticalBrush(x, y1, y2).getContainedPlotsOfGrid();
            area.addAll(coloredPlots);            
        } else {
            // non flat vertical triangle (usual case)
            
            //              *
            //           *  *  *          
            //       * Left *     * 
            //     ******** *  Right *
            //            **********    *
            //                     ********
            
            // sweep from left to right the left half triangle
            if (! leftSegment.isVerticale()) {
                for (x = Math.ceil(pLeft.getX()); x <= pMiddle.getX(); x++) {
                    y1 = leftSegment.getY(x);
                    y2 = lastSegment.getY(x);
                    coloredPlots = new VerticalBrush(x, y1, y2).getContainedPlotsOfGrid();
                    area.addAll(coloredPlots);
                }
            }
            
            // sweep from left to right the right half triangle
            if (! rightSegment.isVerticale()) {
                for (x = Math.ceil(pMiddle.getX()); x <= pRight.getX(); x++) {
                    y1 = rightSegment.getY(x);
                    y2 = lastSegment.getY(x);
                    coloredPlots = new VerticalBrush(x, y1, y2).getContainedPlotsOfGrid();
                    area.addAll(coloredPlots);
                }
            }
        }
        return area;
    }


    /**
     * check if the point is on the grid
     * @param p
     * @return true if it is
     */
    private boolean isOnGrid(Plot p) {
        return Math.ceil(p.getX()) == p.getX();
    }

}
