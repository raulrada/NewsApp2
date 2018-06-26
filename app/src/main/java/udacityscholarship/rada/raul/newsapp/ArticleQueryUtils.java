// code inspired by Udacity QuakeReport app at
// https://github.com/udacity/ud843-QuakeReport/blob/lesson-four/app/src/main/java/com/example/android/quakereport/QueryUtils.java

package udacityscholarship.rada.raul.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Helper class containing only static methods for fetching {@link Article} relevant information
 * from the Guardian API.
 * Class declared as final in order to prevent inheritance for security reasons
 */
public final class ArticleQueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = ArticleQueryUtils.class.getSimpleName();

    /**
     * separator String for {@link Article} date
     */
    private static final String DATE_SEPARATOR = "T";

    /**
     * constant value for identifying a connection timeout
     */
    private static final int CONNECTION_TIMEOUT = 15000;

    /**
     * constant value for identifying a read timeout
     */
    private static final int READ_TIMEOUT = 10000;

    /**
     * Private class contructor in order to prevent creation of objects. Class is relevant only
     * for its static methods, which do not need class instantiations in order to be used.
     */
    private ArticleQueryUtils() {
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT /* milliseconds */);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200 (HttpURLConnection.HTTP_OK)),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response message: " + urlConnection.getResponseMessage());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Static method which formats the date displayed to the user
     * @param dateObject String date which must be formatted
     * @return formmated date
     */
    private static String formatDate(String dateObject) {
        Date date1 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateObject);
        } catch (ParseException e) {

        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL yyyy");

        return dateFormat.format(date1).toString();
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing the given JSON response.
     *
     * @param articleJSON from which the {@link Article} objects data will be extracted
     * @return ArrayList of {@link Article} objects
     */
    private static ArrayList<Article> extractFeatureFromJson(String articleJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        //Create empty ArrayList<Article> which will contain the relevant Articles
        ArrayList<Article> articles = new ArrayList<Article>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject rootJsonResponse = new JSONObject(articleJSON);

            // Get JSONObject containing info about all Articles
            JSONObject baseJsonResponse = rootJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of articles.
            JSONArray articlesArray = baseJsonResponse.getJSONArray("results");

            // for each article in articlesArray, create an {@link Article} object and add it to
            // the articles ArrayList
            for (int i = 0; i < articlesArray.length(); i++) {

                // get a single Article at position i within the list of Articles
                JSONObject currentArticle = articlesArray.getJSONObject(i);

                // extract the value for the key called "webTitle"
                String articleTitle = currentArticle.getString("webTitle");

                // extract the value for the key called "sectionName"
                String articleSection = currentArticle.getString("sectionName");

                // default value for the author of the Article
                String articleAuthor = "";

                // Try to parse the tags of the currentArticle JSONObject. If there's a problem,
                // a JSONException exception object will be thrown.
                // Catch the exception so the app doesn't crash, and print the error message to the logs.
                try {
                    // Extract the JSONArray containing the tags of the currentArticle JSONObject
                    JSONArray currentArticleTagsArray = currentArticle.getJSONArray("tags");

                    // initialize the StringBuilder containing the name of the author(s) of the Article
                    StringBuilder articleAuthorBuilder = new StringBuilder();

                    // articleAuthorBuilder StringBuilder should contain the name of the first
                    // author of the Article, which can be obtained by using the "webTitle" key of
                    // the first JSONObject in the currentArticleTagsArray
                    articleAuthorBuilder.append(currentArticleTagsArray.getJSONObject(0).getString("webTitle"));

                    //continue going through currentArticleTagsArray and get the values at keys
                    // "webTitle" of the JSONObjects in the JSONArray
                    for (int j = 1; j < currentArticleTagsArray.length(); j++) {
                        articleAuthorBuilder.append(", ");
                        articleAuthorBuilder.append(currentArticleTagsArray.getJSONObject(j).getString("webTitle"));
                    }

                    // update articleAuthor value built based on articleAuthorBuilder
                    articleAuthor = articleAuthorBuilder.toString();
                } catch (JSONException e) {
                    Log.e("ArticleQueryUtils", "Problem getting the article tags", e);
                }

                // default value for the Article publishing date
                String articleDate = "";

                //Try to get the value at key "webPublicationDate"
                // a JSONException exception object will be thrown.
                // Catch the exception so the app doesn't crash, and print the error message to the logs.
                try {
                    // Try to update the articleDate, if such information is available
                    String rawDate = currentArticle.getString("webPublicationDate");
                    //format the date and include the resulting formatted String as a member of the Article
                    articleDate = formatDate(rawDate);

                } catch (JSONException e) {
                    Log.e("ArticleQueryUtils", "Problem getting the article tags", e);
                }

                // extract the value for the key called "webUrl"
                String articleUrl = currentArticle.getString("webUrl");

                // create a new {@link Article} object with info from JSON response
                articles.add(new Article(articleTitle, articleSection, articleAuthor, articleDate, articleUrl));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("ArticleQueryUtils", "Problem parsing the article JSON results", e);
        }

        // return the ArrayList of articles
        return articles;
    }

    /**
     * Query the Guardian dataset and return a list of {@link Article} objects.
     */
    public static ArrayList<Article> fetchArticleData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        ArrayList<Article> articles = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return articles;
    }
}
