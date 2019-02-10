package com.psl.classes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class XMLParser {
    Document doc;

    public void parse(String xml) {
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        InputStream is;

        try {
            factory = DocumentBuilderFactory.newInstance();
            is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            builder = factory.newDocumentBuilder();
            doc = builder.parse(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PlayerProfileVO> getUserFantasyteam(String tbl) {
        String result = "";
        List<PlayerProfileVO> playerList = new ArrayList<PlayerProfileVO>();
        try {
            doc.getDocumentElement().normalize();
            NodeList nList = null;
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            if (tbl.equalsIgnoreCase("tbl_Login"))
                nList = doc.getElementsByTagName("tbl_Login");
            else
                nList = doc.getElementsByTagName("tbl_Login");
            int length = nList.getLength();

            for (int i = 0; i < length; i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    PlayerProfileVO mPlayer = new PlayerProfileVO();
                    try {
                        result = eElement.getElementsByTagName("id").item(0).getTextContent();
                        mPlayer.setId(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("player_id").item(0).getTextContent();
                        mPlayer.setPlayer_id(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("PlayerName").item(0).getTextContent();
                        mPlayer.setPlayer_name(result);
                    } catch (Exception e) {
                    }

                    try {
                        result = eElement.getElementsByTagName("Team_Id").item(0).getTextContent();
                        mPlayer.setTeam_id(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("nationality").item(0).getTextContent();
                        mPlayer.setNationality(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("Role").item(0).getTextContent();
                        mPlayer.setRole(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("Batting_style").item(0).getTextContent();
                        mPlayer.setBattingStyle(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("Matches").item(0).getTextContent();
                        mPlayer.setMatchesPlayed(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("innings").item(0).getTextContent();
                        mPlayer.setInnings(result);
                    } catch (Exception e) {
                    }

                    try {
                        result = eElement.getElementsByTagName("runs").item(0).getTextContent();
                        mPlayer.setRuns(result);
                    } catch (Exception e) {
                    }

                    try {
                        result = eElement.getElementsByTagName("Avg").item(0).getTextContent();
                        mPlayer.setAverage(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("hundreds").item(0).getTextContent();
                        mPlayer.setHundreds(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("fifties").item(0).getTextContent();
                        mPlayer.setFifties(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("thirties").item(0).getTextContent();
                        mPlayer.setThirties(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("SR").item(0).getTextContent();
                        mPlayer.setBatting_strike(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("overs").item(0).getTextContent();
                        mPlayer.setOvers(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("runs_conceded").item(0).getTextContent();
                        mPlayer.setRun_conceded(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("WKT").item(0).getTextContent();
                        mPlayer.setWickets(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("Maiden").item(0).getTextContent();
                        mPlayer.setMaiden(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("b_sr").item(0).getTextContent();
                        mPlayer.setBowling_strike_rate(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("economy").item(0).getTextContent();
                        mPlayer.setEconomy(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("wicket5").item(0).getTextContent();
                        mPlayer.setFive_wickets(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("fc_price").item(0).getTextContent();
                        mPlayer.setPrice(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("age").item(0).getTextContent();
                        mPlayer.setAge(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("is_captain").item(0).getTextContent();
                        mPlayer.setIsCaptain(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("is_mom").item(0).getTextContent();
                        mPlayer.setIsMom(result);
                    } catch (Exception e) {
                    }

                    try {
                        result = eElement.getElementsByTagName("is_gg").item(0).getTextContent();
                        mPlayer.setIsGoldengloves(result);
                    } catch (Exception e) {
                    }


                    try {
                        result = eElement.getElementsByTagName("is_purple_cap").item(0).getTextContent();
                        mPlayer.setIsPrurplecap(result);
                    } catch (Exception e) {
                    }


                    try {
                        result = eElement.getElementsByTagName("is_orange_cap").item(0).getTextContent();
                        mPlayer.setIsOrangecap(result);
                    } catch (Exception e) {
                    }


                    try {
                        result = eElement.getElementsByTagName("is_safety").item(0).getTextContent();
                        mPlayer.setIsSafety(result);
                    } catch (Exception e) {
                    }

                    try {
                        result = eElement.getElementsByTagName("is_iconic").item(0).getTextContent();
                        mPlayer.setIsIconic(result);
                    } catch (Exception e) {
                    }

                    try {
                        result = eElement.getElementsByTagName("is_team_safety").item(0).getTextContent();
                        mPlayer.setIsTeamSafety(result);
                    } catch (Exception e) {
                    }

                    try {
                        result = eElement.getElementsByTagName("points").item(0).getTextContent();
                        mPlayer.setP_points(result);
                    } catch (Exception e) {
                    }

                    try {
                        result = eElement.getElementsByTagName("total_points").item(0).getTextContent();
                        mPlayer.setTotal_points(result);
                    } catch (Exception e) {
                    }

                    playerList.add(mPlayer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerList;
    }


    public List<PlayerProfileVO> getPlayersData(String tbl) {
        String result = "";
        List<PlayerProfileVO> playerList = new ArrayList<PlayerProfileVO>();
        try {
            doc.getDocumentElement().normalize();
            NodeList nList = null;
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            if (tbl.equalsIgnoreCase("top_ten"))
                nList = doc.getElementsByTagName("top_ten");
            else
                nList = doc.getElementsByTagName("tbl_fixture");
            int length = nList.getLength();

            for (int i = 0; i < length; i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    PlayerProfileVO mPlayer = new PlayerProfileVO();
                    try {
                        result = eElement.getElementsByTagName("Id").item(0).getTextContent();
                        mPlayer.setId(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("player_id").item(0).getTextContent();
                        mPlayer.setPlayer_id(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("PlayerName").item(0).getTextContent();
                        mPlayer.setPlayer_name(result);
                    } catch (Exception e) {
                    }

                    try {
                        result = eElement.getElementsByTagName("Team_Id").item(0).getTextContent();
                        mPlayer.setTeam_id(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("nationality").item(0).getTextContent();
                        mPlayer.setNationality(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("Role").item(0).getTextContent();
                        mPlayer.setRole(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("Batting_style").item(0).getTextContent();
                        mPlayer.setBattingStyle(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("Matches").item(0).getTextContent();
                        mPlayer.setMatchesPlayed(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("innings").item(0).getTextContent();
                        mPlayer.setInnings(result);
                    } catch (Exception e) {
                    }

                    try {
                        result = eElement.getElementsByTagName("runs").item(0).getTextContent();
                        mPlayer.setRuns(result);
                    } catch (Exception e) {
                    }

                    try {
                        result = eElement.getElementsByTagName("Avg").item(0).getTextContent();
                        mPlayer.setAverage(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("hundreds").item(0).getTextContent();
                        mPlayer.setHundreds(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("fifties").item(0).getTextContent();
                        mPlayer.setFifties(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("thirties").item(0).getTextContent();
                        mPlayer.setThirties(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("SR").item(0).getTextContent();
                        mPlayer.setBatting_strike(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("overs").item(0).getTextContent();
                        mPlayer.setOvers(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("runs_conceded").item(0).getTextContent();
                        mPlayer.setRun_conceded(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("WKT").item(0).getTextContent();
                        mPlayer.setWickets(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("Maiden").item(0).getTextContent();
                        mPlayer.setMaiden(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("b_sr").item(0).getTextContent();
                        mPlayer.setBowling_strike_rate(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("economy").item(0).getTextContent();
                        mPlayer.setEconomy(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("wicket5").item(0).getTextContent();
                        mPlayer.setFive_wickets(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("fc_price").item(0).getTextContent();
                        mPlayer.setPrice(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("age").item(0).getTextContent();
                        mPlayer.setAge(result);
                    } catch (Exception e) {
                    }

                    try {
                        result = eElement.getElementsByTagName("points").item(0).getTextContent();
                        mPlayer.setPoints(result);
                    } catch (Exception e) {
                    }

                    playerList.add(mPlayer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerList;
    }


    public List<FixturesVO> getFixturesData() {
        String result = "";
        List<FixturesVO> fixtureList = new ArrayList<FixturesVO>();
        try {
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("tbl_fixture");
            int length = nList.getLength();

            for (int i = 0; i < length; i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    FixturesVO mFixture = new FixturesVO();

                    try {
                        result = eElement.getElementsByTagName("match_id").item(0).getTextContent();
                        mFixture.setId(result);
                    } catch (Exception e) {
                    }

                    try {
                        result = eElement.getElementsByTagName("Date_").item(0).getTextContent();
                        mFixture.setDate(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("Match").item(0).getTextContent();
                        mFixture.setMatch(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("venue_name").item(0).getTextContent();
                        mFixture.setVenue_name(result);
                    } catch (Exception e) {
                    }
                    fixtureList.add(mFixture);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fixtureList;
    }

    public List<JsLocationsVO> getLocationsData() {
        String result = "";
        List<JsLocationsVO> list = new ArrayList<JsLocationsVO>();
        try {
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("tbl_distance");
            int length = nList.getLength();

            for (int i = 0; i < length; i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    JsLocationsVO mFixture = new JsLocationsVO();
                    try {
                        result = eElement.getElementsByTagName("lat").item(0).getTextContent();
                        mFixture.setLatitude(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("lng").item(0).getTextContent();
                        mFixture.setLongitude(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("address").item(0).getTextContent();
                        mFixture.setAddress(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("agent_name").item(0).getTextContent();
                        mFixture.setLoc_name(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("province").item(0).getTextContent();
                        mFixture.setArea(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("region").item(0).getTextContent();
                        mFixture.setCategory(result);
                    } catch (Exception e) {
                    }
                    list.add(mFixture);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<InventoryClass> getInventroyCount() {
        String result = "";
        List<InventoryClass> inventroyList = new ArrayList<InventoryClass>();
        try {
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("boosters");
            int length = nList.getLength();

            for (int i = 0; i < length; i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    InventoryClass mFixture = new InventoryClass();
                    try {
                        result = eElement.getElementsByTagName("item_id").item(0).getTextContent();
                        mFixture.setId(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("item_name").item(0).getTextContent();
                        mFixture.setName(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("amount").item(0).getTextContent();
                        mFixture.setPrice(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("powers").item(0).getTextContent();
                        mFixture.setPower(result);
                    } catch (Exception e) {
                    }
                    inventroyList.add(mFixture);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inventroyList;
    }


    public List<LeaderboarPositionVO> getLeaderboardPoistionData() {
        String result = "";
        List<LeaderboarPositionVO> dataList = new ArrayList<LeaderboarPositionVO>();
        try {
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("lead");
            int length = nList.getLength();

            for (int i = 0; i < length; i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    LeaderboarPositionVO data = new LeaderboarPositionVO();
                    try {
                        result = eElement.getElementsByTagName("fc_team_id").item(0).getTextContent();
                        data.setId(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("points").item(0).getTextContent();
                        data.setPoints(result);
                    } catch (Exception e) {
                    }

                    try {
                        result = eElement.getElementsByTagName("fc_team_name").item(0).getTextContent();
                        data.setUsername(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("position").item(0).getTextContent();
                        data.setPosition(result);
                    } catch (Exception e) {
                    }
                    dataList.add(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public User getMyProfile() {
        String result = "";
        User mUser = new User();
        try {
            //Document doc = dom.getDocumentElement()
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());//tbls_userExist
            NodeList nList = doc.getElementsByTagName("tbls_userExist");//tbl_Login

            Node nNode = nList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                try {
                    result = eElement.getElementsByTagName("userid").item(0).getTextContent();
                    mUser.setId(result);
                } catch (Exception e) {
                }
                    /*try {
                        result = eElement.getElementsByTagName("username").item(0).getTextContent();
                        mUser.setId(result);
                    } catch (Exception e) {
                    }*/
                try {
                    result = eElement.getElementsByTagName("fullname").item(0).getTextContent();
                    mUser.setName(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("email").item(0).getTextContent();
                    mUser.setEmail(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("first_name").item(0).getTextContent();
                    mUser.setFirst_name(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("last_name").item(0).getTextContent();
                    mUser.setLast_name(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("gender").item(0).getTextContent();
                    mUser.setGender(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("age").item(0).getTextContent();
                    mUser.setAge(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("ContactNo").item(0).getTextContent();
                    mUser.setCellNo(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("PictureURL").item(0).getTextContent();
                    mUser.setPicture(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("CNIC").item(0).getTextContent();
                    mUser.setCnic(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("wallet_id").item(0).getTextContent();
                    mUser.setJswallet(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("fc_team_name").item(0).getTextContent();
                    mUser.setTeam_name(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("transfers").item(0).getTextContent();
                    mUser.setSwaps(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("points").item(0).getTextContent();
                    mUser.setUser_points(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("fc_team_id").item(0).getTextContent();
                    mUser.setTeam_id(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("position").item(0).getTextContent();
                    mUser.setUser_ranking(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("purple_cap").item(0).getTextContent();
                    mUser.setUser_purple_cap(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("orange_cap").item(0).getTextContent();
                    mUser.setUser_orange_cap(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("golden_gloves").item(0).getTextContent();
                    mUser.setUser_goldengloves(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("iconic_player").item(0).getTextContent();
                    mUser.setUser_iconic_player(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("team_safety").item(0).getTextContent();
                    mUser.setUser_teamsafety(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("player_safety").item(0).getTextContent();
                    mUser.setUser_playersafety(result);
                } catch (Exception e) {
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mUser;
    }
    public List<NotificationClass> getNotifications() {
        String result = "";
        List<NotificationClass> playerList = new ArrayList<NotificationClass>();
        try {
            doc.getDocumentElement().normalize();
            NodeList nList = null;
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());

            nList = doc.getElementsByTagName("tbl_Login");
            int length=nList.getLength();

            for (int i = 0 ; i < length; i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode;
                    NotificationClass mPlayer = new NotificationClass();

                    try {
                        result = eElement.getElementsByTagName("not_id").item(0).getTextContent();
                        mPlayer.setId(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("user_id").item(0).getTextContent();
                        //    mPlayer.setName(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("body").item(0).getTextContent();
                        mPlayer.setBody(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("title").item(0).getTextContent();
                        mPlayer.setTitle(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("cd").item(0).getTextContent();
                        mPlayer.setDate(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("is_read").item(0).getTextContent();
                        mPlayer.setIsRead(result);
                    } catch (Exception e) {
                    }
                    playerList.add(mPlayer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerList;
    }
    public User getMyProfileLogin() {
        String result = "";
        User mUser = new User();
        try {
            //Document doc = dom.getDocumentElement()
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());//tbls_userExist
            NodeList nList = doc.getElementsByTagName("tbl_Login");//tbl_Login

            Node nNode = nList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                try {
                    result = eElement.getElementsByTagName("userid").item(0).getTextContent();
                    mUser.setId(result);
                } catch (Exception e) {
                }
                    try {
                        result = eElement.getElementsByTagName("fc_team_name").item(0).getTextContent();
                        mUser.setTeam_name(result);
                    } catch (Exception e) {
                    }
                try {
                    result = eElement.getElementsByTagName("fullname").item(0).getTextContent();
                    mUser.setName(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("email").item(0).getTextContent();
                    mUser.setEmail(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("first_name").item(0).getTextContent();
                    mUser.setFirst_name(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("last_name").item(0).getTextContent();
                    mUser.setLast_name(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("gender").item(0).getTextContent();
                    mUser.setGender(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("age").item(0).getTextContent();
                    mUser.setAge(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("ContactNo").item(0).getTextContent();
                    mUser.setCellNo(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("PictureURL").item(0).getTextContent();
                    mUser.setPicture(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("CNIC").item(0).getTextContent();
                    mUser.setCnic(result);
                } catch (Exception e) {
                }
               try {
                    result = eElement.getElementsByTagName("wallet_id").item(0).getTextContent();
                     mUser.setJswallet(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("transfers").item(0).getTextContent();
                    mUser.setSwaps(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("points").item(0).getTextContent();
                    mUser.setUser_points(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("fc_team_id").item(0).getTextContent();
                    mUser.setTeam_id(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("position").item(0).getTextContent();
                    mUser.setUser_ranking(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("purple_cap").item(0).getTextContent();
                    mUser.setUser_purple_cap(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("orange_cap").item(0).getTextContent();
                    mUser.setUser_orange_cap(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("golden_gloves").item(0).getTextContent();
                    mUser.setUser_goldengloves(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("iconic_player").item(0).getTextContent();
                    mUser.setUser_iconic_player(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("team_safety").item(0).getTextContent();
                    mUser.setUser_teamsafety(result);
                } catch (Exception e) {
                }

                try {
                    result = eElement.getElementsByTagName("player_safety").item(0).getTextContent();
                    mUser.setUser_playersafety(result);
                } catch (Exception e) {
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mUser;
    }
    public TeamInventory getDealOfDay() {
        String result = "";
        TeamInventory inventory = new TeamInventory();
        try {
            //Document doc = dom.getDocumentElement()
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("log");

            Node nNode = nList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                try {
                    result = eElement.getElementsByTagName("deal_id").item(0).getTextContent();
                    inventory.setId(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("purple_cap").item(0).getTextContent();
                    if(!result.equals("0"))
                    inventory.addItem("purple_cap",result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("orange_cap").item(0).getTextContent();
                    if(!result.equals("0"))
                        inventory.addItem("orange_cap",result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("golden_gloves").item(0).getTextContent();
                    if(!result.equals("0"))
                        inventory.addItem("golden_gloves",result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("golden_golves").item(0).getTextContent();
                    if(!result.equals("0"))
                        inventory.addItem("golden_gloves",result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("iconic_player").item(0).getTextContent();
                    if(!result.equals("0"))
                        inventory.addItem("iconic_player",result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("team_safety").item(0).getTextContent();
                    if(!result.equals("0"))
                        inventory.addItem("team_safety",result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("player_safety").item(0).getTextContent();
                    if(!result.equals("0"))
                        inventory.addItem("player_safety",result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("transfers").item(0).getTextContent();
                    if(!result.equals("0"))
                        inventory.addItem("transfers",result);
                   // inventory.setTeamSafety(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("amount").item(0).getTextContent();
                        inventory.setAmount(result);
                    //inventory.setTeamSafety(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("discount").item(0).getTextContent();
                    inventory.setDiscount(result);
                    //inventory.setTeamSafety(result);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inventory;
    }
    public TeamInventory getInventoryV2() {
        String result = "";
        TeamInventory inventory = new TeamInventory();
        try {
            //Document doc = dom.getDocumentElement()
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("GET_INVENTOrRY");

            Node nNode = nList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                try {
                    result = eElement.getElementsByTagName("user_id").item(0).getTextContent();
                    inventory.setId(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("purple_cap").item(0).getTextContent();
                    inventory.addItem("purple_cap",result);
                    inventory.setPurpleCap(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("orange_cap").item(0).getTextContent();
                    inventory.addItem("orange_cap",result);
                    inventory.setOrangeCap(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("golden_gloves").item(0).getTextContent();
                    inventory.addItem("golden_gloves",result);
                    inventory.setGoldenGloves(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("iconic_player").item(0).getTextContent();
                    inventory.addItem("iconic_player",result);
                    inventory.setIconic(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("team_safety").item(0).getTextContent();
                    inventory.addItem("team_safety",result);
                    inventory.setTeamSafety(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("player_safety").item(0).getTextContent();
                    inventory.addItem("player_safety",result);
                    inventory.setPlayerSafety(result);
                } catch (Exception e) {
                }
                try {
                    result = eElement.getElementsByTagName("transfers").item(0).getTextContent();
                    inventory.addItem("transfers",result);
                    //inventory.setTeamSafety(result);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inventory;
    }
    public List<InventoryClass> getInvetoryToShop() {
        String result = "";
        List<InventoryClass> playerList = new ArrayList<InventoryClass>();
        try {
            doc.getDocumentElement().normalize();
            NodeList nList = null;
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());

            nList = doc.getElementsByTagName("boosters");
            int length=nList.getLength();

            for (int i = 0 ; i < length; i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode;
                    InventoryClass mPlayer = new InventoryClass();

                    try {
                        result = eElement.getElementsByTagName("item_id").item(0).getTextContent();
                        mPlayer.setId(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("item_name").item(0).getTextContent();
                        mPlayer.setName(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("amount").item(0).getTextContent();
                        mPlayer.setPrice(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("powers").item(0).getTextContent();
                        mPlayer.setPower(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("description").item(0).getTextContent();
                        mPlayer.setDescription(result);
                    } catch (Exception e) {
                    }

                    mPlayer.setImage(JSUtils.getItemResID(mPlayer.getName()));
                    playerList.add(mPlayer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerList;
    }

    public List<BoosterVO> getMyBoosters() {
        String result = "";
        List<BoosterVO> playerList = new ArrayList<BoosterVO>();
        try {
            doc.getDocumentElement().normalize();
            NodeList nList = null;
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());

            nList = doc.getElementsByTagName("boosters");
            int length=nList.getLength();

            for (int i = 0 ; i < length; i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode;
                    BoosterVO mPlayer = new BoosterVO();

                    try {
                        result = eElement.getElementsByTagName("booster_id").item(0).getTextContent();
                        mPlayer.setID(result);
                    } catch (Exception e) {
                    }
                    try {
                        result = eElement.getElementsByTagName("points").item(0).getTextContent();
                        mPlayer.setBoosterPoints(result);
                    } catch (Exception e) {
                    }

                    playerList.add(mPlayer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerList;
    }
}