import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Wopata java puzzle :-)
 * <br><br>
 * challenge : <a href="http://www.kskills.com/Home/PublicChallengeDetail/493">http://www.kskills.com/Home/PublicChallengeDetail/493</a>
 * <br>
 * solution sources and tests : <a href="https://github.com/bdoolaeghe/wopata-pi.git">https://github.com/bdoolaeghe/wopata-pi.git</a>
 * 
 * @author Bruno DOOLAEGHE
 */
class kskills {
    
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