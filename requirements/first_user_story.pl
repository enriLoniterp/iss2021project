%====================================================================================
% first_user_story description   
%====================================================================================
context(ctxcarparking, "localhost",  "TCP", "8002").
 qactor( parkingmanagerservice, ctxcarparking, "it.unibo.parkingmanagerservice.Parkingmanagerservice").
  qactor( trolley, ctxcarparking, "it.unibo.trolley.Trolley").
