package edu.tacoma.wa.pocketdungeonalt.dice;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import edu.tacoma.wa.pocketdungeonalt.R;

public class DiceWebViewFragment extends Fragment {
    private WebView webView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_dice_web_view, container, false);
        webView = view.findViewById(R.id.dice_view);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(getString(R.string.dice));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        return view;
    }
}
