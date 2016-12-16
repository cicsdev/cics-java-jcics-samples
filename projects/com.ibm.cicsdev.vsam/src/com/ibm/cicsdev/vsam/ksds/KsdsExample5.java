package com.ibm.cicsdev.vsam.ksds;

import java.text.MessageFormat;
import java.util.List;

import com.ibm.cics.server.KSDS;
import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;

/**
 * Simple example to demonstrate browsing a VSAM KSDS file using JCICS.
 */
public class KsdsExample5 
{
    /**
     * Name of the file resource to use.
     */
    private static final String FILE_NAME = "XMPLKSDS";

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

        // Create a reference to the file
        KSDS file = new KSDS();
        file.setName(FILE_NAME);

        // Create a new instance of this class
        KsdsExampleCommon ex = new KsdsExampleCommon(file);

        
        /*
         * Create at least 5 records in the file so we have something to work with.
         */
        
        // Keep track of the lowest generated key
        int key = Integer.MAX_VALUE;
        
        // Add 5 records, keeping track of the lowest key
        for ( int i = 0; i < 5; i++ ) {
            
            // Add a new record to the file 
            StockPart sp = StockPartHelper.generate();
            ex.addRecord(sp);
            
            // Get the key of the new record
            int newKey = sp.getPartId();
            
            // Write out the key and description
            String strMsg = "Wrote to key {0}";
            task.out.println( MessageFormat.format(strMsg, newKey) );
            
            // Decide if this is lowest key so far
            key = newKey < key ? newKey : key;
        }
        
        // Commit the unit of work to harden the inserts to the file
        ex.commitUnitOfWork();            
        

        /*
         * Browse through the file, starting at the lowest key.
         * 
         * Note the next five records we find may not necessarily be the five we
         * added above. It will depend on what existing records were already in 
         * the KSDS file. 
         * 
         * The above code will have guaranteed that at least five records exist.
         */
        
        // Browse through five records, starting at the lowest known key
        List<StockPart> list = ex.browse(key, 5);
        
        // Iterate over this list
        for ( StockPart sp : list ) {
            
            // Display the description
            String strMsg = "Read record with description {0}";
            task.out.println( MessageFormat.format(strMsg, sp.getDescription()) );
        }
        
        // Completion message
        task.out.println("Completed KsdsExample5");
    }
}
