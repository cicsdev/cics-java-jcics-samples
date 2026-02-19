/* Licensed Materials - Property of IBM                                   */
/*                                                                        */
/* SAMPLE                                                                 */
/*                                                                        */
/* (c) Copyright IBM Corp. 2017 All Rights Reserved                       */
/*                                                                        */
/* US Government Users Restricted Rights - Use, duplication or disclosure */
/* restricted by GSA ADP Schedule Contract with IBM Corp                  */
/*                                                                        */

package com.ibm.cicsdev.link;

import com.ibm.cics.server.Program;

/**
 * Superclass used to provide common services used in all of the examples.  
 * 
 * For the sake of brevity, this example does not include comprehensive
 * error-handling logic. See the CICS Java samples around error-handling
 * for more details on catching CICS Exceptions in Java.
 */
public abstract class LinkProgCommon
{
    /**
     * A field to hold a reference to the CICS program
     * this instance will access. 
     */
    protected final Program prog;
    
    /**
     * Constructor used to initialise this class with some common data
     * 
     * @param tsq - a reference to the temporary storage queue we will
     * be manipulating in this example.
     */
    protected LinkProgCommon(Program p)
    {
        // Save reference to the CICS program
        this.prog = p;
    }
}
