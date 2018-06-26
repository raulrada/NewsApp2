package udacityscholarship.rada.raul.newsapp;

/**
 * An {@link Article} fetched from the Guardian
 */
public class Article {

    /**
     * title of the {@link Article}
     */
    private String articleTitle;

    /**
     * section in which the {@link Article} is published
     */
    private String articleSection;

    /**
     * author of the {@link Article}
     */
    private String articleAuthor;

    /**
     * {@link Article} publishing date
     */
    private String articleDate;

    /**
     * {@link Article} url
     */
    private String articleUrl;

    /**
     * {@link Article} constructor
     *
     * @param articleTitle   title of the {@link Article}
     * @param articleSection section in which the {@link Article} is published
     * @param articleAuthor  author of the {@link Article}
     * @param articleDate    publishing date of the {@link Article}
     */
    public Article(String articleTitle, String articleSection, String articleAuthor,
                   String articleDate, String articleUrl) {
        this.articleTitle = articleTitle;
        this.articleSection = articleSection;
        this.articleAuthor = articleAuthor;
        this.articleDate = articleDate;
        this.articleUrl = articleUrl;
    }

    /**
     * @return title of the {@link Article}
     */
    public String getArticleTitle() {
        return articleTitle;
    }

    /**
     * @return section in which {@link Article} is published
     */
    public String getArticleSection() {
        return articleSection;
    }

    /**
     * @return name of the author of the {@link Article}
     */
    public String getArticleAuthor() {
        return articleAuthor;
    }

    /**
     * @return {@link Article} publishing date
     */
    public String getArticleDate() {
        return articleDate;
    }

    /**
     * @return {@link Article} URL
     */
    public String getArticleUrl() {
        return articleUrl;
    }
}
