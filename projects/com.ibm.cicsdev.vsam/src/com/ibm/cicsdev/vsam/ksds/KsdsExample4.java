/* Licensed Materials - Property of IBM                                   */
/*                                                                        */
/* SAMPLE                                                                 */
/*                                                                        */
/* (c) Copyright IBM Corp. 2017 All Rights Reserved                       */
/*                                                                        */
/* US Government Users Restricted Rights - Use, duplication or disclosure */
/* restricted by GSA ADP Schedule Contract with IBM Corp                  */
/*                                                                        */

package com.ibm.cicsdev.vsam.ksds;

import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;

/**
 * Simple example to demonstrate deleting a record from a VSAM KSDS file using JCICS.
 * 
 * This class is just the driver of the test. The main JCICS work is done in the
 * common class {@link KsdsExampleCommon}.
 */
public class KsdsExample4 
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
        task.out.println(" - Starting KsdsExample4");
        task.out.println("VSAM KSDS record delete example");

        // Create a new instance of the common example class
        KsdsExampleCommon ex = new KsdsExampleCommon();

        
        /*
         * Create a record in the file so we have something to work with.
         */
        
        // Keep track of the generated key
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
            
            // Write out the key
            task.out.println( String.format("Wrote to key 0x%08X", key) );
        }
        

        /*
         * Delete the record.
         */
        
        // Scoping of local variables
        {
            // Now delete this known record
            StockPart sp = ex.deleteRecord(key);
            
            // Display the result
            String strMsg = "Deleted record with description %s";
            task.out.println( String.format(strMsg, sp.getDescription().trim()) );
        }
        
        // Unit of work containing the delete will be committed at normal end of task
        
        // Completion message
        task.out.println("Completed KsdsExample4");
    }
}
