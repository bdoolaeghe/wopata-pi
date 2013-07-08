import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;


public class PlotTest {

    @Test
    public void compareByAngle() {
        // case 1
        Plot p1 = new Plot(1, 3);
        Plot p2 = new Plot(1, 1.1);
        Plot[] plots = new Plot[] {p1,p2};
        Arrays.sort(plots, new Plot.ByAngleComparator(new Plot(0,0)));
        assertEquals(p2, plots[0]);
        assertEquals(p1, plots[1]);
        
        // case 2
        p1 = new Plot(1, 3);
        p2 = new Plot(1, 1.1);
        plots = new Plot[] {p1,p2};
        Arrays.sort(plots, new Plot.ByAngleComparator(new Plot(2,0)));
        assertEquals(p1, plots[0]);
        assertEquals(p2, plots[1]);
        
    }
    
    @Test
    public void compareByY() {
        // normal case
        Plot p1 = new Plot(2, 3);
        Plot p2 = new Plot(1, 1.1);
        Plot[] plots = new Plot[] {p1,p2};
        Arrays.sort(plots, Plot.ByYComparator);
        assertEquals(p2, plots[0]);
        assertEquals(p1, plots[1]);
        
        // same y case
        p1 = new Plot(2, 3);
        p2 = new Plot(1, 3);
        plots = new Plot[] {p1,p2};
        Arrays.sort(plots, Plot.ByYComparator);
        assertEquals(p2, plots[0]);
        assertEquals(p1, plots[1]);
        
    }

}
