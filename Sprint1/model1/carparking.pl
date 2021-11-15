%====================================================================================
% carparking description   
%====================================================================================
context(ctxcarparking, "localhost",  "TCP", "8002").
 qactor( parkingmanagerservice, ctxcarparking, "it.unibo.parkingmanagerservice.Parkingmanagerservice").
  qactor( weightsensor, ctxcarparking, "it.unibo.weightsensor.Weightsensor").
  qactor( outsonar, ctxcarparking, "it.unibo.outsonar.Outsonar").
  qactor( thermometer, ctxcarparking, "it.unibo.thermometer.Thermometer").
  qactor( transport_trolley, ctxcarparking, "it.unibo.transport_trolley.Transport_trolley").
