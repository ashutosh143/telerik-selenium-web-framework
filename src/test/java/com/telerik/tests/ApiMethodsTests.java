package com.telerik.tests;

import com.telerik.reports.ExtentReportManager;
import com.telerik.utils.ApiUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.telerik.reports.ExtentReportManager.apiSummaryTest;
import static org.hamcrest.Matchers.equalTo;

public class ApiMethodsTests {

    private static final String BASE_URI = "https://petstore.swagger.io";
    private static long id;


    public void getRequest() {
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("status", "available");

        ApiUtils.ApiResponseWrapper result = ApiUtils.get(BASE_URI, "/v2/pet/findByStatus", headers, queryParams);
        Response response = result.response;

        System.out.println(response.asPrettyString());
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.contentType(), "application/json");

        ExtentReportManager.attachApiLogsAdvanceFormat(apiSummaryTest, result.apiLog);
    }


    public void postRequestTest() {
        String requestBody = "{\n" +
                "  \"id\": 1122,\n" +
                "  \"category\": {\n" +
                "    \"id\": 0,\n" +
                "    \"name\": \"Cate\"\n" +
                "  },\n" +
                "  \"name\": \"karapa11\",\n" +
                "  \"photoUrls\": [\"string\"],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"tag1\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";

        ApiUtils.ApiResponseWrapper result = ApiUtils.post(BASE_URI, "/v2/pet", null, null, requestBody);
        Response response = result.response;

        response.then().statusCode(200)
                .body("category.name", equalTo("Cate"))
                .body("name", equalTo("karapa111"))
                .body("tags[0].name", equalTo("tag1"))
                .body("status", equalTo("available"));

        // Capture the dynamic id from the response
        id = ((Number)response.path("id")).longValue();

        ExtentReportManager.attachApiLogsAdvanceFormat(apiSummaryTest, result.apiLog);
    }

    public void putRequest() {
        String requestBody = "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"category\": {\n" +
                "    \"id\": 0,\n" +
                "    \"name\": \"BulDog\"\n" +
                "  },\n" +
                "  \"name\": \"bahubali\",\n" +
                "  \"photoUrls\": [\"string\"],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"tagAnimal\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";

        ApiUtils.ApiResponseWrapper result = ApiUtils.put(BASE_URI, "/v2/pet", null, null, requestBody);
        Response response = result.response;

        response.then().statusCode(200);

       //x ExtentReportManager.attachApiLogsAdvanceFormat(apiSummaryTest, result.apiLog);
    }


    public void deleteRequest() {
        ApiUtils.ApiResponseWrapper result = ApiUtils.delete(BASE_URI, "/v2/pet/" + id, null, null);
        Response response = result.response;

        System.out.println(response.asPrettyString());
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.contentType(), "application/json");

        //ExtentReportManager.attachApiLogsAdvanceFormat(apiSummaryTest, result.apiLog);
    }
}
