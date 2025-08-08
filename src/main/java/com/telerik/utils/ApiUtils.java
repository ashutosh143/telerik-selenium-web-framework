package com.telerik.utils;

import com.telerik.reports.ExtentReportManager;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiUtils {

    public static class ApiResponseWrapper {
        public Response response;
        public String apiLog;
    }

    private static RequestSpecification buildRequest(Map<String, String> headers, Map<String, String> queryParams, String body) {
        RequestSpecification request = given().log().all();

        if (headers != null) request.headers(headers);
        if (queryParams != null) request.queryParams(queryParams);
        if (body != null) request.body(body);

        return request.contentType(ContentType.JSON).accept(ContentType.JSON);
    }

    public static ApiResponseWrapper get(String baseUri, String endpoint, Map<String, String> headers, Map<String, String> queryParams) {
        String fullUrl = baseUri + endpoint;
        String curl = "curl -X GET \"" + fullUrl + "\" -H \"accept: application/json\"";

        Response response = buildRequest(headers, queryParams, null)
                .baseUri(baseUri)
                .when()
                .get(endpoint);

        String apiLog = buildLog("GET", fullUrl, curl, null, response);
        ExtentReportManager.logApiToTestAndSummary("🔍 GET Request", apiLog);

        ApiResponseWrapper wrapper = new ApiResponseWrapper();
        wrapper.response = response;
        wrapper.apiLog = apiLog;
        return wrapper;
    }

    public static ApiResponseWrapper post(String baseUri, String endpoint, Map<String, String> headers, Map<String, String> queryParams, String body) {
        String fullUrl = baseUri + endpoint;
        String curl = "curl -X POST \"" + fullUrl + "\" -H \"accept: application/json\" -H \"Content-Type: application/json\" -d '" + body + "'";

        Response response = buildRequest(headers, queryParams, body)
                .baseUri(baseUri)
                .when()
                .post(endpoint);

        String apiLog = buildLog("POST", fullUrl, curl, body, response);
        ExtentReportManager.logApiToTestAndSummary("📦 POST Request", apiLog);

        ApiResponseWrapper wrapper = new ApiResponseWrapper();
        wrapper.response = response;
        wrapper.apiLog = apiLog;
        return wrapper;
    }

    public static ApiResponseWrapper put(String baseUri, String endpoint, Map<String, String> headers, Map<String, String> queryParams, String body) {
        String fullUrl = baseUri + endpoint;
        String curl = "curl -X PUT \"" + fullUrl + "\" -H \"accept: application/json\" -H \"Content-Type: application/json\" -d '" + body + "'";

        Response response = buildRequest(headers, queryParams, body)
                .baseUri(baseUri)
                .when()
                .put(endpoint);

        String apiLog = buildLog("PUT", fullUrl, curl, body, response);
        ExtentReportManager.logApiToTestAndSummary("✏️ PUT Request", apiLog);

        ApiResponseWrapper wrapper = new ApiResponseWrapper();
        wrapper.response = response;
        wrapper.apiLog = apiLog;
        return wrapper;
    }

    public static ApiResponseWrapper delete(String baseUri, String endpoint, Map<String, String> headers, Map<String, String> queryParams) {
        String fullUrl = baseUri + endpoint;
        String curl = "curl -X DELETE \"" + fullUrl + "\" -H \"accept: application/json\"";

        Response response = buildRequest(headers, queryParams, null)
                .baseUri(baseUri)
                .when()
                .delete(endpoint);

        String apiLog = buildLog("DELETE", fullUrl, curl, null, response);
        ExtentReportManager.logApiToTestAndSummary("🗑️ DELETE Request", apiLog);

        ApiResponseWrapper wrapper = new ApiResponseWrapper();
        wrapper.response = response;
        wrapper.apiLog = apiLog;
        return wrapper;
    }

    private static String buildLog(String method, String url, String curl, String body, Response response) {
        String testMethod = "UnknownTestMethod";
        String testClass = "UnknownTestClass";

        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("com.telerik.tests") && !element.getMethodName().equals("buildLog")) {
                testClass = element.getClassName().substring(element.getClassName().lastIndexOf('.') + 1);
                testMethod = element.getMethodName();
                break;
            }
        }

        return String.format("""
        📌 Invoked From: %s.%s

        🧭 Request Method: %s
        🌍 Request URL: %s
        🌀 cURL: %s
        📦 Request Body:
        %s

        ✅ Status Code: %d
        📭 Response Body:
        %s
        """,
                testClass,
                testMethod,
                method,
                url,
                curl,
                body == null ? "(no body)" : body,
                response.getStatusCode(),
                response.asPrettyString()
        );
    }
}
