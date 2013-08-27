
public class Killable {

    private boolean isKilled = false;

    
    /**
     * stop the exeuction
     */
    public void kill() {
        this.isKilled = true;
    }
    
    public void checkIsNotKilled() throws KilledException {
        if (isKilled)
            throw new KilledException();
    }
    

}
