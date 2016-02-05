package pingpong;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

/**
 * Created by mahesh.govind on 2/4/16.
 */
public class Client  extends AbstractActor{

    String name = null;

    Client (String name ){
        this.name = name;
        System.out.println(String.format("Starting %s",name));
    }
    private final LoggingAdapter log = Logging.getLogger(context().system(), this);

    {
        receive(ReceiveBuilder.
                match(Done.class, s -> {
                    log.info("Received String message: {}","Done");
                }).
                match (PingPongMsg.class,m->m.msg.equals("AreYouReady") ,m -> {
                    log.info("Received String message: {}", String.format(m.msg));
                    sender().tell(new PingPongMsg("IAmReady"),self());
                    sender().tell(new PingPongMsg("IAmReady2"),self());
                    sender().tell(new PingPongMsg("IAmReady3"),self());
                }).

                matchAny(o -> log.info("received unknown message")).build()
        );

    }
}
