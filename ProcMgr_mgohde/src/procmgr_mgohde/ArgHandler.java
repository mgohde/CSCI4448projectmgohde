/*
 * This is a simple container class that parses command line arguments.
 */
package procmgr_mgohde;

/**
 *
 * @author mgohde
 */
public class ArgHandler {
    public boolean invalid;
    public String command;
    public String arg;
    public String username;
    public String password;
    public int servicePort;
    
    public void printUsage()
    {
        System.out.println("ProcMgr CLI Usage: ");
        System.out.println("ProcMgr [action] <job file, name, or id OR server ID if admin> [optional arguments]");
        System.out.println("Where action is one of the following:");
        System.out.println("\tschedule\tSchedules a job to run.");
        System.out.println("\tregister\tRegisters a new user.");
        System.out.println("\tlist\tLists running jobs.");
        System.out.println("\tcancel\tCancels a job.");
        System.out.println("\tnode\tRuns ProcMgr as a server node.");
        System.out.println("\tservice\tRuns ProcMgr service.");
        System.out.println();
        System.out.println("If you are an administrator, you may also the following:");
        System.out.println("\tlistservers\tLists running job servers.");
        System.out.println("\toffline\tTakes a server offline.");
        System.out.println();
        System.out.println("Optional arguments:");
        System.out.println("\t-u\tSpecifies username.");
        System.out.println("\t-p\tSpecifies password.");
    }
    
    public ArgHandler(String args[]) 
    {
        boolean u=false;
        boolean p=false;
        
        this.servicePort=9000;
        
        if(args.length>0)
        {
            this.command=args[0];
            
            for(int i=1;i<args.length;i++)
            {
                if(args[i].equals("-u"))
                {
                    u=true;
                }
                
                else if(args[i].equals("-p"))
                {
                    p=true;
                }
                
                else if(u)
                {
                    this.username=args[i];
                    u=false;
                }
                
                else if(p)
                {
                    this.password=args[i];
                    p=false;
                }
                
                else
                {
                    this.arg=args[i];
                }
            }
            
            invalid=false;
        }
        
        else
        {
            invalid=true;
        }
    }
}
