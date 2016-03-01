package com.collegeessentials.database;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This program makes a connection to our outside server database: Heroku Postgresql
 *
 * It retrieves the image name and binary and converts it to a file to be displayed on the HomeScreen
 *
 * @version 1.0
 * @see com.collegeessentials.main.HomeScreen
 */
public class HerokuConnection{

    private Connection connection;

    public HerokuConnection(){
        connect();//on creation of object make a connection to the heroku server
    }

    public void connect(){
        try {
            Class.forName("org.postgresql.Driver");//register driver jar file for postgres
        }catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? Include it in your library path!");
            e.printStackTrace();
        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        try{
            System.out.println("Attempting coonection to Heroku");
            String url = "jdbc:postgresql://ec2-54-217-231-152.eu-west-1.compute.amazonaws.com:5432/daf9u3a3kponk5?user=jscbqvrsggltha&password=Pqk5GYTC9brm3hp9xNDXzTQhdP&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
            connection = DriverManager.getConnection(url);
            System.out.println("-------- Heroku Connection Done ------------");
        }catch (Exception e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
    }

    /*
        Pulls down the images name and it's binary format and converts it back into a file in the user phone memory
     */
    public void getImagesFromDB(Context context, String name) {

        ResultSet rs;

        try {
            FileOutputStream out = context.openFileOutput(name, Context.MODE_PRIVATE);

            PreparedStatement ps = connection.prepareStatement("SELECT imageBinary FROM selection WHERE imageName = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                byte[] imgBytes = rs.getBytes(1);
                out.write(imgBytes);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
