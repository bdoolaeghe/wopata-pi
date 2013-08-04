import java.util.HashSet;
import java.util.Set;



/**
 * a power set
 * @author Bruno DOOLAEGHE
 * @see <a href="http://en.wikipedia.org/wiki/Power_set">power set</a>
 */
class PowerPlotSet {

    private PlotSet allPlots;

    public PowerPlotSet(PlotSet allPlots) {
        super();
        this.allPlots = allPlots;
    }

    /**
     * @param radius
     * @return the max number of plots we can rope in a circle
     */
    public int maxReachablePlots(int radius) {
        // start recursive browse
        Set<PlotSet> rootPlotSetSet = new HashSet<PlotSet>();
        rootPlotSetSet.add(allPlots);
        return maxReachablePlots(rootPlotSetSet, radius);
    }

    
    /**
     * recursive scan : 
     * first, the top floor of the set of powerset
     * then, invoke recurivly on the next floor
     * @param currentPlotSetSet
     * @param radius
     * @return
     */
    private int maxReachablePlots(Set<PlotSet> currentPlotSetSet, int radius) {
        // scan first floor
        int maxReachablePlots = maxReachablePlotsOnFloor(currentPlotSetSet, radius);
    
        if (maxReachablePlots != 0) {
            return maxReachablePlots;
        } else {
            // scan next floor
            Set<PlotSet> inferiorPlotSetSet = getNextFloor(currentPlotSetSet);
            return maxReachablePlots(inferiorPlotSetSet, radius);
        }
    }

    /**
     * scan among all set of plotset having same size if there is a plotset fully reachable
     * @param plotSetSet
     * @param radius
     * @return 0 if none is found, or number of reached plots if found
     */
    private int maxReachablePlotsOnFloor(Set<PlotSet> plotSetSet, int radius) {
        for (PlotSet plotSet : plotSetSet) {
          if (isFullyReachable(plotSet, radius)) {
              // the plotset is a maximum !
              return plotSet.size();
          }
        }
        return 0;
    }

    /**
     * given a set of <n> sized plotset, returns the set of <n-1> sized plotset
     * @param superiorPlotSetSet
     * @return
     */
    private Set<PlotSet> getNextFloor(Set<PlotSet> superiorPlotSetSet) {
        Set<PlotSet> inferiorPlotSetSet = new HashSet<PlotSet>();
        
        for (PlotSet plotSet : superiorPlotSetSet) {
            for (Plot blackPlot : plotSet) {
                PlotSet subSet = plotSet.subSetWithout(blackPlot);
                inferiorPlotSetSet.add(subSet);
            }
        }
        
        return inferiorPlotSetSet;
    }
    
    /**
     * visit the plotset and say if all plots are in a same circle  
     * @param plotset
     * @param radius
     * @return true is all a plots are reachable, or false if not or fi plots have been already previously visited 
     */
    private boolean isFullyReachable(PlotSet plotset, int radius) {
        // try to reach all plots 
        Plot center = plotset.baricenter();
//        System.out.println(center);
        return plotset.isFullyReachableFrom(center, radius);
    }


}
