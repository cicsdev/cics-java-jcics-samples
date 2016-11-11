      *----------------------------------------------------------------*
      *  Licensed Materials - Property of IBM                          *
      *  SAMPLE                                                        *
      *  (c) Copyright IBM Corp. 2016 All Rights Reserved              *
      *  US Government Users Restricted Rights - Use, duplication or   *
      *  disclosure restricted by GSA ADP Schedule Contract with       *
      *  IBM Corp                                                      *
      *----------------------------------------------------------------*

      ******************************************************************
      *                                                                *
      * Module Name        EDUCHAN.CBL                                 *
      *                                                                *
      * CICS back-end channel/container sample                         *
      *                                                                *
      * This program expects to be invoked with a CHAR container named *
      * INPUTDATA and returns the following containers:                *
      * A CHAR containing containing the reversed input string         *
      * A CHAR container containing the time                           *
      * A BIT container containing the CICS return code from reading   *
      * the input container                                            *
      ******************************************************************

       IDENTIFICATION DIVISION.
       PROGRAM-ID. EDUCHAN.

       ENVIRONMENT DIVISION.
       CONFIGURATION SECTION.
       DATA DIVISION.
       WORKING-STORAGE SECTION.

      *  Container name declarations
      *  Channel and container names are case sensitive
       01 DATE-CONT          PIC X(16) VALUE 'CICSTIME'.
       01 INPUT-CONT         PIC X(16) VALUE 'INPUTDATA'.
       01 OUTPUT-CONT        PIC X(16) VALUE 'OUTPUTDATA'.
       01 LENGTH-CONT        PIC X(16) VALUE 'INPUTDATALENGTH'.
       01 ERROR-CONT         PIC X(16) VALUE 'ERRORDATA'.
       01 RESP-CONT          PIC X(16) VALUE 'CICSRC'.


      *  Data fields used by the program
       01 INPUTLENGTH        PIC S9(8) COMP-4.
       01 DATALENGTH         PIC S9(8) COMP-4.
       01 CURRENTTIME        PIC S9(15) COMP-3.
       01 ABENDCODE          PIC X(4) VALUE SPACES.
       01 CHANNELNAME        PIC X(16) VALUE SPACES.
       01 INPUTSTRING        PIC X(72) VALUE SPACES.
       01 OUTPUTSTRING       PIC X(72) VALUE SPACES.
       01 RESPCODE           PIC S9(8) COMP-4 VALUE 0.
       01 RESPCODE2          PIC S9(8) COMP-4 VALUE 0.
       01 DATE-TIME.
         03 DATESTRING         PIC X(10) VALUE SPACES.
         03 TIME-SEP           PIC X(1) VALUE SPACES.
         03 TIMESTRING         PIC X(8) VALUE SPACES.
       01 RC-RECORD          PIC S9(8) COMP-4 VALUE 0.
       01 ERR-RECORD.
         03 ERRORCMD           PIC X(16) VALUE SPACES.
         03 ERRORSTRING        PIC X(32) VALUE SPACES.


       PROCEDURE DIVISION.
      *  -----------------------------------------------------------
       MAIN-PROCESSING SECTION.
      *  -----------------------------------------------------------

      *  Get name of channel
           EXEC CICS ASSIGN CHANNEL(CHANNELNAME)
                            END-EXEC.

      *  If no channel passed in, terminate with abend code NOCH
           IF CHANNELNAME = SPACES THEN
               MOVE 'NOCH' TO ABENDCODE
               PERFORM ABEND-ROUTINE
           END-IF.


      *  Read content and length of input container
           MOVE LENGTH OF INPUTSTRING TO INPUTLENGTH.
           EXEC CICS GET CONTAINER(INPUT-CONT)
                            CHANNEL(CHANNELNAME)
                            FLENGTH(INPUTLENGTH)
                            INTO(INPUTSTRING)
                            RESP(RESPCODE)
                            RESP2(RESPCODE2)
                            END-EXEC.

      *  Place RC in binary container for return to caller
           MOVE RESPCODE TO RC-RECORD.
           EXEC CICS PUT CONTAINER(RESP-CONT)
                            FROM(RC-RECORD)
                            FLENGTH(LENGTH OF RC-RECORD)
                            BIT
                            RESP(RESPCODE)
                            END-EXEC.

           IF RESPCODE NOT = DFHRESP(NORMAL)
             PERFORM RESP-ERROR
           END-IF.

      *  Place reversed string in output container
           MOVE FUNCTION REVERSE(INPUTSTRING) TO OUTPUTSTRING.

           EXEC CICS PUT CONTAINER(OUTPUT-CONT)
                            FROM(OUTPUTSTRING)
                            FLENGTH(LENGTH OF OUTPUTSTRING)
                            CHAR
                            RESP(RESPCODE)
                            END-EXEC.

           IF RESPCODE NOT = DFHRESP(NORMAL)
             PERFORM RESP-ERROR
           END-IF.

      *  Get the current time
           EXEC CICS ASKTIME ABSTIME(CURRENTTIME)
                            END-EXEC.

      *  Format date and time
           EXEC CICS FORMATTIME
                     ABSTIME(CURRENTTIME)
                     DDMMYYYY(DATESTRING)
                     DATESEP('/')
                     TIME(TIMESTRING)
                     TIMESEP(':')
                     RESP(RESPCODE)
                     END-EXEC.

      *  Check return code
           IF RESPCODE NOT = DFHRESP(NORMAL)
               STRING 'Failed' DELIMITED BY SIZE
                            INTO DATESTRING END-STRING
           END-IF.

      *  Place current date in container CICSTIME
           EXEC CICS PUT CONTAINER(DATE-CONT)
                            FROM(DATE-TIME)
                            FLENGTH(LENGTH OF DATE-TIME)
                            CHAR
                            RESP(RESPCODE)
                            END-EXEC.
      *  Check return code
           IF RESPCODE NOT = DFHRESP(NORMAL)
             PERFORM RESP-ERROR
           END-IF.



      *  Return back to caller
           PERFORM END-PGM.

      *  -----------------------------------------------------------
       RESP-ERROR.
             MOVE 'EDUC' TO ABENDCODE
             PERFORM ABEND-ROUTINE.

           PERFORM END-PGM.

      *  -----------------------------------------------------------
      *  Abnormal end
      *  -----------------------------------------------------------
       ABEND-ROUTINE.
           EXEC CICS ABEND ABCODE(ABENDCODE) END-EXEC.

      *  -----------------------------------------------------------
      *  Finish
      *  -----------------------------------------------------------
       END-PGM.
           EXEC CICS RETURN END-EXEC.

