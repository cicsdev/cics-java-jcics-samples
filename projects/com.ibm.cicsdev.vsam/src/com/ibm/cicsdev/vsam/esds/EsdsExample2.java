package com.ibm.cicsdev.vsam.esds;

import java.text.MessageFormat;

import com.ibm.cics.server.KSDS;
import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;
import com.ibm.cicsdev.vsam.ksds.KsdsExampleCommon;


/**
 * Simple example to demonstrate reading a record from a VSAM KSDS file using JCICS.
 * 
 * This class is just the driver of the test. The main JCICS work is done in the
 * superclass {@link KsdsExampleCommon}.
 */
public class EsdsExample2
{
    /**
     * Name of the file resource to use.
     */
    private static final String FILE_NAME = "SMPLXMPL";

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

        // Create a reference to the file
        KSDS file = new KSDS();
        file.setName(FILE_NAME);

        // Create a new instance of this class
        KsdsExampleCommon ex = new KsdsExampleCommon(file);
        
        // Add a new record to the file so we have something to work with
        StockPart spNew = StockPartHelper.generate();
        ex.addRecord(spNew);
        
        // Keep track of the key
        int key = spNew.getPartId();
        
        // Write out the original description
        task.out.println( MessageFormat.format("Wrote record with key {0}", key) );
        
        // Commit the current unit of work harden new record to the file
        ex.commitUnitOfWork();
            
        // Now read the record with the specified key
        StockPart spRead = ex.readRecord(key);
            
        // Did we read successfully?
        if ( spRead != null ) {
            // Display the read description
            String strMsg = "Read record with description {0}";
            task.out.println( MessageFormat.format(strMsg, spRead.getDescription()) );
        }
        
        // Completion message
        task.out.println("Completed KsdsExample2");
    }
}
