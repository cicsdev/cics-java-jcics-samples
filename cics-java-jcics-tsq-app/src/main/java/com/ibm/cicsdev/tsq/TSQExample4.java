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
import com.ibm.cics.server.ItemErrorException;
import com.ibm.cics.server.ItemHolder;
import com.ibm.cics.server.TSQ;
import com.ibm.cics.server.TSQType;
import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.TsqRecord;

/**
 * Extends the {@link TSQExample3} class by including a method which
 * updates items in the queue. 
 */
public class TSQExample4 extends TSQExample3
{
    /**
     * Name of the TSQ to use.
     */
    private static final String TSQ_NAME = "MYTSQ";
    
    /**
     * Main entry point to a CICS OSGi program.
     * 
     * The FQ name of this class should be added to the CICS-MainClass entry in
     * the parent OSGi bundle's manifest.
     */     
    public static void main(String[] args)
    {
        // Get details about our current CICS task
        Task task = Task.getTask();
        task.out.println(" - Starting TSQExample4");
    
        // Create a reference to the TSQ
        TSQ tsq = new TSQ();
        
        // Specify the queue name and that we are using main storage        
        tsq.setName(TSQ_NAME);
        tsq.setType(TSQType.MAIN);
    
        // Create a new instance of this class
        TSQExample4 ex = new TSQExample4(tsq);
    
        // Write some initial data to the TSQ
        ex.writeToQueue();
        
        // Update the TSQ with a browse and rewrite
        ex.updateQueue();
    
        // Read from the TSQ to confirm update
        ex.readFromQueue();
        
        // Completion message
        task.out.println("Completed TSQExample4");
    }
    
    /**
     * Constructor used to pass data to superclass constructor.
     * 
     * @param tsq - the temporary storage queue reference we will
     * be manipulating in this example.
     */
    public TSQExample4(TSQ tsq)
    {
        super(tsq);
    }

    /**
     * Browses through a queue created by {@link TSQExample3} and updates each record.
     * 
     * Each item in the queue is assumed to have a layout specified by the generated
     * {@link TsqRecord} class.
     */    
    public void updateQueue()
    {
        // Current item we are reading (TSQ indices are 1-based)
        int currentItem = 1;
        
        // Holder to receive data from TSQ 
        ItemHolder holder = new ItemHolder();
        
        // Loop until we break out at end of queue
        while ( true ) {

            try {
                // Browse through the queue sequentially
                this.tsq.readNextItem(holder);
            }
            catch (ItemErrorException iee) {
                // ITEMERR represents end of browse - normal condition here
                break;
            }
            catch (CicsConditionException cce) {
                // Crude error handling - propagate an exception back to caller
                throw new RuntimeException(cce);
            }
            
            // Extract the record
            // This class has been generated using the JZOS record generator utility
            TsqRecord record = new TsqRecord(holder.getValue());
            
            // Update the record ID and character fields
            record.setRecordId(record.getRecordId() + 100);
            record.setCharacterString("Updated: " + record.getCharacterString());

            try {
                // Write record back to the queue
                this.tsq.rewriteItem(currentItem, record.getByteBuffer());
            }
            catch (CicsConditionException cce) {
                // Crude error handling - propagate an exception back to caller
                throw new RuntimeException(cce);
            }
            
            // Next queue item
            currentItem++;
        }
    }
}
