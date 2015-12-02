package notificationFramework;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.time.Clock;
import java.time.LocalTime;

public class Notification<T>
{

	T					event;
	LocalTime			eventTime;
	NotificationSource<T>	eventSource;


	
	
	/**
	 * @param event
	 * @param eventTime
	 * @param eventSource
	 */
	public Notification(T event, NotificationSource<T> eventSource)
	{
		super();
		setEvent(event);
		setEventTime();
		setEventSource(eventSource);
	}


	
	
	
	public T getEvent()
	{
		return event;
	}




	public synchronized void setEvent(T event)
	{
		this.event = event;
	}


	public synchronized String getName()
	{
		String currentLocation = "location_unknown";
		try
		{
			currentLocation = InetAddress.getLocalHost().getHostName();
		}
		catch (Exception e)
		{
		}
		return this.eventTime + " @ " + "(" + currentLocation + ")";
	}


	public LocalTime getEventTime()
	{
		return eventTime;
	}


	public void setEventTime()
	{
		this.eventTime = LocalTime.now(Clock.systemDefaultZone());
	}


	public NotificationSource<T> getEventSource()
	{
		return eventSource;
	}


	public void setEventSource(NotificationSource<T> eventSource)
	{
		this.eventSource = eventSource;
	}


	/**
	 * @throws RemoteException
	 */
	public String describe() throws RemoteException
	{
		return getName() + "[" + ((eventTime == null) ? "null" : eventTime) + "," + ((eventSource == null) ? "null" : eventSource) + "]";
	}
}
