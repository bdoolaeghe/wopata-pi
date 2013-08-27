import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * a composite concurrent impl, browsing concurrently from the top and from the bottom the powerset
 * @author Bruno DOOLAEGHE
 *
 */
public class PowerPlotSetCompositeConcurrentServiceImpl implements IPowerPlotSetService {
    
    private PlotSet allPlots;

    public PowerPlotSetCompositeConcurrentServiceImpl(PlotSet allPlots) {
        super();
        this.allPlots = allPlots;
    }
    
    @Override
    public int maxReachablePlots(final int radius) {
        // create delegates services
        final PowerPlotSetAscServiceImpl powerPlotSetService1 = new PowerPlotSetAscServiceImpl(allPlots);
        final PowerPlotSetDescServiceImpl powerPlotSetService2 = new PowerPlotSetDescServiceImpl(allPlots);
        
        // create parallel workers
        Callable<Integer> worker1 = new Callable<Integer>() {
            
            @Override
            public Integer call() throws Exception {
                int maxReachablePlots = powerPlotSetService1.maxReachablePlots(radius);
                powerPlotSetService2.kill();
                return maxReachablePlots;
            }
        };  
        Callable<Integer> worker2 = new Callable<Integer>() {
            
            @Override
            public Integer call() throws Exception {
                int maxReachablePlots = powerPlotSetService2.maxReachablePlots(radius);
                powerPlotSetService1.kill();
                return maxReachablePlots;
            }
        };  
            
        // run in parallel 2 workers
        ExecutorService workerPool = Executors.newFixedThreadPool(2);
        Future<Integer> futureResult1 = workerPool.submit(worker1);
        Future<Integer> futureResult2 = workerPool.submit(worker2);
        
        // get result from winner worker
        Integer result = null;
        try {
            result = futureResult1.get();
//            System.out.println(powerPlotSetService1.getClass().getSimpleName() + " has found the solution : " + result); 
        } catch (InterruptedException | ExecutionException e) {
            try {
                result = futureResult2.get();
//                System.out.println(powerPlotSetService2.getClass().getSimpleName() + " has found the solution : " + result); 
            } catch (InterruptedException | ExecutionException e1) {
                //should never happen
                throw new RuntimeException("None of the 2 workers ended successfully", e1);
            }
        }
        
        return result.intValue();
    }

}
