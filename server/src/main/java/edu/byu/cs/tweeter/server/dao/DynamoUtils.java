package edu.byu.cs.tweeter.server.dao;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.util.List;
import java.util.Map;

public class DynamoUtils
{
    Table table;
    String tableName;
    DynamoDB dynamoDB;

    public DynamoUtils(String tableName)
    {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        dynamoDB = new DynamoDB(client);

        table = dynamoDB.getTable(tableName);
        this.tableName = tableName;
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

    public ItemCollection<QueryOutcome> queryTable(QuerySpec spec)
    {
        ItemCollection<QueryOutcome> items = null;
        try
        {
            items = table.query(spec);
        }
        catch (Exception ex)
        {
            System.out.println("Query on table " + tableName + "had issues");
            ex.printStackTrace();
        }
        return items;
    }

    public ItemCollection<QueryOutcome> queryIndex(QuerySpec spec, String indexName)
    {
        Index index = table.getIndex(indexName);
        ItemCollection<QueryOutcome> items = null;
        try
        {
            items = index.query(spec);
        }
        catch (Exception ex)
        {
            System.out.println("Query on table " + tableName + ", index " + indexName + " had issues");
            ex.printStackTrace();
        }
        return items;
    }

    public void batchWrite(TableWriteItems items)
    {
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);

        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
        }
    }
}
