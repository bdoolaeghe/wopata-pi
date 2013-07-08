import static org.junit.Assert.*;

import org.junit.Test;


public class TriangleTest {


    @Test
    public void getAreaPlotsSimple() {
        // simple
        Plot p0 = new Plot(3, 1);
        Plot p1 = new Plot(1, 1);
        Plot p2 = new Plot(2, 2);
        Triangle t = new Triangle(p0, p1, p2);
        PlotSet area = t.getGridContainedPlots();
        assertEquals(4, area.size());
    }
    
    @Test
    public void getAreaPlotsSimpleUpSideDown() {
        // simple updisedown
        Plot p0 = new Plot(3, 3);
        Plot p1 = new Plot(1, 3);
        Plot p2 = new Plot(2, 2);
        Triangle t = new Triangle(p0, p1, p2);
        PlotSet area = t.getGridContainedPlots();
        
        assertEquals(4, area.size());
    }
    
    @Test
    public void getAreaPlotsRandom() {
        // simple updisedown
        Plot p0 = new Plot(1, 1);
        Plot p1 = new Plot(2, 3);
        Plot p2 = new Plot(3, 2);
        Triangle t = new Triangle(p0, p1, p2);
        PlotSet area = t.getGridContainedPlots();
        
        assertEquals(4, area.size());
    }
    
    
    @Test
    public void getAreaPlotsOuterGrid() {
        // outer "grid" triangle
        Plot p0 = new Plot(3.1, 3.1);
        Plot p1 = new Plot(0.8, 3);
        Plot p2 = new Plot(2, 1.7);
        Triangle t = new Triangle(p0, p1, p2);
        PlotSet area = t.getGridContainedPlots();
        
        assertEquals(4, area.size());
    }
    
    @Test
    public void getAreaPlotsInnerGrid() {
        // inner "grid" triangle
        Plot p0 = new Plot(2.9, 3.1);
        Plot p1 = new Plot(1.1, 3.1);
        Plot p2 = new Plot(2, 2.2);
        Triangle t = new Triangle(p0, p1, p2);
        PlotSet area = t.getGridContainedPlots();
        
        assertEquals(1, area.size());
    }
    
    @Test
    public void getAreaPlotsOfRectangleTriangles() {
        // left rectangle triangle
        Plot p0 = new Plot(1, 3.1);
        Plot p1 = new Plot(1, 1);
        Plot p2 = new Plot(2.9, 1);
        Triangle t = new Triangle(p0, p1, p2);
        PlotSet area = t.getGridContainedPlots();
        
        assertEquals(4, area.size());
        
        // righe rectangle triangle
        p0 = new Plot(3, 3.1);        
        p1 = new Plot(3, 1);
        p2 = new Plot(1, 1);
        t = new Triangle(p0, p1, p2);
        area = t.getGridContainedPlots();
        
        assertEquals(6, area.size());

    }
    

    @Test
    public void getAreaPlotsDegenratedTriangles() {
        // flat triangle horizontal
        Plot p0 = new Plot(3, 3);
        Plot p1 = new Plot(1, 3);
        Plot p2 = new Plot(2, 3);
        Triangle t = new Triangle(p0, p1, p2);
        PlotSet area = t.getGridContainedPlots();
        
        assertEquals(3, area.size());   
        
        // flat triangle vertical
        p0 = new Plot(1, 1);
        p1 = new Plot(1, 2);
        p2 = new Plot(1, 3);
        t = new Triangle(p0, p1, p2);
        area = t.getGridContainedPlots();
        
        assertEquals(3, area.size());
        
        // flat triangle vertical not on grid
        p0 = new Plot(1.1, 1);
        p1 = new Plot(1.1, 2);
        p2 = new Plot(1.1, 3);
        t = new Triangle(p0, p1, p2);
        area = t.getGridContainedPlots();
        
        assertEquals(0, area.size());

    }
    
}
