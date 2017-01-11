/* Licensed Materials - Property of IBM                                   */
/*                                                                        */
/* SAMPLE                                                                 */
/*                                                                        */
/* (c) Copyright IBM Corp. 2017 All Rights Reserved                       */
/*                                                                        */
/* US Government Users Restricted Rights - Use, duplication or disclosure */
/* restricted by GSA ADP Schedule Contract with IBM Corp                  */
/*                                                                        */

package com.ibm.cicsdev.vsam.esds;

import java.util.List;

import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;

/**
 * Simple example to demonstrate browsing a VSAM ESDS file using JCICS.
 * 
 * This class is just the driver of the test. The main JCICS work is done in the
 * common class {@link EsdsExampleCommon}.
 */
public class EsdsExample5 
{
    /**
     * Number of records to add and then browse through.
     */
    private static final int RECORDS_TO_BROWSE = 5;
    
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
        task.out.println(" - Starting EsdsExample5");
        task.out.println("VSAM ESDS file browse example");

        // Create a new instance of the common example class
        EsdsExampleCommon ex = new EsdsExampleCommon();

        
        /*
         * Create some records in the file so we have something to work with.
         */
        
        // ESDS records are always stored in the sequence they were added
        long rbaFirst = -1;
        
        // Add 5 records
        for ( int i = 0; i < RECORDS_TO_BROWSE; i++ ) {
            
            // Add a new record to the file 
            StockPart sp = StockPartHelper.generate();
            long rba = ex.addRecord(sp);
            
            // Write out the RBA of the new record
            task.out.println( String.format("Wrote to RBA 0x%016X", rba) );
            
            // Keep track of the RBA of the first insert
            if ( rbaFirst == -1 ) {
                rbaFirst = rba;
            }
        }
        
        // Commit the unit of work to harden the inserts to the file
        ex.commitUnitOfWork();            
        

        /*
         * Browse through the file, starting at the lowest RBA.
         * 
         * The above code will have guaranteed sufficient records exist.
         */
        
        // Browse through the records, starting at the lowest known RBA
        List<StockPart> list = ex.browse(rbaFirst, RECORDS_TO_BROWSE);
        
        // Iterate over this list
        for ( StockPart sp : list ) {
            
            // Display the description
            String strMsg = "Read record with description %s";
            task.out.println( String.format(strMsg, sp.getDescription().trim()) );
        }
        
        // Completion message
        task.out.println("Completed EsdsExample5");
    }
}
