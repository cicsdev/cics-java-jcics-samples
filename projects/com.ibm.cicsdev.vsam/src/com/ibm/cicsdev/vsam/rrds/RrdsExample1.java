package com.ibm.cicsdev.vsam.rrds;

import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;

/**
 * Simple example to demonstrate adding a record to a VSAM RRDS file using JCICS.
 * 
 * This class is just the driver of the test. The main JCICS work is done in the
 * superclass {@link RrdsExampleCommon}.
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

        // Create a new instance of the common ESDS class
        RrdsExampleCommon ex = new RrdsExampleCommon();
        
        // Create a new random record for writing to the file
        StockPart sp = StockPartHelper.generate();
        
        // Add a new record to the file
        ex.addRecord(sp);
        
        // Write out the new RBA
        task.out.println( String.format("Wrote record with RBA 0x%016X") );
        
        // Unit of work containing the write will be committed at normal end of task
        
        // Completion message
        task.out.println("Completed EsdsExample1");
    }
}
