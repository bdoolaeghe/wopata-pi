import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

/**
 * a Set of plots  
 * 
 * @author bdoolaeghe
 * 
 */
public class PlotSet extends HashSet<Plot> {
    
    private static final long serialVersionUID = 1L;
    
    private ConvexHull convexHull;

    /**
     * create a new Plot and add it to the {@link PlotSet}
     * @param x of the plot to add
     * @param y of the plot to add
     */
    public void addPlot(double x, double y) {
        this.add(new Plot(x, y));
    }
    
    /**
     * @return the <a href="http://fr.wikipedia.org/wiki/Enveloppe_convexe">convex hull</a> of all plots of the {@link PlotSet} (that is the smallest convex polygon containing all plots) 
     */
    public ConvexHull getConvexHull() {
        if (this.convexHull == null) {
            this.convexHull  = new ConvexHull(this); 
        }
        return this.convexHull;
    }


    /**
     * a convex hull of a {@link PlotSet}  
     * @author Bruno DOOLAEGHE
     * @see <a href="http://fr.wikipedia.org/wiki/Enveloppe_convexe">convex hull</a>
     */
    public static class ConvexHull extends ArrayList<Plot>  {
        
        private static final long serialVersionUID = 1L;

        public ConvexHull(PlotSet plots) {
            super();
            computeConvexHull(plots);
        }
        
        /**
         * @param plots 
         * @return the built convex hull 
         * @see <a href="http://fr.wikipedia.org/wiki/Parcours_de_Graham"> use Graham algo</a>
         */
        private void computeConvexHull(PlotSet plots) {
            if (plots.size() < 3) {
                // handle degenrated plot set case
                //////////////////////////////////
                addAll(plots);
            } else {
                // else, apply (golden ;-) ) graham algo
                ////////////////////////////////////////
                
                // choose pivot plot
                Plot pivot = getPivot(plots);
                
                // sort plots
                List<Plot> orderedPlots = sortPlots(pivot, plots);
                orderedPlots.add(pivot);
                Stack<Plot> stack = new Stack<Plot>();
                stack.push(orderedPlots.get(0));
                stack.push(orderedPlots.get(1));
                
                for (int i = 2; i < orderedPlots.size(); i++) {
                    Plot candidatePlot = orderedPlots.get(i);
                    Plot underTheTop = stack.get(stack.size() - 2);
                    Plot top = stack.peek();
                    while(stack.size() >= 2 &&
                            vectorialProduct(underTheTop, top, candidatePlot) <= 0) {
                        stack.pop();
                        underTheTop = stack.get(stack.size() - 2);
                        top = stack.peek();
                    }
                    stack.push(candidatePlot);
                }
                
                this.addAll(stack);
            }
        }

        
        /**
         * computes the vectorial product
         * @param p1
         * @param p2
         * @param p3
         * @return vectorial product
         */
        static double vectorialProduct(Plot p1, Plot p2, Plot p3) {
            return (p2.getX() - p1.getX())*(p3.getY() - p1.getY()) - (p3.getX() - p1.getX())*(p2.getY() - p1.getY()); 
        }

        /**
         * order the plots for graham algo
         * @param pivot
         * @param plotSet 
         * @return
         */
        static List<Plot> sortPlots(Plot pivot, PlotSet plotSet) {
            List<Plot> orderedPlots = new ArrayList<Plot>(plotSet); 
            Collections.sort(orderedPlots, new Plot.ByAngleComparator(pivot));
            return orderedPlots;                
        }

        /**
         * get pivot in the graham algo
         * @param plotSet 
         * @return
         */
        static Plot getPivot(PlotSet plotSet) {
            return Collections.min(plotSet, Plot.ByYComparator);
        }

        /**
         * get the area contained in the convex hull 
         * (more precisly, all plots on the grid and into or on the convex hull)
         * 
         * @return the plots of grid in or on the convex hull
         */
        public PlotSet getGridContainedPlots() {
            PlotSet area = new PlotSet();
            
            if (size() == 0) {
                return area;
            } else if (size() < 3) {
                // degenerated hull
                Plot p0 = get(0);
                Plot p1 = (size() > 1) ? get(1) : get(0);
                Plot p2 = (size() > 2) ? get(2) : get(0);
                area.addAll(new Triangle(p0, p1, p2).getGridContainedPlots());
            } else {
                // else, compute the area "by triangles" rotating around a pivot plot
                Plot pivot = this.get(0);
                
                for (int i = 2; i < this.size(); i++) {
                    Plot p1 = get(i - 1);
                    Plot p2 = get(i);
                    
                    // build triangle (P0 P1 P2) and sweet it
                    Triangle t = new Triangle(pivot, p1, p2);
                    area.addAll(t.getGridContainedPlots());
                }
            }
            
            return area;
        }
    }

    @Override
    public String toString() {
        String s = super.toString();
        String[] splits = s.split("\\),");
        Arrays.sort(splits);
        s = ""; 
        for (String split : splits) {
            s = s + split + ")\n";
        }        
        
        return s;
    }
    
};
