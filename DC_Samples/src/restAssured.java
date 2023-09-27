

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.IOException;


public class restAssured {
public static void main(String args[]) {
   RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

   // Make an HTTP GET request
   Response response = RestAssured.get("/posts/1");

   // Get the response status code
   int statusCode = response.getStatusCode();
   System.out.println("Response Status Code: " + statusCode);

   // Get the response body as a string
   String responseBody = response.getBody().asString();
   System.out.println("Response Body:\n" + responseBody);
}
}