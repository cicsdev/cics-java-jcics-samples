/* Licensed Materials - Property of IBM                                   */
/*                                                                        */
/* SAMPLE                                                                 */
/*                                                                        */
/* (c) Copyright IBM Corp. 2016 All Rights Reserved                       */
/*                                                                        */
/* US Government Users Restricted Rights - Use, duplication or disclosure */
/* restricted by GSA ADP Schedule Contract with IBM Corp                  */
/*                                                                        */

package com.ibm.cicsdev.tdq;

import java.text.MessageFormat;

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.InvalidQueueIdException;
import com.ibm.cics.server.TDQ;
import com.ibm.cics.server.Task;

/**
 * Superclass used to provide common services used in all of the TDQ
 * examples.  
 */
public abstract class TDQCommon
{
    /**
     * A field to hold a reference to the CICS transient 
     * data queue this instance will access. 
     */
    protected final TDQ tdq;
    
    /**
     * Constructor used to initialise this class with some
     * common data used by all TDQ examples.
     *  
     * @param tdq - a reference to the transient data queue we will
     * be manipulating in this example.
     */
    protected TDQCommon(TDQ tdq)
    {
        // Save reference to the TDQ
        this.tdq = tdq;
        
        // Delete any old TDQ that's lying around
        deleteQueue();
    }
    
    /**
     * Deletes all the data associated with the instance transient data queue.
     */
    private void deleteQueue()
    {
        try {
            // Empty the TDQ
            this.tdq.delete();
        }
        catch (InvalidQueueIdException iqe) {
            
            // QIDERR occurs when the queue cannot be found.
            // Unlike the TSQ case, this is NOT an acceptable condition,
            // because TDQs cannot be generated dynamically
            String msg = MessageFormat.format("Could not find queue \"{0}\"", this.tdq.getName());
            Task.getTask().out.println(msg);
            
            // Crude error handling - propagate an exception back to caller
            throw new RuntimeException(iqe);
        }
        catch (CicsConditionException cce) {
            // All other errors
            // Crude error handling - propagate an exception back to caller
            throw new RuntimeException(cce);
        }
    }
}
