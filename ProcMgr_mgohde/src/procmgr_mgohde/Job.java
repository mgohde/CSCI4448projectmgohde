/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procmgr_mgohde;

/**
 *
 * @author mgohde
 */
public interface Job 
{
    abstract boolean run();
    abstract double getMemUsageMegabytes();
    abstract int getCoreUsage();
    abstract User getOwner();
    abstract String getName();
}
