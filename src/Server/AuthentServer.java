package Server;

import remoteObjects.AuthentServerImpl;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class AuthentServer {

    public static void main(String[] args) {
        try {
            Management.AuthentServer authentServer = new AuthentServerImpl();
            Management.AuthentServer stub = (Management.AuthentServer) UnicastRemoteObject.exportObject(authentServer, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("AuthentServer", stub);

            System.err.println("Authent server ready");

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

}
