import json.JSONArray;
import json.JSONObject;

public class PlayerInfo {

    private String region;
    private String summonerName;
    private long summonerID;
    private long summonerLevel;
    private long accountId;
    private JSONArray matchlist;

    public PlayerInfo(String summonerNameQuery, String queryRegion) throws Exception {
        //identifies subdomain for each region.
        if (queryRegion.equals("NA")) {
            region = "na1";
        } else if (queryRegion.equals("EUW")) {
            region = "euw1";
        } else if (queryRegion.equals("EUNE")) {
            region = "eun1";
        } else if (queryRegion.equals("KR")) {
            region = "kr";
        } else {
            throw new IllegalArgumentException("Invalid region key.");
        }

        JSONObject summonerJSON = RiotAPIRetriever.getSummonerJSON(summonerNameQuery, region);
        //gets player's summoner ID and properly formatted name
        summonerID = summonerJSON.getLong("id");
        summonerName = summonerJSON.getString("name");
        summonerLevel = summonerJSON.getLong("summonerLevel");
        accountId = summonerJSON.getLong("accountId");

        matchlist = RiotAPIRetriever.getMatchlist(accountId, region).getJSONArray("matches");
    }

//    public MatchInfo currentGame() throws Exception {
//
//                        LEAGUE
////        for (int i = 0; i < 5; i += 1) {
////            System.out.println(allies[i].getString("summonerName") + "           " +
////                    enemies[i].getString("summonerName"));
////        }
////        return null;
//    }

    public MatchInfo lastGame(int i) throws Exception {
        return new MatchInfo(this, matchlist.getJSONObject(i).getLong("gameId"));
    }

    long getSummonerID() {
        return summonerID;
    }

    String getRegion() {
        return region;
    }
}
