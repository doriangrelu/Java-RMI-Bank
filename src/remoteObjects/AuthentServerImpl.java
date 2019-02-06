package remoteObjects;

import Management.AccessToken;
import Management.AuthentServer;

import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.util.HashMap;

public class AuthentServerImpl extends RemoteObject implements AuthentServer {

    private HashMap<Integer, AccessToken> _tokensMap;

    public AuthentServerImpl() {
        this._tokensMap = new HashMap<>();
    }

    @Override
    public void link(int idAccount, AccessToken token) throws RemoteException  {
        this._tokensMap.put(idAccount, token);
    }

    @Override
    public boolean checkKey(int idAccount, AccessToken token) throws RemoteException {
        if (!this._tokensMap.containsKey(idAccount)) {
            return false;
        }
        return this._tokensMap.get(idAccount).equals(token);
    }
}
