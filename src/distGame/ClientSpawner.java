package distGame;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

public class ClientSpawner
{

	private ArrayList<GameClient> clientList;


	/**
	 * Creates a new list of clients
	 */
	public ClientSpawner()
	{
		super();
		this.setClientList(new ArrayList<GameClient>());
	}


	/**
	 * Creates 20 clients, and adds them to the client list, after 5 seconds, randomly drops one of them from the system
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{

		ClientSpawner clientSpawner = new ClientSpawner();
		for (int i = 0; i < 20; i++)
		{
			try
			{
				Random rand = new Random();
				int randInt = rand.nextInt(6) + 1;
				clientSpawner.getClientList().add(new GameClient(i, randInt));
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		Random rand = new Random();
		int randomClient = rand.nextInt(clientSpawner.clientList.size());
		try
		{
			(clientSpawner.clientList.get(randomClient)).drop();
		}
		catch (RemoteException e)
		{

			e.printStackTrace();
		}
	}


	/**
	 * @return
	 */
	private ArrayList<GameClient> getClientList()
	{
		return clientList;
	}


	/**
	 * @param clientList
	 */
	private void setClientList(ArrayList<GameClient> clientList)
	{
		this.clientList = clientList;
	}

}
