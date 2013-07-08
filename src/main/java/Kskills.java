import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;

/**
 * Wopata java puzzle :-)
 * 
 * @author bdoolaeghe
 */
class kskills {

    private static PlotSet world; // the world !
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
        } else if (piSet.size() <= 1) {
            bestReachablePi = 1;            
            bestSite = piSet.iterator().next();
        } else {
            // at last 2 PI
            for (Plot candidate : searcheArea) {
                int reachablePi = 0;
                for (Plot pi : piSet) {
                    double distance = distance(candidate, pi);
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
     * by croping the minimal rectangle wrapping all PIs
     * This croped rectangle may not be the minimal area wrapping all PIs, but as a rectangle, it is much very lowcoast to compute
     * @param piSet
     * @return 
     */
    private static PlotSet reduceSearchArea(PlotSet piSet) {
        throw new RuntimeException("not yet implemented !");
    }
    
    
//    private static PlotSet fillTriangle(Plot a, Plot b, Plot c) {
//        PlotSet triangleArea = new PlotSet();
//        
//        return null;
//        
//    }
    

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
        if (piSet.size() > 1) {
            for (Plot plot1 : piSet) {
                // is plot1 "lonely" in its area ?
                boolean isAlone = true;
                for (Plot plot2 : piSet) {
                    if (plot1 != plot2 && distance(plot1, plot2) < (2 * radius)) {
                        isAlone = false;
                        break;
                    }
                }
                
                if (isAlone) {
                    // plot1 is alone, we can evict it to reduce the search area
                    piSet.remove(plot1);
                }
            }
        }
        return piSet;
    }

    /**
     * compute distance between a and b plots
     * 
     * @param a
     * @param b
     * @return the distance
     */
    private static double distance(Plot a, Plot b) {
        return Math.sqrt(
                Math.pow((b.getX() - a.getX()), 2) 
                + 
                Math.pow((b.getY() - a.getY()), 2)
               );
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
        final int m = Integer.parseInt(split[0]);
        final int n = Integer.parseInt(split[1]);
        // create the corresponding world "grid"
        world = createWorld(m, n);

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

    /**
     * create the dimensionned world
     * 
     * @param m the world length
     * @param n the world height
     * @return a new world
     */
    private static PlotSet createWorld(final int m, final int n) {
        PlotSet world = new PlotSet();
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                world.add(new Plot(x, y));
            }
        }
        return world;
    }

}