/*
 * This is an extremely simple container class
 * that stores a user and a job.
 */
package procmgr_mgohde;

/**
 *
 * @author mgohde
 */
public class UserJob 
{
    private User u;
    private Job j;
    
    public UserJob(User u, Job j)
    {
        this.u=u;
        this.j=j;
    }
    
    public User getUser()
    {
        return u;
    }
    
    public Job getJob()
    {
        return j;
    }
    
    public boolean mayCancel(User u)
    {
        return u.isAdmin()||u.getUsername().equals(this.u.getUsername());
    }
}
