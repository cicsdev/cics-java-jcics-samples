package com.ibm.cicsdev.vsam.ksds;

import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;

/**
 * Simple example to demonstrate updating a record in a VSAM KSDS file using JCICS.
 * 
 * This class is just the driver of the test. The main JCICS work is done in the
 * common class {@link KsdsExampleCommon}.
 */
public class KsdsExample3
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
        task.out.println(" - Starting KsdsExample3");
        task.out.println("VSAM KSDS record update example");

        // Create a new instance of the common example class
        KsdsExampleCommon ex = new KsdsExampleCommon();

        
        /*
         * Create a record in the file so we have something to work with.
         */
        
        // Keep track of the key of the new record
        int key;

        // Scoping of local variables
        {
            // Add a new record to the file 
            StockPart sp = StockPartHelper.generate();
            ex.addRecord(sp);
            
            // Commit the unit of work
            ex.commitUnitOfWork();
         
            // Get hold of the new part ID
            key = sp.getPartId();
            
            // Write out the key and description
            String strMsg = "Wrote to key 0x%08X with description %s";
            task.out.println( String.format(strMsg, key, sp.getDescription().trim()) );
        }
        
        
        /*
         * Now update this known record with a new description.
         */
        
        // Scoping of local variables
        {
            // Generate a new part description
            String strDesc = StockPartHelper.generateDescription();

            // Now update the known record with a specified description
            StockPart sp = ex.updateRecord(key, strDesc);
            
            // Display the updated description
            String strMsg = "Updated record with description %s";
            task.out.println( String.format(strMsg, sp.getDescription().trim()) );
        }
        
        // Unit of work containing the update will be committed at normal end of task
        
        // Completion message
        task.out.println("Completed KsdsExample3");
    }
}
