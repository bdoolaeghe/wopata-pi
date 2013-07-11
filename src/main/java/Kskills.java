
/**
 * Wopata java puzzle :-)
 * 
 * @author bdoolaeghe
 */
class kskills {

    static IoService ioService = new IoService();
    
    static MapService mapService = new MapService();
    
    public static void main(String[] args) throws java.lang.Exception {
        // read and parse input
        InputBean inputBean = ioService.parseInput();

        // compute the best place for holiday
        BestSite bestSite = mapService.findBestPlaceForHolidays(inputBean.getPiSet(), inputBean.getK());

        // ouput the best number of reachable PI
        System.out.println(bestSite.getReachablePointsOfInterest());
    }



}