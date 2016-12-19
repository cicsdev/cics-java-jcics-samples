package com.ibm.cicsdev.vsam.rrds;

import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;

/**
 * Simple example to demonstrate adding a record to a VSAM RRDS file using JCICS.
 * 
 * This class is just the driver of the test. The main JCICS work is done in the
 * common class {@link RrdsExampleCommon}.
 */
public class RrdsExample1
{
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
        task.out.println(" - Starting RrdsExample1");
        task.out.println("VSAM RRDS record addition example");

        // Create a new instance of the common ESDS class
        RrdsExampleCommon ex = new RrdsExampleCommon();
        
        // Unlike the KSDS and ESDS examples, we need an empty file before we start
        ex.emptyFile();
        
        // Create a new random record for writing to the file
        StockPart sp = StockPartHelper.generate();
        
        // Add a new record to the file at RRN 1
        long rrn = 1;
        
        // Perform the add
        ex.addRecord(rrn, sp);
        
        // Write out the new RRN
        task.out.println( String.format("Wrote record with RRN 0x%016X", rrn) );
        
        // Unit of work containing the write will be committed at normal end of task
        
        // Completion message
        task.out.println("Completed RrdsExample1");
    }
}
