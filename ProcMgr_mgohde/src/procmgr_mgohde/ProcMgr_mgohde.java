/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procmgr_mgohde;

import java.io.IOException;

/**
 *
 * @author mgohde
 */
public class ProcMgr_mgohde 
{

    /**
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
                break;
            case "register":
                break;
            case "list":
                //Terrible asumption: the queue service is local:
                JobList j=new JobList("localhost", 9000, u);
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
                
                break;
            case "listservers":
                break;
            case "offline":
                break;
            case "stopservice":
                StopCommand stop=new StopCommand(a.servicePort, "localhost");
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
