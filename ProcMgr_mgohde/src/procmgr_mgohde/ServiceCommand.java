/*
 * LONG SHOT GOAL: make this interface symmetric: ie. instances of ServiceCommands
 * in both the client and service can be used to carry out the commands specified.
 */
package procmgr_mgohde;

/**
 *
 * @author mgohde
 */
public interface ServiceCommand 
{   
    /**
     * Sends a command (presumably to a server) with the specified user.
     * @param u
     * @return 
     */
    public boolean sendCommand(User u);
}
