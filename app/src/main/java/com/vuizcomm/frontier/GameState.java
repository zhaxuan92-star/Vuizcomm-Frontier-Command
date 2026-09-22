package com.vuizcomm.frontier;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public final class GameState {
    public String nation = "Vzcomm";
    public String region = "Vuizcomm";
    public long population = 505_000_000L;
    public double treasury = 125_000_000_000.0;
    public int influence = 82;
    public int stability = 74;
    public int industry = 68;
    public int diplomacy = 55;
    public int defense = 61;
    public final List<String> happenings = new ArrayList<>();

    public GameState() {
        happenings.add("Vzcomm established the Frontier Command.");
        happenings.add("Vuizcomm map synchronized.");
    }

    public void tick() {
        population += Math.max(1000, population / 18000);
        treasury += industry * 120_000.0;
        stability = Math.max(0, Math.min(100, stability + (diplomacy > 60 ? 1 : -1)));
        influence = Math.max(0, Math.min(100, influence + (diplomacy > 65 ? 1 : 0)));
        if (happenings.size() > 12) happenings.remove(0);
        happenings.add("Frontier update: population and economy advanced.");
    }

    public String toJson() {
        try {
            JSONObject o = new JSONObject();
            o.put("nation", nation); o.put("region", region);
            o.put("population", population); o.put("treasury", treasury);
            o.put("influence", influence); o.put("stability", stability);
            o.put("industry", industry); o.put("diplomacy", diplomacy);
            o.put("defense", defense);
            o.put("happenings", new JSONArray(happenings));
            return o.toString();
        } catch (Exception e) { return "{}"; }
    }

    public static GameState fromJson(String json) {
        GameState s = new GameState();
        try {
            JSONObject o = new JSONObject(json);
            s.nation=o.optString("nation",s.nation); s.region=o.optString("region",s.region);
            s.population=o.optLong("population",s.population); s.treasury=o.optDouble("treasury",s.treasury);
            s.influence=o.optInt("influence",s.influence); s.stability=o.optInt("stability",s.stability);
            s.industry=o.optInt("industry",s.industry); s.diplomacy=o.optInt("diplomacy",s.diplomacy);
            s.defense=o.optInt("defense",s.defense);
            s.happenings.clear(); JSONArray a=o.optJSONArray("happenings");
            if(a!=null) for(int i=0;i<a.length();i++) s.happenings.add(a.getString(i));
        } catch(Exception ignored) {}
        return s;
    }
}
