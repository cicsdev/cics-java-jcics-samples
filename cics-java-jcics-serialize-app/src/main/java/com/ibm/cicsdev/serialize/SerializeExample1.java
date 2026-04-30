/* Licensed Materials - Property of IBM                                   */
/*                                                                        */
/* SAMPLE                                                                 */
/*                                                                        */
/* (c) Copyright IBM Corp. 2017 All Rights Reserved                       */
/*                                                                        */
/* US Government Users Restricted Rights - Use, duplication or disclosure */
/* restricted by GSA ADP Schedule Contract with IBM Corp                  */
/*                                                                        */

package com.ibm.cicsdev.serialize;

import java.util.concurrent.ThreadLocalRandom;

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.LengthErrorException;
import com.ibm.cics.server.NameResource;
import com.ibm.cics.server.ResourceUnavailableException;
import com.ibm.cics.server.Task;

/**
 * Simple example of resource serialization using the CICS enqueue and dequeue
 * mechanism from the JCICS API. 
 */
public class SerializeExample1
{
    /**
     * The name of the lock used to protect the shared resource.
     * 
     * The name is defined by an application and has no special meaning
     * to CICS.
     * 
     * This string must be a maximum of 255 bytes after conversion from
     * a Java String to a byte array in the local CCSID.
     * 
     * This string must be a minimum of 1 character in length.
     */
    private static final String RESOURCE_LOCK_NAME = "MYAPP.SYNC.RES1";
    
    /**
     * A constant to represent the number of times we will attempt to
     * lock the common application resource before giving up.
     */
    private static final int RESOURCE_TRY_COUNT = 10;
    
    /**
     * Main entry point to a CICS OSGi program.
     * 
     * The FQ name of this class should be added to the CICS-MainClass entry in
     * the parent OSGi bundle's manifest.
     */    
    public static void main(String[] args) 
    {
        // Flag to indicate we have the lock
        boolean bHaveLock = false;
        
        // Get details about our current CICS task
        Task task = Task.getTask();
        task.out.println(" - Starting SerializeExample1");

        // Create a named synchronization resource  
        NameResource lock = new NameResource();
        lock.setName(RESOURCE_LOCK_NAME);
        
        // Attempt to access the lock multiple times
        for ( int i = 0; i < RESOURCE_TRY_COUNT; i++ ) {

            // We are trying to acquire the lock
            String strMsg = "Attempting to acquire resource lock %s";            
            task.out.println( String.format(strMsg, RESOURCE_LOCK_NAME) );
       
            // Try to acquire the lock
            bHaveLock = acquireLock(lock);

            // Did we get the lock?
            //   No  : Wait for a random period of time before retry
            //   Yes : Exit loop
            if ( ! bHaveLock ) { 
                randomSleep();
            }
            else {
                break;
            }
        }
       
        // Verify we have the lock. If not, perform some crude error-handling
        if ( ! bHaveLock ) {
            Task.getTask().abend("LOCK", false);
        }

        // Now have the lock at this point - confirm to the user            
        task.out.println("Resource lock has been acquired");
        
        try {
            // Perform update of some shared resource
            doUpdate();
        }
        catch (CicsConditionException cce) {
            // No clever exception handling here - keep it simple for demo purposes
            throw new RuntimeException(cce);
        }
        finally {
            try {
                // Release the lock inside the finally block for correct error-handling 
                lock.dequeue();
            
                // Confirmation we have released the lock
                task.out.println("Resource lock has been released");
            }
            catch (CicsConditionException cce) {
                // No clever exception handling here - keep it simple for demo purposes
                throw new RuntimeException(cce);
            }
        }
                
        // Completion message
        task.out.println("Completed SerializeExample1");        
    }

    /**
     * Attempts to acquire the shared resource lock. This method calls the
     * {@link NameResource#enqueue()} method. Using the {@link NameResource#tryEnqueue()}
     * method would perform the same operation, but would not block waiting for the
     * lock to become available.
     * 
     * @param lock the lock to acquire
     * 
     * @return a boolean flag to indicate the lock has been successfully acquired.
     * 
     * @throws CicsConditionException if there was an error accessing the lock resource.
     */
    private static boolean acquireLock(NameResource lock)
    {
        try {
            // Attempt to acquire the lock
            lock.enqueue();
            
            // We have successfully obtained the lock
            return true;
        }
        catch (LengthErrorException lee) {
            // Corresponds to LENGERR - bad programming
            // Crude error-handling in this case: simply wrapper and throw
            throw new RuntimeException(lee);
        }
        catch (ResourceUnavailableException e) {
            // Corresponds to ENQBUSY - return lock not held
            return false;
        }        
    }
    
    /**
     * Sleep this thread for a random interval.
     */
    private static void randomSleep()
    {
        // Generate 1 <= t < 21
        long t = ThreadLocalRandom.current().nextLong(1, 21);
        
        try {
            // Sleep for the random period of time
            Thread.sleep(t);
        }
        catch (InterruptedException ie) {
            // We have been awoken - that is all
        }
    }
    
    /**
     * Provides a dummy method to represent an update of some shared application
     * resource.
     * 
     * @throws CicsConditionException never thrown, but declared to
     * give the caller something to worry about.
     */
    private static void doUpdate() throws CicsConditionException
    {
        // Null block for demonstration purposes
    }    
}
