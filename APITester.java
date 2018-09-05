public class APITester {
    public static void main(String[] args) throws Exception{
        System.out.println("Please make sure you are using a valid developer key! (These expire " +
                "every 2 days)");
        PlayerInfo player = new PlayerInfo("TF Blade", "NA");
        MatchInfo lastGame = player.lastGame(0);
        //CHAMPION

        System.out.println("Victory: " + lastGame.victory);
        System.out.println("Vision Score: " + lastGame.visionScore);
        System.out.println("CS: " + lastGame.cs);
        System.out.println("Total Gold Earned: " + lastGame.goldEarned);
        System.out.println("Game Length: " + lastGame.gameDuration_minutes + " mins");
        System.out.println("GPM: " + lastGame.gpm);
        System.out.println("KDA: " + lastGame.kills + "/" + lastGame.deaths + "/" + lastGame.assists);

        //match timeline information including gold, xp, cs history to be returned
        System.out.println(lastGame.matchTimelineDto);

    }
}
