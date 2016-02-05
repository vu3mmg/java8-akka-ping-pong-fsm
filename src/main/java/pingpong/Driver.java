package pingpong;
import akka.actor.*;

/**
 * Created by mahesh.govind on 2/4/16.
 */
public class Driver {

    public static void main(String args[]){

        //Create Actor System
        final ActorSystem system = ActorSystem.create("PingPongSystem");
        final ActorRef ClientActor = system.actorOf(Props.create(Client.class,"Client"));
        final ActorRef ServerActor = system.actorOf(Props.create(Server.class,"Server", ClientActor));

    }
}
