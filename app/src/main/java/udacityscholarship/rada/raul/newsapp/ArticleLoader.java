//code inspired by Udacity QuakeReport app at
//https://github.com/udacity/ud843-QuakeReport/blob/f0f9cd5ee7a8d67bd2e6f7e2539664a95499831b/app/src/main/java/com/example/android/quakereport/EarthquakeLoader.java

package udacityscholarship.rada.raul.newsapp;

import java.util.ArrayList;
import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Loads a list of {@link Article} objects by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = ArticleLoader.class.getName();

    /**
     * Query URL
     */
    private String url;

    /**
     * Constructs a new {@link Article}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public ArticleLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Article> loadInBackground() {
        if (url == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of {@link Articles}.
        List<Article> articles = ArticleQueryUtils.fetchArticleData(url);
        return articles;
    }
}
