package notificationFramework;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.time.Clock;
import java.time.LocalTime;

public class Notification<T>
{

	T						event;
	LocalTime				eventTime;
	NotificationSource<T>	eventSource;


	/**
	 * Creates a new notification, with the notification payload and a reference to the source of the notification,
	 * along with the timestamp of the notification
	 * 
	 * @param event
	 * @param eventSource
	 */
	public Notification(T event, NotificationSource<T> eventSource)
	{
		super();
		setEvent(event);
		setEventTime();
		setEventSource(eventSource);
	}


	/**
	 * @return
	 */
	public T getEvent()
	{
		return event;
	}


	/**
	 * @param event
	 */
	public synchronized void setEvent(T event)
	{
		this.event = event;
	}


	/**
	 * @return
	 */
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


	/**
	 * @return
	 */
	public LocalTime getEventTime()
	{
		return eventTime;
	}


	/**
	 * Sets the time of the notification to the current local time
	 */
	public void setEventTime()
	{
		this.eventTime = LocalTime.now(Clock.systemDefaultZone());
	}


	/**
	 * @return
	 */
	public NotificationSource<T> getEventSource()
	{
		return eventSource;
	}


	/**
	 * @param eventSource
	 */
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
