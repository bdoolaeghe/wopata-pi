import static org.junit.Assert.*;

import org.junit.Test;


/**
 * @author Bruno DOOLAEGHE
 */
public class PowerPlotSetServiceBothTest {

    @Test(timeout=1000)
    public void maxReachablePlots3() {
        Plot p0 = new Plot(3, 1);
        Plot p1 = new Plot(1, 1);
        Plot p2 = new Plot(2, 2);
        PlotSet plots = new PlotSet(p0, p1, p2);

        int maxReachablePlots = new PowerPlotSetBothServiceImpl(plots).maxReachablePlots(2);
        assertEquals(3, maxReachablePlots);
    }

    @Test(timeout=1000)
    public void maxReachablePlots2() {
        Plot p0 = new Plot(1, 1);
        Plot p1 = new Plot(1, 2);
        Plot p2 = new Plot(3, 1);
        PlotSet plots = new PlotSet(p0, p1, p2);
        
        int maxReachablePlots = new PowerPlotSetBothServiceImpl(plots).maxReachablePlots(1);
        assertEquals(2, maxReachablePlots);
    }
    
    @Test(timeout=1000)
    public void maxReachablePlots1() {
        Plot p0 = new Plot(1, 1);
        Plot p1 = new Plot(1, 4);
        Plot p2 = new Plot(4, 1);
        PlotSet plots = new PlotSet(p0, p1, p2);
        
        int maxReachablePlots = new PowerPlotSetBothServiceImpl(plots).maxReachablePlots(1);
        assertEquals(1, maxReachablePlots);
    }
    
    @Test(timeout=1000)
    public void maxReachablePlots1Exemple() {
        Plot p0 = new Plot(4, 4);
        Plot p1 = new Plot(6, 3);
        Plot p2 = new Plot(8, 3);
        Plot p3 = new Plot(7, 6);
        Plot p4 = new Plot(12, 10);
        PlotSet plots = new PlotSet(p0, p1, p2, p3, p4);
        
        int maxReachablePlots = new PowerPlotSetBothServiceImpl(plots).maxReachablePlots(3);
        assertEquals(4, maxReachablePlots);
    }
    

    @Test(timeout=1000)
    public void maxReachablePlots1MiniPlan() {
        Plot p0 = new Plot(0, 0);
        Plot p1 = new Plot(0, 1);
        Plot p2 = new Plot(1, 0);
        PlotSet plots = new PlotSet(p0, p1, p2);
        
        int maxReachablePlots = new PowerPlotSetBothServiceImpl(plots).maxReachablePlots(4);
        assertEquals(3, maxReachablePlots);
    }

    
    @Test(timeout=1000)
    public void maxReachablePlots1GrandPlan() {
        Plot [] plots = new Plot[] {
                new Plot(2,2),
                new Plot(3,8),
                new Plot(4,14),
                new Plot(6,11),
                new Plot(7,8),
                new Plot(8,5),
                new Plot(9,12),
                new Plot(12,3),
                new Plot(13,10),
                new Plot(16,6),
                new Plot(16,12),
                new Plot(17,2),
                new Plot(18,9),
                new Plot(19,13),
                new Plot(22,3),
                new Plot(25,4),
                new Plot(26,10),
                new Plot(29,3),
                new Plot(30,6),
                new Plot(30,14)
        };
        PlotSet plotset = new PlotSet(plots);
        
        int maxReachablePlots = new PowerPlotSetBothServiceImpl(plotset).maxReachablePlots(5);
        assertEquals(5, maxReachablePlots);
    }
    
    @Test(timeout=1000)
    public void maxReachablePlots1GrandPlanGrandCercel() {
        Plot [] plots = new Plot[] {
                new Plot(2,2),
                new Plot(3,8),
                new Plot(4,14),
                new Plot(6,11),
                new Plot(7,8),
                new Plot(8,5),
                new Plot(9,12),
                new Plot(12,3),
                new Plot(13,10),
                new Plot(16,6),
                new Plot(16,12),
                new Plot(17,2),
                new Plot(18,9),
                new Plot(19,13),
                new Plot(22,3),
                new Plot(25,4),
                new Plot(26,10),
                new Plot(29,3),
                new Plot(30,6),
                new Plot(30,14)
        };
        PlotSet plotset = new PlotSet(plots);
        
        int maxReachablePlots = new PowerPlotSetBothServiceImpl(plotset).maxReachablePlots(20);
        assertEquals(20, maxReachablePlots);
    }
    
    
    @Test(timeout=1000)
    public void maxReachablePlots11SeulPointPossible() {
        Plot [] plots = new Plot[] {
                new Plot(0,0),
                new Plot(2,4),
                new Plot(7,0),
                new Plot(7,5)
                };
        PlotSet plotset = new PlotSet(plots);
        
        int maxReachablePlots = new PowerPlotSetBothServiceImpl(plotset).maxReachablePlots(2);
        assertEquals(1, maxReachablePlots);
    }
    

}
