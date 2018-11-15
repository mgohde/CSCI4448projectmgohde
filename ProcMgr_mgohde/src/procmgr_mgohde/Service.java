/*
 * This class represents a server service that accepts commands locally and keeps
 * track of remote execution nodes.
 */
package procmgr_mgohde;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
    
    public Service(int port, User u) throws IOException
    {
        //We'll listen on the port specified and allow for up to 10 connections in the queue:
        this.ss=new ServerSocket(port, 10);
        this.admin=u;
    }
    
    private void printExit(String s, PrintWriter out)
    {
        out.write(s);
    }
    
    private void printOK(PrintWriter out)
    {
        printExit("OK\n", out);
    }
    
    private void printERR(PrintWriter out)
    {
        printExit("ERR\n", out);
    }
    
    public void run() throws IOException
    {
        Socket s;
        boolean keepRunning=true;
        ArrayList<Thread> threadList=new ArrayList<Thread>();
        
        while(keepRunning)
        {
            System.out.println("Waiting for command...");
            s=ss.accept();
            
            System.out.println("Got command!");
            
            //Since I've spent the last day debugging a project at work, I'll just keep this simple (and in one thread!)
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            //Accept the client's command:
            String command=in.readLine();
            
            //Cool! The sender is registering itself as a node.
            if(command.equals("node"))
            {
                clients.add(new ServerInfo(s.getInetAddress().getHostAddress(), "node"+clients.size()));
                
                //Respond:
                printOK(out);
            }
            
            else if(command.equals("list"))
            {
                for(int i=0;i<clients.size();i++)
                {
                    out.write(clients.get(i).getName()+"\n");
                }
                
                printOK(out);
            }
            
            //This is a node telling us its job is done:
            else if(command.equals("done"))
            {
                String jobName=in.readLine();
                for(int i=0;i<jobs.size();i++)
                {
                    
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
    }
}
