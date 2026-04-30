/* Licensed Materials - Property of IBM                                   */
/*                                                                        */
/* SAMPLE                                                                 */
/*                                                                        */
/* (c) Copyright IBM Corp. 2016 All Rights Reserved                       */
/*                                                                        */
/* US Government Users Restricted Rights - Use, duplication or disclosure */
/* restricted by GSA ADP Schedule Contract with IBM Corp                  */
/*                                                                        */

package com.ibm.cicsdev.link;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.InvalidRequestException;
import com.ibm.cics.server.Program;
import com.ibm.cics.server.Task;

/**
 * Provides a simple example of LINKing to a CICS program using JCICS,
 * passing a byte array for a COMMAREA.
 * 
 * This class executes in an OSGi JVM server environment.
 */
public class LinkProg1 extends LinkProgCommon
{
    /**
     * Name of the COBOL program to be invoked.
     */
    private static final String PROG_NAME = "EC01";
    
    /**
     * Length of commarea returned by program EC01.
     */
    private static final int CA_LEN = 18;
    
    /**
     * CCSID of the CICS region.
     */
    private static final String CCSID = System.getProperty("com.ibm.cics.jvmserver.local.ccsid");

    /**
     * Constructor used to pass data to superclass constructor.
     * 
     * @param prog - the program reference we will be manipulating in this example.
     */
    private LinkProg1(Program prog)
    {
        super(prog);
    }

    /**
     * Main entry point to a CICS OSGi program.
     * This can be called via a LINK or a 3270 attach.
     * 
     * The fully-qualified name of this class should be added to the
     * CICS-MainClass entry in the parent OSGi bundle's manifest.
     */
    public static void main(String[] args)
    {
        // Get details about our current CICS task
        Task task = Task.getTask();
        task.out.println(" - Starting LinkProg1");

        // Create a reference to the Program we will invoke
        Program prog = new Program();

        // Specify the properties on the program
        prog.setName(PROG_NAME);
        
        // Don't syncpoint between remote links, this is the default 
        // Setting true ensures each linked program runs in its own UOW and
        // allows the a remote server program to use a syncpoint command
        prog.setSyncOnReturn(false);  

        // Create a new instance of the class        
        LinkProg1 lp = new LinkProg1(prog);

        // Init commarea and invoke the LINK to CICS program
        // Commarea byte array should be as long as the DFHCOMMAREA structure in COBOL
        // Commarea will be padded with nulls which ensures CICS can null truncate DPL flows
        byte[] ca = new byte[CA_LEN];
        lp.linkProg(ca);

        // Build output string from commarea assuming returned data encoded in CICS local EBCDIC ccsid
        String resultStr;
        try {
            resultStr = new String(ca,CCSID);
        } catch (UnsupportedEncodingException ue) {
            throw new RuntimeException(ue);     
        } 

        // Completion message  
        String msg = MessageFormat.format("Returned from link to {0} with {1}", prog.getName(), resultStr);
        task.out.println(msg);
    }


    /**
     * Link to the CICS COBOL program catching any errors from CICS
     * The invoked CICS progra will retrun the date and time
     * 
     * @param ca - byte array for input and output COMMAREA.
     */ 
    private void linkProg(byte[] ca) {

        try {
            // Execute the LINK to the CICS program 
            // The COMMAREA byte array is updated after the call and does not need to be returned
            this.prog.link(ca);
        }
        catch (InvalidRequestException ire) {
            // Ignore invalid request and just log
            Task.getTask().out.println("Invalid request on link - INVREQ");
        }
        catch (CicsConditionException cce) {
            throw new RuntimeException(cce);
        }    
    }
}
