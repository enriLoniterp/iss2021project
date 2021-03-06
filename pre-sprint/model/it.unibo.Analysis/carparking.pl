%====================================================================================
% carparking description   
%====================================================================================
context(ctxcarparking, "localhost",  "TCP", "8002").
 qactor( parkingservicegui, ctxcarparking, "it.unibo.parkingservicegui.Parkingservicegui").
  qactor( parkingmanagerservice, ctxcarparking, "it.unibo.parkingmanagerservice.Parkingmanagerservice").
  qactor( actuatorscontroller, ctxcarparking, "it.unibo.actuatorscontroller.Actuatorscontroller").
  qactor( parkingservicestatusgui, ctxcarparking, "it.unibo.parkingservicestatusgui.Parkingservicestatusgui").
  qactor( datacollector, ctxcarparking, "it.unibo.datacollector.Datacollector").
  qactor( weightsensor, ctxcarparking, "it.unibo.weightsensor.Weightsensor").
  qactor( outsonar, ctxcarparking, "it.unibo.outsonar.Outsonar").
  qactor( fan, ctxcarparking, "it.unibo.fan.Fan").
  qactor( thermometer, ctxcarparking, "it.unibo.thermometer.Thermometer").
  qactor( trolley, ctxcarparking, "it.unibo.trolley.Trolley").
