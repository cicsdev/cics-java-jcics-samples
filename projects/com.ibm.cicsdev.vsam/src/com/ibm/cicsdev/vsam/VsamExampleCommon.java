package com.ibm.cicsdev.vsam;

import com.ibm.cics.server.InvalidRequestException;
import com.ibm.cics.server.RolledBackException;
import com.ibm.cics.server.Task;


/**
 * An abstract class that provides services common across all of the VSAM
 * sample classes.
 */
public abstract class VsamExampleCommon
{
    /**
     * Example of committing a unit of work within an OSGi JVM server.
     * 
     * When performing a commit in Java, the following two exceptions may be thrown:
     * 
     * <ul>
     * <li><code>RolledBackException</code> - CICS was unable to commit the current
     * UoW because a remote system was unable to commit.</li>
     * 
     * <li><code>InvalidRequestException</code> - This can happen in Java on a commit
     * for one of two reasons:
     * <ol>
     * <li>Task.commit() was called in a program that is linked to from a remote system
     * that has not specified the SYNCONRETURN option, or if it has been linked to
     * locally and is defined with EXECUTIONSET=DPLSUBSET.</li>
     * <li>Task.commit() was called in a Java environment where we also have a JTA
     * transaction active. (This is not an issue in an OSGi JVM server).</li>
     * </ol></li>
     * 
     * </ul>
     */
    public void commitUnitOfWork()
    {
        try {
            // Issue a CICS syncpoint
            Task.getTask().commit();
        }
        catch (RolledBackException rbe) {
            // See javadoc for description 
            // For this example, propagate the error out of the Java program
            throw new RuntimeException(rbe);
        }
        catch (InvalidRequestException ire) {
            // See javadoc for description 
            // For this example, propagate the error out of the Java program
            throw new RuntimeException(ire);
        }        
    }
}
