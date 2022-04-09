%====================================================================================
% sonaronrasp description   
%====================================================================================
context(ctxsonaronrasp, "localhost",  "TCP", "8068").
context(ctxsonarresource, "192.168.178.101",  "TCP", "8028").
 qactor( sonarresource, ctxsonarresource, "external").
  qactor( sonarsimulator, ctxsonaronrasp, "sonarSimulator").
  qactor( sonardatasource, ctxsonaronrasp, "sonarHCSR04Support2021").
  qactor( datacleaner, ctxsonaronrasp, "dataCleaner").
  qactor( sonar, ctxsonaronrasp, "it.unibo.sonar.Sonar").
tracing.
