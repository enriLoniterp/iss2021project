%====================================================================================
% carparking description   
%====================================================================================
context(ctxcarparking, "localhost",  "TCP", "8002").
 qactor( park_client_service, ctxcarparking, "park_client_service.Park_client_service").
 qactor( park_manager_service, ctxcarparking, "park_manager_service.Park_manager_service").
  qactor( trolley_controller, ctxcarparking, "trolley_controller.Trolley_controller").
  qactor( fan, ctxcarparking, "fan.Fan").
