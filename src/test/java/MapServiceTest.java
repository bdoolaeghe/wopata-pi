import static org.junit.Assert.*;

import org.junit.Test;


public class MapServiceTest {

    @Test
    public void evictLonelyPi() {
        // standard case
        Plot p0 = new Plot(0, 0);
        Plot p1 = new Plot(2, 4);
        Plot p2 = new Plot(7, 0);
        Plot p3 = new Plot(17, 15);
        
        PlotSet plots = new PlotSet();
        plots.add(p0);
        plots.add(p1);
        plots.add(p2);
        plots.add(p3);
        
        PlotSet reducedPiSet = new MapService().evictLonelyPi(plots, 5);
        
        assertEquals(3, reducedPiSet.size());
        
        // limit case
        p0 = new Plot(0, 0);
        p1 = new Plot(4, 0);
        plots = new PlotSet();
        plots.add(p0);
        plots.add(p1);
        
        reducedPiSet = new MapService().evictLonelyPi(plots, 2);
        
        assertEquals("2 plots are at the limit", 2, reducedPiSet.size());

        // out of limit case
        p0 = new Plot(0, 0);
        p1 = new Plot(4.1, 0);
        plots = new PlotSet();
        plots.add(p0);
        plots.add(p1);
        
        reducedPiSet = new MapService().evictLonelyPi(plots, 2);
        
        assertEquals("2 plots are too far", 1, reducedPiSet.size());
        
    }

}
