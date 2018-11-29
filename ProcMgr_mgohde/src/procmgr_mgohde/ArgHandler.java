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
    public String ip;
    
    /**
     * Prints a usage statement for the program.
     */
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
        System.out.println("\tstopservice\tStops the running job service.");
        System.out.println("\tstopnode\tTakes a job server/node offline.");
        System.out.println();
        System.out.println("Optional arguments:");
        System.out.println("\t-u [username]\tSpecifies username.");
        System.out.println("\t-p [password]\tSpecifies password.");
        System.out.println("\t-port [port]\tSpecifies port to use for service communications.");
        System.out.println("\t-ip [address]\tSpecifies an address at which to contact the job service");
    }
    
    /**
     * Maps arguments passed from the command line to internal representations.
     * @param args 
     */
    public ArgHandler(String args[]) 
    {
        boolean u=false;
        boolean p=false;
        boolean port=false;
        boolean ip=false;
        
        this.servicePort=9000;
        this.ip="localhost";
        
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
                
                else if(args[i].equals("-port"))
                {
                    port=true;
                }
                
                else if(args[i].equals("-ip"))
                {
                    ip=true;
                }
                
                else if(port)
                {
                    this.servicePort=Integer.parseInt(args[i]);
                    port=false;
                }
                
                else if(ip)
                {
                    this.ip=args[i];
                    ip=false;
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
