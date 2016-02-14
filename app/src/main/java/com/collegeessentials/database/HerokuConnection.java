package com.collegeessentials.database;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HerokuConnection{

    private String url = "jdbc:postgresql://ec2-54-217-231-152.eu-west-1.compute.amazonaws.com:5432/daf9u3a3kponk5?user=jscbqvrsggltha&password=Pqk5GYTC9brm3hp9xNDXzTQhdP&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
    private Connection connection;

    public HerokuConnection(){
        connect();
    }

    public void connect(){
        System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");

        try {
            Class.forName("org.postgresql.Driver");
        }catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
            e.printStackTrace();
        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        try{
            connection = DriverManager.getConnection(url);
            System.out.println("-------- JDBC Connection Done ------------");
        }catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try {
            System.out.println("-------- Closing JDBC Connection ------------");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sqlStatement) throws SQLException{
        ResultSet rs = null;

        try {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(sqlStatement);
        }
        catch (SQLException e) {
            throw e;
        }
        return rs;
    }

    public void getImagesFromDB(Context context, String name) {

        ResultSet rs = null;

        try {
            FileOutputStream out = context.openFileOutput(name, Context.MODE_PRIVATE);

            PreparedStatement ps = connection.prepareStatement("SELECT imageBinary FROM selection WHERE imageName = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                byte[] imgBytes = rs.getBytes(1);
                out.write(imgBytes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
