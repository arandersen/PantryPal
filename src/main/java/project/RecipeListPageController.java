package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import org.json.Cookie;

import javafx.event.ActionEvent;

public class RecipeListPageController implements Controller {
    // actions to account for: addButton, RecipeItem, detailedViewButton
    private RecipeListPage view;
    private Model model;

    public RecipeListPageController(RecipeListPage view ,Model model){
        this.view = view;
        this.model = model;

        this.view.setAddButtonAction(event -> {
            try {
                handleAddButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.view.setDetailedViewButtonAction(event -> {
            try {
                handleDetailedViewButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.view.setSignOutButtonAction(event -> {
            try {
                handleSignOutButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // this.view.setSortBoxAction(event -> {
        //     try {
        //         handleSortBoxButton(event);
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // });

        // this.view.setFilterBoxAction(event -> {
        //     try {
        //         handleSortBoxButton(event);
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // });
    }

    private void handleAddButton(ActionEvent event) throws IOException{
        SpecifyMealTypePage temp = new SpecifyMealTypePage();
        Main.setPage(temp);
        Main.setController(new SpecifyMealTypePageController(temp, model));
    }

    private void handleDetailedViewButton(ActionEvent event) throws IOException{
        Recipe recipe = view.getRecipe();
        DetailedRecipePage temp = new DetailedRecipePage(recipe, false);
        Main.setPage(temp);
        Main.setController(new DetailedRecipePageController(temp, model));
    }

    // private void handleSortBoxButton(ActionEvent event) throws IOException {
    //     String selectedItem = view.getSortBox().getSelectionModel().getSelectedItem();
    //     if (selectedItem == 'A-Z') {sort A-Z logic}
    //     else if (selectedItem == 'Z-A') {sort Z-A logic}
    //     else if (selectedItem == 'oldest First') {sort oldest first logic use creation time}
    //     else { default; newest first }
    // }

    // private void handleFilterBoxButton(ActionEvent event) throws IOException {
    //     String selectedItem = view.getFilterBox().getSelectionModel().getSelectedItem();
    //     if (selectedItem == 'Dinner') {show only dinneritems}
    //     else if (selectedItem == 'Breakfast') {show only breakfast items}
    //     else if (selectedItem == 'Lunch') {show only lunch items}
    //     else { default; All }
    // }

    private void handleSignOutButton(ActionEvent event) throws IOException{
        LoginPage loginPage = new LoginPage();
        LoginPageController controller = new LoginPageController(loginPage, new Model());
        
        // Reset Rememvber Me Logic
        FileWriter file = new FileWriter("RememberMe.csv", false);
        file.write("0");
        file.close();

        // Set Page
        Main.setPage(loginPage);
        Main.setController(controller);
    }

   
}
