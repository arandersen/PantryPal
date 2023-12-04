package project;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.util.*;

import org.bson.Document;
import org.json.JSONException;

public class RequestHandler implements HttpHandler {

  private static final String TEMP_AUDIO_FILE_PATH = "tempAudio.wav";
  private static Whisper whisper;
  public static ChatGPT chatGPT;
  public static DallE dallE;
  // private DALLE dallE;

  Map<String, String> queryParams;

  RequestHandler() {
    whisper = new Whisper();
    chatGPT = new ChatGPT();
    dallE = new DallE();
  }

  public void handle(HttpExchange httpExchange) throws IOException {
    String response = "Request Received";
    String method = httpExchange.getRequestMethod();

    try {
      if (method.equals("GET")) {
        response = handleGet(httpExchange);
      } else if (method.equals("POST")) {
        response = handlePost(httpExchange);
      } else if (method.equals("DELETE")) {
        response = handleDelete(httpExchange);
      } else {
        throw new Exception("Not Valid Request Method");
      }
    } catch (Exception e) {
      System.out.println("An erroneous request");
      response = e.toString();
      e.printStackTrace();
    }

    // Sending back response to the client
    httpExchange.sendResponseHeaders(200, response.length());
    OutputStream outStream = httpExchange.getResponseBody();
    outStream.write(response.getBytes());
    outStream.close();
  }

  // Login & getRecipeList & Refresh
  private String handleGet(HttpExchange httpExchange) throws IOException {
    String response = "Invalid GET Request";
    queryParams = parseQueryParams(httpExchange.getRequestURI().getQuery());
    String action = queryParams.get("action");
    if (action == null) {
      return response;
    }

    if (action.equals("getRecipeList")) {
      // get recipe list for acct
      String username = queryParams.get("username");
      String password = queryParams.get("password");
      List<Document> recipes = MongoDBProject.getRecipeList(username, password);
      // ArrayList<String> jsons = new ArrayList<String>();
      response = "{";
      for (Document d : recipes) {
        response += d.toJson() + ",";
      }
      response += "}";
    } else if (action.equals("getImage")) {
      // Do DALL-E call
      String title = queryParams.get("password");
    } else if (action.equals("getRecipeDetails")) {
      // Fetch a specific recipe's details
      String username = queryParams.get("username");
      String password = queryParams.get("password");
      String title = queryParams.get("title");

      Document recipe = MongoDBProject.getRecipeByTitle(username, password, title);
      if (recipe != null) {
        response = recipe.toJson();
      } else {
        response = "Recipe not found";
      }
    }

    return response;
  }

  // ChatGPT, Whisper, DALL-E, addRecipe
  private String handlePost(HttpExchange httpExchange) throws IOException {
    String response = "Invalid POST Request";
    queryParams = parseQueryParams(httpExchange.getRequestURI().getQuery());
    String action = queryParams.get("action");
    if (action == null) {
      return response;
    }

    if (action.equals("transcribeaudioFilePath")) {
      // transcribe audio to text
      String audioFilePath = queryParams.get("audioFilePath");
      try {
        response = whisper.transcribeAudio(audioFilePath);
      } catch (JSONException e) {
        e.printStackTrace();
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }

    } else if (action.equals("login")) {
      // try logging in with acct details
      String username = queryParams.get("username");
      String password = queryParams.get("password");
      response = String.valueOf(MongoDBProject.login(username, password));

    } else if (action.equals("signup")) {
      // try signing up with acct details
      String username = queryParams.get("username");
      String password = queryParams.get("password");
      response = String.valueOf(MongoDBProject.createUser(username, password));

    } else if (action.equals("createRecipe")) {
      String username = queryParams.get("username");
      String password = queryParams.get("password");
      String mealType = queryParams.get("mealType");
      String ingredients = queryParams.get("ingredients");
      String title = queryParams.get("title");
      String instructions = queryParams.get("instructions");
      String imageURL = queryParams.get("imageURL");
      // TODO change to addRecipe once the method is added in MongoDBProject
      MongoDBProject.updateRecipe(username, password, title, mealType, ingredients, instructions, 0, imageURL);
      response = "Added recipe: " + title;

    } else if (action.equals("updateRecipe")) {
      // update/add recipe
      String username = queryParams.get("username");
      String password = queryParams.get("password");
      String mealType = queryParams.get("mealType");
      String ingredients = queryParams.get("ingredients");
      String title = queryParams.get("title");
      String instructions = queryParams.get("instructions");
      Long creationTime = Long.parseLong(queryParams.get("creationTime"));
      String imageURL = queryParams.get("imageURL");
      MongoDBProject.updateRecipe(username, password, title, instructions, mealType, ingredients, creationTime,
          imageURL);
      response = "Updated recipe: " + title;
      // replace with what we are expecting as a response

    } else if (action.equals("generateRecipe")) {
      String mealType = queryParams.get("mealType");
      String ingredients = queryParams.get("ingredients");
      try {
        response = chatGPT.getGPTResponse(ingredients, mealType);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    }

    // response include generated recipe and image url (in the last part)
    return response;
  }

  // deleteRecipe
  private String handleDelete(HttpExchange httpExchange) throws IOException {
    String response = "Invalid DELETE request";
    queryParams = parseQueryParams(httpExchange.getRequestURI().getQuery());
    String action = queryParams.get("action");
    if (action == null) {
      return response;
    }

    if (action.equals("deleteRecipe")) {
      String username = queryParams.get("username");
      String password = queryParams.get("password");
      String title = queryParams.get("title");
      MongoDBProject.deleteRecipe(username, password, title);
      response = "Deleted recipe: " + title;
      // replace with what we are expecting as a response
    } else if (action.equals("deleteUser")) {
      String username = queryParams.get("username");
      String password = queryParams.get("password");
      MongoDBProject.deleteUser(username, password);
      response = "Deleted user: " + username + ", " + password;
    }
    return response;
  }

  private Map<String, String> parseQueryParams(String query) {
    Map<String, String> params = new HashMap<>();
    if (query != null) {
      String[] pairs = query.split("&");
      for (String pair : pairs) {
        String[] keyValue = pair.split("=");
        if (keyValue.length == 2) {
          params.put(keyValue[0], keyValue[1]);
        }
      }
    }
    return params;
  }

}