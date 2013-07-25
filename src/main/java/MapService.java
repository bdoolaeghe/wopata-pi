
/**
 * services for plots and map of points of interest
 * 
 * @author bdoolaeghe
 *
 */
class MapService {

    /**
     * search the best place for the hotel in the search area
     * and update {@link #bestReachablePi} 
     * @param searcheArea
     * @param piSet the set of PI
     * @param k the radius around the hotel we are looking for
     * @return the best place (center of the circle) or null if none is found
     */
    BestSite searchBestPlace(PlotSet searcheArea, PlotSet piSet, int k) {
        Plot bestSite = null;
        int bestReachablePi = 0;
        
        if (piSet.size() >= 1) {
            bestReachablePi = 1;            
            bestSite = piSet.iterator().next();

            // at last 2 PI
            for (Plot candidate : searcheArea) {
                int reachablePi = 0;
                for (Plot pi : piSet) {
                    double distance = new Segment(candidate, pi).getSize();
                    if (distance < k) {
                        // PI is reachable !
                        reachablePi++;
                    }
                }
                
                // is the site intersting ?
                if (reachablePi > bestReachablePi) {
                    bestReachablePi = reachablePi;
                    bestSite = candidate;
                }
            }
        }
        return new BestSite(bestSite, bestReachablePi);
    }

    /**
     * reduce the search area,
     * by croping to the minimal convexe hull wrapping all PIs
     * This croped 
     * @param piSet
     * @return the reduced search area 
     */
    PlotSet reduceSearchArea(PlotSet piSet) {
        return piSet.getConvexHull().getGridContainedPlots();
    }
    

    /**
     * remove the aberrant points of interest (isolated one whe have no chance to regroup)
     * such a PI has necessarily no other PI inside a 2k radius circle 
     * <br>     
     * NB : This operation has a cost, which probbly remains interesting because in a real world, the world is big and number of PI is small. 
     * In this case it should speed up problem resolution
     * @param piSet2
     * @param k2
     * @return reduced PI set
     */
    PlotSet evictLonelyPi(PlotSet piSet, int radius) {
        PlotSet lonelyPlots = new PlotSet();
        
        if (piSet.size() > 1) {
            for (Plot plot1 : piSet) {
                // is plot1 "lonely" in its area ?
                boolean isAlone = true;
                for (Plot plot2 : piSet) {
                    if (plot1 != plot2 && new Segment(plot1, plot2).getSize() <= (2 * radius)) {
                        isAlone = false;
                        break;
                    }
                }
                
                if (isAlone) {
                    // plot1 is alone, we can evict it to reduce the search area
                    lonelyPlots.add(plot1);
                }
            }
            if (lonelyPlots.size() == piSet.size()) {
                // all plots are isolated... we keep just one plot
                lonelyPlots.remove(piSet.iterator().next());
            }
        }
        
        piSet.removeAll(lonelyPlots);
        
        return piSet;
    }


    /**
     * find the best place where we can reach the most of PI
     * 
     * @param piSet the set of PI
     * @param k the radius of search area
     * @return the plot of the best place to be !
     */
    public BestSite findBestPlaceForHolidays(PlotSet piSet, int k) {
        // remove the aberrant points of interest (isolated one whe have no chance to regroup)
        piSet = evictLonelyPi(piSet, k);
        
        // reduce the best place search area
        PlotSet searcheArea = reduceSearchArea(piSet);
        
        // search the best place in the search area to reach the most of non-evicted PI
        BestSite bestSite = searchBestPlace(searcheArea, piSet, k);

        return bestSite;
    }
    
}
