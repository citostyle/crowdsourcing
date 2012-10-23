package tuwien.aic.crowdsourcing.rss;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class PeriodicArticleService implements ServletContextListener {

    private Timer timer;
    private ArticleFetcher articleFetcher;

    private class RSSLookupTask extends TimerTask {
        @Override
        public void run() {
            Map<String, String> newArticles = articleFetcher.getNewArticles();
            // TODO add new articles to DB
            System.out.println(newArticles);
        }
    }

    @Override
    public synchronized void contextDestroyed(ServletContextEvent event) {
        if (timer != null) {
            timer.cancel(); // Terminate the timer thread
        }
    }

    // This method is invoked when the Web Application
    // is ready to service requests

    @Override
    public synchronized void contextInitialized(ServletContextEvent event) {
        timer = new Timer();
        articleFetcher = new ArticleFetcherImpl();
        articleFetcher.addFeed("http://finance.yahoo.com/rss/usmarkets");
        // TODO enable next line again for RSS
        // timer.schedule(new RSSLookupTask(), 5000, 5000);
    }

}