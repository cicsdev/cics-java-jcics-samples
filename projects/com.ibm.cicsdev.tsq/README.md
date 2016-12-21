com.ibm.cicsdev.tsq
===

Provides examples for using VSAM files from a JCICS environment.
All examples have a ESDS, KSDS, and RRDS variant.
The numbered example classes are driving classes, with the ExampleCommon classes performing the JCICS calls.

* `TSQExample1` - a simple class that writes Java strings to a TSQ and then reads them back.
* `TSQExample2` - an alternative implementation of `TSQExample1`, that writes and reads Java strings, but performs all of the byte array to string conversion manually. This avoids the pre-requisite of CICS TS V5.1, but is slightly more work to code in Java.
* `TSQExample3` - a more complex example that uses a JZOS generated record to write a structured record to a TSQ and then read it back.
* `TSQExample4` - an extension of the `TSQExample3` sample program, which populates a TSQ, updates items within the queue, then reads them back to confirm.
* `TSQCommon` - Super class used to provide common services for the TSQ samples. 


## Supporting files

* `lib/TsqRecord.jar` - a pre-built JAR containing the JZOS generated record that maps the copybook structure used in `TSQExample3` (includes source). 
* [`/etc/TSQ`](../../etc/TSQ) - contains sample JCL to define the required VSAM files, and the output of a DFHCSDUP EXTRACT operation needed to define the required programs and transactions.
    

## Running the Example

At a 3270 terminal screen, enter the transaction you wish to run, for example JVK1 will run TSQ example 1. 

    JTS1

and the following output will be returned 

    JTS1 - Starting TSQExample1                        
    Read data from queue "TSQ write from JCICS item 1" 
    Read data from queue "TSQ write from JCICS item 2" 
    Read data from queue "TSQ write from JCICS item 3" 
    Read data from queue "TSQ write from JCICS item 4" 
    Read data from queue "TSQ write from JCICS item 5" 
    Completed TSQExample1                                          

