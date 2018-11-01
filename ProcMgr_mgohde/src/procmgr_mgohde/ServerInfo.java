/*
 * Returns and stores information about a currently running job server.
 */
package procmgr_mgohde;

/**
 *
 * @author mgohde
 */
public class ServerInfo 
{
    private String ip, name;
    
    public String getIP()
    {
        return ip;
    }
    
    public String getName()
    {
        return name;
    }
    
    public ServerInfo(String ip, String name)
    {
        this.ip=ip;
        this.name=name;
    }
}
