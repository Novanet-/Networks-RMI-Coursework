package distGame;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;

import notificationFramework.Notification;
import notificationFramework.NotificationSink;
import notificationFramework.NotificationSource;

public class GameClient implements NotificationSink<GameResult>, Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4282174207734023407L;
	private int					clientNo;
	private int					desiredDieValue;


	/**
	 * Initialises a client with a unique number and a desired die value Registers it with all servers found in the
	 * local rmi registry, and then exports it
	 * 
	 * @param clientNo
	 * @param desiredDieValue
	 * @throws RemoteException
	 */
	public GameClient(int clientNo, int desiredDieValue) throws RemoteException
	{
		this.clientNo = clientNo;
		this.desiredDieValue = desiredDieValue;
		Registry reg = LocateRegistry.getRegistry("localhost", 70);
		try
		{
			ArrayList<String> serverList = new ArrayList<String>(Arrays.asList(reg.list()));
			for (String serverName : serverList)
			{
				NotificationSource<GameResult> server = (NotificationSource<GameResult>) reg.lookup(serverName);
				server.registerClient(this);
			}

			UnicastRemoteObject.exportObject(this, 71);

		}
		catch (NotBoundException | IOException e)
		{
			e.printStackTrace();
		}

	}


	/**
	 * Unregisters the client from all servers in the local rmi registry, and unexports it
	 * 
	 * @throws RemoteException
	 */
	public void drop() throws RemoteException
	{
		Registry reg = LocateRegistry.getRegistry("localhost", 70);
		try
		{
			ArrayList<String> serverList = new ArrayList<String>(Arrays.asList(reg.list()));
			for (String serverName : serverList)
			{
				NotificationSource<GameResult> server = (NotificationSource<GameResult>) reg.lookup(serverName);
				server.unregisterClient(this);
			}

			UnicastRemoteObject.unexportObject(this, false);

		}
		catch (NotBoundException | IOException e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * Checks to see if the die value within the game result is equal to the client's desired die value, if true, then
	 * prints that the client has won, with that die value rolled
	 * 
	 * @see notificationFramework.NotificationSink#recieveNotificationOnClient(notificationFramework.Notification)
	 */
	@Override
	public void recieveNotificationOnClient(Notification<GameResult> notification)
	{
		GameResult recievedResult = notification.getEvent();
		int dieRoll = recievedResult.getDieResult();
		if (dieRoll == getDesiredDieValue())
		{
			System.out.println("Client: " + getClientNo() + ": " + "Yay! I won, because the dice rolled " + getDesiredDieValue());
		}
	}


	/**
	 * @return
	 */
	public int getClientNo()
	{
		return clientNo;
	}


	/**
	 * @return
	 */
	private int getDesiredDieValue()
	{
		return desiredDieValue;
	}

}
