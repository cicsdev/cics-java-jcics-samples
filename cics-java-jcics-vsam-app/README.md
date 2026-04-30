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
* [`/etc/VSAM`](../../etc/VSAM) - contains sample JCL to define the required VSAM files, and the output of a DFHCSDUP EXTRACT operation needed to define the required programs and transactions.
    

## Running the Example

At a 3270 terminal screen, enter the transaction you wish to run, for example JVK1 will run the VSAM KSDS example 1. 

    JVK1

and the following output will be returned 

    JVK1 - Starting KsdsExample1      
    VSAM KSDS record addition example 
    Wrote record with key 0x0003E712  
    Completed KsdsExample1            
