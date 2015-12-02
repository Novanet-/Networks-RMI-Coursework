package distGame;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import notificationFramework.Notification;
import notificationFramework.NotificationSink;

public class GameClient implements NotificationSink<GameResult>
{

	private int	clientNo;
	private int	desiredDieValue;


	public GameClient(int clientNo, int desiredDieValue) throws RemoteException
	{
		this.clientNo = clientNo;
		this.desiredDieValue = desiredDieValue;
		Registry reg = LocateRegistry.getRegistry();
		try
		{
			GameServer server = (GameServer) reg.lookup("server");
			server.registerClient(this);
		}
		catch (NotBoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UnicastRemoteObject.exportObject(this, 71);
	}


	@Override
	public void recieveNotificationOnClient(Notification<GameResult> notification) throws RemoteException
	{
		GameResult recievedResult = notification.getEvent();
		int dieRoll = recievedResult.getDieResult();
		if (dieRoll == getDesiredDieValue())
		{
			System.out.println(getClientNo() + ": " + "Yay! I won, because the dice rolled " + getDesiredDieValue());
		}
	}

	

	
	private int getClientNo()
	{
		return clientNo;
	}


	private int getDesiredDieValue()
	{
		return desiredDieValue;
	}

}
