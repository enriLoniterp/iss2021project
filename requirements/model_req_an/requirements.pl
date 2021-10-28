%====================================================================================
% requirements description   
%====================================================================================
context(ctxcarparking, "localhost",  "TCP", "8002").
 qactor( fan, ctxcarparking, "it.unibo.fan.Fan").
  qactor( trolley, ctxcarparking, "it.unibo.trolley.Trolley").
