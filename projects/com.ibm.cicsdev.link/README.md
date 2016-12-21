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

    
    
    

### Project com.ibm.cicsdev.link

* LinkProg1 - A class that demonstrates linking to the COBOL application EC01 using a COMMAREA
* LinkProg2 - A class that demonstrates linking to the COBOL application EDUPGM using a COMMAREA mapped using a JZOS generated record
* LinkProg3 - A class that demonstrates linking to COBOL application EDUCHAN using channels and containers  
* LinkProgCommon - Super class used to provide common service for the LinkProg samples  
* LinkServerEduchan - A Java version of the EDUCHAN COBOL program used as the back-end to LinkProg3     



### Running the Example


At a 3270 terminal screen, enter the transaction you wish to run, for example JOL1 will run the Link example 1. 

    JOL1

and the following output will be returned 

    JOL1 - Starting LinkProg1                         
    Returned from link to EC01 with 19/08/16 09:17:01 




1. If using the LINK sample compile and deploy the COBOL programs EC01 and EDUCHAN, and either define PROGRAM definitions or enable program autoinstall.  

1. Optionally add a CICS Java program definiton for LinkServerEduchan called EDUCHAN if you wish to replace the EDUCHAN COBOL sample with the Java implemenation. 




### Project com.ibm.cicsdev.link

* LinkProg1 - A class that demonstrates linking to the COBOL application EC01 using a COMMAREA
* LinkProg2 - A class that demonstrates linking to the COBOL application EDUPGM using a COMMAREA mapped using a JZOS generated record
* LinkProg3 - A class that demonstrates linking to COBOL application EDUCHAN using channels and containers  
* LinkProgCommon - Super class used to provide common service for the LinkProg samples  
* LinkServerEduchan - A Java version of the EDUCHAN COBOL program used as the back-end to LinkProg3     


