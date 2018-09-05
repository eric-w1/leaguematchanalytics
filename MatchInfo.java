import json.JSONArray;
import json.JSONObject;

public class MatchInfo {
    private boolean liveGame;
    JSONObject[] allies;
    long allyTeamID;
    JSONObject[] enemies;
    long enemyTeamID;
    JSONObject self_ParticipantDto = null;

    boolean victory;
    long visionScore;
    int cs;
    int goldEarned;
    int gpm;
    int gameDuration_minutes;
    int kills;
    int deaths;
    int assists;

    JSONObject matchTimelineDto;

    /**
     * Creates MatchInfo object for a PAST match.
     * @param player
     * @param matchID
     */
    MatchInfo(PlayerInfo player, long matchID) throws Exception {
        liveGame = false;
        JSONObject matchDto = RiotAPIRetriever.getPastGameJSON(matchID, player.getRegion());
        populateTeams(player, matchDto);
        JSONObject self_ParticipantStatsDto = self_ParticipantDto.getJSONObject("stats");

        victory = self_ParticipantStatsDto.getBoolean("win");
        visionScore = self_ParticipantStatsDto.getLong("visionScore");
        cs = self_ParticipantStatsDto.getInt("totalMinionsKilled") + self_ParticipantStatsDto.getInt("neutralMinionsKilled");
        goldEarned = self_ParticipantStatsDto.getInt("goldEarned");
        gameDuration_minutes = (int) matchDto.getLong("gameDuration") / 60;
        gpm = goldEarned / gameDuration_minutes;
        kills = self_ParticipantStatsDto.getInt("kills");
        deaths = self_ParticipantStatsDto.getInt("deaths");
        assists = self_ParticipantStatsDto.getInt("assists");

        matchTimelineDto = RiotAPIRetriever.getMatchTimeline(matchID, player.getRegion());
        // xp cs, gold timeline
//        take out self_MatchParticipantFrameDto
    }


    /**
     * Identifies enemies/allies and populates team arrays.
     * @param player
     * @param gameData
     */
    private void populateTeams(PlayerInfo player, JSONObject gameData) {
        JSONArray ParticipantDtoList = gameData.getJSONArray("participants");

        JSONObject[] team1 = new JSONObject[5];
        int k = 0;
        for (int i = 0; i < 10; i += 1) {
            JSONObject ith_ParticipantDto = ParticipantDtoList.getJSONObject(i);
            long teamID = ith_ParticipantDto.getLong("teamId");
            if (teamID == 100) {
                team1[k] = ith_ParticipantDto;
                k += 1;
            }
        }

        JSONObject[] team2 = new JSONObject[5];
        k = 0;
        for (int i = 0; i < 10; i += 1) {
            JSONObject ith_ParticipantDto = ParticipantDtoList.getJSONObject(i);
            long teamID = ith_ParticipantDto.getLong("teamId");
            if (teamID == 200) {
                team2[k] = ith_ParticipantDto;
                k += 1;
            }
        }

        JSONArray ParticipantIdentityDtoList = gameData.getJSONArray("participantIdentities");
        int self_participantId = 0;
        for (int i = 0; i < 10; i += 1) {
            JSONObject PlayerDto = ParticipantIdentityDtoList.getJSONObject(i).getJSONObject("player");
            if (PlayerDto.getLong("summonerId") == player.getSummonerID()) {
                self_participantId = ParticipantIdentityDtoList.getJSONObject(i).getInt("participantId");
            }
        }

        for (int i = 0; i < 5; i += 1) {
            if (team1[i].getInt("participantId") == self_participantId) {
                self_ParticipantDto = team1[i];
                allies = team1;
                enemies = team2;
                allyTeamID = self_ParticipantDto.getInt("teamId");
                enemyTeamID = team2[0].getInt("teamId");
            }
        }

        if (self_ParticipantDto == null) {
            for (int i = 0; i < 5; i += 1) {
                if (team2[i].getInt("participantId") == self_participantId) {
                    self_ParticipantDto = team2[i];
                    allies = team2;
                    enemies = team1;
                    allyTeamID = self_ParticipantDto.getInt("teamId");
                    enemyTeamID = team1[0].getInt("teamId");
                }
            }
        }
    }

    public JSONObject returnMatchTimelineDto() {
        return matchTimelineDto;
    }
}
