import java.util.HashSet;
import java.util.Set;


/**
 * a solution browsing the powerset boooom->up and top->down to converge to the middle
 * @author Bruno DOOLAEGHE
 */
public class PowerPlotSetBothServiceImpl  implements IPowerPlotSetService {

    private PlotSet allPlots;
    
    public PowerPlotSetBothServiceImpl(PlotSet allPlots) {
        super();
        this.allPlots = allPlots;
    }

    /**
     *          UP
     * 
     *        (A,B,C)
     *   (A,B) (A,C) (B,C)
     *      (A) (B) (C)
     *      
     *         DOWN
     * @see IPowerPlotSetService#maxReachablePlots(int)
     */
    public int maxReachablePlots(int radius)  {
        // a. init bottomup recursive call with a plotSet set of one plot
        Set<PlotSet> bootomUpRootPlotSetSet = new HashSet<PlotSet>();
        for (Plot plot : allPlots) {
            PlotSet pSet = new PlotSet();
            pSet.add(plot);
            bootomUpRootPlotSetSet.add(pSet);
        }
        
        // b. init topdown recursive call with a full plotset
        Set<PlotSet> topDownRootPlotSetSet = new HashSet<PlotSet>();
        topDownRootPlotSetSet.add(allPlots);

        
        // laucn recursive call
        return maxReachablePlots(bootomUpRootPlotSetSet, topDownRootPlotSetSet, radius);
    }

    /**
     * recursive function computing the max number of reachable plots
     * @param bootomUpPlotSetSet a set of growing plotSet
     * @param topDownPlotSetSet  a set of shrinking  plotsets
     * @param radius of the circle
     * @return the computed max number
     * @throws KilledException 
     */
    private int maxReachablePlots(Set<PlotSet> bootomUpPlotSetSet, Set<PlotSet> topDownPlotSetSet, int radius)  {
        // a. bottom up currnt floor plotset set browsing
        /////////////////////////////////////////////////
        
        int currentPlotSetsSize = bootomUpPlotSetSet.iterator().next().size();

        // first, let's prune all plotset we can not fully reach from baricenter
        Set<PlotSet> remainingPlotSetSet = new HashSet<PlotSet>();
        for (PlotSet candidatePlotSet : bootomUpPlotSetSet) {
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
            Set<PlotSet> superiorBottomUpPlotSetSet = new HashSet<PlotSet>();
            for(PlotSet stillInCursePlotSet : remainingPlotSetSet) {
                for(Plot p : allPlots) {
                    // new plotset with one more plot
                    PlotSet biggerPlotSet = stillInCursePlotSet.copy();
                    biggerPlotSet.add(p);
                    if (biggerPlotSet.size() == currentPlotSetsSize + 1) {
                        superiorBottomUpPlotSetSet.add(biggerPlotSet);
                    }
                }
            }

            
            // b. top down currnt floor plotset set browsing
            /////////////////////////////////////////////////
            // scan floor
            int maxReachablePlots = maxReachablePlotsOnFloor(topDownPlotSetSet, radius);
            if (maxReachablePlots != 0) {
                return maxReachablePlots;
            } 
            
            // else, compute next inferior floor
            Set<PlotSet> inferiorTopDownPlotSetSet = getNextFloor(topDownPlotSetSet);
            
            // let's test the 2 next up and down floors of powerset
            return maxReachablePlots(superiorBottomUpPlotSetSet, inferiorTopDownPlotSetSet, radius);
        }
    }

    /**
     * given a set of <n> sized plotset, returns the set of <n-1> sized plotset
     * @param superiorPlotSetSet
     * @return
     * @throws KilledException 
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
     * scan among all set of plotset having same size if there is a plotset fully reachable
     * @param plotSetSet
     * @param radius
     * @return 0 if none is found, or number of reached plots if found
     * @throws KilledException 
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
