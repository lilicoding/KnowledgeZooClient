package edu.monash.neo4j.knowledgezooclient.util;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import edu.monash.neo4j.knowledgezooclient.Config;

public class CypherExecutor 
{
	private static Driver driver;

	public static void setup()
	{
		Config.load();
		
		driver = GraphDatabase.driver(Config.url, AuthTokens.basic(Config.username, Config.password));
	}
	
	public static void release()
	{
		driver.close();
	}

	public static void run(final String statement)
	{
		System.out.println(statement  + ";" + "\n");
		
		try (Session session = driver.session()) 
		{
			session.writeTransaction( new TransactionWork<Integer>()
		  	{
		        @Override
		        public Integer execute(Transaction tx )
		        {
		            tx.run(statement);
		            return 1;
		        }
		  	} );
		}
	}
}
