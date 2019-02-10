package com.psl.classes;

/**
 * Created by yaqoob.khan on 1/10/2018.
 */

public class TeamVO {

    String id;
    String team_id;
    String team_name;
    String team_fixtures;
    String shortname;

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getTeam_fixtures() {
        return team_fixtures;
    }

    public void setTeam_fixtures(String team_fixtures) {
        this.team_fixtures = team_fixtures;
    }
}
