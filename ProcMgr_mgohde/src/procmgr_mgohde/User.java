/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procmgr_mgohde;
import java.util.Scanner;
import java.io.*;

/**
 *
 * @author mgohde
 */
public class User implements Serializable
{
    private String userName;
    private String password;
    private boolean admin;
    
    /**
     * Generates a user given parameters.
     * @param userName
     * @param password
     * @param admin 
     */
    public User(String userName, String password, boolean admin)
    {
        this.userName=userName;
        this.password=password;
        this.admin=admin;
    }
    
    /**
     * Generates an empty user with no username, password, or admin status.
     */
    public User()
    {
        userName="";
        password="";
        admin=false;
    }
    
    /**
     * Generates a user from a file (currently unused).
     * @param userFile
     * @throws FileNotFoundException 
     */
    public User(String userFile) throws FileNotFoundException
    {
        Scanner s=new Scanner(new File(userFile));
        
        this.userName=s.nextLine();
        this.password=s.nextLine();
        this.admin=s.nextLine().equals("true");
    }
    
    /**
     * Reads a user from a BufferedReader.
     * This is often used in network communications as it removes the need to
     * utilize ObjectStreamWriter/Readers in the majority of circumstances.
     * @param in
     * @throws IOException 
     */
    public User(BufferedReader in) throws IOException
    {
        this.userName=in.readLine();
        this.password=in.readLine();
    }
    
    /**
     * Gets the user's name.
     * @return 
     */
    public String getUsername()
    {
        return userName;
    }
    
    /**
     * Checks whether a password is correct.
     * @param password
     * @return 
     */
    public boolean checkPassword(String password)
    {
        return this.password.equals(password);
    }
    
    /**
     * Returns whether the user is admin or not (currently useless).
     * @return 
     */
    public boolean isAdmin()
    {
        return admin;
    }
    
    /**
     * Determines if a given username and password matches that of another user.
     * @param u
     * @return 
     */
    public boolean equals(User u)
    {
        return this.password.equals(u.password) && this.userName.equals(u.userName);
    }
    
    /**
     * Saves a user to disk. Presently not used.
     * @param userDir
     * @throws FileNotFoundException 
     */
    public void save(String userDir) throws FileNotFoundException
    {
        PrintWriter pw=new PrintWriter(userDir+"/"+this.userName);
        
        write(pw, false);
        pw.flush();
        pw.close();
    }
    
    /**
     * Writes user information to a PrintWriter, which is used to simplify a number of network operations.
     * @param pw
     * @param writeAdmin 
     */
    public void write(PrintWriter pw, boolean writeAdmin)
    {
        pw.println(this.userName);
        pw.println(this.password);
        
        if(writeAdmin)
        {
            pw.println(this.admin);
        }
    }
}
