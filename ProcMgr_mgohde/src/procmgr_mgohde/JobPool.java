/*
 * This represents a pool of running jobs.
 */
package procmgr_mgohde;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author mgohde
 */
public class JobPool 
{
    private ArrayList<Thread> threads;
    private String callbackServer;
    private int callbackPort;
    
    /**
     * This method is used to perform cleanup operations when a given job terminates.
     * It further reinforces the observer model by instructing the running job service to
     * remove a given job from its running jobs list.
     * @param threadId
     * @param jobName 
     */
    private void jobPoolCallback(Thread threadId, String jobName)
    {
        //On job termination, this callback searches through the thread list and prunes stopped entries.
        threads.remove(threadId);
        
        //We finished our job, so we need to inform the server of such:
        try
        {
            Socket s=new Socket(this.callbackServer, this.callbackPort);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            out.write("done\n");
            out.write(jobName+"\n");
            
            out.flush(); //Just to be totally sure we sent the command.
            
            String ret=in.readLine();
            
            out.close();
            in.close();
            s.close();
        } catch(IOException e)
        {
            System.err.println("Job removal callback failed for job "+jobName+" and server "+this.callbackServer);
        }
    }
    
    /**
     * Builds a JobPool object with information related to job service callbacks.
     * @param callbackServer
     * @param callbackPort 
     */
    public JobPool(String callbackServer, int callbackPort)
    {
        threads=new ArrayList<Thread>();
        this.callbackPort=callbackPort;
        this.callbackServer=callbackServer;
    }
    
    /**
     * Waits for all running jobs to terminate.
     */
    public void waitAll()
    {
        for(Thread t:threads)
        {
            try
            {
                t.wait();
            } catch(InterruptedException e)
            {
                System.err.println("Interrupted...");
            }
        }
    }
    
    /**
     * Spawns a worker thread to execute a given job.
     * @param j 
     */
    public void acceptJob(Job j)
    {
        Thread t=new Thread()
        {
            @Override
            public void run()
            {
                if(j.run())
                {
                    System.err.println("Job "+j.getName()+" completed successfully.");
                }
                
                else
                {
                    System.err.println("Job "+j.getName()+" failed.");
                }
                
                jobPoolCallback(this, j.getName());
            }
        };
        
        threads.add(t);
        t.start();
    }
}
