
/**
 * a bean containing the problem inputs
 * @author bdoolaeghe
 *
 */
class InputBean {

    private int k;
    
    private PlotSet piSet;
    
    public InputBean(int k, PlotSet piSet) {
        super();
        this.k = k;
        this.piSet = piSet;
    }
    
    /**
     * @return the radius of search area
     */
    public int getK() {
        return k;
    }
    
    /**
     * @return the set of PI
     */
    public PlotSet getPiSet() {
        return piSet;
    }
    
}
