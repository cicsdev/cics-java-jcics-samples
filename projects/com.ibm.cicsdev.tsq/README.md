com.ibm.cicsdev.tsq
===

This repository contains various Java sample programs to demonstrate use of the JCICS API for manipulating
a temporary storage queue (TSQ).

A TSQ is a set of data items that can be read and reread in any sequence. TSQ resources can be dynamically
created at runtime, unlike transient data queues that must be defined in advance.

All of the examples in this repository use the sample queue named `MYTSQ`.

* `TSQExample1` - a simple class that writes Java strings to a TSQ and then reads them back.
* `TSQExample2` - an alternative implementation of `TSQExample1`, that writes and reads Java strings, but performs all
of the byte array to string conversion manually. This avoids the pre-requisite of CICS TS V5.1, but is slightly more work to code in Java.
* `TSQExample3` - a more complex example that uses a JZOS generated record to write a structured record to a TSQ and then read it back.
* `TSQExample4` - an extension of the `TSQExample3` sample program, which populates a TSQ, updates items within the queue, then
reads them back to confirm.
* `TSQCommon` - superclass used to provide common services for the TSQ samples. The constructor of this class will
delete any existing TSQ before executing the test, in order to avoid multiple executions of the program from producing
incorrect results.  

For the sake of brevity, all of the examples lack comprehensive error-handling logic.
To demonstrate exactly where an exception may be thrown when using the JCICS API,
one `try { ... } catch { ... }` block has been included per API call that may throw an exception.


## Supporting files

* `lib/TsqRecord.jar` - a pre-built JAR containing the JZOS generated record that maps the copybook structure used in `TSQExample3` (includes source). 
* [`/etc/TSQ`](../../etc/TSQ) - contains the output of a DFHCSDUP EXTRACT operation needed to define the required programs and transactions.
    

## Structured record

The COBOL structure that is mapped by the TsqRecord class is as follows:

        01 TSQ-RECORD.                           
           03 RECORD-ID        PIC 9(8)   COMP.  
           03 BINARY-DIGIT     PIC 9(4)   COMP.  
           03 CHARACTER-STRING PIC X(30).        
           03 NUMERIC-VALUE    PIC 9(18).        
           03 PACKED-DEC       PIC 9(15)  COMP-3.
           03 SIGNED-PACKED    PIC S9(12) COMP-3.


## Running the Example

At a 3270 terminal screen, enter the transaction you wish to run, for example JTS1 will run TSQ example 1. 

    JTS1

and the following output will be returned 

    JTS1 - Starting TSQExample1                        
    Read data from queue "TSQ write from JCICS item 1" 
    Read data from queue "TSQ write from JCICS item 2" 
    Read data from queue "TSQ write from JCICS item 3" 
    Read data from queue "TSQ write from JCICS item 4" 
    Read data from queue "TSQ write from JCICS item 5" 
    Completed TSQExample1                                          

