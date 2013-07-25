
/**
 * 
 * A Best site to find the hotel
 * 
 * @author bdoolaeghe
 */
class BestSite {
    
    private Plot location;
    
    private int reachablePointsOfInterest = 0;

    public BestSite(Plot location, int reachablePointsOfInterest) {
        super();
        this.location = location;
        this.reachablePointsOfInterest = reachablePointsOfInterest;
    }


    public int getReachablePointsOfInterest() {
        return reachablePointsOfInterest;
    }


    public Plot getLocation() {
        return location;
    }

}
