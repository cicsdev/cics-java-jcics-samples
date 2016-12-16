//DEFVSAM  JOB MSGLEVEL=(1,1),MSGCLASS=A,REGION=4M,MEMLIMIT=0M,
//          NOTIFY=&SYSUID.                                    
//*                                                            
//DEFINE   EXEC PGM=IDCAMS                                     
//SYSPRINT DD SYSOUT=*                                         
//SYSIN    DD *                                                
  DEFINE CLUSTER ( -                                           
    NAME ( IBURNET.JAVAED.TEST.DATA.STOCK ) -                  
    RECORDS ( 100 10 ) -                                       
    INDEXED -                                                  
    KEYS ( 8 0 ) -                                             
    RECORDSIZE ( 80 80 ) -                                     
  )                                                            
/*                                                             
//                                                             