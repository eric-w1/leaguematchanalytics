import json.JSONException;
import json.JSONObject;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * For V3 API, update call parameters for newer versions. Designed for 5v5 matches only
 */
public class RiotAPIRetriever {
    /**
     * Please ensure you are using a valid API key (unexpired, sufficient rate limits).
     * See https://developer.riotgames.com
     */
    public static final String DEVELOPER_API_KEY = "RGAPI-87cfcf4b-eef6-4a40-8a23-560952d013af";

    /**
     * @param urlAddress The URL containing desired JSON data
     * @return JSONObject containing all information from URL
     * @throws Exception
     */
    static JSONObject getJSONDataFromLink(String urlAddress) throws Exception {
        //creates a JSONObject
        URL url = new URL(urlAddress);
        Scanner sc = new Scanner(url.openStream());
        String parsedJSON = "";
        while (sc.hasNext()) {
            parsedJSON += sc.nextLine();
        }
        sc.close();
        JSONObject jsonData = new JSONObject(parsedJSON);

        validateResponseCode(jsonData);
        return jsonData;
    }

    /**
     * Checks for invalid inputs and throws an error with associated code, if any.
     */
    private static void validateResponseCode(JSONObject jsonData) {
        try {
            JSONObject status = jsonData.getJSONObject("status");
            throw new IllegalArgumentException(status.getInt("status_code") + " "
                    + status.getString("message"));
        } catch (JSONException e) {
            assert true;
        }
    }

    static JSONObject getCurrentGameJSON(long summonerID, String region) throws Exception {
        String address = "https://" + region + ".api.riotgames.com/lol"
                + "/spectator/v3/active-games/by-summoner/"
                + URLEncoder.encode(Long.toString(summonerID), "UTF-8")
                + "?api_key=" + DEVELOPER_API_KEY;
        JSONObject jsonData = RiotAPIRetriever.getJSONDataFromLink(address);
        return jsonData;
    }

    static JSONObject getMatchlist(long accountID, String region) throws Exception {
        String address = "https://" + region + ".api.riotgames.com/lol"
                + "/match/v3/matchlists/by-account/"
                + URLEncoder.encode(Long.toString(accountID), "UTF-8")
                + "?api_key=" + DEVELOPER_API_KEY;
        JSONObject jsonData = RiotAPIRetriever.getJSONDataFromLink(address);
        return jsonData;
    }

    static JSONObject getPastGameJSON(long matchID, String region) throws Exception {
        String address = "https://" + region + ".api.riotgames.com/lol"
                + "/match/v3/matches/"
                + URLEncoder.encode(Long.toString(matchID), "UTF-8")
                + "?api_key=" + DEVELOPER_API_KEY;
        JSONObject jsonData = RiotAPIRetriever.getJSONDataFromLink(address);
        return jsonData;
    }

    static JSONObject getMatchTimeline(long matchID, String region) throws Exception {
        String address = "https://" + region + ".api.riotgames.com/lol"
                + "/match/v3/timelines/by-match/"
                + URLEncoder.encode(Long.toString(matchID), "UTF-8")
                + "?api_key=" + DEVELOPER_API_KEY;
        JSONObject jsonData = RiotAPIRetriever.getJSONDataFromLink(address);
        return jsonData;
    }

    static JSONObject getSummonerJSON(String summonerName, String region) throws Exception {
        String address = "https://" + region + ".api.riotgames.com/lol/summoner/v3/summoners/"
                + "by-name/" + summonerName.replaceAll(" ", "%20")
                + "?api_key=" + DEVELOPER_API_KEY;
        JSONObject jsonData = RiotAPIRetriever.getJSONDataFromLink(address);
        return jsonData;
    }

    static JSONObject getChampionJSON(long championID, String region, String locale)
            throws Exception {
        String address = "https://" + region + ".api.riotgames.com/lol/static-data/v3/champions/"
                + championID + "?api_key=" + DEVELOPER_API_KEY + "&locale=en_US&champData=image";
        JSONObject jsonData = RiotAPIRetriever.getJSONDataFromLink(address);
        return jsonData;
    }
}
