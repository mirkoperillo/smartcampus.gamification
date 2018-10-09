package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import eu.trentorise.game.model.ChallengeConcept.ChallengeState;

public class GroupChallenge {

    private static final String MODEL_NAME_COMPETITIVE_PERFORMANCE = "groupCompetitivePerformance";

    private String id;

    private String gameId;
    private String instanceName;
    private List<Attendee> attendees = new ArrayList<>();

    private PointConceptRef challengePointConcept;
    private Reward reward;

    private ChallengeState state;
    private Map<ChallengeState, Date> stateDate = new HashMap<>();

    public GroupChallenge() {
        init(null);
    }
    public GroupChallenge(ChallengeState initialState) {
        init(initialState);
    }

    private void init(ChallengeState initialState) {
        if (initialState == null) {
            initialState = ChallengeState.ASSIGNED;
        }

        state = initialState;
        stateDate.put(state, new Date());

    }


    private String origin;
    private Date start;
    private Date end;
    private int priority;


    public GroupChallenge update(List<PlayerState> attendeeStates) {
        attendees.forEach(attendee -> {
            attendee.setChallengeScore(
                    challengeScore(attendee.getPlayerId(), challengePointConcept, attendeeStates));
        });
        return this;
    }

    public List<Attendee> winners() {
        List<String> winnerIds = new ArrayList<>();
        double max = 0;
       for(Attendee attendee : attendees){
            if (max < attendee.getChallengeScore()) {
                max = attendee.getChallengeScore();
                winnerIds.clear();
                winnerIds.add(attendee.getPlayerId());
            } else if (max == attendee.getChallengeScore()) {
                winnerIds.add(attendee.getPlayerId());
            }
        }
       
        winnerIds.forEach(id -> {
            attendees.stream().filter(a -> a.getPlayerId().equals(id)).findFirst()
                    .ifPresent(a -> a.setWinner(true));
        });

        return attendees.stream().filter(a -> a.isWinner()).collect(Collectors.toList());
    }

    private double challengeScore(String playerId, PointConceptRef pointConcept, List<PlayerState> attendeeStates) {
        Optional<PlayerState> playerState = attendeeStates.stream().filter(state -> state.getPlayerId().equals(playerId)).findFirst();
        
        return playerState.map(state -> {
            PointConcept challengePointConceptState =
                    state.pointConcept(pointConcept.getName());
            return challengePointConceptState.getPeriodScore(
                    pointConcept.getPeriod(), instantInChallenge(end));
        }).orElseThrow(() -> new IllegalArgumentException(
                String.format("attendeeStates doesn't contain player %s", playerId)));

    }

    private long instantInChallenge(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, -1);
        return cal.getTime().getTime();

    }

    public ChallengeConcept toChallengeConcept(String playerId) {
        ChallengeConcept ch = new ChallengeConcept();
        Optional<Attendee> player =
                attendees.stream().filter(a -> a.getPlayerId().equals(playerId)).findFirst();
        player.ifPresent(p -> {
            ch.setModelName(MODEL_NAME_COMPETITIVE_PERFORMANCE);
            ch.setName(instanceName);
            ch.setStart(start);
            ch.setEnd(end);
            ch.setOrigin(origin);
            ch.setPriority(priority);
            setChallengeState(ch, p);
            setFields(ch, p);
        });
        return ch;
    }

    private ChallengeConcept setFields(ChallengeConcept challenge, Attendee player) {
        challenge.getFields().put("challengeScoreName",
                challengePointConcept != null ? challengePointConcept.getName() : null);
        challenge.getFields().put("challengeScore", player.getChallengeScore());
        List<Map<String, Object>> otherAttendeeScores = new ArrayList<>();
        attendees.stream().filter(a -> !a.getPlayerId().equals(player.getPlayerId())).forEach(a -> {
            Map<String, Object> attendeeScore = new HashMap<>();
            attendeeScore.put("playerId", a.getPlayerId());
            attendeeScore.put("challengeScore", a.getChallengeScore());
            otherAttendeeScores.add(attendeeScore);
        });
        challenge.getFields().put("otherAttendeeScores", otherAttendeeScores);
        return challenge;

    }
    private ChallengeConcept setChallengeState(ChallengeConcept challenge, Attendee attendee) {
        challenge.setState(state);
        copyStateStoryIntoChallenge(ChallengeState.PROPOSED, challenge);
        copyStateStoryIntoChallenge(ChallengeState.ASSIGNED, challenge);
        
        if (challenge.getState() == ChallengeState.COMPLETED) {
            if(attendee.isWinner){
                challenge.setState(ChallengeState.COMPLETED);
                challenge.getStateDate().put(ChallengeState.COMPLETED,
                        stateDate.get(ChallengeState.COMPLETED));
            } else {
                challenge.setState(ChallengeState.FAILED);
                challenge.getStateDate().put(ChallengeState.FAILED,
                        stateDate.get(ChallengeState.COMPLETED));
            }
        }

        return challenge;

    }

    private void copyStateStoryIntoChallenge(ChallengeState state, ChallengeConcept challenge) {
        final Date dateOfTheState = stateDate.get(state);
        if (dateOfTheState != null) {
            challenge.getStateDate().put(state, dateOfTheState);
        }
    }

    public static class Attendee {
        private String playerId;
        private Role role;
        private boolean isWinner;
        private double challengeScore;
        private Date valuationTime;

        public enum Role {
            PROPOSER, GUEST
        }

        public String getPlayerId() {
            return playerId;
        }

        public void setPlayerId(String playerId) {
            this.playerId = playerId;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public boolean isWinner() {
            return isWinner;
        }

        public void setWinner(boolean isWinner) {
            this.isWinner = isWinner;
        }

        public double getChallengeScore() {
            return challengeScore;
        }

        public void setChallengeScore(double challengeScore) {
            this.challengeScore = challengeScore;
        }

        public Date getValuationTime() {
            return valuationTime;
        }

        public void setValuationTime(Date valuationTime) {
            this.valuationTime = valuationTime;
        }

    }

    public static class PointConceptRef {
        private String name;
        private String period;



        public PointConceptRef(String name, String period) {
            this.name = name;
            this.period = period;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    public PointConceptRef getChallengePointConcept() {
        return challengePointConcept;
    }

    public void setChallengePointConcept(PointConceptRef challengePointConcept) {
        this.challengePointConcept = challengePointConcept;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public ChallengeState getState() {
        return state;
    }

    public void setState(ChallengeState state) {
        this.state = state;
    }

    public Map<ChallengeState, Date> getStateDate() {
        return stateDate;
    }

    public void setStateDate(Map<ChallengeState, Date> stateDate) {
        this.stateDate = stateDate;
    }
}