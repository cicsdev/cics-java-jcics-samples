package com.ibm.cicsdev.vsam.esds;

import java.text.MessageFormat;

import com.ibm.cics.server.KSDS;
import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;
import com.ibm.cicsdev.vsam.ksds.KsdsExampleCommon;

/**
 * Simple example to demonstrate adding a record to a VSAM KSDS file using JCICS.
 * 
 * This class is just the driver of the test. The main JCICS work is done in the
 * superclass {@link KsdsExampleCommon}.
 */
public class EsdsExample1
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
        task.out.println(" - Starting KsdsExample1");

        // Create a reference to the file
        KSDS file = new KSDS();        
        file.setName(FILE_NAME);

        // Create a new instance of the common KSDS class
        KsdsExampleCommon ex = new KsdsExampleCommon(file);
        
        // Create a new random record for writing to the file        
        StockPart sp = StockPartHelper.generate();
        
        // Add a new record to the file
        ex.addRecord(sp);
        
        // Write out the part ID
        String strMsg = "Wrote record with key {0}.";
        task.out.println( MessageFormat.format(strMsg, sp.getPartId()) );
        
        // Unit of work containing the write will be committed at normal end of task
        
        // Completion message
        task.out.println("Completed KsdsExample1");
    }
}
