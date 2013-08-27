

/**
 * a power set tool kit
 * @author Bruno DOOLAEGHE
 * @see <a href="http://en.wikipedia.org/wiki/Power_set">power set</a>
 */
public interface IPowerPlotSetService {

    /**
     * give the max number or plots (points of interest) you can rope in a cicle
     * 
     * @param radius of the circle
     * @return the max number of plots we can rope in a circle
     * @throws KilledException 
     */
    public abstract int maxReachablePlots(int radius) throws KilledException;

}