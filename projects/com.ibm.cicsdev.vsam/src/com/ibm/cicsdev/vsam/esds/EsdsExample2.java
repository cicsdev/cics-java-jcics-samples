package com.ibm.cicsdev.vsam.esds;

import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;
import com.ibm.cicsdev.vsam.ksds.KsdsExampleCommon;


/**
 * Simple example to demonstrate reading a record from a VSAM ESDS file using JCICS.
 * 
 * This class is just the driver of the test. The main JCICS work is done in the
 * common class {@link KsdsExampleCommon}.
 */
public class EsdsExample2
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
        task.out.println(" - Starting EsdsExample2");
        task.out.println("VSAM ESDS record read example");

        // Create a new instance of the common ESDS class
        EsdsExampleCommon ex = new EsdsExampleCommon();
        
        // Add a new record to the file so we have something to work with
        StockPart spNew = StockPartHelper.generate();
        long rba = ex.addRecord(spNew);
        
        // Commit the current unit of work harden new record to the file
        ex.commitUnitOfWork();
            
        // Write out the RBA of the record we have just written
        task.out.println( String.format("Wrote record with RBA 0x%016X", rba) );
        
        // Now read the record with the specified key
        StockPart spRead = ex.readRecord(rba);
            
        // Did we read successfully?
        if ( spRead != null ) {
            // Display the read description
            String strMsg = "Read record with description %s";
            task.out.println( String.format(strMsg, spRead.getDescription().trim()) );
        }
        
        // Completion message
        task.out.println("Completed EsdsExample2");
    }
}
