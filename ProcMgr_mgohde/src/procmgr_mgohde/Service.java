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
    
    public Service(int port) throws IOException
    {
        //We'll listen on the port specified and allow for up to 10 connections in the queue:
        this.ss=new ServerSocket(port, 10);
    }
    
    public void run() throws IOException
    {
        Socket s;
        boolean keepRunning=true;
        ArrayList<Thread> threadList=new ArrayList<Thread>();
        
        while(keepRunning)
        {
            s=ss.accept();
            
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
                out.write("ok\n");
            }
            
            else if(command.equals("list"))
            {
                for(int i=0;i<clients.size();i++)
                {
                    out.write(clients.get(i).getName()+"\n");
                }
                out.write("OK\n");
            }
            
            //This is a node telling us its job is done:
            else if(command.equals("done"))
            {
                String jobName=in.readLine();
                for(int i=0;i<jobs.size();i++)
                {
                    
                }
                
                out.write("OK");
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
                
                out.write("OK");
            }
            
            out.flush();
            out.close();
            in.close();
            s.close();
        }
    }
}
