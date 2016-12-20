package com.ibm.cicsdev.vsam.esds;

import com.ibm.cics.server.Task;


/**
 * This is a dummy test class to remain consistent with the KSDS and RRDS examples.
 * 
 * Records cannot be deleted from an ESDS dataset, either in JCICS or in the
 * EXEC CICS API.
 */
public class EsdsExample4 
{
    public static void main(String[] args)
    {
        // Get details about our current CICS task
        Task task = Task.getTask();
        task.out.println(" - Starting EsdsExample4");
        task.out.println("VSAM ESDS record delete example");
        
        // VSAM ESDS does not allow record deletion
        task.out.println("Record deletion not permitted for VSAM ESDS files");
        
        // Completion message
        task.out.println("Completed EsdsExample4");
    }
}
