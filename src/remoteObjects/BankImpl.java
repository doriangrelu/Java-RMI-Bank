package remoteObjects;

import Management.AccessToken;
import Management.Account;
import Management.AuthentServer;
import Management.Bank;
import Exceptions.*;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;
import java.util.*;

public class BankImpl extends RemoteObject implements Bank {

    private Random rand;
    private String _name;
    private HashMap<Integer, Account> _accountMap;
    private HashMap<Integer, Integer[]> _tempAuthent;
    private AuthentServer _authenServer;

    public BankImpl(String name) {
        this._name = name;
        this._accountMap = new HashMap<>();
        this.rand = new Random();
        this._tempAuthent = new HashMap<>();
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry();
            this._authenServer = (AuthentServer) registry.lookup("AuthentServer");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }


    public String getName() {
        return _name;
    }

    @Override
    public AccessToken createAccount(String lastname, String firstname, double originalBalance) throws RemoteException {
        Account account = new Account(lastname, firstname, originalBalance);
        int bankCode = this.rand.nextInt();
        int tokenCode = this.rand.nextInt();
        AccessToken token = new AccessToken(tokenCode, account.getId(), bankCode);


        this._accountMap.put(account.getId(), account);
        this._tempAuthent.put(tokenCode, new Integer[]{bankCode, account.getId()});
        return token;
    }

    @Override
    public Boolean deposit(AccessToken token, int idAccount, double amount) throws RemoteException, ForbiddenException {
        this._checkToken(token, idAccount);
        if (!this._accountMap.containsKey(idAccount)) {
            return false;
        }
        return this._accountMap.get(idAccount).deposit(amount);
    }

    @Override
    public Boolean getMoney(AccessToken token, int idAccount, double amount) throws RemoteException, ForbiddenException {
        this._checkToken(token, idAccount);

        if (!this._accountMap.containsKey(idAccount)) {
            return false;
        }
        return this._accountMap.get(idAccount).getMoney(amount);
    }

    @Override
    public boolean transfer(AccessToken token, int idAccountOrigin, int idAccountDistant, double amount) throws RemoteException, ForbiddenException {
        this._checkToken(token, idAccountOrigin);
        if (!this._accountMap.containsKey(idAccountOrigin) || !this._accountMap.containsKey(idAccountDistant)) {
            return false;
        }

        boolean firstOperation = this._accountMap.get(idAccountOrigin).getMoney(amount);
        if (!firstOperation) {
            return false;
        }

        return this._accountMap.get(idAccountDistant).deposit(amount);
    }

    /**
     * Register token trhougth auth services
     *
     * @param token
     * @return
     * @throws ForbiddenException
     */
    public int registerToken(AccessToken token) throws ForbiddenException, RemoteException {
        if (!this._tempAuthent.containsKey(token.getIdToken())) {
            throw new ForbiddenException();
        }

        int bankId = this._tempAuthent.get(token.getIdToken())[0];
        int accountId = this._tempAuthent.get(token.getIdToken())[1];
        //MAP IdToken --> IDBanque, IDClient

        if (token.validateCleBanque(bankId)) {
            _authenServer.link(accountId, token);
            this._tempAuthent.remove(token.getIdToken());
            return accountId;
        }
        throw new ForbiddenException();
    }

    private boolean _checkToken(AccessToken token, int idAccount, boolean fail) throws ForbiddenException, RemoteException {
        if (!this._authenServer.checkKey(idAccount, token)) {
            if (fail) {
                throw new ForbiddenException();
            }
            return false;
        }
        return true;
    }

    private boolean _checkToken(AccessToken token, int idAccount) throws ForbiddenException, RemoteException {
        return this._checkToken(token, idAccount, true);
    }


    @Override
    public double getAccountAmount(AccessToken token, int idAccount) throws RemoteException, ForbiddenException {
        this._checkToken(token, idAccount);
        if (!this._accountMap.containsKey(idAccount)) {
            throw new ForbiddenException();
        }
        return this._accountMap.get(idAccount).getAmount();
    }
}
