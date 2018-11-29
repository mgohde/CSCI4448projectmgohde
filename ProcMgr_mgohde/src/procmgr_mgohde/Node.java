/*
 * This broadcasts its availability to a job service server and accepts tasks to run.
 * In doing so, it effectively implements an Observer design pattern over the network.
 */
package procmgr_mgohde;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author mgohde
 */
public class Node 
{
    private String jobAddr;
    private int jobPort;
    private int contactPort;
    private User admin;
    private ArrayList<Job> runningJobs;
    private JobPool jp;
    
    /**
     * Constructs a Node object with information related to self-registration with a job service.
     * This is the basis for the Observer model implemented here.
     * @param jobServerAddr
     * @param jobServerPort
     * @param admin 
     */
    public Node(String jobServerAddr, int jobServerPort, User admin)
    {
        this.jobAddr=jobServerAddr;
        this.jobPort=jobServerPort;
        this.admin=admin;
        this.runningJobs=new ArrayList<Job>();
        this.jp=new JobPool(jobServerAddr, jobServerPort);
    }
    
    /**
     * Registers the client with a given job service and stores an assigned communications port.
     * @return 
     */
    private boolean register()
    {
        try
        {
            Socket s=new Socket(this.jobAddr, this.jobPort);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            out.write("node\n");
            out.flush(); //Just in case.
            
            this.contactPort=Integer.parseInt(in.readLine());
            System.out.println("Service assigned port: "+this.contactPort);
            
            String resp=in.readLine();
            
            if(resp.equals("OK"))
            {
                return true;
            }
        } catch(UnknownHostException e)
        {
            System.err.println("Unable to connect to job management server: "+jobAddr);
            
        } catch(IOException ex)
        {
            System.err.println("Error when registering with job management server: "+jobAddr);
        }
        
        System.err.println("Node cannot proceed to accept jobs.");
        
        return false;
    }
    
    /**
     * Accepts commands and jobs for execution, then terminates when requested by a job service.
     * @return 
     */
    public boolean runNode()
    {
        //Attempt to register a connection with the job server:
        if(!register())
        {
            return false;
        }
        
        //Now set up a server socket
        try
        {
            ServerSocket ss=new ServerSocket(this.contactPort, 10);
            
            while(true)
            {
                Socket s=ss.accept();
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                //BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                
                //Read the job information:
                //A job specifies its user:
                ObjectInputStream objIn=new ObjectInputStream(s.getInputStream());
                Job j=(Job) objIn.readObject();
                
                //Now determine if this is a special command job (yes, this is an awful way to encode
                //job service commands):
                if(j.getName().equals("stopnode"))
                {
                    if(j.getOwner().equals(admin))
                    {
                        out.println("OK");
                        out.close();
                        objIn.close();
                        System.err.println("Waiting for jobs to terminate...");
                        jp.waitAll();
                        //Todo: add some logic to purge the running job list.
                        break;
                    }
                    
                    else
                    {
                        out.println("ERR");
                    }
                }
                
                else
                {
                    jp.acceptJob(j);
                }
                
                out.println("OK");
                objIn.close();
                out.close();
                s.close();
            }
            
            return true;
        } catch(IOException e)
        {
            System.err.println("Communications error. Exiting...");
            return false;
        } catch(ClassNotFoundException ex)
        {
            System.err.println("Service attempted to send invalid object. Exiting...");
            return false;
        }
    }
}
