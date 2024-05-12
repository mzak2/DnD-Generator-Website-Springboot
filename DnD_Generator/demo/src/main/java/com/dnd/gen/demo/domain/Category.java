package com.dnd.gen.demo.domain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Category {

    private int category_id;
    private String category_name;
    private List<Subcategory> subcategories;

    public Category(int category_id, String category_name, List<Subcategory> subcategories) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.subcategories = subcategories;
    }

    public Category(int category_id, String category_name) {
        this.category_id = category_id;
        this.category_name = category_name;
    }

    public Category() {
        subcategories = new ArrayList<>(); // Initialize subcategories list
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void addSubcategory(Connection connection) throws SQLException {
        if (subcategories == null) {
            subcategories = new ArrayList<>(); // Initialize subcategories list if null
        }

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM sub_categories WHERE category_id = " + category_id + " ORDER BY id");

        if (!rs.next()) {
            System.out.println("No subcategories found for category: " + category_name);
        } else {
            do {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                subcategories.add(new Subcategory(id, name, category_id));
            } while (rs.next());
        }

        statement.close();
    }

    public List<Subcategory> getSubcategories() {
        if (subcategories == null) {
            return Collections.emptyList(); // Return empty list if subcategories is null
        }
        return subcategories;
    }
}

