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
 * Simple example to demonstrate reading a record from a VSAM KSDS file using JCICS.
 * 
 * This class is just the driver of the test. The main JCICS work is done in the
 * superclass {@link KsdsExampleCommon}.
 */
public class KsdsExample2
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
        task.out.println(" - Starting KsdsExample2");
        task.out.println("VSAM KSDS record read example");

        // Create a new instance of the common example class
        KsdsExampleCommon ex = new KsdsExampleCommon();
        
        // Add a new record to the file so we have something to work with
        StockPart spNew = StockPartHelper.generate();
        ex.addRecord(spNew);
        
        // Keep track of the key
        int key = spNew.getPartId();
        
        // Write out the original description
        task.out.println( String.format("Wrote record with key 0x%08X", key) );
        
        // Commit the current unit of work harden new record to the file
        ex.commitUnitOfWork();
            
        // Now read the record with the specified key
        StockPart spRead = ex.readRecord(key);
            
        // Display the read description
        String strMsg = "Read record with description %s";
        task.out.println( String.format(strMsg, spRead.getDescription().trim()) );
        
        // Completion message
        task.out.println("Completed KsdsExample2");
    }
}
