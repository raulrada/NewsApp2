package udacityscholarship.rada.raul.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = NewsActivity.class.getName();
    /**
     * URL String for {@link Article} data from the Guardian
     */
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?section=football&from-date=2018-06-20&show-tags=contributor&page-size=200&q=football&api-key=53a3a1b5-f5b3-4fa4-bc0f-41379837a268";
    /**
     * Private API key provided by the Guardian. DO NOT SHARE!!!
     */
    private static final String GUARDIAN_API_KEY = "53a3a1b5-f5b3-4fa4-bc0f-41379837a268";
    /**
     * Constant value for the article loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int ARTICLE_LOADER_ID = 1;
    /**
     * TextView displayed when the ListView is empty
     */
    private TextView emptyTextView;
    /**
     * adapter for the list of Articles
     */
    private ArticleAdapter articleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        emptyTextView = (TextView) findViewById(R.id.empty_text_view);

        /** ListView containing a list of articles */
        ListView articlesListView = (ListView) findViewById(R.id.articles_list_view);
        articlesListView.setEmptyView(emptyTextView);

        // Create a new adapter that takes an empty list of Articles as input
        articleAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        articlesListView.setAdapter(articleAdapter);

        // click listener for ListView items
        articlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get the Article that was clicked on in the ListView
                Article currentArticle = articleAdapter.getItem(position);

                //convert the String Article URL into an Uri object, used to pass into Intent constructor
                Uri articleUri = Uri.parse(currentArticle.getArticleUrl());

                // intent to open web browser on user's device, in order to see selected article
                Intent openWebSiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Send the intent to launch a new activity
                startActivity(openWebSiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new ArticleLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No articles available."
        emptyTextView.setText(R.string.no_articles);

        // Clear the adapter of previous article data
        articleAdapter.clear();

        // If there is a valid list of {@link Article} objectss, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (articles != null && !articles.isEmpty()) {
            articleAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Loader reset, so we can clear out our existing data.
        articleAdapter.clear();
    }

    /**
     * Inflates the settings menu specified in main.xml when NewsActivity opens up
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
