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
public class User 
{
    private String userName;
    private String password;
    private boolean admin;
    
    public User(String userName, String password, boolean admin)
    {
        this.userName=userName;
        this.password=password;
        this.admin=admin;
    }
    
    public User()
    {
        userName="";
        password="";
        admin=false;
    }
    
    public User(String userFile) throws FileNotFoundException
    {
        Scanner s=new Scanner(new File(userFile));
        
        this.userName=s.nextLine();
        this.password=s.nextLine();
        this.admin=s.nextLine().equals("true");
    }
    
    public User(BufferedReader in) throws IOException
    {
        this.userName=in.readLine();
        this.password=in.readLine();
    }
    
    public String getUsername()
    {
        return userName;
    }
    
    public boolean checkPassword(String password)
    {
        return this.password.equals(password);
    }
    
    public boolean isAdmin()
    {
        return admin;
    }
    
    public boolean equals(User u)
    {
        return this.password.equals(u.password) && this.userName.equals(u.userName);
    }
    
    public void save(String userDir) throws FileNotFoundException
    {
        PrintWriter pw=new PrintWriter(userDir+"/"+this.userName);
        
        write(pw, false);
        pw.flush();
        pw.close();
    }
    
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
