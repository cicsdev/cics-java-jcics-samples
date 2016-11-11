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

import java.text.MessageFormat;

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.ItemHolder;
import com.ibm.cics.server.TSQ;
import com.ibm.cics.server.TSQType;
import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.TsqRecord;

/**
 * More complex example to demonstrate writing and reading 
 * a record to and from a TSQ using JCICS.
 * 
 * This uses the generated class {@link TsqRecord}.
 */
public class TSQExample3 extends TSQCommon
{
    /**
     * Name of the TSQ to use.
     */
    private static final String TSQ_NAME = "MYTSQ";

    /**
     * Number of items to write to the queue.
     */
    private static final int DEPTH_COUNT = 5;

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
        task.out.println(" - Starting TSQExample3");

        // Create a reference to the TSQ
        TSQ tsq = new TSQ();
        
        // Specify the queue name and that we are using main storage        
        tsq.setName(TSQ_NAME);
        tsq.setType(TSQType.MAIN);

        // Create a new instance of this class
        TSQExample3 ex = new TSQExample3(tsq);
        
        // Write data to the queue
        ex.writeToQueue();
        
        // Read data from the queue
        ex.readFromQueue();
        
        // Completion message
        task.out.println("Completed TSQExample3");
    }

    /**
     * Constructor used to pass data to superclass constructor.
     * 
     * @param tsq - the temporary storage queue reference we will
     * be manipulating in this example.
     */
    public TSQExample3(TSQ tsq)
    {
        super(tsq);
    }
    
    /**
     * Write of byte[] data to a TSQ.
     */
    public void writeToQueue()
    {
        // Create a wrapper object to write to the queue
        // This class has been generated using the JZOS record generator utility
        TsqRecord data = new TsqRecord();
        
        // Populate with some dummy data
        data.setBinaryDigit(42);
        data.setCharacterString("TSQ test example");
        data.setNumericValue(123456789);
        data.setPackedDec(123);
        data.setSignedPacked(-99);        
        
        // Write several items to the queue
        for (int i = 1; i <= DEPTH_COUNT; i++) {

            // Update record for this iteration
            data.setRecordId(i);            
            
            // Extract the byte data from the wrapper object
            // The generated class handles any codepage conversion required
            byte[] record = data.getByteBuffer();
            
            try {
                // Write the data to the TSQ
                this.tsq.writeItem(record);
            }
            catch (CicsConditionException cce) {
                // Crude error handling - propagate an exception back to caller
                throw new RuntimeException(cce);
            }
        }
    }
    
    /**
     * Read of byte[] data from a TSQ.
     */
    public void readFromQueue()
    {
        // A holder object to receive the data from CICS
        ItemHolder holder = new ItemHolder();
        
        // Read data from the queue
        for (int i = 1; i <= DEPTH_COUNT; i++) {
            
            try {
                // Read the data from CICS (TSQ indices are 1-based)
                this.tsq.readItem(i, holder);
            }
            catch (CicsConditionException cce) {
                // Crude error handling - propagate an exception back to caller
                throw new RuntimeException(cce);
            }
    
            // Obtain the raw bytes from the TSQ item
            byte[] itemData = holder.getValue();
        
            // Convert to Java object form
            // This class has been generated using the JZOS record generator utility
            TsqRecord record = new TsqRecord(itemData);
            
            // Break the record apart
            String msg = MessageFormat.format("Record: ({0}) ({1}) ({2}) ({3}) ({4}) ({5})", 
                    record.getRecordId(),
                    record.getBinaryDigit(),
                    record.getCharacterString(),
                    record.getNumericValue(),
                    record.getPackedDec(),
                    record.getSignedPacked());
            
            // Write out the record to the terminal
            this.task.out.println(msg);
        }
    }    
}
