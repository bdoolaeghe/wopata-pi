
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Wopata java puzzle :-)
 * <br><br>
 * challenge : <a href="http://www.kskills.com/Home/PublicChallengeDetail/493">http://www.kskills.com/Home/PublicChallengeDetail/493</a>
 * <br>
 * solution sources and tests : <a href="https://github.com/bdoolaeghe/wopata-pi.git">https://github.com/bdoolaeghe/wopata-pi.git</a>
 * @version 4.0 
 * @author Bruno DOOLAEGHE
 */
class kskillsAll {

    static class Plot {

        private double x;
        private double y;
        private int hashCode;

        public Plot(double x, double y) {
            super();
            this.x = x;
            this.y = y;
            // immuable object
            this.hashCode = computeHashcode();
        }

        /**
         * @return the X of plot
         */
        public double getX() {
            return x;
        }

        /**
         * @return the Y of plot
         */
        public double getY() {
            return y;
        }
        
        @Override
        public String toString() {
            return "P(" + x +" , " + y + ")";
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        private int computeHashcode() {
            final int prime = 31;
            int result = 1;
            long temp;
            temp = Double.doubleToLongBits(x);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(y);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        /**
         * 2 plots are equals if  x1 = x2 and y1 = y2
         * @see linked with #hashCode() 
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Plot other = (Plot) obj;
            if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
                return false;
            if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
                return false;
            return true;
        }
    }


    static class PlotSet extends HashSet<Plot> {

        private static final long serialVersionUID = 1L;

        public PlotSet() {
            super();
        }
        
        public PlotSet(Plot...plots) {
            super();
            for (Plot plot : plots) {
                add(plot);
            }
        }

        public PlotSet copy() {
            PlotSet copy = new PlotSet();
            copy.addAll(this);
            return copy;
        }
        
        /**
         * create a new Plot and add it to the {@link PlotSet}
         * @param x of the plot to add
         * @param y of the plot to add
         */
        public void addPlot(double x, double y) {
            this.add(new Plot(x, y));
        }

        /**
         * create a new sub plot set, without the black plot
         * @param blackPlot
         * @return a new subset equals to this subset minus blackplot
         */
        public PlotSet subSetWithout(Plot blackPlot) {
            PlotSet subSet = new PlotSet();
            subSet.addAll(this);
            subSet.remove(blackPlot);
            return subSet;
        }
        
        /**
         * compute the baricenter of plots
         * @return the baricenter
         */
        public Plot baricenter() {
            double sumX = 0d;
            double sumY = 0d;
            for(Plot p : this) {
                sumX = sumX + p.getX();
                sumY = sumY + p.getY();
            }
            return new Plot(sumX / (double) size(), sumY / (double) size());
        }
        
        /**
         * @param center
         * @param range
         * @return true if all plots are closer than "range" from the center
         */
        public boolean isFullyReachableFrom(Plot center, double range) {
            for(Plot p : this) {
                if (length(center, p) > range) {
                    return false;
                }
            }
            // else, all plots have been reached
            return true;
        }
        
        /**
         * get the gap between 2 plots
         * @param p1
         * @param p2
         * @return
         */
        private double length(Plot p1, Plot p2) {
            return Math.sqrt(
                    Math.pow(p2.getX() - p1.getX(), 2)
                    + 
                    Math.pow(p2.getY() - p1.getY(), 2)
                    );
        }

        @Override
        public String toString() {
            String s = super.toString().replaceAll("\\[", "").replaceAll("\\]", "");
            String[] splits = s.split("\\),");
            Arrays.sort(splits);
            s = "<\n"; 
            for (String split : splits) {
                s = s + split + ")\n";
            }        
            
            return s + ">";
        }
    }


    static class KilledException extends Exception {
        private static final long serialVersionUID = 1L;
    }
    
    static  class Killable {

        private boolean isKilled = false;

        
        /**
         * stop the exeuction
         */
        public void kill() {
            this.isKilled = true;
        }
        
        public void checkIsNotKilled() throws KilledException {
            if (isKilled)
                throw new KilledException();
        }
    }

    
    /**
     * a power set tool kit
     * @author Bruno DOOLAEGHE
     * @see <a href="http://en.wikipedia.org/wiki/Power_set">power set</a>
     */
    static interface IPowerPlotSetService {

        /**
         * give the max number or plots (points of interest) you can rope in a cicle
         * 
         * @param radius of the circle
         * @return the max number of plots we can rope in a circle
         * @throws KilledException 
         */
        public abstract int maxReachablePlots(int radius) throws KilledException;

    }
    
    /**
     * an impl using powerset of plots ascending algorithm 
     * @author Bruno DOOLAEGHE
     * @see <a href="http://en.wikipedia.org/wiki/Power_set">power set</a>
     */
    static class PowerPlotSetAscServiceImpl extends Killable implements IPowerPlotSetService {

        private PlotSet allPlots;

        
        public PowerPlotSetAscServiceImpl(PlotSet allPlots) {
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
         * @throws KilledException 
         */
        @Override
        public int maxReachablePlots(int radius) throws KilledException {
            // init recursive call with a plotSet set of one plot
            Set<PlotSet> rootPlotSetSet = new HashSet<PlotSet>();
            for (Plot plot : allPlots) {
                PlotSet pSet = new PlotSet();
                pSet.add(plot);
                rootPlotSetSet.add(pSet);

                checkIsNotKilled();
            }
            // laucn recursive call
            return maxReachablePlots(rootPlotSetSet, radius);
        }

        /**
         * recursive function computing the max number of reachable plots
         * @param plotSetSet a set of plotSet
         * @param radius of the circle
         * @return the computed max number
         * @throws KilledException 
         */
        private int maxReachablePlots(Set<PlotSet> plotSetSet, int radius) throws KilledException {
            int currentPlotSetsSize = plotSetSet.iterator().next().size();

            // else, let's prune all plotset we can not fully reach from baricenter
            Set<PlotSet> remainingPlotSetSet = new HashSet<PlotSet>();
            for (PlotSet candidatePlotSet : plotSetSet) {
                if (isFullyReachable(candidatePlotSet, radius)) {
                    // this plotset still in curse !
                    remainingPlotSetSet.add(candidatePlotSet);
                }
                
                checkIsNotKilled();
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
                        
                        checkIsNotKilled();
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

    /**
     * an impl using powerset of plots descending algorithm 
     * @author Bruno DOOLAEGHE
     * @see <a href="http://en.wikipedia.org/wiki/Power_set">power set</a>
     */

    static class PowerPlotSetDescServiceImpl extends Killable implements IPowerPlotSetService {

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

    
    /**
     * a composite concurrent impl, browsing concurrently from the top and from the bottom the powerset
     * @author Bruno DOOLAEGHE
     */
    static class PowerPlotSetCompositeConcurrentServiceImpl implements IPowerPlotSetService {
        
        private PlotSet allPlots;

        public PowerPlotSetCompositeConcurrentServiceImpl(PlotSet allPlots) {
            super();
            this.allPlots = allPlots;
        }
        
        @Override
        public int maxReachablePlots(final int radius) {
            // create delegates services
            final PowerPlotSetAscServiceImpl powerPlotSetService1 = new PowerPlotSetAscServiceImpl(allPlots);
            final PowerPlotSetDescServiceImpl powerPlotSetService2 = new PowerPlotSetDescServiceImpl(allPlots);
            
            // create parallel workers
            Callable<Integer> worker1 = new Callable<Integer>() {
                
                @Override
                public Integer call() throws Exception {
                    int maxReachablePlots = powerPlotSetService1.maxReachablePlots(radius);
                    powerPlotSetService2.kill();
                    return maxReachablePlots;
                }
            };  
            Callable<Integer> worker2 = new Callable<Integer>() {
                
                @Override
                public Integer call() throws Exception {
                    int maxReachablePlots = powerPlotSetService2.maxReachablePlots(radius);
                    powerPlotSetService1.kill();
                    return maxReachablePlots;
                }
            };  
                
            // run in parallel 2 workers
            ExecutorService workerPool = Executors.newFixedThreadPool(2);
            Future<Integer> futureResult1 = workerPool.submit(worker1);
            Future<Integer> futureResult2 = workerPool.submit(worker2);
            
            // get result from winner worker
            Integer result = null;
            try {
                result = futureResult1.get();
//                System.out.println(powerPlotSetService1.getClass().getSimpleName() + " has found the solution : " + result); 
            } catch (InterruptedException | ExecutionException e) {
                try {
                    result = futureResult2.get();
//                    System.out.println(powerPlotSetService2.getClass().getSimpleName() + " has found the solution : " + result); 
                } catch (InterruptedException | ExecutionException e1) {
                    //should never happen
                    throw new RuntimeException("None of the 2 workers ended successfully", e1);
                }
            }
            
            return result.intValue();
        }

    }

    
    static class InputBean {

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

    public static void main(String[] args) throws java.lang.Exception {
        // read and parse input
        InputBean inputBean = parseInput();

        // compute the best place for holiday
        int k = inputBean.getK();
        PlotSet piSet = inputBean.getPiSet();
        int maxReachablePlots = new PowerPlotSetCompositeConcurrentServiceImpl(piSet).maxReachablePlots(k);

        // ouput the best number of reachable PI
        System.out.println(maxReachablePlots);
    }

    /**
     * parse the input parameters
     * 
     * @return a bean containing the PI set, and the search area radios
     * @throws IOException
     */
    public static InputBean parseInput() throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        // parse the world size
        String[] split = r.readLine().split(";");

        // parse circle and PI coordonates
        split = r.readLine().split(";");
        int k = Integer.parseInt(split[0]);
        int plots = Integer.parseInt(split[1]);

        // parse points of interest plots
        PlotSet piSet = new PlotSet();
        for (int i = 0; i < plots; i++) {
            split = r.readLine().split(",");
            piSet.add(new Plot(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
        }

        // dispose
        r.close();

        return new InputBean(k, piSet);
    }


}