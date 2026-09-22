package com.vuizcomm.frontier;

import android.content.Context;

public final class SaveManager {
    private static final String KEY = "frontier_state";
    private SaveManager() {}

    public static void save(Context c, GameState state) {
        c.getSharedPreferences("save", Context.MODE_PRIVATE)
            .edit().putString(KEY, state.toJson()).apply();
    }

    public static GameState load(Context c) {
        String json = c.getSharedPreferences("save", Context.MODE_PRIVATE).getString(KEY, null);
        return json == null ? new GameState() : GameState.fromJson(json);
    }
}
