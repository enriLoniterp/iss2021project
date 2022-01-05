%====================================================================================
% carparking description   
%====================================================================================
context(ctxcarparking, "localhost",  "TCP", "8002").
 qactor( transport_trolley_controller, ctxcarparking, "it.unibo.trolley_controller.Trolley_controller").
  qactor( fan, ctxcarparking, "it.unibo.fan.Fan").
