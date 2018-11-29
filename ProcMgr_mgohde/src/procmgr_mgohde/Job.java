/*
 * Instead of serializing java classes (which presents its own sort of headaches),
 * we're just going to send executable scripts over the network. This should 
 * be more in line with how actual job schedulers like Slurm work.
 * Each file is expected to have some header (like in Slurm) that provides
 * information about how the job is to be executed:
 *
 * #!/bin/bash
 * #name myjob
 * #memusage 10
 * #coreusage 1
 * ls -Alh . >out.txt
 */
package procmgr_mgohde;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

/**
 *
 * @author mgohde
 */
public class Job implements Serializable
{
    //All of these values should be serializable:
    private double memUsage;
    private int coreUsage;
    private User u;
    private String name;
    //This is a terrible idea:
    private byte[] jobFileContents;
    
    /**
     * This constructor exists so that the Service may send commands to Nodes.
     * @param name
     * @param u 
     */
    public Job(String name, User u)
    {
        this.name=name;
        this.u=u;
    }
    
    /**
     * This constructor exists so that executable jobs may be easily created.
     * @param jobFile
     * @param u
     * @throws IOException 
     */
    public Job(File jobFile, User u) throws IOException
    {
        this.u=u;
        
        FileInputStream s=new FileInputStream(jobFile);
        
        //First task: read the job file and store it in jobFileContents:
        //(the conversion from long to int is reasonable because I don't think users
        // are going to send multi-gigabyte scripts to be executed!)
        jobFileContents=new byte[(int) jobFile.length()];
        s.read(jobFileContents);
        s=new FileInputStream(jobFile);
        
        Scanner sc=new Scanner(s);
        
        //Read the file header block:
        String line=sc.nextLine();
        
        while(line.charAt(0)=='#')
        {
            String lineToks[]=line.split(" ");
            
            if(lineToks.length>1)
            {
                if(lineToks[0].equals("#memusage"))
                {
                    this.memUsage=Double.parseDouble(lineToks[1]);
                }
                
                else if(lineToks[0].equals("#coreusage"))
                {
                    this.coreUsage=Integer.parseInt(lineToks[1]);
                }
                
                else if(lineToks[0].equals("#name"))
                {
                    this.name=lineToks[1];
                }
            }
            
            line=sc.nextLine();
        }
    }
    
    /**
     * Runs a given job.
     * @return 
     */
    boolean run()
    {
        try
        {
            File f=File.createTempFile("job", ".sh");
            String fileName=f.getName();
            System.out.println("Filename: "+fileName);
            f.deleteOnExit();
            f=new File(fileName); //Just use the temp file's name.
            f.createNewFile();
            f.setExecutable(true);
            f.deleteOnExit();
            System.out.println("File path: "+f.getAbsolutePath());
            FileOutputStream o=new FileOutputStream(f);
            System.out.println("Length of job file: "+jobFileContents.length);
            o.write(jobFileContents);
            o.close();
            
            //Now run the thing:
            Runtime.getRuntime().exec(f.getAbsolutePath());
            
            return true; //Presumably the process completed.
        } catch(IOException e)
        {
            System.err.println("IO error when writing job file for execution.");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Gets expected memory usage in megabytes.
     * @return 
     */
    double getMemUsageMegaBytes()
    {
        return this.memUsage;
    }
    
    /**
     * Gets expected core usage.
     * @return 
     */
    int getCoreUsage()
    {
        return this.coreUsage;
    }
    
    /**
     * Gets the owner of this job.
     * @return 
     */
    User getOwner()
    {
        return this.u;
    }
    
    /**
     * Gets the job's name.
     * @return 
     */
    String getName()
    {
        return this.name;
    }
}
