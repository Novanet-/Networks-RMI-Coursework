package distGame;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
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
	private static final long									serialVersionUID	= -7269343809886333913L;
	private volatile ArrayList<NotificationSink<GameResult>>	nSinks;
	private int													maxDieValue;
	private int													currentRound;


	/**
	 * Exports the server on a random port, repeating this till a valid one is found, and then binds the server in the
	 * local rmi registry(on port 70), if it wasn't already created, then creates the rmi registry (on port 70) Sets the
	 * current round and max die values to default values (0 and 6 respectively)
	 */
	public GameServer()
	{
		try
		{
			boolean serverBound = false;
			while (!(serverBound))
			{
				try
				{
					ServerSocket servSocket = new ServerSocket(0);
					int portNo = servSocket.getLocalPort();
					servSocket.close();
					UnicastRemoteObject.exportObject(this, portNo);
					serverBound = true;
					Registry reg = LocateRegistry.createRegistry(70);
					reg.bind("server" + LocalTime.now(), this);
				}
				catch (AlreadyBoundException e)
				{
					continue;
				}
				catch (ExportException e)
				{
					Registry reg = LocateRegistry.getRegistry(70);
					try
					{
						reg.bind("server" + LocalTime.now(), this);
					}
					catch (AlreadyBoundException e1)
					{
						e1.printStackTrace();
					}
				}
			}
			setCurrentRound(0);
			setMaxDieValue(6);
			nSinks = new ArrayList<NotificationSink<GameResult>>();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * Adds the given NotificationSink to the server's list of sinks
	 * 
	 * @see notificationFramework.NotificationSource#registerClient(notificationFramework.NotificationSink)
	 */
	@Override
	public void registerClient(NotificationSink<GameResult> nSink) throws RemoteException
	{
		this.nSinks.add(nSink);
		System.out.println("Client " + (this.nSinks.size() - 1) + " added");
	}


	/**
	 * Removes the given NotificationSink from the server's list of sinks
	 * 
	 * @see notificationFramework.NotificationSource#unregisterClient(notificationFramework.NotificationSink)
	 */
	@Override
	public void unregisterClient(NotificationSink<GameResult> nSink) throws RemoteException
	{
		this.nSinks.remove(nSink);
		System.out.println("Client dropped");
	}


	/**
	 * Sends a notification object (of type GameResult) to all sinks in the server's sink list
	 * 
	 * @param notification
	 */
	public void sendNotificationToClients(Notification<GameResult> notification)
	{
		for (NotificationSink<GameResult> nSink : nSinks)
		{
			try
			{
				nSink.recieveNotificationOnClient(notification);
			}
			catch (RemoteException e)
			{

			}
		}
	}


	/**
	 * Rolls a random value between 1 and the maxDieValue
	 * 
	 * @param maxDieValue
	 * @return
	 */
	public int runDiceGame(int maxDieValue)
	{
		Random rand = new Random();
		return (rand.nextInt(maxDieValue) + 1);

	}


	/**
	 * Creates a new game server, on this server, every 2 seconds runs a new round of the dice game, sending
	 * notifications of the GameResult to all clients, and then printing out which clients won that round
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		GameServer gameServer = new GameServer();
		ScheduledExecutorService gameRunner = Executors.newScheduledThreadPool(1);
		gameRunner.scheduleAtFixedRate(() ->
		{
			try
			{
				gameServer.setCurrentRound(gameServer.getCurrentRound() + 1);
				GameResult result = new GameResult(gameServer.runDiceGame(gameServer.getMaxDieValue()));
				Notification<GameResult> notification = new Notification<GameResult>(result, gameServer);
				System.out.println("Round: " + gameServer.getCurrentRound());
				System.out.println("--------------------------");
				gameServer.sendNotificationToClients(notification);
				System.out.println();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		} , 0, 2, TimeUnit.SECONDS);
	}


	/**
	 * @return
	 */
	private int getMaxDieValue()
	{
		return maxDieValue;
	}


	/**
	 * @param maxDieValue
	 */
	private void setMaxDieValue(int maxDieValue)
	{
		this.maxDieValue = maxDieValue;
	}


	/**
	 * @return
	 */
	private int getCurrentRound()
	{
		return currentRound;
	}


	/**
	 * @param currentRound
	 */
	private void setCurrentRound(int currentRound)
	{
		this.currentRound = currentRound;
	}

}
