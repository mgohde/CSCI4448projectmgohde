/*
 * Retrieves a list of jobs from an executor service or node.
 */
package procmgr_mgohde;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author mgohde
 */
public class JobList 
{
    private ArrayList<String> list;
    
    public JobList(String addr, int port, User u)
    {
        list=new ArrayList<String>();
        try
        {
            Socket s=new Socket(addr, port);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            u.write(out, false);
            String line=in.readLine();
            
            while(!line.equals("OK") && !line.equals("ERR"))
            {
                list.add(line);
                line=in.readLine();
            }
        } catch(IOException e)
        {
            System.err.println("Queue service is not running on "+addr+":"+port);
        }
    }
    
    public ArrayList<String> getJobList()
    {
        return list;
    }
}
