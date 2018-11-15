/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procmgr_mgohde;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author mgohde
 */
public class StopCommand implements ServiceCommand
{
    private int port;
    private String host;
    
    public StopCommand(int port, String host)
    {
        this.port=port;
        this.host=host;
    }
    
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
