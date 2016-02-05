package pingpong;

import akka.actor.*;
import akka.actor.ActorRef;
import akka.actor.AbstractFSM;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.Duration;


public  class Server extends AbstractFSM<PingPongState, StateData>{

    private final LoggingAdapter log = Logging.getLogger(context().system(), this);

    private  String name = null;
    Server(String name ,ActorRef clientActor ){
        this.name = name;
        System.out.println(String.format("Starting %s",name));
        //ping the client and go to client wait state
        clientActor.tell(new PingPongMsg("AreYouReady"),self());
    }
    {
        startWith(PingPongState.ZOMBIE, new StateData());
        log.info("Current {}",this.stateName());
        when(PingPongState.ZOMBIE,
                        matchEvent(PingPongMsg.class,(e,d)->e.msg.equals("IAmReady"), (a, b)->doSomething()).
                                event(PingPongMsg.class,(e,d)->e.msg.equals("IAmReady3"), (a,b)->  doSomeThing2(a,b))
        );
        when(PingPongState.READY,
                matchEvent(PingPongMsg.class,(e,d)->e.msg.equals("IAmReady"), (a, b)-> goTo(PingPongState.READY)).
                        event(Pong.class,StateData.class,(a,b)->doSomething())
        );
        when(PingPongState.PINGED,
                matchEvent(PingPongMsg.class,(e,d)->e.msg.equals("IAmReady"), (a, b)-> goTo(PingPongState.READY)).
                        event(PingPongMsg.class,(e,d)->e.msg.equals("IAmReady2"), (a,b)->  doSomeThing2(a,b)).
                        event(PingPongMsg.class,(e,d)->e.msg.equals("TimeOut"), (a,b)->  doSomeThing3(a,b)).
                        event(PingPongMsg.class,(e,d)->e.msg.equals("IAmReady3"), (a,b)->  doSomeThing2(a,b))
        );
        when(PingPongState.PONGED,
                matchEvent(PingPongMsg.class,(e,d)->e.msg.equals("IAmReady"), (a, b)-> goTo(PingPongState.READY)).
                        event(Pong.class,StateData.class,(a,b)->doSomething())
        );
        onTransition(
                matchState(PingPongState.ZOMBIE, PingPongState.PINGED,
                        () ->{
                            System.out.println("Starting timer");
                            setTimer("timeout", new PingPongMsg("TimeOut"), Duration.create(5, "seconds"));}).
                        state(PingPongState.READY, null, () -> cancelTimer("timeout")).
                        state(null, PingPongState.ZOMBIE, (f, t) -> log().info("entering READY  from " + f)));
        initialize();

    }



    private FSM.State<PingPongState,StateData> doSomething() {
        return goTo(PingPongState.PINGED);
    }

    private FSM.State<PingPongState,StateData> doSomeThing2(PingPongMsg m,StateData d) {

        d.incMsgNum();
        log.info("Current State {}",this.stateName());
        log.info("Current Message {}",m.msg);
        log.info("Current StateData {}",d.getMsgNum());
        return goTo(PingPongState.PINGED);
    }
    private FSM.State<PingPongState,StateData> doSomeThing3(PingPongMsg m,StateData d) {

        d.incMsgNum();
        log.info("Current State {}",this.stateName());
        log.info("Current Message {}",m.msg);
        log.info("Current StateData {}",d.getMsgNum());
        return goTo(PingPongState.PINGED);
    }
}
