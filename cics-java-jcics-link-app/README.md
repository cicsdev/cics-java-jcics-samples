cics-java-jcics-link
===

Provides examples for performing CICS LINK operations using both COMMAREAs, and channels and containers.

The following Java source code files are provided:

* `LinkProg1` - a class that demonstrates linking to the COBOL application `EC01` using a COMMAREA.
* `LinkProg2` - a class that demonstrates linking to the COBOL application `EDUPGM` using a COMMAREA mapped using a JZOS generated record.
* `LinkProg3` - a class that demonstrates linking to COBOL application `EDUCHAN` using channels and containers.  
* `LinkProgCommon` - superclass used to provide common services for the `LinkProg` samples.  
* `LinkServerEduchan` - A Java version of the `EDUCHAN` COBOL program used as the back-end to `LinkProg3`.     
* `LinkServerEC01` - A Java version of the `EC01` COBOL program used as the back-end to `LinkProg1`.     


## Supporting files

* [`/etc/Link`](../etc/Link) - DFHCSDUP input files needed to define the required programs and transactions.
* [`/src/Cobol`](../etc/src/Cobol) - COBOL programs `EC01` and `EDUCHAN` required by these samples.
* `cics-java-jcics-edupgm-1.0.jar` - a pre-built JAR containing the generated class `JZOSCommareaWrapperclass` that is used in the `LinkProg2` example to map the fields in the COMMAREA used by the COBOL program `EDUPGM`. This is packaged in a Maven [`local-repo`](../local-repo/com/ibm/cicsdev/cics-java-jcics-edupgm/) and the source is available in the [cics-java-jzosprog](https://github.com/cicsdev/cics-java-jzosprog/tree/main/src/Cobol) repository.




## Configuring CICS

1. Compile and deploy the COBOL programs `EC01`, `EDUPGM`, and `EDUCHAN`.
1. Define the resource definitions for the Transaction IDs and Java programs using the DFHCSDUP source in `etc/Link/DFHCSD.txt`
1. Optionally if you wish to replace the EDUCHAN and EC01 COBOL programs with the Java implementations then use the commented out information supplied in et`DFHCSD.txt`
1. If program autoinstall is not enabledd define PROGRAM definitions for the COBOL programs.


## Running the Example

At a 3270 terminal screen, enter the transaction you wish to run, for example JLN1 will run the LINK example 1. 

>    JLN1

and the following output will be returned 

    JLN1 - Starting LinkProg1
    Returned from link to EC01 with 19/08/16 09:17:01 
