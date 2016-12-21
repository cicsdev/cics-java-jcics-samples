com.ibm.cicsdev.vsam
===

Provides examples for using VSAM files from a JCICS environment.
All examples have a ESDS, KSDS, and RRDS variant.
The numbered example classes are driving classes, with the ExampleCommon classes performing the JCICS calls.

* `*Example1` - Writes a single record to a file.
* `*Example2` - Reads a single record from a file.
* `*Example3` - Updates a single record in a file.
* `*Example4` - Deletes a single record in a file (not ESDS).
* `*Example5` - Browses a VSAM file.
* `*ExampleCommon` - Various routines that perform the required JCICS calls.

## Supporting files

* `lib/vsam.jar` - a pre-built JAR containing the JZOS generated record that maps the copybook structure used in all the VSAM samples (includes source). 
* `/etc/VSAM` - contains sample JCL to define the required VSAM files, and the output of a DFHCSDUP EXTRACT operation needed to define the required programs and transactions.
    

## Running the Example

At a 3270 terminal screen, enter the transaction you wish to run, for example JVK1 will run the VSAM KSDS example 1. 

    JVK1

and the following output will be returned 

    JVK1 - Starting KsdsExample1      
    VSAM KSDS record addition example 
    Wrote record with key 0x0003E712  
    Completed KsdsExample1            

    
    
### Project com.ibm.cicsdev.tsq

* TSQExample1 - A simple class that writes Java strings to a TSQ and then reads them back.
* TSQExample2 - An alternative implementation of TSQExample1 that writes and reads Java strings, but performs all of the byte array to string conversion manually. This avoids the pre-req of CICS TS V5.1, but is slightly more
work to code in Java.
* TSQExample3 - A more complex example that uses a JZOS generated record to write a structured record to a TSQ and then read it back.
* TSQExample4 - An extension of the TSQExample3 sample program, which populates a TSQ, updates items within the queue, then reads them back to confirm.
* TSQCommon - Super class used to provide common services for the TSQ samples 



* TsqRecord.jar - A pre-built JAR containing the JZOS generated record that maps the copybook structure used in the TSQExample3.
