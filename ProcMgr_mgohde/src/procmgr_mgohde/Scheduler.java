/*
 * Unfortunately, this was never used.
 * The long shot goal would be to actually have a reasonable scheduling algorithm which could take into 
 * account available resources and present utilization. Instead, the job service just uses round robin scheduling, and
 * the node/executor service simply spawns a new thread to handle each job.
 */
package procmgr_mgohde;
import java.util.ArrayList;
import java.util.Timer;
/**
 *
 * @author mgohde
 */
public class Scheduler 
{
    private int maxThreads;
    private double maxMegabytes;
    private ArrayList<Thread> threadPool;
    private Timer t;
    
    public Scheduler(int nThreads, double maxMegabytes)
    {
        this.threadPool=new ArrayList<Thread>();
        this.maxThreads=nThreads;
        this.maxMegabytes=maxMegabytes;
    }
    
    public void schedule(User u, Job j)
    {
        
    }
    
    private boolean cancelJob(User caller, Job j)
    {
        return false;
    }
    
    private void checkAndExecuteNextCallback()
    {
        
    }
}
