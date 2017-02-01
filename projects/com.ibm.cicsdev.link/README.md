com.ibm.cicsdev.link
===

Provides examples for performing CICS LINK operations using both commareas, and channels and containers.

* `LinkProg1` - a class that demonstrates linking to the COBOL application `EC01` using a COMMAREA.
* `LinkProg2` - a class that demonstrates linking to the COBOL application `EDUPGM` using a COMMAREA mapped using a JZOS generated record.
* `LinkProg3` - a class that demonstrates linking to COBOL application `EDUCHAN` using channels and containers.  
* `LinkProgCommon` - superclass used to provide common services for the `LinkProg` samples.  
* `LinkServerEduchan` - A Java version of the `EDUCHAN` COBOL program used as the back-end to `LinkProg3`.     
* `LinkServerEC01` - A Java version of the `EC01` COBOL program used as the back-end to `LinkProg1`.     


## Supporting files

* `lib/EDUPGM.jar` - a pre-built JAR containing the JZOS generated record that maps the copybook structure used in the `EDUPGM` COBOL sample. The generated class `JZOSCommareaWrapperclass` is used in the `LinkProg2` example to map the fields in the COMMAREA used by the COBOL program `EDUPGM`. 
* [`/etc/Link`](../../etc/Link) - contains the output of a DFHCSDUP EXTRACT operation needed to define the required programs and transactions.
* [`/src/Cobol`](../../src/Cobol) - contains the COBOL programs `EC01` and `EDUCHAN` required by these samples.
* [cics-java-jzosprog repo](https://github.com/cicsdev/cics-java-jzosprog/tree/master/src/Cobol) - repository containing the `EDUPGM` COBOL program.


## Configuration

1. Compile and deploy the COBOL programs `EC01`, `EDUPGM`, and `EDUCHAN`.
1. Define appropriate PROGRAM definitions, or enable program autoinstall.  
1. Optionally add a CICS Java program definiton for LinkServerEduchan called EDUCHAN if you wish to replace the EDUCHAN COBOL sample with the Java implemenation. 
1. Optionally add a CICS Java program definiton for LinkServerEC01 called EV01 if you wish to replace the EC01 COBOL sample with the Java implemenation. 


## Running the Example

At a 3270 terminal screen, enter the transaction you wish to run, for example JLN1 will run the LINK example 1. 

    JLN1

and the following output will be returned 

    JLN1 - Starting LinkProg1
    Returned from link to EC01 with 19/08/16 09:17:01 

