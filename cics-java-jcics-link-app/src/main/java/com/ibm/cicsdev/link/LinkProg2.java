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

import java.text.MessageFormat;

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.Program;
import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.JZOSCommareaWrapper;

/**
 * Provides a simple example of LINKing to a CICS program using JCICS,
 * passing a byte array for a COMMAREA. In this example, the COMMAREA
 * byte array is constructed using a JZOS generated class.
 * 
 * This class executes in an OSGi JVM server environment.
 */
public class LinkProg2 extends LinkProgCommon
{
    /**
     * Name of the COBOL program to be invoked.
     */
    private static final String PROG_NAME = "EDUPGM";

    /**
     * Constructor used to pass data to superclass constructor.
     * 
     * @param prog - the program reference we will be manipulating in this example.
     */
    private LinkProg2(Program prog)
    {
        super(prog);
    }

    /**
     * Main entry point to a CICS OSGi program.
     * This can be called via a LINK or a 3270 attach.
     * 
     * The fully qualified name of this class should be added to the CICS-MainClass 
     * entry in the parent OSGi bundle's manifest.
     */
    public static void main(String[] args)
    {
        // Get details about our current CICS task
        Task task = Task.getTask();
        task.out.println(" - Starting LinkProg2");

        // Create a reference to the Program we will invoke
        Program prog = new Program();

        // Specify the properties on the program    
        prog.setName(PROG_NAME);
        
        // Don't syncpoint between remote links, this is the default 
        // Setting true ensures each linked program runs in its own UOW and
        // allows the a remote server program to use a syncpoint command
        prog.setSyncOnReturn(false); 

        // Create a new instance of the class        
        LinkProg2 lp = new LinkProg2(prog);

        // build commarea byte array using JZOS record   
        JZOSCommareaWrapper cw = lp.buildCommarea();

        // Invoke the LINK to CICS program converting the record to a byte array
        lp.linkProg(cw.getByteBuffer());

        // Get output data from commarea
        // Use the getters from the commarea record to access the output fields 
        // in the returned commarea
        String resultStr = cw.getResultText();
        Integer resultCode = cw.getResultCode();

        // Completion message          
        String msg = MessageFormat.format("Returned from link to {0} with rc({1}) {2}",
                prog.getName(), resultCode, resultStr);
        task.out.println(msg);
    }


    /**
     * Link to the CICS COBOL program and catch any errors from CICS.
     * 
     * @param commarea - byte array as input and output commarea
     */ 
    private void linkProg(byte[] commarea) {

        try {
            // LINK to the CICS program
            this.prog.link(commarea);
        }
        catch (CicsConditionException cce) {
            // Crude error handling to keep logic simple
            throw new RuntimeException(cce);
        }
    }

    /**
     * Build the commarea using the supplied JZOS wrapper
     * and set the input fields as required.
     * 
     * @return JZOS COMMAREA record for EDUPGM copybook 
     */ 
    private JZOSCommareaWrapper buildCommarea() {

        // Create a new instance of the generated class
        JZOSCommareaWrapper cw = new JZOSCommareaWrapper();
        cw.setBinaryDigit(1);
        cw.setCharacterString("hello");
        cw.setNumericString(1234);
        cw.setPackedDigit(123456789);
        cw.setSignedPacked(-100);
        cw.setBool("1");
        
        // Return the constructed object
        return cw;
    }
}
