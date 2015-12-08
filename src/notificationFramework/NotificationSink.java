package notificationFramework;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationSink<T> extends Remote
{

	/**
	 * A method for where a NotificationSource invokes a method on a NotificationSink for the NotificationSink to then
	 * receive a Notification object
	 * 
	 * @param notification
	 * @throws RemoteException
	 */
	public void recieveNotificationOnClient(Notification<T> notification) throws RemoteException;
}
