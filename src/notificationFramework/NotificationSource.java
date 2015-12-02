package notificationFramework;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationSource<T> extends Remote
{
	
	public void registerClient(NotificationSink<T> nSink) throws RemoteException;
}
