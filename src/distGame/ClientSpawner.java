package distGame;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

public class ClientSpawner
{
	private ArrayList<GameClient> clientList;

	/**
	 * @param clientList
	 */
	public ClientSpawner()
	{
		super();
		this.setClientList(new ArrayList<GameClient>());
	}
	
	public static void main(String[] args)
	{
		ClientSpawner clientSpawner = new ClientSpawner();
		for (int i = 0; i <10; i++)
		{
			try
			{
				Random rand = new Random();
				int randInt = rand.nextInt(6);
				clientSpawner.getClientList().add(new GameClient(i, randInt));
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}
	}

	private ArrayList<GameClient> getClientList()
	{
		return clientList;
	}

	private void setClientList(ArrayList<GameClient> clientList)
	{
		this.clientList = clientList;
	}
	
	
}
