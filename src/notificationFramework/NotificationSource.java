package notificationFramework;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationSource<T> extends Remote
{

	/**
	 * Registers a given NotificationSink with the NotificationSource
	 * 
	 * @param nSink
	 * @throws RemoteException
	 */
	public void registerClient(NotificationSink<T> nSink) throws RemoteException;


	/**
	 * Unregisters a NotificationSink from a NotificationSource
	 * 
	 * @param nSink
	 * @throws RemoteException
	 */
	public void unregisterClient(NotificationSink<T> nSink) throws RemoteException;
}
