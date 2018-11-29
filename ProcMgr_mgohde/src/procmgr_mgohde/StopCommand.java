package procmgr_mgohde;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * The initial plan with this class was to have some interface through which actions could
 * be executed with some symmetry by the client and job service. Unfortunately, the number of different
 * command types (ie. those requiring a different interface to function) proved to make this useless.
 * @author mgohde
 */
public class StopCommand implements ServiceCommand
{
    private int port;
    private String host;
    
    /**
     * Creates a Stop command sender with a given host and port.
     * @param port
     * @param host 
     */
    public StopCommand(int port, String host)
    {
        this.port=port;
        this.host=host;
    }
    
    /**
     * Sends the command payload given a user.
     * @param u
     * @return 
     */
    @Override
    public boolean sendCommand(User u) {
        try
        {
            Socket s=new Socket(host, port);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            out.write("stop\n");
            u.write(out, false);
            
            out.flush(); //Just to be totally sure we sent the command.
            
            String ret=in.readLine();
            
            out.close();
            in.close();
            s.close();
            System.out.println(ret);
            return ret.equals("OK");
            
        } catch(UnknownHostException e)
        {
            return false;
        } catch(IOException ex)
        {
            return false;
        }
    }
    
}
