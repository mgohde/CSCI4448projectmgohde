/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procmgr_mgohde;

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
                
            case "register":
                break;
            case "list":
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
            case "service":
                //Special case: the user automatically becomes the admin of the server service:
                u=new User(a.username, a.password, true);
                break;
            default:
                a.printUsage();
        }
    }
    
}
