import java.util.HashSet;
import java.util.Set;

/**
 * an impl using powerset of plots descending algorithm 
 * @author Bruno DOOLAEGHE
 * @see <a href="http://en.wikipedia.org/wiki/Power_set">power set</a>
 */

public class PowerPlotSetDescServiceImpl extends Killable implements IPowerPlotSetService {

    private PlotSet allPlots;

    public PowerPlotSetDescServiceImpl(PlotSet allPlots) {
        super();
        this.allPlots = allPlots;
    }

    /**
     * give the max number or plots (points of interest) you can rope in a cicle
     * <br>
     * This algorithm tries to reach from baricenter all plots of the following plot sets :
     * <ul>
     * <li> plot set all (n) plots</li>
     * <li> then plot sets of n-1 plots</li>
     * <li> then plot sets of n-2 plots</li>
     * <li> etc...</li>  
     *</ul>
     * ... untill all plots of current size plot set can be roped in baricentered circle (or plot sets have only one plot)
     * @param radius of the circle
     * @return the max number of plots we can rope in a circle
     * @throws KilledException 
     */
    @Override
    public int maxReachablePlots(int radius) throws KilledException {
        // start recursive browse
        Set<PlotSet> rootPlotSetSet = new HashSet<PlotSet>();
        rootPlotSetSet.add(allPlots);
        return maxReachablePlots(rootPlotSetSet, radius);
    }


    /**
     * recursive scan :
     * first, the top floor of the set of powerset
     * then, invoke recurivly on the next under-floor
     * @param currentPlotSetSet
     * @param radius
     * @return
     * @throws KilledException 
     */
    private int maxReachablePlots(Set<PlotSet> currentPlotSetSet, int radius) throws KilledException {
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
     * @throws KilledException 
     */
    private int maxReachablePlotsOnFloor(Set<PlotSet> plotSetSet, int radius) throws KilledException {
        for (PlotSet plotSet : plotSetSet) {
            if (isFullyReachable(plotSet, radius)) {
                // the plotset is a maximum !
                return plotSet.size();
            }
            checkIsNotKilled();
        }
        return 0;
    }

    /**
     * given a set of <n> sized plotset, returns the set of <n-1> sized plotset
     * @param superiorPlotSetSet
     * @return
     * @throws KilledException 
     */
    private Set<PlotSet> getNextFloor(Set<PlotSet> superiorPlotSetSet) throws KilledException {
        Set<PlotSet> inferiorPlotSetSet = new HashSet<PlotSet>();

        for (PlotSet plotSet : superiorPlotSetSet) {
            for (Plot blackPlot : plotSet) {
                PlotSet subSet = plotSet.subSetWithout(blackPlot);
                inferiorPlotSetSet.add(subSet);
                checkIsNotKilled();
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
        // System.out.println(center);
        return plotset.isFullyReachableFrom(center, radius);
    }



}
