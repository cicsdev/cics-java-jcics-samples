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
import com.ibm.cicsdev.bean.TdqRecord;

/**
 * Example to demonstrate writing reading simple text-based records
 * to and from a TDQ using JCICS.
 */
public class TDQExample3 extends TDQCommon
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
        task.out.println(" - Starting TDQExample3");

        // Create a reference to the TDQ
        TDQ tdq = new TDQ();
        tdq.setName(TDQ_NAME);

        // Create a new instance of this class
        TDQExample3 ex = new TDQExample3(tdq);
        
        // Write text to the queue
        ex.writeToQueue();
        
        // Read text from the queue
        ex.readFromQueue();
        
        // Completion message
        task.out.println("Completed TDQExample3");
    }

    /**
     * Constructor used to pass data to superclass constructor.
     * 
     * @param tdq - the transient data queue reference we will
     * be manipulating in this example.
     */
    public TDQExample3(TDQ tdq)
    {
        super(tdq);
    }

    /**
     * Write of byte[] data to the instance TDQ.
     */
    public void writeToQueue()
    {
        // Create a wrapper object to write to the queue
        // This class has been generated using the JZOS record generator utility
        TdqRecord data = new TdqRecord();
        
        // Populate with some dummy data
        data.setBinaryDigit(42);
        data.setCharacterString("TDQ test example");
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
                // Write the data to the TDQ
                this.tdq.writeData(record);
            }
            catch (CicsConditionException cce) {
                // Crude error handling - propagate an exception back to caller
                throw new RuntimeException(cce);
            }
        }
        
    }
    
    /**
     * Read of byte[] data from a TDQ.
     */
    public void readFromQueue()
    {
        // A holder object to receive the data from CICS
        DataHolder holder = new DataHolder();
        
        // Read data from the queue
        for (int i = 1; i <= DEPTH_COUNT; i++) {
            
            try {
                // Read the data from CICS
                this.tdq.readData(holder);
            }
            catch (CicsConditionException cce) {
                // Crude error handling - propagate an exception back to caller
                throw new RuntimeException(cce);
            }
    
            // Obtain the raw bytes from the TDQ item
            byte[] itemData = holder.getValue();
        
            // Convert to Java object form
            // This class has been generated using the JZOS record generator utility
            TdqRecord record = new TdqRecord(itemData);
            
            // Break the record apart
            String msg = MessageFormat.format("Record: ({0}) ({1}) ({2}) ({3}) ({4}) ({5})", 
                    record.getRecordId(),
                    record.getBinaryDigit(),
                    record.getCharacterString(),
                    record.getNumericValue(),
                    record.getPackedDec(),
                    record.getSignedPacked());
            
            // Write out the record to the terminal
            Task.getTask().out.println(msg);
        }        
    }    
}
