package com.dnd.gen.demo.service;

import com.dnd.gen.demo.domain.Category;
import com.dnd.gen.demo.domain.Subcategory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.dnd.gen.demo.domain.Subcategory.getSubcategoryTable;
import static com.dnd.gen.demo.service.RollTableService.*;

@Service
public class ConsoleAppService {

    public void start() throws SQLException {
        Scanner scanner = new Scanner(System.in);


        //-----------Database connection
        String DB_URL = System.getenv("DB_URL");
        String USER = System.getenv("DB_USER");
        String PASS = System.getenv("DB_PASS");

        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        connection.setAutoCommit(true);

        //TODO fix magic_items, currently rolls as "magic"
        //TODO tables to add content (non-placeholder for)
        //CIVILIZATION//Castles and Keeps//Churches//Cities//Town events//Villages//graveyards
        //WILDERNESS //Travel setbacks
        //CHARACTERS//Adventurers//Arcane (npcs)//BBEG//monsters (w/ lair actions)//nobles//priests//random npcs//villagers
        //ENCHANTMENTS//blessings//curses//Divinations and fortunes
        //FLAVOR//melee combat//nightmares//spell casting//potions

        //TODO specific logic for magic_items, potions, town_events, travel_setbacks, CHARACTERS, ENCHANTMENTS

        //------------START PROGRAM-------------
        // Populate our subcategories to our categories
        for(Category category : getCategories(connection)){
            if(category.getSubcategories() == null || category.getSubcategories().isEmpty()) {
                category.addSubcategory(connection);
            }
        }

        while (true) {
            // Print categories
            //TODO put consoleStartApp into own controller/services
            System.out.println("What Category would you like to select?");
            for (Category category : getCategories(connection)){
                System.out.println(category.getCategory_id() + ". " + category.getCategory_name());
            }
            System.out.println("100. Exit");

            //create a list of all Categories to check against the user's selection
            List<Integer> existCategory = new ArrayList<>();
            for (Category category : getCategories(connection)){
                existCategory.add(category.getCategory_id());
            }

            String user_input = scanner.nextLine();
            int user_num = 0;
            Category categoryChosen = null;

            try {
                user_num = Integer.parseInt(user_input);
            } catch (NumberFormatException e){
                user_num = 0;
                e.printStackTrace();
            }

            if (existCategory.contains(user_num)  && user_num != 100) {
                categoryChosen = getCategory(connection, user_num);

                // Loop for subcategories
                while (true) {

                    System.out.println("You selected " + categoryChosen.getCategory_name() + "\n Please Select from the following tables:");
                    List<Subcategory> subcategories = categoryChosen.getSubcategories();
                    for (Subcategory subcategory : subcategories) {
                        System.out.println("\t" + subcategory.getId() + ". " + subcategory.getName());
                    }
                    System.out.println("100. Back");

                    //create a list of possible subcategories to check against user input
                    List<Integer> existSubcategory = new ArrayList<>();
                    for (Subcategory subcategory : subcategories){
                        existSubcategory.add(subcategory.getId());
                    }

                    // Ask user which subcategory they want
                    user_input = scanner.nextLine();

                    try {
                        user_num = Integer.parseInt(user_input);
                    } catch (NumberFormatException e){
                        user_num = 0;
                        e.printStackTrace();
                    }

                    if (existSubcategory.contains(user_num) && user_num != 100) {
                        Subcategory subcategory = getSubcategory(connection, user_num);
                        String chosenTable = getSubcategoryTable(subcategory);

                        if (subcategory.getCategory_id() == 2) {
                            rollWilderness(connection, chosenTable);
                        } else if(subcategory.getCategory_id() == 1 && !chosenTable.equals("events")) {
                            rollCivilization(connection, chosenTable);
                        } else if (chosenTable.equals("potions")) {
                            rollPotions(connection);
                        } else if (chosenTable.equals("magicitems")){ //(subcategory.getId() == 29) {
                            rollMagicItems(connection);
                        } else if (chosenTable.equals("events")){ //(subcategory.getId() == 4) {
                            rollTownEvent(connection);
                        } else {
                            rollTable(connection, chosenTable);
                        }
                    } else if (user_input.toLowerCase().trim().equals("100") || user_input.toLowerCase().trim().equals("back")) {
                        break; // Go back to the category selection
                    } else {
                        System.out.println("Please enter a valid option");
                    }
                }
            } else if (user_input.toLowerCase().trim().equals("100") || user_input.toLowerCase().trim().equals("exit")) {
                break; // Exit the program
            } else {
                System.out.println("Please select from a valid option");
            }
        }

        connection.close();
    }

}
