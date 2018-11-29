/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procmgr_mgohde;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author mgohde
 */
public class ProcMgr_mgohde 
{
    /**
     * Convenience method that sends a job to a given job service.
     * @param jobFile
     * @param ip
     * @param port
     * @param u 
     */
    private static void sendJob(File jobFile, String ip, int port, User u)
    {
        try
        {
            Job j=new Job(jobFile, u);
            Socket s=new Socket(ip, port);
            BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw=new PrintWriter(s.getOutputStream(), true);
            pw.println("schedule");
            ObjectOutputStream objOut=new ObjectOutputStream(s.getOutputStream());
            
            objOut.writeObject(j);
            objOut.flush();
            
            if(in.readLine().equals("OK"))
            {
                System.out.println("Job submitted.");
            }
            
            else
            {
                System.err.println("Job submission failure. Check service logs for more information.");
            }
            
            pw.close();
            objOut.close();
            in.close();
            s.close();
        } catch(UnknownHostException e)
        {
            System.out.println("Could not contact host to submit job.");
        } catch(IOException ex)
        {
            System.err.println("Job file specified is invalid.");
        }
    }
    
    /**
     * Convenience method that requests that the job service kill a worker node.
     * @param ip
     * @param port
     * @param u
     * @param nodeName 
     */
    private static void stopNode(String ip, int port, User u, String nodeName)
    {
        try
        {
            Socket s=new Socket(ip, port);
            BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw=new PrintWriter(s.getOutputStream(), true);
            pw.println("stopnode");
            pw.println(nodeName);
            u.write(pw, false);
            
            pw.flush();
            
            if(in.readLine().equals("OK"))
            {
                System.out.println("Job submitted.");
            }
            
            else
            {
                System.err.println("Job submission failure. Check service logs for more information.");
            }
            
            pw.close();
            in.close();
            s.close();
        } catch(UnknownHostException e)
        {
            System.out.println("Could not contact host to submit job.");
        } catch(IOException ex)
        {
            System.err.println("Job file specified is invalid.");
        }
    }

    /**
     * Main method for the program.
     * Parses command line arguments and furthers 
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        ArgHandler a=new ArgHandler(args);
        
        if(a.invalid)
        {
            a.printUsage();
            return;
        }
        
        //It's presently unknown whether this user is admin.
        User u=new User(a.username, a.password, false);
        
        //SERIOUS TODO: represent commands and actions as objects of various classes that get instantiated and run here.
        //Maybe action handlers could be good.
        switch(a.command)
        {
            case "schedule":
                File jobFile=new File(a.arg);
                System.out.println(a.arg);
                sendJob(jobFile, a.ip, a.servicePort, u);
                break;
            case "register":
                break;
            case "list":
                //Terrible asumption: the queue service is local:
                JobList j=new JobList(a.ip, a.servicePort, u, false);
                System.out.println("User job list:");
                for(String s:j.getJobList())
                {
                    System.out.println(s);
                }
                
                break;
            case "cancel":
                break;
            case "node":
                //Special case: the user automatically becomes the admin of the node:
                u=new User(a.username, a.password, true);
                Node n=new Node(a.ip, a.servicePort, u);
                n.runNode();
                break;
            case "listservers":
                //Terrible asumption: the queue service is local:
                JobList jl=new JobList(a.ip, a.servicePort, u, true);
                System.out.println("Running Node List:");
                for(String s:jl.getJobList())
                {
                    System.out.println(s);
                }
                
                break;
            case "offline":
                break;
            case "stopnode":
                stopNode(a.ip, a.servicePort, u, a.arg);
                break;
            case "stopservice":
                StopCommand stop=new StopCommand(a.servicePort, a.ip);
                if(stop.sendCommand(new User(a.username, a.password, true)))
                {
                    System.out.println("Instructed local service to stop.");
                }
                
                else
                {
                    System.out.println("Unable to stop local service. Is it running?");
                }
                break;
            case "service":
                //Special case: the user automatically becomes the admin of the server service:
                u=new User(a.username, a.password, true);
                try
                {
                    System.out.println("Starting service...");
                    Service s=new Service(a.servicePort, u);
                    s.run();
                } catch(IOException e)
                {
                    System.out.println("Error: running service failed.");
                }
                break;
            default:
                a.printUsage();
        }
    }
    
}
