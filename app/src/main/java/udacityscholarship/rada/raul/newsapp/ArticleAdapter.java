package udacityscholarship.rada.raul.newsapp;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * {@link ArticleAdapter} is an {@link ArrayAdapter} that will provide the layout for each element
 * in a list of {@link Article} objects
 */
public class ArticleAdapter extends ArrayAdapter<Article> {

    private Context context;

    /**
     * constructor of {@link ArticleAdapter} objects
     *
     * @param context  is the current context (i.e. Activity) that the adapter is being created in.
     * @param articles is the list of {@link Article} objects to be displayed in the list.
     */
    public ArticleAdapter(Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
        this.context = context;
    }

    /**
     * Provides a View for the ListView
     *
     * @param position    in the list of data that should be displayed in the list item view.
     * @param convertView the recycled view to populate.
     * @param parent      the parent ViewGroup that is used for inflation.
     * @return the View for the position in the ListView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // create {@link ViewHolder} object storing each of the component views of list_item.xml
        ViewHolder holder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent,
                    false);

            // find relevant objects in the list item layout to be populated with info about
            // {@link Article} and store them in the {@link holder} object
            holder = new ViewHolder();
            holder.listItemTitleTextView = (TextView) convertView.findViewById
                    (R.id.list_item_title_text_view);
            holder.listItemSectionTextView = (TextView) convertView.findViewById
                    (R.id.list_item_section_text_view);
            holder.listItemAuthorTextView = (TextView) convertView.findViewById
                    (R.id.list_item_author_text_view);
            holder.listItemDateTextView = (TextView) convertView.findViewById
                    (R.id.list_item_date_text_view);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        /** get the {@link Article} located at the current position in the ListView */
        Article currentArticle = getItem(position);

        // set the title of the Article in the corresponding TextView
        holder.listItemTitleTextView.setText(currentArticle.getArticleTitle());

        // set the section of the Article in the corresponding TextView
        holder.listItemSectionTextView.setText(context.getString(R.string.section,
                currentArticle.getArticleSection()));

        // display the name of the Article author(s), if available, otherwise remove the
        // relevant TextView
        if (!TextUtils.isEmpty(currentArticle.getArticleAuthor())) {
            holder.listItemAuthorTextView.setVisibility(View.VISIBLE);
            holder.listItemAuthorTextView.setText(context.getString(R.string.author,
                    currentArticle.getArticleAuthor()));
        } else holder.listItemAuthorTextView.setVisibility(View.GONE);

        // display the publishing date of the Article author(s), if available, otherwise remove the
        // relevant TextView
        if (!TextUtils.isEmpty(currentArticle.getArticleDate())) {
            holder.listItemDateTextView.setVisibility(View.VISIBLE);
            holder.listItemDateTextView.setText(context.getString(R.string.published,
                    currentArticle.getArticleDate()));
        } else holder.listItemDateTextView.setVisibility(View.GONE);

        return convertView;
    }

    /**
     * Class of {@link ViewHolder} objects used to store each of the component views of
     * list_item.xml inside the tag field of the Layout, so you can immediately access them without
     * the need to look them up repeatedly
     */
    static class ViewHolder {
        private TextView listItemTitleTextView;
        private TextView listItemSectionTextView;
        private TextView listItemAuthorTextView;
        private TextView listItemDateTextView;
    }
}
