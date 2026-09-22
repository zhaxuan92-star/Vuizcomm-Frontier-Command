package com.vuizcomm.frontier;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
    private FrontierView game;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        game = new FrontierView(this);
        setContentView(game);
    }

    @Override protected void onPause() {
        super.onPause();
        game.save();
    }

    void notice(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
