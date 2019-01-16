projects
===

Complete Eclipse projects demonstrating how to use the JCICS API in an OSGi JVM server environment.


## Overall project structure

* `com.ibm.cicsdev.*` - Eclipse project containing the Java source code.
* `com.ibm.cicsdev.*.cicsbundle` - Eclipse project for a CICS bundle that can be deployed into a CICS region.
* `com.ibm.cicsdev.*.resources.cicsbundle` - Eclipse project for a CICS bundle that defines the required
program and transaction resources for the sample.


## Samples overview

* [`com.ibm.cicsdev.link`](com.ibm.cicsdev.link) - Performing CICS LINK operations using both commareas,
and channels and containers.
* [`com.ibm.cicsdev.serialize`](com.ibm.cicsdev.serialize) - Serializing access to a common resource using
the CICS ENQ and DEQ mechanism.
* [`com.ibm.cicsdev.tdq`](com.ibm.cicsdev.tdq) - Accessing transient data queues.
* [`com.ibm.cicsdev.terminal`](com.ibm.cicsdev.terminal) - Reading in data from a terminal for an OSGi application.
* [`com.ibm.cicsdev.tsq`](com.ibm.cicsdev.tsq) - Accessing temporary storage queues.
* [`com.ibm.cicsdev.vsam`](com.ibm.cicsdev.vsam) - Accessing KSDS, ESDS, and RRDS VSAM files.


## Adding the resources to Eclipse

1. Using an Eclipse development environment import the project from the project folder using the menu
**File** -> **Import** -> **Existing Projects into Workspace**.
1. Define and set a CICS Target Platform for the workspace using the menu **Window** -> **Preferences** ->
**Target Platform**.


## Generated resources


The TS, TD, VSAM and LINK sample projects manipulate byte-oriented record structures using Java classes
generated using the IBM Record Generator for Java. The generated classes can be found packaged in a JAR
file, found in the `lib/` subdirectory of the relevant project.  Only the compiled helper classes are
required to compile and run the application, however the generated source files are included for reference.

If you are manually adding source files to your Eclipse development environment, you will need to add the
generated JAR file to the build path in order to compile the samples, using the project context menu
**Build Path** -> **Configure Build Path** -> **Libraries**.
