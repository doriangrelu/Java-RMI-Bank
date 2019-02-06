package Server;

import Management.Bank;
import remoteObjects.BankImpl;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class BankServer {


    public static void main(String[] args) {
        try {
            Bank bankImpl = new BankImpl("CAAP");
            Registry registry = LocateRegistry.createRegistry(8888);
            Bank stub = (Bank) UnicastRemoteObject.exportObject(bankImpl, 8888);
            BankServer._bindBank(registry, stub);

            System.err.println("Bank server Ready");
        } catch (RemoteException  e) {
            e.printStackTrace();
        }

    }

    private static void _bindBank(Registry registry, Bank bank) throws RemoteException {
        System.out.println("Bank binded : RemoteBankAccess:" + bank.getName());
        registry.rebind("RemoteBankAccess:" + bank.getName(), bank);
    }

}
