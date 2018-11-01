/*
 * This is a classloader-like hackjob intended to transmit and recieve classes
 * over a network.
 */
package procmgr_mgohde;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author mgohde
 */
public class JobLoader 
{
    public JobLoader()
    {
        
    }
    
    boolean transmit(PrintWriter pw, OutputStream o, String path) throws IOException
    {
        byte[] fileBytes=Files.readAllBytes(Paths.get(path));
        Charset c=Charset.forName("UTF-8");
        pw.println(path);
        pw.println(fileBytes.length);
        o.write(fileBytes);
        return true;
    }
    
    String recieve(InputStreamReader i)
    {
        String fileName;
        int fileLength;
        byte fileBytes[];
        
        BufferedReader r=new BufferedReader(i);
        
        return "";
    }
}
