package com.ibm.cicsdev.vsam.rrds;

import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;

/**
 * Simple example to demonstrate updating a record in a VSAM RRDS file using JCICS.
 * 
 * This class is just the driver of the test. The main JCICS work is done in the
 * common class {@link RrdsExampleCommon}.
 */
public class RrdsExample3
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
        task.out.println(" - Starting RrdsExample3");
        task.out.println("VSAM RRDS record update example");

        // Create a new instance of the common example class
        RrdsExampleCommon ex = new RrdsExampleCommon();

        // Unlike the KSDS and ESDS examples, we need an empty file before we start
        ex.emptyFile();
        
        // We will always add and read RRN 1
        long rrn = 1;
        
        
        /*
         * Create a record in the file so we have something to work with.
         */
        
        // Scoping of local variables
        {
            // Add a new record to the file 
            StockPart sp = StockPartHelper.generate();
            ex.addRecord(rrn, sp);
            
            // Commit the unit of work
            ex.commitUnitOfWork();
         
            // Write out the key and description
            String strMsg = "Wrote to RRN 0x%016X with description %s";
            task.out.println( String.format(strMsg, rrn, sp.getDescription().trim()) );
        }
        
        
        /*
         * Now update this known record with a new description.
         */
        
        // Scoping of local variables
        {
            // Generate a new part description
            String strDesc = StockPartHelper.generateDescription();

            // Now update the known record with a specified description
            StockPart sp = ex.updateRecord(rrn, strDesc);
            
            // Display the updated description
            String strMsg = "Updated record with description %s";
            task.out.println( String.format(strMsg, sp.getDescription().trim()) );
        }
        
        // Unit of work containing the update will be committed at normal end of task
        
        // Completion message
        task.out.println("Completed RrdsExample3");
    }
}
