package Management;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthentServer extends Remote {
    public void link(int idAccount, AccessToken token) throws RemoteException;

    public boolean checkKey(int idAccount, AccessToken token) throws RemoteException;
}
