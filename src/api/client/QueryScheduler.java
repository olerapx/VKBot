package api.client;

/**
 * Provides a query limitation per time unit.
 */
public class QueryScheduler
{
	private long queryDelayMs;
	private int allowedQueryCount;
	
	private int queryCount = 0;
	private long queryTime[];
	
	public QueryScheduler(long queryDelayMs, int allowedQueryCount)
	{
		this.queryDelayMs = queryDelayMs;
		this.allowedQueryCount = allowedQueryCount;
		this.queryTime = new long[allowedQueryCount];
	}
	
	public boolean canPostQuery()
	{
		updateTime();
		
		if (queryCount < allowedQueryCount)
		{
			queryCount ++;
			queryTime[queryCount-1] = System.currentTimeMillis();			
			return true;
		}
		return false;
	}
	
	private void updateTime()
	{	
		int firstQueryPos = getFirstQueryPos();
		
		if (firstQueryPos == -1)
		{
			clearAll();			
			return;	
		}		
		move(firstQueryPos);
	}
	
	private int getFirstQueryPos()
	{
		long currTime = System.currentTimeMillis();
		int firstQueryPos = -1;
		
		for (int i=0; i<queryCount; i++)
		{
			long delta = currTime - queryTime[i];
			
			if (delta < queryDelayMs)
			{
				firstQueryPos = i;
				break;
			}			
		}
		return firstQueryPos;
	}
	
	private void clearAll()
	{
		for (int j=0; j<allowedQueryCount; j++)
			queryTime[j] = 0;
		queryCount = 0;
	}
	
	private void move(int firstQueryPos)
	{
		int index = 0;
		
		for (int i = firstQueryPos; i<allowedQueryCount; i++)
		{
			queryTime[index] = queryTime[i];
			index++;
		}
		
		for (int i=index; i<allowedQueryCount; i++)
		{
			queryTime[i] = 0;
			queryCount --;
		}
	}
}
