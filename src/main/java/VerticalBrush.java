/**
 * 
 * a vertical "brush" used to sweep an area from left to right.
 * 
 * @author bdoolaeghe
 */
class VerticalBrush {

    private double x;
    private double yTop;
    private double yBottom;

    /**
     * @param x the x-coord of the brush
     * @param y1 the top of the brush
     * @param y2 the bootom of the brush
     */
    public VerticalBrush(double x, double y1, double y2) {
        super();
        this.yTop = Math.max(y1, y2);
        this.yBottom = Math.min(y1, y2);
        this.x = x;
    }


    /**
     * get all grid plots between ytop and ybottom
     * 
     * @return all found point (only points of the grid)
     */
    public PlotSet getContainedPlotsOfGrid() {
        PlotSet plots = new PlotSet();

        for (int y = (int) (Math.ceil(yBottom)); y <= yTop; y++) {
            plots.addPlot(x, y);
        }
        return plots;
    }

}
