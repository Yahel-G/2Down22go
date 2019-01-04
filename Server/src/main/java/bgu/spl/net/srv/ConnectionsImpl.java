package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.Connections;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Yahel on 22/12/2018.
 */
public class ConnectionsImpl<T> implements Connections<T> {
    private int id;

    private ConcurrentHashMap<Integer, bgu.spl.net.srv.bidi.ConnectionHandler<T>> handlersMap; // todo: concurrent?

    public ConnectionsImpl(){
        handlersMap = new ConcurrentHashMap<>();
        id = 0;
    }

    public boolean send(int connectionId, T msg){
        if(handlersMap.containsKey(connectionId)){
            if(msg != null){ // maybe still send msg even if it's null? maybe it can't be null?
                handlersMap.get(connectionId).send(msg);
                return true;
            } else return true;
        } else {// else, still send the msg to the client when he logs in?
            return false;
        }
    }

    public void broadcast(T msg){
        for(Integer userId : handlersMap.keySet()) {
            handlersMap.get(userId).send(msg);
        }
    }

    public void disconnect(int connectionId){

        handlersMap.remove(connectionId);
    }

    public ConcurrentHashMap<Integer, bgu.spl.net.srv.bidi.ConnectionHandler<T>> getHandlersMap() {
        return handlersMap;
    }

    public void addConnection(BlockingConnectionHandler handler){
        handlersMap.put(id, (bgu.spl.net.srv.bidi.ConnectionHandler<T>) handler);
        id++;
    }
}
