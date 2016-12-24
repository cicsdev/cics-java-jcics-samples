projects
===

Complete Eclipse projects demonstrating how to use the JCICS API in an OSGi JVM server environment.


## Overall project structure

* `com.ibm.cicsdev.*` - Eclipse project containing the Java source code.
* `com.ibm.cicsdev.*.cicsbundle` - Eclipse project for a CICS bundle that can be deployed into a CICS region.
* `com.ibm.cicsdev.*.resources.cicsbundle` - Eclipse project for a CICS bundle that defines the required program and transaction resources for the sample.


## Samples overview

* [`com.ibm.cicsdev.link`](com.ibm.cicsdev.link) - Performing CICS LINK operations using both commareas, and channels and containers.
* [`com.ibm.cicsdev.serialize`](com.ibm.cicsdev.serialize) - Serializing access to a common resource using the CICS ENQ and DEQ mechanism.
* [`com.ibm.cicsdev.tdq`](com.ibm.cicsdev.tdq) - Accessing transient data queues.
* [`com.ibm.cicsdev.terminal`](com.ibm.cicsdev.terminal) - Reading in data from a terminal for an OSGi application.
* [`com.ibm.cicsdev.tsq`](com.ibm.cicsdev.tsq) - Accessing temporary storage queues.
* [`com.ibm.cicsdev.vsam`](com.ibm.cicsdev.vsam) - Accessing KSDS, ESDS, and RRDS VSAM files.


## Adding the resources to Eclipse

1. Using an Eclipse development environment import the project from the project folder using the menu File -> Import -> Existing Projects into Workspace.
1. Define and set a CICS Target Platform for the workspace using the menu Window -> Preferences -> Target Platform.



