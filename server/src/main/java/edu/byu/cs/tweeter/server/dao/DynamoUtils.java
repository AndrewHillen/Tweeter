package edu.byu.cs.tweeter.server.dao;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

public class DynamoUtils
{
    Table table;

    public DynamoUtils(String tableName)
    {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        table = dynamoDB.getTable(tableName);
    }

    public Table getTable()
    {
        return table;
    }

    public void put(Item item)
    {
        try
        {
            System.out.println("Makes into put try");
            PutItemOutcome outcome = table.putItem(item);
        }
        catch (Exception ex)
        {
            System.out.println("Something screwed up");
            ex.printStackTrace();
        }
    }

    public Item get(GetItemSpec spec)
    {
        try
        {
            Item item = table.getItem(spec);
            return item;
        }
        catch (Exception ex)
        {
            System.out.println("Something else screwed up");
            ex.printStackTrace();
        }
        return null;
    }

    public void delete(DeleteItemSpec spec)
    {
        try
        {
            System.out.println(spec.getKeyComponents().toString());
            table.deleteItem(spec);
        }
        catch (Exception ex)
        {
            System.out.println("Something else screwed up");
            ex.printStackTrace();
        }
    }
}
