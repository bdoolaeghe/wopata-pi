import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Wopata java puzzle :-)
 * 
 * @author bdoolaeghe
 */
class kskills {

//    private static PlotSet world; // the world !
    private static PlotSet piSet; // points of interest in the world
    private static int k; // radius of circle where PI should be reachable
    private static int bestReachablePi = 0;

    public static void main(String[] args) throws java.lang.Exception {
        // read and parse input
        parseInput();

        // compute the best place for holiday
        findBestPlaceForHolidays();

        // ouput the best number of reachable PI
        System.out.println(bestReachablePi);
    }

    /**
     * find the best place where we can reach the most of PI
     * 
     * @return the plot of the best place to be !
     */
    private static Plot findBestPlaceForHolidays() {
        // remove the aberrant points of interest (isolated one whe have no chance to regroup)
        piSet = evictLonelyPi(piSet, k);
        
        // reduce the best place search area
        PlotSet searcheArea = reduceSearchArea(piSet);
        
        // search the best place in the search area to reach the most of non-evicted PI
        Plot bestSite = searchBestPlace(searcheArea, piSet);

        return bestSite;
    }

    /**
     * search the best place for the hotel in the search area
     * and update {@link #bestReachablePi} 
     * @param searcheArea
     * @param piSet the set of PI
     * @return the best place (center of the circle) or null if none is found
     */
    private static Plot searchBestPlace(PlotSet searcheArea, PlotSet piSet) {
        Plot bestSite = null;
        if (piSet.size() <= 0) {
            bestReachablePi = 0;            
        } else if (piSet.size() >= 1) {
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
        return bestSite;
    }

    /**
     * reduce the search area,
     * by croping to the minimal convexe hull wrapping all PIs
     * This croped 
     * @param piSet
     * @return the reduced search area 
     */
    private static PlotSet reduceSearchArea(PlotSet piSet) {
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
    private static PlotSet evictLonelyPi(PlotSet piSet, int radius) {
        PlotSet lonelyPlots = new PlotSet();
        
        if (piSet.size() > 1) {
            for (Plot plot1 : piSet) {
                // is plot1 "lonely" in its area ?
                boolean isAlone = true;
                for (Plot plot2 : piSet) {
                    if (plot1 != plot2 && new Segment(plot1, plot2).getSize() < (2 * radius)) {
                        isAlone = false;
                        break;
                    }
                }
                
                if (isAlone) {
                    // plot1 is alone, we can evict it to reduce the search area
                    lonelyPlots.add(plot1);
                }
            }
        }
        
        piSet.removeAll(lonelyPlots);
        
        return piSet;
    }

    /**
     * parse the input parameters
     * 
     * @throws IOException
     */
    private static void parseInput() throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        // parse the world size
        String[] split = r.readLine().split(";");
//        final int m = Integer.parseInt(split[0]);
//        final int n = Integer.parseInt(split[1]);
        // create the corresponding world "grid"
//        world = createWorld(m, n);

        // parse circle and PI coordonates
        split = r.readLine().split(";");
        k = Integer.parseInt(split[0]);
        int plots = Integer.parseInt(split[1]);

        // parse points of interest plots
        piSet = new PlotSet();
        for (int i = 0; i < plots; i++) {
            split = r.readLine().split(",");
            piSet.add(new Plot(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
        }

        // dispose
        r.close();
    }
//
//    /**
//     * create the dimensionned world
//     * 
//     * @param m the world length
//     * @param n the world height
//     * @return a new world
//     */
//    private static PlotSet createWorld(final int m, final int n) {
//        PlotSet world = new PlotSet();
//        for (int x = 0; x < m; x++) {
//            for (int y = 0; y < n; y++) {
//                world.add(new Plot(x, y));
//            }
//        }
//        return world;
//    }

}