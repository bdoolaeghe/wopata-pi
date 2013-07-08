import static org.junit.Assert.*;

import org.junit.Test;

public class SegmentTest {

    @Test
    public void testLineEquation() {
        // affine
        Segment s = new Segment(new Plot(-2,1), new Plot(3,-1));
        
        assertEquals(1, s.getY(-2), 0.001);
        assertEquals(-2, s.getX(1), 0.001);

        // verticale
        s = new Segment(new Plot(-2,1), new Plot(-2,2));
        try {
            assertEquals(-2, s.getX(4), 0);
            s.getY(2);
            fail("not possible computation");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }

        // horizontale
        s = new Segment(new Plot(-2,1), new Plot(-3,1));
        try {
            assertEquals(1, s.getY(3), 0);
            
            s.getX(2);
            fail("not possible computation");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
        
    }
    
    @Test
    public void getContainedPlotsOfGrid() {
        // line
        PlotSet plots = new VerticalBrush(1, 3.7d, 0.2d).getContainedPlotsOfGrid();
        assertEquals(3, plots.size());
        for (Plot p : plots) {
            assertEquals(1, p.getX(), 0.001);
            assertTrue(p.getY() >= 1);
            assertTrue(p.getY() <= 3);
        }
    }

}
