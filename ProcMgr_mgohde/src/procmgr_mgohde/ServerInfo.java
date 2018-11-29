/*
 * Returns and stores information about a currently running job server.
 */
package procmgr_mgohde;

/**
 * This class encapsulates information related to a running job execution node.
 * It's used by the Service class in order to 
 * @author mgohde
 */
public class ServerInfo 
{
    private String ip, name;
    private int port;
    
    /**
     * Returns the IP address of the job node.
     * @return 
     */
    public String getIP()
    {
        return ip;
    }
    
    /**
     * Returns the port through which to contact the job node.
     * Ports are automatically assigned to execution nodes so as to allow multiple
     * such nodes to run on the same machine if desired.
     * @return 
     */
    public int getPort()
    {
        return port;
    }
    
    /**
     * Returns the designated human-readable name of the job node.
     * @return 
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Constructor which accepts an IP address, a human-readable node name, and a port through which to contact the node.
     * @param ip
     * @param name
     * @param port 
     */
    public ServerInfo(String ip, String name, int port)
    {
        this.ip=ip;
        this.name=name;
        this.port=port;
    }
}
