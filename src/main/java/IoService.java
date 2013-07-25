import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Some IO services
 * @author bdoolaeghe
 */
class IoService {
    
    /**
     * parse the input parameters
     * 
     * @return a bean containing the PI set, and the search area radios
     * @throws IOException
     */
    public InputBean parseInput() throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        // parse the world size
        String[] split = r.readLine().split(";");
//        final int m = Integer.parseInt(split[0]);
//        final int n = Integer.parseInt(split[1]);
        // create the corresponding world "grid"
//        world = createWorld(m, n);

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
