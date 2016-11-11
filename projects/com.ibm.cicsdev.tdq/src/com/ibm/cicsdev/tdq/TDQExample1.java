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
import com.ibm.cics.server.DataHolder;
import com.ibm.cics.server.TDQ;
import com.ibm.cics.server.Task;

/**
 * Example to demonstrate writing reading simple text-based records
 * to and from a TDQ using JCICS.
 */
public class TDQExample1 extends TDQCommon
{
    /**
     * Name of the TDQ to use.
     */
    private static final String TDQ_NAME = "MYQ1";

    /**
     * Number of items to write to the queue.
     */
    private static final int DEPTH_COUNT = 5;

    /**
     * Main entry point to this CICS Java program.
     */
    public static void main(String[] args)
    {
        // Get details about our current CICS task
        Task task = Task.getTask();
        task.out.println(" - Starting TDQExample1");

        // Create a reference to the TDQ
        TDQ tdq = new TDQ();
        tdq.setName(TDQ_NAME);

        // Create a new instance of this class
        TDQExample1 ex = new TDQExample1(tdq);
        
        // Write text to the queue
        ex.writeToQueue();
        
        // Read text from the queue
        ex.readFromQueue();
        
        // Completion message
        task.out.println("Completed TDQExample1");
    }
    
    /**
     * Constructor used to pass data to superclass constructor.
     * 
     * @param tdq - the transient data queue reference we will
     * be manipulating in this example.
     */
    public TDQExample1(TDQ tdq)
    {
        super(tdq);
    }

    /**
     * Write some sample data to the instance TDQ.
     */
    public void writeToQueue()
    {
        // Write several items to the queue
        for (int i = 1; i <= DEPTH_COUNT; i++) {
            
            // Construct a message for writing to the queue
            String msg = MessageFormat.format("TDQ write from JCICS item {0}", i);
        
            try {
                // Write the data to the TDQ
                // No codepage conversion required for this method
                // Requires CICS TS V5.1 and later (JCICS 1.401.0)
                this.tdq.writeString(msg);
            }
            catch (CicsConditionException cce) {
                // Crude error handling - propagate an exception back to caller
                throw new RuntimeException(cce);
            }
        }
    }
    
    /**
     * Simple read of string data from a TDQ.
     */
    public void readFromQueue()
    {
        // A holder object to receive the data from CICS
        DataHolder holder = new DataHolder();
        
        for ( int i = 1; i <= DEPTH_COUNT; i++ ) {
            
            try {
                // Read the queue sequentially (destructive read)
                this.tdq.readData(holder);
            }
            catch (CicsConditionException cce) {
                // Crude error handling - propagate an exception back to caller
                throw new RuntimeException(cce);
            }

            // Assume this is a valid character string in the CICS local CCSID 
            // Requires CICS TS V5.1 and later (JCICS 1.401.0)
            String strData = holder.getStringValue();
            
            // Write out to the console
            String msg = MessageFormat.format("Read data from queue \"{0}\"", strData);
            this.task.out.println(msg);
        }
    }    
}
