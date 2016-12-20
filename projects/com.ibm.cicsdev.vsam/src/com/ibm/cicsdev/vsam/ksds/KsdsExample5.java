package com.ibm.cicsdev.vsam.ksds;

import java.util.List;

import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;

/**
 * Simple example to demonstrate browsing a VSAM KSDS file using JCICS.
 * 
 * This class is just the driver of the test. The main JCICS work is done in the
 * common class {@link KsdsExampleCommon}.
 */
public class KsdsExample5 
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
        task.out.println(" - Starting KsdsExample5");
        task.out.println("VSAM KSDS file browse example");

        // Create a new instance of the common example class
        KsdsExampleCommon ex = new KsdsExampleCommon();

        
        /*
         * Create some records in the file so we have something to work with.
         */
        
        // Keep track of the lowest generated key
        int key = Integer.MAX_VALUE;
        
        // Add records, keeping track of the lowest key
        for ( int i = 0; i < RECORDS_TO_BROWSE; i++ ) {
            
            // Add a new record to the file 
            StockPart sp = StockPartHelper.generate();
            ex.addRecord(sp);
            
            // Get the key of the new record
            int newKey = sp.getPartId();
            
            // Write out the key and description
            task.out.println( String.format("Wrote to key 0x%08X", newKey) );
            
            // Decide if this is lowest key so far
            key = newKey < key ? newKey : key;
        }
        
        // Commit the unit of work to harden the inserts to the file
        ex.commitUnitOfWork();            
        

        /*
         * Browse through the file, starting at the lowest key.
         * 
         * Note the next n records we find may not necessarily be the n records we
         * added above. It will depend on what existing records were already in 
         * the KSDS file. 
         * 
         * The above code will have guaranteed that at least RECORDS_TO_BROWSE
         * records exist.
         */
        
        // Browse through the records, starting at the lowest known key
        List<StockPart> list = ex.browse(key, RECORDS_TO_BROWSE);
        
        // Iterate over this list
        for ( StockPart sp : list ) {
            
            // Display the description
            String strMsg = "Read record with description %s";
            task.out.println( String.format(strMsg, sp.getDescription().trim()) );
        }
        
        // Completion message
        task.out.println("Completed KsdsExample5");
    }
}
