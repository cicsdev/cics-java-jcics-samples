package com.ibm.cicsdev.vsam.esds;

import java.text.MessageFormat;

import com.ibm.cics.server.KSDS;
import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;
import com.ibm.cicsdev.vsam.ksds.KsdsExampleCommon;


/**
 * Simple example to demonstrate reading and writing to a VSAM KSDS
 * file using JCICS.
 */
public class EsdsExample3
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
        task.out.println(" - Starting KsdsExample3");

        // Create a reference to the file
        KSDS file = new KSDS();
        file.setName(FILE_NAME);

        // Create a new instance of this class
        KsdsExampleCommon ex = new KsdsExampleCommon(file);

        
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
            String strMsg = "Wrote to key {0} with description {1}";
            task.out.println( MessageFormat.format(strMsg, key, sp.getDescription()) );
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
            String strMsg = "Updated record with description {0}";
            task.out.println( MessageFormat.format(strMsg, sp.getDescription()) );
        }
        
        // Unit of work containing the update will be committed at normal end of task
        
        // Completion message
        task.out.println("Completed KsdsExample3");
    }
}
