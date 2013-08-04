import java.util.HashSet;
import java.util.Set;



/**
 * a power set tool kit
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
     * give the max number or plots (points of interest) you can rope in a cicle
     * <br>
     * This algorithm tries to reach from baricenter all plots of the following plot sets :
     * <ul>
     * <li> plot set of 1 plot</li>
     * <li> then plot set of 2 plots</li>
     * <li> then plot set of 3 plots containing a set of 2 plots which could have been roped in a circle in the previous step</li>
     * <li> then plot set of 4 plots containing a set of 3 plots which could have been roped in a circle in the previous step</li>
     * <li> etc...</li>
     *</ul>
     * untill we got plot sets to big to rope all plots in a circle or untill we manage to rope all plots of maps
     * @param radius of the circle
     * @return the max number of plots we can rope in a circle
     */
    public int maxReachablePlots(int radius) {
        // init recursive call with a plotSet set of one plot
        Set<PlotSet> rootPlotSetSet = new HashSet<PlotSet>();
        for (Plot plot : allPlots) {
            PlotSet pSet = new PlotSet();
            pSet.add(plot);
            rootPlotSetSet.add(pSet);
        }
        // laucn recursive call
        return maxReachablePlots(rootPlotSetSet, radius);
    }

    /**
     * recursive function computing the max number of reachable plots
     * @param plotSetSet a set of plotSet
     * @param radius of the circle
     * @return the computed max number
     */
    private int maxReachablePlots(Set<PlotSet> plotSetSet, int radius) {
        int currentPlotSetsSize = plotSetSet.iterator().next().size();

        // else, let's prune all plotset we can not fully reach from baricenter
        Set<PlotSet> remainingPlotSetSet = new HashSet<PlotSet>();
        for (PlotSet candidatePlotSet : plotSetSet) {
            if (isFullyReachable(candidatePlotSet, radius)) {
                // this plotset still in curse !
                remainingPlotSetSet.add(candidatePlotSet);
            }
        }

        if (remainingPlotSetSet.size() == 0) {
            // no more candidates in curse... That means we had just reached the maximum number of plots 
            return currentPlotSetsSize - 1;
        } else if (currentPlotSetsSize == allPlots.size()) {
            // all plots can be roped in a circle... game over 
            return currentPlotSetsSize;
        } else {
            // else, can I add one more plot ?

            // first, let's build the set of n+1 plots plotSets
            Set<PlotSet> biggerPlotSetSet = new HashSet<PlotSet>();
            for(PlotSet stillInCursePlotSet : remainingPlotSetSet) {
                for(Plot p : allPlots) {
                    // new plotset with one more plot
                    PlotSet biggerPlotSet = stillInCursePlotSet.copy();
                    biggerPlotSet.add(p);
                    if (biggerPlotSet.size() == currentPlotSetsSize + 1) {
                        biggerPlotSetSet.add(biggerPlotSet);
                    }
                }
            }

            // let's test this set of bigger plotset
            return maxReachablePlots(biggerPlotSetSet, radius);
        }
    }


    /**
     * visit the plotset and say if all plots can be all roped in a circle  
     * @param plotset
     * @param radius
     * @return true is all a plots are reachable, or false if not or if plots have been already previously visited 
     */
    private boolean isFullyReachable(PlotSet plotset, int radius) {
        // try to reach all plots 
        Plot center = plotset.baricenter();
        return plotset.isFullyReachableFrom(center, radius);
    }


}
