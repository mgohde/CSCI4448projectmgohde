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
    public boolean sendCommand(User u);
}
