package distGame;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import notificationFramework.Notification;
import notificationFramework.NotificationSink;
import notificationFramework.NotificationSource;

public class GameServer implements NotificationSource<GameResult>, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7269343809886333913L;
	private volatile ArrayList<NotificationSink<GameResult>>	nSinks;
	private int									maxDieValue;


	public GameServer() throws RemoteException
	{
		try
		{
			Registry reg;
			reg = LocateRegistry.getRegistry();
			reg.bind("server", this);
			setMaxDieValue(6);
			nSinks = new ArrayList<NotificationSink<GameResult>>();
			UnicastRemoteObject.exportObject(this, 71);
		}
		catch (AlreadyBoundException e)
		{
			e.printStackTrace();
		}
	}


	@Override
	public void registerClient(NotificationSink<GameResult> nSink) throws RemoteException
	{
		this.nSinks.add(nSink);
	}


	public void sendNotificationToClients(Notification<GameResult> notification) throws RemoteException
	{
		for (NotificationSink<GameResult> nSink : nSinks)
		{
			nSink.recieveNotificationOnClient(notification);
		}
	}


	public int runDiceGame(int maxDieValue)
	{
		Random rand = new Random();
		return (rand.nextInt(maxDieValue) + 1);

	}


	public static void main(String[] args)
	{
		try
		{
			GameServer gameServer = new GameServer();
			ScheduledExecutorService gameRunner = Executors.newScheduledThreadPool(1);
			gameRunner.scheduleAtFixedRate(() ->
			{
				try
				{
					GameResult result = new GameResult(gameServer.runDiceGame(gameServer.getMaxDieValue()));
					Notification<GameResult> notification = new Notification<GameResult>(result, gameServer);
					gameServer.sendNotificationToClients(notification);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}, 0, 5, TimeUnit.SECONDS);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}


	private int getMaxDieValue()
	{
		return maxDieValue;
	}


	private void setMaxDieValue(int maxDieValue)
	{
		this.maxDieValue = maxDieValue;
	}
}
