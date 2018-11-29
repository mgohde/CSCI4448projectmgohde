/*
 * This class represents a server service that accepts commands locally and keeps
 * track of remote execution nodes.
 */
package procmgr_mgohde;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author mgohde
 */
public class Service 
{
    private ServerSocket ss;
    private ArrayList<ServerInfo> clients;
    private ArrayList<Job> jobs;
    private User admin;
    private int port;
    private int lastAssignedPort;
    
    /**
     * Constructor for the job service, which accepts an administrative user (used for authentication), and a port on which to listen.
     * @param port
     * @param u
     * @throws IOException 
     */
    public Service(int port, User u) throws IOException
    {
        //We'll listen on the port specified and allow for up to 10 connections in the queue:
        this.ss=new ServerSocket(port, 10);
        this.admin=u;
        this.port=port;
        this.lastAssignedPort=port+1;
        
        clients=new ArrayList<ServerInfo>();
        jobs=new ArrayList<Job>();
    }
    
    /**
     * Convenience method to print a given bit of information to a PrintWriter.
     * @param s
     * @param out 
     */
    private void printExit(String s, PrintWriter out)
    {
        out.write(s);
    }
    
    /**
     * Convenience method to print an OK status code.
     * @param out 
     */
    private void printOK(PrintWriter out)
    {
        printExit("OK\n", out);
    }
    
    /**
     * Convenience method used to print an ERR status code.
     * @param out 
     */
    private void printERR(PrintWriter out)
    {
        printExit("ERR\n", out);
    }
    
    /**
     * Sends a job to a worker node.
     * @param nodeName
     * @param j
     * @return 
     */
    private boolean sendNodeCommand(String nodeName, Job j)
    {
        try
        {
            //First determine what node we're supposed to terminate:
            ServerInfo srvr=null;
            
            for(ServerInfo s:clients)
            {
                if(s.getName().equals(nodeName))
                {
                    srvr=s;
                    break;
                }
            }
            
            if(srvr==null)
            {
                System.err.println("Could not find node: "+nodeName);
                return false;
            }
            
            Socket s=new Socket(srvr.getIP(), srvr.getPort());
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            ObjectOutputStream objOut=new ObjectOutputStream(s.getOutputStream());
            objOut.writeObject(j);
            objOut.flush();
            
            //Now get input info:
            String retCode=in.readLine();
            
            objOut.close();
            in.close();
            s.close();
            
            return retCode.equals("OK");
        } catch(UnknownHostException e)
        {
            System.err.println("Unable to connect to node: "+nodeName);
            return false;
        } catch(IOException ex)
        {
            System.err.println("Unable to communicate with node: "+nodeName);
            return false;
        }
    }
    
    /**
     * Listens on the specified port for commands, jobs, and callbacks from both the client and job nodes.
     * @throws IOException 
     */
    public void run() throws IOException
    {
        Socket s;
        boolean keepRunning=true;
        ArrayList<Thread> threadList=new ArrayList<Thread>();
        int nodeIdx=0;
        
        while(keepRunning)
        {
            System.out.println("Waiting for command...");
            s=ss.accept();
            
            
            
            //Since I've spent the last day debugging a project at work, I'll just keep this simple (and in one thread!)
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            //Accept the client's command:
            String command=in.readLine();
            System.out.println("Got command: "+command);
            
            //Cool! The sender is registering itself as a node.
            if(command.equals("node"))
            {
                clients.add(new ServerInfo(s.getInetAddress().getHostAddress(), "node"+clients.size(), this.lastAssignedPort));
                out.println(this.lastAssignedPort);
                this.lastAssignedPort++;
                
                //Respond:
                printOK(out);
            }
            
            else if(command.equals("schedule"))
            {
                try
                {
                    //Accept a job:
                    ObjectInputStream objIn=new ObjectInputStream(s.getInputStream());
                    Job j=(Job) objIn.readObject();
                    
                    //Forward it to a node (using round robin scheduling because this project is somewhat imminently due):
                    if(clients.isEmpty())
                    {
                        System.err.println("No registered nodes to forward job to.");
                        printERR(out);
                    }
                    
                    else
                    {
                        nodeIdx++;
                        nodeIdx=nodeIdx%clients.size();
                    }
                    
                    if(sendNodeCommand(clients.get(nodeIdx).getName(), j))
                    {
                        printOK(out);
                    }
                    
                    else
                    {
                        printERR(out);
                    }
                } catch(ClassNotFoundException e)
                {
                    System.err.println("Job sent was invalid. Discarding...");
                    printERR(out);
                }
            }
            
            else if(command.equals("list"))
            {
                for(ServerInfo i:clients)
                {
                    out.write(i.getName()+"\t"+i.getIP()+"\n");
                }
                
                printOK(out);
            }
            
            else if(command.equals("stopnode"))
            {
                //Determine what node to stop:
                String nodeName=in.readLine();
                User u=new User(in);
                Job j=new Job("stopnode", u);
                
                if(sendNodeCommand(nodeName, j))
                {
                    printOK(out);
                    
                    for(ServerInfo serv:clients)
                    {
                        if(serv.getName().equals(nodeName))
                        {
                            clients.remove(serv);
                            break;
                        }
                    }
                }
                
                else
                {
                    printERR(out);
                }
            }
            
            //This is a node telling us its job is done:
            else if(command.equals("done"))
            {
                String jobName=in.readLine();
                for(Job j:jobs)
                {
                    if(j.getName().equals(jobName))
                    {
                        jobs.remove(j);
                        break;
                    }
                }
                
                printOK(out);
            }
            
            else if(command.equals("listjobs"))
            {
                User u=new User(in);
                for(Job j:jobs)
                {
                    if(j.getOwner().getUsername().equals(u.getUsername()))
                    {
                        out.write(j.getName()+"\n");
                    }
                }
                
                printOK(out);
            }
            
            else if(command.equals("stop"))
            {
                User u=new User(in);
                
                System.out.println(u.getUsername());
                
                if(u.equals(this.admin))
                {
                    keepRunning=false;
                    printOK(out);
                }
                
                else
                {
                    printERR(out);
                }
            }
            
            out.flush();
            out.close();
            in.close();
            s.close();
        }
        
        ss.close();
    }
}
