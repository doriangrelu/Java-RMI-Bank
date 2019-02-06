package Client;

import Exceptions.ForbiddenException;
import Management.AccessToken;
import Management.Bank;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(8888);
            Bank bankCAAP = (Bank) registry.lookup("RemoteBankAccess:CAAP");

            AccessToken token = bankCAAP.createAccount("GRELU", "Dorian", 125);
            token.setIdentifiants("dodo", "dodo");
            int accountId = bankCAAP.registerToken(token);
            bankCAAP.deposit(token, accountId, 500);
            System.out.println(bankCAAP.getAccountAmount(token, accountId));
        } catch (RemoteException | NotBoundException | ForbiddenException e) {
            e.printStackTrace();
        }

    }

}
