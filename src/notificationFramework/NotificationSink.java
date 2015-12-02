package notificationFramework;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationSink<T> extends Remote
{

	public void recieveNotificationOnClient(Notification<T> notification) throws RemoteException; 
}
