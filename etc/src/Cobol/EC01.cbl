
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
      * Module Name        EC01.CBL                                    *
      *                                                                *
      * CICS LINK back-end COMMAREA sample                             *
      *                                                                *
      * This program expects to be invoked with a COMMARAEA            *
      * and returns the date and time. Note sample orginally supplied  *
      * with IBM CICS Transaction Gateway.                             *
      ******************************************************************


       IDENTIFICATION DIVISION.
       PROGRAM-ID. EC01.

       ENVIRONMENT DIVISION.
       DATA DIVISION.
       WORKING-STORAGE SECTION.
      *****************************************************************
      * WORKING STORAGE STARTS HERE                                   *
      *****************************************************************
       01  FILLER                            PIC X(32)  VALUE
           '** WORKING STORAGE STARTS HERE**'.

      *****************************************************************
      * THIS WS AREA CONTAINS ALL WORKING VARIABLES.                  *
      *****************************************************************
       01  FILLER                           PIC X(8)   VALUE 'WS-'.
       01  WS-DEBUG-AREA.
           05  WS-RAWTIME                  PIC S9(15) COMP-3.
           05  WS-DATE-DEBUG-AREA          PIC X(8).
           05  WS-TIME-DEBUG-AREA          PIC X(8).
           05  WS-EIBRESP-DISP             PIC S9(9)
               SIGN LEADING SEPARATE.
           05  WS-CICS-RESP                OCCURS 2 TIMES
                                           PIC X(10).
           05  WS-DEBUG-ON-FLAG             PIC X VALUE 'Y'.
               88  DEBUG-ON                 VALUE 'Y'.


      *****************
       LINKAGE SECTION.
      *****************
       01  DFHCOMMAREA.
           05  LK-DATE-OUT      PIC X(8).
           05  LK-SPACE-OUT     PIC X(1).
           05  LK-TIME-OUT      PIC X(8).
           05  LK-LOWVAL-OUT    PIC X(1).

      ********************
       PROCEDURE DIVISION.
      ********************
       A-CONTROL SECTION.

           IF DEBUG-ON
              EXEC CICS HANDLE CONDITION ERROR (ZZX-CICS-ERROR-ROUTINE)
                         END-EXEC
           END-IF.

           IF EIBCALEN < LENGTH OF DFHCOMMAREA
           THEN
               PERFORM ZZX-CICS-ERROR-ROUTINE
           END-IF.

           MOVE SPACES TO DFHCOMMAREA.

           EXEC CICS
               ASKTIME ABSTIME(WS-RAWTIME)
           END-EXEC.

           EXEC CICS
               FORMATTIME ABSTIME(WS-RAWTIME)
                          DDMMYY(LK-DATE-OUT)
                          DATESEP('/')
                          TIME(LK-TIME-OUT)
                          TIMESEP(':')
           END-EXEC.
           MOVE LOW-VALUES  TO LK-LOWVAL-OUT.

           MOVE LK-DATE-OUT TO WS-DATE-DEBUG-AREA.
           MOVE LK-TIME-OUT TO WS-TIME-DEBUG-AREA.

           EXEC CICS RETURN END-EXEC.

           GOBACK.

      ********************************
       ZZX-CICS-ERROR-ROUTINE SECTION.
      ********************************

           IF EIBCALEN < LENGTH OF DFHCOMMAREA
           THEN
             EXEC CICS
                 ABEND
                 ABCODE('ECOM')
             END-EXEC
           ELSE
             EXEC CICS
                 ABEND
                 ABCODE('ERRO')
             END-EXEC
           END-IF.
           GOBACK.

       ZZX-EXIT.
           EXIT.
           EJECT
      *END OF PROGRAM
