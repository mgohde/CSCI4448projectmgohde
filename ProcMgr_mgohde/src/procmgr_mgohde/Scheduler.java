/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
