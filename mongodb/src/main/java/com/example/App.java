package com.example;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.example.models.EmployeeDetails;
import com.example.models.EmployeeDetailsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class App {

    public static void main(String[] args) {

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File("passwords.json"));
            
            String dbName = "testDB";

            // STEP 1 (OPTIONAL) : connecting with atlas

            // String userName = "";
            // String password = "";

            // String connectionString = String.format(
            //     "mongodb+srv://%s:%s@cluster0.mongodb.net/%s?retryWrites=true&w=majority",
            //     userName, password, dbName
            // );

            // STEP 2 : connecting with localhost (ensure mongodb client running in your local)
            String connectionString = "mongodb://localhost:27017";


            // STEP 3 : create a client
            try (MongoClient mongoClient = MongoClients.create(connectionString)) {
                MongoDatabase database = mongoClient.getDatabase(dbName);
                System.out.println("Connected to database: " + database.getName());

                // helps you create a new collection / connects to existing one
                MongoCollection<Document> collection = database.getCollection("employees");

                // a builder pattern model used in the project
                EmployeeDetails details = new EmployeeDetailsBuilder()
                                                    .withEmployee("George")
                                                    .withAge(32)
                                                    .withDepartment("CSE")
                                                    .build();

                // I. INSERT Document into the collections //////////////////////////

                Document document = new Document()
                                        .append("employee", details.getEmployee())
                                        .append("age", details.getAge())
                                        .append("department", details.getDepartment());


                collection.insertOne(document);

                // System.out.println("inserted document to the server");

                // verify using
                // use testDB
                //  1. show collections
                //  2. db.employees.find().pretty()

                // II. READ data from the collections ///////////////////////////////

                System.out.println("reading data from collection");

                collection.find().forEach(doc -> System.out.println(doc.toJson()));


                System.out.println("reading filtered data from collections");
                Bson filter = Filters.and(Filters.gte("age", 15), Filters.eq("department", "ECE"));

                collection.find(filter).forEach(doc -> System.out.println(doc.toJson()));

                // select * from employees
                // where age > 15 && age <= 25 and department = 'ECE';

                // III. Aggregate operations used in collections //////////////////////////

                // count of all departments

                // Bson groupStage = Aggregates.group("$department");
               //  Bson countStage = Aggregates.count("count");

                // select count(department)
                // group by department

                 // practice exercise : 

                 // count the departments where avg age of employees greater than 20

                Bson groupStage = Aggregates.group("$department", 
                                        Accumulators.avg("averageAge", "$age"));

                Bson matchStage = Aggregates.match(Filters.gt("averageAge", 30));
                 
                Bson countStage = Aggregates.count("departmentCount");

                AggregateIterable<Document> result = collection.aggregate(Arrays.asList(groupStage, matchStage, countStage));

                Document countDoc = result.first();

                System.out.println("countDoc:"+  countDoc.toJson());
                System.out.println("dept count:" +  countDoc.get("departmentCount"));


                // IV. Update documents         ////////////////////////////////

               Bson filter1 = Filters.eq("employee", "CYbil");
               Bson update = Updates.set("employee", "Cybil");

                // update employees
                // set department = 'ECE'
                // where employee = 'Wilbert';

                //select * from employees;

                // select employee, age from employees;
                //

                UpdateResult result2 = collection.updateOne(filter1, update);
                System.out.println("Matched: " + result2.getMatchedCount() + ", Modified: " + result2.getModifiedCount());

                // V. delete documents

               Bson filter3 = Filters.eq("employee", "CYbil");

               DeleteResult result3 = collection.deleteOne(filter3);

               System.out.println("Deleted documents: " + result3.getDeletedCount());


                mongoClient.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
