package com.ibm.cicsdev.vsam.esds;

import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;

/**
 * Simple example to demonstrate updating  a record in a VSAM ESDS file using JCICS.
 * 
 * This class is just the driver of the test. The main JCICS work is done in the
 * common class {@link EsdsExampleCommon}.
 */
public class EsdsExample3
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
        task.out.println("VSAM ESDS record update example");

        // Create a new instance of the common ESDS class
        EsdsExampleCommon ex = new EsdsExampleCommon();

        
        /*
         * Create a record in the file so we have something to work with.
         */
        
        // Keep track of the RBA of the new record
        long rba;

        // Scoping of local variables
        {
            // Add a new record to the file 
            StockPart sp = StockPartHelper.generate();
            rba = ex.addRecord(sp);
            
            // Commit the unit of work
            ex.commitUnitOfWork();
         
            // Write out the RBA and description
            String strMsg = "Wrote to RBA 0x%016X with description %s";
            task.out.println( String.format(strMsg, rba, sp.getDescription().trim()) );
        }
        
        
        /*
         * Now update this known record with a new description.
         */
        
        // Scoping of local variables
        {
            // Generate a new part description
            String strDesc = StockPartHelper.generateDescription();

            // Update the known record with a specified description
            StockPart sp = ex.updateRecord(rba, strDesc);
            
            // Display the updated description
            String strMsg = "Updated record with description %s";
            task.out.println( String.format(strMsg, sp.getDescription().trim()) );
        }
        
        // Unit of work containing the update will be committed at normal end of task
        
        // Completion message
        task.out.println("Completed EsdsExample3");
    }
}
