package model;

import java.sql.Date;

public class notifications {
    private final long notifId;
    private final long matchId;
    private final String message;
    private final Date sentDate;

    public notifications(long notifId, long matchId, String message , Date sentDate) {
        this.notifId = notifId;
        this.matchId = matchId;
        this.message = message;
        this.sentDate = sentDate; 
    }

    public String getMessage() { return message; }
    public Date getSentDate() { return sentDate; }
    public long getMatchId() { return matchId; }
}

