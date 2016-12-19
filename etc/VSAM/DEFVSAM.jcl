//DEFVSAM  JOB MSGLEVEL=(1,1),MSGCLASS=A,REGION=4M,MEMLIMIT=0M,
//          NOTIFY=&SYSUID.                                    
//*
//DEFINE   EXEC PGM=IDCAMS                                     
//SYSPRINT DD SYSOUT=*                                         
//SYSIN    DD *                                                
                                                     
  DELETE JAVAED.TEST.KSDS.STOCK CLUSTER PURGE
  DELETE JAVAED.TEST.ESDS.STOCK CLUSTER PURGE
  DELETE JAVAED.TEST.RRDS.STOCK CLUSTER PURGE
                                                     
  SET MAXCC = 0                                      
                                                     
  DEFINE CLUSTER ( -                                 
    NAME ( JAVAED.TEST.KSDS.STOCK ) -        
    RECORDS ( 100 10 ) -                             
    INDEXED -                                        
    KEYS ( 8 0 ) -                                   
    RECORDSIZE ( 80 80 ) -                           
  )                                                  
                                                     
  DEFINE CLUSTER ( -                                 
    NAME ( JAVAED.TEST.ESDS.STOCK ) -        
    RECORDS ( 100 10 ) -                             
    NONINDEXED -                                     
    KEYS ( 8 0 ) -                                   
    RECORDSIZE ( 80 80 ) -                           
  )                                                  
                                                     
  DEFINE CLUSTER ( -                                 
    NAME ( JAVAED.TEST.RRDS.STOCK ) -        
    RECORDS ( 100 10 ) -                             
    NUMBERED -                                       
    RECORDSIZE ( 80 80 ) -                           
  )                                                  

/*                                                             
//                                                             
