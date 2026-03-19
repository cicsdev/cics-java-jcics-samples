/* Licensed Materials - Property of IBM                                   */
/*                                                                        */
/* SAMPLE                                                                 */
/*                                                                        */
/* (c) Copyright IBM Corp. 2016 All Rights Reserved                       */
/*                                                                        */
/* US Government Users Restricted Rights - Use, duplication or disclosure */
/* restricted by GSA ADP Schedule Contract with IBM Corp                  */
/*                                                                        */

package com.ibm.cicsdev.tsq;

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.InvalidQueueIdException;
import com.ibm.cics.server.TSQ;

/**
 * Superclass used to provide common services used in all of the TSQ
 * examples.  
 * 
 * For the sake of brevity, this example does not include comprehensive
 * error-handling logic. See the CICS Java samples around error-handling
 * for more details on catching CICS Exceptions in Java.
 */
public abstract class TSQCommon
{
    /**
     * A field to hold a reference to the CICS temporary
     * storage queue this instance will access. 
     */
    protected final TSQ tsq;
    
    /**
     * Constructor used to initialise this class with some
     * common data used by all TSQ examples.
     *  
     * @param tsq - a reference to the temporary storage queue we will
     * be manipulating in this example.
     */
    protected TSQCommon(TSQ tsq)
    {
        // Save reference to the supplied TSQ
        this.tsq = tsq;
        
        // Delete any old TSQ that's lying around
        deleteQueue();
    }
    
    /**
     * Deletes a temporary storage queue.
     */
    private void deleteQueue()
    {
        try {
            // Delete the TSQ
            this.tsq.delete();
        }
        catch (InvalidQueueIdException iqe) {
            // QIDERR occurs when the queue cannot be found
            // This is an acceptable condition in this program
        }
        catch (CicsConditionException cce) {
            // All other errors
            // Crude error handling - propagate an exception back to caller
            throw new RuntimeException(cce);
        }
    }
}
