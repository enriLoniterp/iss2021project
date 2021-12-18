%====================================================================================
% carparking description   
%====================================================================================
context(ctxcarparking, "localhost",  "TCP", "8002").
 qactor( transport_trolley_controller, ctxcarparking, "it.unibo.transport_trolley_controller.Transport_trolley_controller").
  qactor( trolley, ctxcarparking, "it.unibo.trolley.Trolley").
