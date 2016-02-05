package pingpong;

/**
 * Created by mahesh.govind on 2/4/16.
 */
public class StateData {
    int msgNum = 0;
    StateData(){

    }

    void incMsgNum(){
        this.msgNum++;
    }

    int getMsgNum(){
        return msgNum;
    }
}
