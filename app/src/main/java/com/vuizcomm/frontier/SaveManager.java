package com.vuizcomm.frontier;
import android.content.Context;
public final class SaveManager {
    private static final String PREF="frontier_save", KEY="state";
    private SaveManager() {}
    public static void save(Context c, GameState s) {
        c.getSharedPreferences(PREF,Context.MODE_PRIVATE).edit().putString(KEY,s.toJson()).apply();
    }
    public static GameState load(Context c) {
        String json=c.getSharedPreferences(PREF,Context.MODE_PRIVATE).getString(KEY,null);
        return json==null?new GameState():GameState.fromJson(json);
    }
}
