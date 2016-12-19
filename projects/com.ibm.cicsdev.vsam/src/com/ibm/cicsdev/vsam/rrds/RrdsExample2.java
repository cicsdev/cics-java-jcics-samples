package com.ibm.cicsdev.vsam.rrds;

import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;


/**
 * Simple example to demonstrate reading a record from a VSAM RRDS file using JCICS.
 * 
 * This class is just the driver of the test. The main JCICS work is done in the
 * common class {@link RrdsExampleCommon}.
 */
public class RrdsExample2
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
        task.out.println(" - Starting RrdsExample2");
        task.out.println("VSAM RRDS record read example");

        // Create a new instance of this class
        RrdsExampleCommon ex = new RrdsExampleCommon();
        
        // Unlike the KSDS and ESDS examples, we need an empty file before we start
        ex.emptyFile();
        
        // We will always add and read RRN 1
        long rrn = 1;
        
        // Add a new record to the file so we have something to work with
        StockPart spNew = StockPartHelper.generate();
        ex.addRecord(rrn, spNew);
        
        // Write out the original description
        task.out.println( String.format("Wrote record with RRN 0x%016X", rrn) );
        
        // Commit the current unit of work harden new record to the file
        ex.commitUnitOfWork();
            
        // Now read the record with the specified key
        StockPart spRead = ex.readRecord(rrn);
            
        // Did we read successfully?
        if ( spRead != null ) {
            // Display the read description
            String strMsg = "Read record with description %s";
            task.out.println( String.format(strMsg, spRead.getDescription().trim()) );
        }
        
        // Completion message
        task.out.println("Completed RrdsExample2");
    }
}
