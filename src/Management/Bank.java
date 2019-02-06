package Management;

import Exceptions.ForbiddenException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface Bank extends Remote {
    public AccessToken createAccount(String lastname, String firstname, double originalBalance) throws RemoteException;

    public Boolean deposit(AccessToken token,int accountId, double amount) throws RemoteException, ForbiddenException;

    public Boolean getMoney(AccessToken token, int accountId,  double amount) throws RemoteException, ForbiddenException;

    public boolean transfer(AccessToken originAccess, int originAccountId, int targetAccountId, double amount) throws RemoteException, ForbiddenException;

    public double getAccountAmount(AccessToken token, int accountId) throws RemoteException, ForbiddenException;

    public String getName() throws RemoteException;

    public int registerToken(AccessToken token) throws ForbiddenException, RemoteException;

}
