package com.dnd.gen.demo.controller;

import com.dnd.gen.demo.domain.Category;
import com.dnd.gen.demo.service.DatabaseConnectionService;
import com.dnd.gen.demo.service.RollTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private DatabaseConnectionService databaseConnectionService;

    @Autowired
    private RollTableService rollTableService;

    @RequestMapping("/")
    public String home(Model model){
        try(Connection connection = databaseConnectionService.getConnection()){
            List<Category> categoryList = rollTableService.getCategories(connection);
            model.addAttribute("category", categoryList);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return "index";
    }

}
