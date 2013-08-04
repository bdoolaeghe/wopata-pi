
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

/**
 * Wopata java puzzle :-)
 * <br><br>
 * challenge : <a href="http://www.kskills.com/Home/PublicChallengeDetail/493">http://www.kskills.com/Home/PublicChallengeDetail/493</a>
 * <br>
 * solution sources and tests : <a href="https://github.com/bdoolaeghe/wopata-pi.git">https://github.com/bdoolaeghe/wopata-pi.git</a>
 * @version 3.0 
 * @author Bruno DOOLAEGHE
 */
class kskillsAll {

    static class Plot {

        private double x;
        private double y;

        public Plot(double x, double y) {
            super();
            this.x = x;
            this.y = y;
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

        /**
         * @return a copy of {@link PlotSet} (containing same {@link Plot}s)
         */
        public PlotSet copy() {
            PlotSet copy = new PlotSet();
            copy.addAll(this);
            return copy;
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

    };


    static class PowerPlotSet {

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
        int maxReachablePlots = new PowerPlotSet(piSet).maxReachablePlots(k);

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