/**
 * This class sets up a webView to display a website for user to search compendium.
 *
 * @author: Meng Yang
 */
package edu.tacoma.wa.pocketdungeonalt.compendium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.tacoma.wa.pocketdungeonalt.R;

public class CompendiumWebViewFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_compendium_webview, container, false);
        WebView webView = view.findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(getString(R.string.compendium));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        return view;
    }
}
