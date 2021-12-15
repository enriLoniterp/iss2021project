package transportTrolley;


import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.CoapExchange;


class PublishResource extends CoapResource {
    public PublishResource() {
        super("publish");
        getAttributes().setTitle("Publish Resource");
    }
    public void handlePOST(CoapExchange exchange) {
        System.out.println(exchange.getRequestText());
        exchange.respond("POST_REQUEST_SUCCESS");
    }
    public void handleGET(CoapExchange exchange) {
        exchange.respond("GET_REQUEST_SUCCESS");
    }
}