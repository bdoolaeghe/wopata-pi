import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;




public class ConvexHullTest {

    @Test
    public void getPivot() {
        // case1
        Plot p0 = new Plot(3, 1);
        Plot p1 = new Plot(1, 1);
        Plot p2 = new Plot(2, 2);
        
        PlotSet plots = new PlotSet();
        plots.add(p0);
        plots.add(p1);
        plots.add(p2);
        
        assertEquals(p1, PlotSet.ConvexHull.getPivot(plots));
        
        // case2
        p0 = new Plot(3, 2);
        p1 = new Plot(1, 1);
        p2 = new Plot(2, 2);
        Plot p3 = new Plot(3, 4);
        
        plots = new PlotSet();
        plots.add(p0);
        plots.add(p1);
        plots.add(p2);
        plots.add(p3);
        
        assertEquals(p1, PlotSet.ConvexHull.getPivot(plots));
        
    }

    @Test
    public void sortPlots() {
        Plot p0 = new Plot(3, 2);
        Plot p1 = new Plot(1, 1);
        Plot p2 = new Plot(-2, 3);
        Plot p3 = new Plot(3, 4);
        
        PlotSet plots = new PlotSet();
        plots.add(p0);
        plots.add(p1);
        plots.add(p2);
        plots.add(p3);

        Plot pivot = p1;
        
        List<Plot> sortPlots = PlotSet.ConvexHull.sortPlots(pivot, plots);
        
        assertEquals(p1, sortPlots.get(0));
        assertEquals(p0, sortPlots.get(1));
        assertEquals(p3, sortPlots.get(2));
        assertEquals(p2, sortPlots.get(3));
    }

    @Test
    public void computeConvexHull() {
        Plot p0 = new Plot(3, 2);
        Plot p1 = new Plot(1, 1);
        Plot p2 = new Plot(2, 3);
        Plot p3 = new Plot(3, 4);
        Plot p4 = new Plot(2, 2);
        Plot p5 = new Plot(3, 3);
        
        PlotSet plots = new PlotSet();
        plots.add(p0);
        plots.add(p1);
        plots.add(p2);
        plots.add(p3);
        plots.add(p4);
        plots.add(p5);

        PlotSet.ConvexHull convexHull = plots.getConvexHull();
        
        assertEquals(p1, convexHull.get(0));
        assertEquals(p0, convexHull.get(1));
        assertEquals(p3, convexHull.get(2));
        assertEquals(p2, convexHull.get(3));
        assertEquals(p1, convexHull.get(4));
    }

    @Test
    public void getContainedAreaGridForEmptyPlotSet() {
        PlotSet plots = new PlotSet();
        
        PlotSet.ConvexHull convexHull = plots.getConvexHull();
        PlotSet containedAreaGrid = convexHull.getGridContainedPlots();
        
        assertTrue(containedAreaGrid.isEmpty());
    }
        
    @Test
    public void getContainedAreaGridFor1Plot() {
        Plot p0 = new Plot(3, 3);
        PlotSet plots = new PlotSet();
        plots.add(p0);
        
        PlotSet.ConvexHull convexHull = plots.getConvexHull();
        PlotSet containedAreaGrid = convexHull.getGridContainedPlots();
        
        PlotSet expectedArea = new PlotSet();
        expectedArea.add(p0);
        assertEquals(expectedArea, containedAreaGrid);
    }
    
    @Test
    public void getContainedAreaGridFor2Plots() {
        Plot p0 = new Plot(3, 3);
        Plot p1 = new Plot(1, 1);
        PlotSet plots = new PlotSet();
        plots.add(p0);
        plots.add(p1);
        
        PlotSet.ConvexHull convexHull = plots.getConvexHull();
        PlotSet containedAreaGrid = convexHull.getGridContainedPlots();

        PlotSet expectedArea = new PlotSet();
        expectedArea.add(p0);
        expectedArea.add(p1);
        expectedArea.add(new Plot(2,2));
        assertEquals(expectedArea, containedAreaGrid);
    }
    
    @Test
    public void getContainedAreaGridFor3Plots() {
        Plot p0 = new Plot(2, 3);
        Plot p1 = new Plot(1, 1);
        Plot p2 = new Plot(0, 2);
        PlotSet plots = new PlotSet();
        plots.add(p0);
        plots.add(p1);
        plots.add(p2);
        
        PlotSet.ConvexHull convexHull = plots.getConvexHull();
        PlotSet containedAreaGrid = convexHull.getGridContainedPlots();
        
        PlotSet expectedArea = new PlotSet();
        expectedArea.add(p0);
        expectedArea.add(p1);
        expectedArea.add(p2);
        expectedArea.add(new Plot(1,2));
        assertEquals(expectedArea, containedAreaGrid);
    }

    @Test
    public void getContainedAreaGridOnlyOneSolution() {
        Plot p0 = new Plot(0, 0);
        Plot p1 = new Plot(2, 4);
        Plot p2 = new Plot(7, 0);
        Plot p3 = new Plot(7, 5);
        
        PlotSet plots = new PlotSet();
        plots.add(p0);
        plots.add(p1);
        plots.add(p2);
        plots.add(p3);

        PlotSet.ConvexHull convexHull = plots.getConvexHull();
        PlotSet containedAreaGrid = convexHull.getGridContainedPlots();

        // build the expected area
        PlotSet expectedArea = new PlotSet();
        expectedArea.add(new Plot(0,0));
        for (int i = 1; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                expectedArea.add(new Plot(i,j));        
            }
        }
        for (int i = 2; i < 8; i++) {
            for (int j = 3; j < 5; j++) {
                expectedArea.add(new Plot(i,j));        
            }
        }
        expectedArea.add(new Plot(7,5));
        
        assertEquals(expectedArea.toString(), containedAreaGrid.toString());
    }
    

    
    @Test
    public void getContainedAreaGrid() {
        Plot p0 = new Plot(3, 2);
        Plot p1 = new Plot(1, 1);
        Plot p2 = new Plot(1, 3);
        Plot p3 = new Plot(3, 4);
        Plot p4 = new Plot(2, 2);
        Plot p5 = new Plot(3, 3);
        
        PlotSet plots = new PlotSet();
        plots.add(p0);
        plots.add(p1);
        plots.add(p2);
        plots.add(p3);
        plots.add(p4);
        plots.add(p5);

        PlotSet.ConvexHull convexHull = plots.getConvexHull();
        PlotSet containedAreaGrid = convexHull.getGridContainedPlots();

        PlotSet expectedArea = new PlotSet();
        expectedArea.add(p0);
        expectedArea.add(p1);
        expectedArea.add(p2);
        expectedArea.add(p3);
        expectedArea.add(p4);
        expectedArea.add(p5);
        expectedArea.add(new Plot(1,2));
        expectedArea.add(new Plot(2,3));
        assertEquals(expectedArea, containedAreaGrid);
    }
    
    
}
