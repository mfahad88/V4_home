package com.psl.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.psl.fantasy.league.season2.InfoDialog;
import com.psl.fantasy.league.season2.Profile;
import com.psl.fantasy.league.season2.R;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by aamir.shehzad on 5/2/2017.
 */

public class Config {

    // global topic to receive app wide push notifications//
    public static final String TOPIC_GLOBAL = "global";


    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String GUID = "GUID";
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String FCM_Reg_ID = "FCM_REGID";
    public static final String USER_BUDGET = "USER_BUDGET";
    public static final String USER_RANK = "USER_RANK";
    public static String SELECTED_PLAYERS_FILE_NAME = "selested_players.ers";
    public static final String USER_KEY = "user_key";

    public static String RULES_TEXT = "" ;
    public static Fragment prevFragment = null;
    public static final String SHARED_PREF = "PSL_FANTSY";
    public static final String SWAP_COUNT = "swap_count";
    public static final String TEAM_FORMATION = "team_formation";
    public static final String USER_ID = "USER_ID";
    public static final String TEAM_ID = "TEAM_ID";
    public static final String NAME = "NAME";
    public static final String CNIC = "CNIC";
    public static final String EMAIL = "EMAIL";
    public static final String PICTURE = "PICTURE";
    public static final String JSWALLET = "JSWALLET";
    public static final String CELL_NO = "CELL_NO";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String GENDER = "GENDER";
    public static final String AGE = "AGE";
    public static String JS_WALLET_ACCOUNT = "JS_WALLET_ACCOUNT";
    public static final String IMAGE_DATA = "image_data";

    public static final String JS_Mobile_Number = "Mobile_Number";
    public static final String JS_OTP_Purpose = "OTP_Purpose";
    public static final String JS_Auth_Header = "Auth_Header";
    public static final String JS_Encrypted_OTP = "JS_Encrypted_OTP";
    public static final String JS_Action = "JS_Action";
    public static final String JS_Item_Purchase_Res = "JS_Item_Purchase_Res";
    public static final String JS_Item_Purchase_Name = "JS_Item_Purchase_Name";
    public static final String JS_Item_Purchase_Price = "JS_Item_Purchase_Price";

    public static boolean WALLET_TIMEOUT = false;
    public static final String USER_ORANGE_CAP = "OC";
    public static final String USER_PURPLE_CAP = "PC";
    public static final String USER_ICONIC_PLAYER = "IP";
    public static final String USER_GOLDEN_GLOVES = "GG";
    public static final String USER_TEAM_SAFETY = "TS";
    public static final String USER_PLAYER_SAFETY = "PS";
    public static Fragment homeFragment=null;
    public static String RULE_BOOK_TEXT = "";
    public static final NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
    public static List<FixturesVO> FIXTURES_LIST;
    public static boolean SHOULD_REFRESH_TEAM = true;


    public static final String AGEes = "AGE";

    public static String OTPSender = "8012";

    public static final String Group_Id = "GROUP_ID";
    public static int Expirelimit = 5;

    public static final int match_time = 29;
    public static final int post_match_time = 29;

    public static final String Active_Group_Name = "Active_Group_Name";
    public static final String Active_Group_ID = "Active_Group_ID";
    public static final String TEAM_NAME = "USER_TEAM_NAME";
    public static String JS_TransactionID = "Transaction ID";
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
    public static final String TERMS_CONDITIONS = "1. JSFL\n" +
            "JSFL is the brand of JS Bank Limited (\"JS\"). JSFL as used herein shall be construed as a collective reference to the JSFL App.\n" +
            "2. Usage of JSFL\n" +
            "•\tAny person utilizing JSFL or the JSFL App (\"User\") for availing the online information and database retrieval services, including, gaming services, offered therein \"JSFL Services\") or participating in the various leagues and, games (including fantasy games), being conducted on JSFL (\"League(s)\") shall be bound by these Terms and Conditions, and all other rules, regulations and terms of use referred to herein or provided by JSFL in relation to any JSFL Services.\n" +
            "•\tJSFL shall be entitled to modify these Terms and Conditions, rules, regulations and terms of use referred to herein or provided by JSFL in relation to any JSFL Services, at any time, by posting the same on JSFL. Use of JSFL constitutes the User's acceptance of such Terms and Conditions, rules, regulations and terms of use referred to herein or provided by JSFL in relation to any JSFL Services, as may be amended from time to time. JSFL may also notify the User of any change or modification in these Terms and Conditions, rules, regulations and terms of use referred to herein or provided by JSFL, by way of sending an email to the User's registered email address or posting notifications in the User accounts. The User may then exercise the options provided in such an email or notification to indicate non-acceptance of the modified Terms and Conditions, rules, regulations and terms of use referred to herein or provided by JSFL. If such options are not exercised by the User within the time frame prescribed in the email or notification, the User will be deemed to have accepted the modified Terms and Conditions, rules, regulations and terms of use referred to herein or provided by JSFL.\n" +
            "•\tCertain JSFL Services being provided on JSFL may be subject to additional rules and regulations set down in that respect. To the extent that these Terms and Conditions are inconsistent with the additional conditions set down, the additional conditions shall prevail.\n" +
            "•\tJSFL may, at its sole and absolute discretion:\n" +
            "\uF0A7\tRestrict, suspend, or terminate any User's access to all or any part of JSFL or JSFL Services;\n" +
            "\uF0A7\tChange, suspend, or discontinue all or any part of the JSFL Services;\n" +
            "\uF0A7\tReject, move, or remove any material that may be submitted by a User;\n" +
            "\uF0A7\tMove or remove any content that is available on JSFL;\n" +
            "\uF0A7\tDeactivate or delete a User's account and all related information and files on the account;\n" +
            "\uF0A7\tEstablish general practices and limits concerning use of JSFL;\n" +
            "\uF0A7\tRevise or make additions to the roster of players available for selection in a League on account of revisions to the roster of players involved in the relevant Sports Event;\n" +
            "\uF0A7\tAssign its rights and liabilities to all User accounts hereunder to any entity (post intimation of such assignment shall be sent to all Users to their registered email ids)\n" +
            "•\tIn the event any User breaches, or JSFL reasonably believes that such User has breached these Terms and Conditions, or has illegally or improperly used JSFL or the JSFL Services, JSFL may, at its sole and absolute discretion, and without any notice to the User, restrict, suspend or terminate such User's access to all or any part of JSFL or the JSFL Services, deactivate or delete the User's account and all related information on the account, delete any content posted by the User on JSFL and further, take technical and legal steps as it deems necessary.\n" +
            "3. Intellectual Property\n" +
            "•\tJSFL includes a combination of content created by JSFL, its partners, licensors, associates and/or Users. The intellectual property rights (\"Intellectual Property Rights\") in all software underlying JSFL and the JSFL Services and material published on JSFL, including (but not limited to) games, Leagues, software, advertisements, written content, photographs, graphics, images, illustrations, marks, logos, audio or video clippings and Flash animation, is owned by JSFL, its partners, licensors and/or associates. Users may not modify, publish, transmit, participate in the transfer or sale of, reproduce, create derivative works of, distribute, publicly perform, publicly display, or in any way exploit any of the materials or content on JSFL either in whole or in part without express written license from JSFL.\n" +
            "•\tUsers may request permission to use any JSFL content by writing in to JSFL Helpdesk.\n" +
            "•\tUsers are solely responsible for all materials (whether publicly posted or privately transmitted) that they upload, post, e-mail, transmit, or otherwise make available on JSFL(\"Users' Content\"). Each User represents and warrants that he/she owns all Intellectual Property Rights in the User's Content and that no part of the User's Content infringes any third party rights. Users further confirm and undertake to not display or use of the names, logos, marks, labels, trademarks, copyrights or intellectual and proprietary rights of any third party on JSFL. Users agree to indemnify and hold harmless JSFL, its directors, employees, affiliates and assigns against all costs, damages, loss and harm including towards litigation costs and counsel fees, in respect of any third party claims that may be initiated including for infringement of Intellectual Property Rights arising out of such display or use of the names, logos, marks, labels, trademarks, copyrights or intellectual and proprietary rights on JSFL, by such User or through the User's commissions or omissions.\n" +
            "•\tUsers hereby grant to JSFL and its affiliates, partners, licensors and associates a worldwide, irrevocable, royalty-free, non-exclusive, sub-licensable license to use, reproduce, create derivative works of,distribute, publicly perform, publicly display, transfer, transmit, and/or publish Users' Content for any of the following purposes:\n" +
            "\uF0A7\tdisplaying Users' Content on JSFL\n" +
            "\uF0A7\tdistributing Users' Content, either electronically or via other media, to other Users seeking to download or otherwise acquire it, and/or\n" +
            "\uF0A7\tstoring Users' Content in a remote database accessible by end users, for a charge.\n" +
            "\uF0A7\tThis license shall apply to the distribution and the storage of Users' Content in any form, medium, or technology.\n" +
            "•\tAll names, logos, marks, labels, trademarks, copyrights or intellectual and proprietary rights on JSFL(s) belonging to any person (including User), entity or third party are recognized as proprietary to the respective owners and any claims, controversy or issues against these names, logos, marks, labels, trademarks, copyrights or intellectual and proprietary rights must be directly addressed to the respective parties under notice to JSFL.\n" +
            "4. Third Party Sites, Services and Products\n" +
            "•\tJSFL may contain links to other Internet sites owned and operated by third parties. Users' use of each of those sites is subject to the conditions, if any, posted by the sites. JSFL does not exercise control over any Internet sites apart from JSFL, and cannot be held responsible for any content residing in any third party Internet site. JSFL's inclusion of third-party content or links to third-party Internet sites is not an endorsement by JSFL of such third-party Internet site.\n" +
            "•\tUsers' correspondence, transactions or related activities with third parties, including payment providers and verification service providers, are solely between the User and that third party. Users' correspondence, transactions and usage of the services of such third party shall be subject to the terms and conditions, policies and other service terms adopted/implemented by such third party, and the User shall be solely responsible for reviewing the same prior to transacting or availing of the services of such third party. User agrees that JSFL will not be responsible or liable for any loss or damage of any sort incurred as a result of any such transactions with third parties. Any questions, complaints, or claims related to any third party product or service should be directed to the appropriate vendor.\n" +
            "•\tJSFL contains content that is created by JSFL as well as content provided by third parties. JSFL does not guarantee the accuracy, integrity, quality of the content provided by third parties and such content may not relied upon by the Users in utilizing the JSFL Services provided on JSFL including while participating in any of the leagues hosted on JSFL.\n" +
            "5. Privacy Policy\n" +
            "All information collected from Users, such as registration and credit card information, is subject to JSFL's Privacy Policy which is available at Privacy Policy\n" +
            "6. User Conduct\n" +
            "•\tUsers agree to abide by these Terms and Conditions and all other rules, regulations and terms of use of the Website. In the event User does not abide by these Terms and Conditions and all other rules, regulations and terms of use, JSFL may, at its sole and absolute discretion, take necessary remedial action, including but not limited to:\n" +
            "\uF0A7\trestricting, suspending, or terminating any User's access to all or any part of JSFL Services;\n" +
            "\uF0A7\tdeactivating or deleting a User's account and all related information and files on the account. Any amount remaining unused in the User's Game account or Winnings Account on the date of deactivation or deletion shall be transferred to the User's bank account on record with JSFL subject to a processing fee (if any) applicable on such transfers as set out herein; or\n" +
            "\uF0A7\trefraining from awarding any prize(s) to such User.\n" +
            "•\tUsers agree to provide true, accurate, current and complete information at the time of registration and at all other times (as required by JSFL). Users further agree to update and keep updated their registration information.\n" +
            "•\tA User shall not register or operate more than one User account with JSFL.\n" +
            "•\tUsers agree to ensure that they can receive all communication from JSFL by marking e-mails from JSFL as part of their \"safe senders\" list. JSFL shall not be held liable if any e-mail remains unread by a User as a result of such e-mail getting delivered to the User's junk or spam folder.\n" +
            "•\tAny password issued by JSFL to a User may not be revealed to anyone else. Users may not use anyone else's password. Users are responsible for maintaining the confidentiality of their accounts and passwords. Users agree to immediately notify JSFL of any unauthorized use of their passwords or accounts or any other breach of security.\n" +
            "•\tUsers agree to exit/log-out of their accounts at the end of each session. JSFL shall not be responsible for any loss or damage that may result if the User fails to comply with these requirements.\n" +
            "•\tUsers agree not to use cheats, exploits, automation, software, bots, hacks or any unauthorised third party software designed to modify or interfere with JSFL Services and/or JSFL experience or assist in such activity.\n" +
            "•\tUsers agree not to copy, modify, rent, lease, loan, sell, assign, distribute, reverse engineer, grant a security interest in, or otherwise transfer any right to the technology or software underlying JSFL or JSFL Services.\n" +
            "•\tUsers agree that without JSFL's express written consent, they shall not modify or cause to be modified any files or software that are part of JSFL's Services.\n" +
            "•\tUsers agree not to disrupt, overburden, or aid or assist in the disruption or overburdening of (a) any computer or server used to offer or support JSFL or the JSFL Services (each a \"Server\"); or (2) the enjoyment of JSFL Services by any other User or person.\n" +
            "•\tUsers agree not to institute, assist or become involved in any type of attack, including without limitation to distribution of a virus, denial of service, or other attempts to disrupt JSFL Services or any other person's use or enjoyment of JSFL Services.\n" +
            "•\tUsers shall not attempt to gain unauthorised access to the User accounts, Servers or networks connected to JSFL Services by any means other than the User interface provided by JSFL, including but not limited to, by circumventing or modifying, attempting to circumvent or modify, or encouraging or assisting any other person to circumvent or modify, any security, technology, device, or software that underlies or is part of JSFL Services.\n" +
            "•\tWithout limiting the foregoing, Users agree not to use JSFL for any of the following:\n" +
            "\uF0A7\tTo engage in any obscene, offensive, indecent, racial, communal, anti-national, objectionable, defamatory or abusive action or communication;\n" +
            "\uF0A7\tTo harass, stalk, threaten, or otherwise violate any legal rights of other individuals;\n" +
            "\uF0A7\tTo publish, post, upload, e-mail, distribute, or disseminate (collectively, \"Transmit\") any inappropriate, profane, defamatory, infringing, obscene, indecent, or unlawful content;\n" +
            "\uF0A7\tTo Transmit files that contain viruses, corrupted files, or any other similar software or programs that may damage or adversely affect the operation of another person's computer, JSFL, any software, hardware, or telecommunications equipment;\n" +
            "\uF0A7\tTo advertise, offer or sell any goods or services for any commercial purpose on JSFL without the express written consent of JSFL;\n" +
            "\uF0A7\tTo Transmit content regarding services, products, surveys, leagues, pyramid schemes, spam, unsolicited advertising or promotional materials, or chain letters;\n" +
            "\uF0A7\tTo advertise, offer or sell any goods or services for any commercial purpose on JSFL without the express written consent of JSFL;\n" +
            "\uF0A7\tTo Transmit content regarding services, products, surveys, leagues, pyramid schemes, spam, unsolicited advertising or promotional materials, or chain letters;\n" +
            "\uF0A7\tTo download any file, recompile or disassemble or otherwise affect our products that you know or reasonably should know cannot be legally obtained in such manner;\n" +
            "\uF0A7\tTo falsify or delete any author attributions, legal or other proper notices or proprietary designations or labels of the origin or the source of software or other material;\n" +
            "\uF0A7\tTo restrict or inhibit any other user from using and enjoying any public area within our sites;\n" +
            "\uF0A7\tTo collect or store personal information about other Users;\n" +
            "\uF0A7\tTo interfere with or disrupt JSFL, servers, or networks;\n" +
            "\uF0A7\tTo impersonate any person or entity, including, but not limited to, a representative of JSFL, or falsely state or otherwise misrepresent User's affiliation with a person or entity;\n" +
            "\uF0A7\tTo forge headers or manipulate identifiers or other data in order to disguise the origin of any content transmitted through JSFL or to manipulate User's presence on JSFL(s);\n" +
            "\uF0A7\tTo take any action that imposes an unreasonably or disproportionately large load on our infrastructure;\n" +
            "•\tIf a User chooses a username that, in JSFL's considered opinion is obscene, indecent, abusive or that might subject JSFL to public disparagement or scorn, JSFL reserves the right, without prior notice to the User, to change such username and intimate the User or delete such username and posts from JSFL, deny such User access to JSFL, or any combination of these options.\n" +
            "•\tUnauthorized access to JSFL is a breach of these Terms and Conditions, and a violation of the law. Users agree not to access JSFL by any means other than through the interface that is provided by JSFL for use in accessing JSFL. Users agree not to use any automated means, including, without limitation, agents, robots, scripts, or spiders, to access, monitor, or copy any part of our sites, except those automated means that we have approved in advance and in writing.\n" +
            "•\tUse of JSFL is subject to existing laws and legal processes. Nothing contained in these Terms and Conditions shall limit JSFL's right to comply with governmental, court, and law-enforcement requests or requirements relating to Users' use of JSFL.\n" +
            "•\tUsers may contact JSFL Helpdesk with problems or questions, as appropriate.\n" +
            "•\tPersons below the age of eighteen (18) years are required to seek permission or consent from their parents or legal guardians before furnishing data, participating or entering on JSFL or the JSFL Services or inter alia, in the league.season2, uploading pictures, playing games or being part, directly or indirectly, of any activity on JSFL. Entry to JSFL without consent from parent/s or legal guardian and consequent participation in any activity on JSFL Website is not permitted and such person is subject to disqualification at the sole and absolute discretion of JSFL, whenever it comes to the knowledge of JSFL.\n" +
            "•\tJSFL believes that parents should supervise their children's online activities and consider using parental control tools available from online services and software manufacturers that help provide a child-friendly online environment. These tools can also prevent children from disclosing online their name, address and other personal information without parental permission.\n" +
            "•\tAlthough persons below the age of 18 years are allowed to use certain JSFL Services on the JSFL with the consent of their parent/s or legal guardians, they may not (where expressly stated in the rules of the League) participate in League(s) hosted by JSFL.\n" +
            "•\tJSFL may not be held responsible for any content contributed by Users on the JSFL.\n" +
            "7. Registration\n" +
            "•\tIn order to register for the League(s), Participants are required to accurately provide the following information:\n" +
            "o\tFull Name\n" +
            "o\tTeam Name(s)\n" +
            "o\tE-mail address\n" +
            "o\tPassword\n" +
            "o\tGender\n" +
            "o\tDate of birth\n" +
            "•\tParticipants are also required to confirm that they have read, and shall abide by, these Terms and Conditions.\n" +
            "•\tOnce the Participants have entered the above information, and clicked on the \"register\" tab, and such Participants are above the age of 18 years, they are sent an email confirming their registration and containing their login information.\n" +
            "8. League(s), Participation and Prizes\n" +
            "•\tAs part of its services, JSFL may also conduct league.season2(s) on the JSFL. \n" +
            "Currently, JSFL provides A fantasy cricket game. Individual users wishing to participate in the JSFL Services (\"Participants\") are invited to create their own fantasy team (\"Team/s\") consisting of real life cricketers (as applicable) involved in the real-life cricket match (as applicable. JSFL offers Participants fantasy game League(s) relating to PSL, and Participants can participate in such League(s) with their Team. Teams are awarded points on the basis of the real life cricketers' (as applicable) performances at the end of a designated match, round or tournament of the League. The Participant(s) whose Team(s) have achieved the highest aggregate score(s) in the League(s) shall be declared winners (\"Winners\"). In certain pre-specified Leagues, JSFL may declare more than one Winner and distribute prizes to such Winners in increasing order of their Team's aggregate score at the end of the designated round(s) of the Leagues. \n" +
            "The League(s) across the JSFL Services shall, in addition to the Terms and Conditions, rules and regulations mentioned herein, be governed by \"Fantasy Rules\" available at How To Play - Fantasy Cricket \n" +
            "•\tCurrently, the League(s) made available by JSFL Users may participate in the League(s) as provided in the App. The Participant with the highest aggregate points at the end of the pre-determined round shall be eligible to win a pre-designated prize, as stated in the App.\n" +
            "•\tParticipants acknowledge and agree that they may enter only one Team in any League offered in relation to a PSL. The Participant will be permitted to edit or revise their Team for participation. In addition, it is expressly clarified that JSFL may, from time to time, restrict the maximum number of Team that may be created by a single User account (for each format of the JSFL Services), in each case to such number as determined by JSFL in its sole discretion.\n" +
            "•\tJSFL provides League(s). in two separate formats of JSFL Services, (1) as a public league.season2 where Users can participate in a league.season2 with other Users without any restriction on participation and (2) private league.season2, where Users can invite specific Users into a League and restrict participation to such invited Users. All rules applicable to League(s) as set out herein shall be applicable to both formats of the League(s).\n" +
            "•\tPrivate leagues\n" +
            "o\tIn the Private league.season2 format of the League(s), JSFL enables Users to create a league.season2 (\"Private league.season2\") and invite other users, whether existing Users or otherwise, (\"Invited User\") to enlist their Team and participate in the League(s). Users may create a Private league.season2 to consist of a pre-specified number of Participants, that is, consisting of either 2 Participants, 3 Participants, 5 Participants or 10 Participants. The User shall supply a name for the Private league.season2. The User creating the Private league.season2 shall provide JSFL with the email address or Facebook account username of Invited Users to enable JSFL to send a message or mail inviting such Invited User to register with JSFL (if necessary) and participate in the Private league.season2 in relation to which the invite has been issued.\n" +
            "10. Legality of Game of Skill\n" +
            "•\tThe League (s) described above (across the JSFL Services) are games of skill as success of Participants depends primarily on their superior knowledge of the games of cricket and knowledge of players' relative form, players' performance in a particular territory, conditions and/or format (such as ODIs, test cricket and Twenty20 in the cricket fantasy game), attention and dedication towards the League(s) and adroitness in playing the League(s). The League(s) also requires Participants to field well-balanced sides with limited resources and make substitutions at appropriate times to gain the maximum points.\n" +
            "•\tBy participating in this League(s), each Participant acknowledges and agrees that he/she is participating in a game of skill.\n" +
            "11. Eligibility\n" +
            "•\tPersons who wish to participate must have a valid phone number and email address.\n" +
            "•\tOnly those Participants who have successfully registered on the JSFL as well as registered prior to each round in accordance with the procedure outlined above shall be eligible to participate in the League and win prizes.\n" +
            "13. Tabulation of fantasy points\n" +
            "JSFL may obtain the score feed and other information required for the computation and tabulation of fantasy points from third party service provider(s). In the rare event that any error in the computation or tabulation of fantasy points, selection of winners, etc., as a result of inaccuracies in or incompleteness of the feed provided by the third party service provider comes to its attention, JSFL shall use best efforts to rectify such error prior to the distribution of prizes. However, JSFL hereby clarifies that it relies on the accuracy and completeness of such third party score/statistic feeds and does not itself warrant or make any representations concerning the accuracy thereof and, in any event, shall take no responsibility for inaccuracies in computation and tabulation of fantasy points or the selection of winners as a result of any inaccurate or incomplete scores/statistics received from such third party service provider. Users and Participants agree not to make any claim or raise any complaint against JSFL in this respect.\n" +
            "14. Selection and Verification of Winners and Conditions relating to the Prizes\n" +
            "•\tSelection of Winners\n" +
            "Winners will be decided on the basis of the scores of the Teams in a designated round (which may last anywhere between one day and an entire tournament) of the League(s). The Participant(s) owning the Team(s) with the highest aggregate score in a particular round shall be declared the Winner(s). In certain pre-specified Leagues, JSFL may declare more than one Winner and distribute prizes to such Winners in increasing order of their Team's aggregate score at the end of the designated round of the League. The contemplated number of Winners and the prize due to each Winner in such League shall be as specified on the League page prior to the commencement of the League. \n" +
            "Participants creating Teams on behalf of any other Participant or person shall be disqualified. \n" +
            "In the event of a tie, the winning Participants shall be declared Winners and the prize shall be equally divided among such Participants. \n" +
            "JSFL shall not be liable to pay any prize if it is discovered that the Winner(s) have not abided by these Terms and Conditions, and other rules and regulations in relation to the use of the JSFL, League, \"Fantasy Rules\", etc.\n" +
            "•\tContacting Winners \n" +
            "Winners shall be contacted by JSFL or the third party conducting the League on the e-mail address or phone number provided at the time of registration. The verification process and the documents required for the collection of prize shall be detailed to the Winners at this stage. As a general practice, winners will be required to provide following documents:\n" +
            "o\tPhotocopy of the User's CNIC;\n" +
            "JSFL shall not permit a Winner to withdraw his/her prize(s)/accumulated winnings unless the above-mentioned documents have been received and verified within the time-period stipulated by JSFL. The User represents and warrants that the documents provided in the course of the verification process are true copies of the original documents to which they relate. \n" +
            "\n" +
            "Participants are required to provide proper and complete details at the time of registration. JSFL shall not be responsible for communications errors, commissions or omissions including those of the Participants due to which the results may not be communicated to the Winner. \n" +
            "\n" +
            "The list of Winners shall be posted on a separate web-page on the JSFL. The winners will also be intimated by e-mail. \n" +
            "\n" +
            "In the event that a Participant has been declared a Winner on the abovementioned web-page but has not received any communication from JSFL, such Participant may contact JSFL within the time specified on the webpage.\n" +
            "•\tVerification process \n" +
            "Only those Winners who successfully complete the verification process and provide the required documents within the time limit specified by JSFL shall be permitted to withdraw/receive their accumulated winnings (or any part thereof). JSFL shall not entertain any claims or requests for extension of time for submission of documents. \n" +
            "JSFL shall scrutinise all documents submitted and may, at its sole and absolute discretion, disqualify any Winner from withdrawing his accumulated winnings (or any part thereof) on the following grounds:\n" +
            "o\tDetermination by JSFL that any document or information submitted by the Participant is incorrect, misleading, false, fabricated, incomplete or illegible; or\n" +
            "o\tParticipant does not fulfill the Eligibility Criteria specified in Clause 10 above; or\n" +
            "o\tAny other ground.\n" +
            "•\tTaxes Payable \n" +
            "All prizes shall be subject to deduction of tax. The Winners shall be responsible for payment of any other applicable tax, including but not limited to, income tax, gift tax, etc..\n" +
            "•\tMiscellaneous \n" +
            "The decision of JSFL with respect to the awarding of prizes shall be grand, binding and non-leagueable. \n" +
            "\n" +
            "If it is found that a Participant playing the League is under the age of eighteen (18), JSFL shall be entitled, at its sole and absolute discretion, to disqualify such Participant and forfeit his/her prize. Further, JSFL may, at its sole and absolute discretion, suspend or terminate such Participant's account. \n" +
            "\n" +
            "To the extent permitted by law, JSFL makes no representations or warranties as to the quality, suitability or merchantability of any prizes and shall not be liable in respect of the same. \n" +
            "\n" +
            "JSFL may, at its sole and absolute discretion, vary or modify the prizes being offered to winners. Participants shall not raise any claim against JSFL or question its right to modify such prizes being offered, prior to closure of the League. \n" +
            "\n" +
            "JSFL will not bear any responsibility for the transportation or packaging of prizes to the respective winners. JSFL shall not be held liable for any loss or damage caused to any prizes at the time of such transportation. \n" +
            "\n" +
            "The Winners shall bear the shipping, courier or any other delivery cost in respect of the prizes. \n" +
            "\n" +
            "The Winners shall bear all transaction charges levied for delivery of cash prizes. \n" +
            "\n" +
            "All prizes are non-transferable and non-refundable. Prizes cannot be exchanged / redeemed for cash or kind. No cash claims can be made in lieu of prizes in kind.\n" +
            "15. Publicity\n" +
            "Acceptance of a prize by the Winner constitutes permission for JSFL, and its affiliates to use the Winner's name, likeness, voice and comments for advertising and promotional purposes in any media worldwide for purposes of advertising and trade without any further permissions or consents and / or additional compensation whatsoever. \n" +
            "The Winners further undertake that they will be available for promotional purposes as planned and desired by JSFL without any charge. The exact dates remain the sole discretion of JSFL. Promotional activities may include but not be limited to press events, internal meetings and ceremonies/functions.\n" +
            "16. General Conditions\n" +
            "If it comes to the notice of JSFL that any governmental, statutory or regulatory compliances or approvals are required for conducting any League or if it comes to the notice of JSFL that conduct of any such Leagueis prohibited, then JSFL shall withdraw and / or cancel such League without prior notice to any Participants or winners of any League. Users agree not to make any claim in respect of such cancellation or withdrawal of the League, or league.season2 it in any manner. \n" +
            "\n" +
            "Employees, directors, affiliates, relatives and family members of JSFL, will not be eligible to participate in any League(s).\n" +
            "17. Dispute and Dispute Resolution\n" +
            "•\tThe courts of competent jurisdiction at Mumbai shall have exclusive jurisdiction to determine any and all disputes arising out of, or in connection with, the JSFL Services provided by JSFL (including the League)), the construction, validity, interpretation and enforceability of these Terms and Conditions, or the rights and obligations of the User(s) (including Participants) or JSFL, as well as the exclusive jurisdiction to grant interim or preliminary relief in case of any dispute referred to arbitration as given below. All such issues and questions shall be governed and construed in accordance with the laws of the Republic of India.\n" +
            "•\tIn the event of any legal dispute (which may be a legal issue or question) which may arise, the party raising the dispute shall provide a written notification (\"Notification\") to the other party. On receipt of Notification, the parties shall first try to resolve the dispute through discussions. In the event that the parties are unable to resolve the dispute within fifteen (15) days of receipt of Notification, the dispute shall be settled by arbitration.\n" +
            "•\tThe place of arbitration shall be Karachi, Pakistan. \n" +
            "•\tThe arbitration award will be grand and binding on the Parties, and each Party will bear its own costs of arbitration and equally share the fees of the arbitrator unless the arbitral tribunal decides otherwise. The arbitrator shall be entitled to pass interim orders and awards, including the orders for specific performance and such orders would be enforceable in competent courts. The arbitrator shall give a reasoned award.\n" +
            "•\tNothing contained in these Terms and Conditions shall prevent JSFL from seeking and obtaining interim or permanent equitable or injunctive relief, or any other relief available to safeguard JSFL's interest prior to, during or following the filing of arbitration proceedings or pending the execution of a decision or award in connection with any arbitration proceedings from any court having jurisdiction to grant the same. The pursuit of equitable or injunctive relief shall not constitute a waiver on the part of JSFL to pursue any remedy for monetary damages through the arbitration described herein.\n" +
            "18. Release and Limitations of Liability\n" +
            "•\tUsers shall access the JSFL Services provided on JSFL voluntarily and at their own risk. JSFL shall, under no circumstances be held responsible or liable on account of any loss or damage sustained (including but not limited to any accident, injury, death, loss of property) by Users or any other person or entity during the course of access to the JSFL Services (including participation in the League(s)) or as a result of acceptance of any prize.\n" +
            "•\tBy entering the JSFL and accessing the JSFL Services provided therein, Users hereby release from and agree to indemnify JSFL, and/ or any of its directors, employees, partners, associates and licensors, from and against all liability, cost, loss or expense arising out their access to the JSFL Services including (but not limited to) personal injury and damage to property and whether direct, indirect, consequential, foreseeable, due to some negligent act or omission on their part, or otherwise.\n" +
            "•\tJSFL accepts no liability, whether jointly or severally, for any errors or omissions, whether on behalf of itself or third parties in relation to the prizes.\n" +
            "•\tUsers shall be solely responsible for any consequences which may arise due to their access of JSFL Services by conducting an illegal act or due to non-conformity with these Terms and Conditions and other rules and regulations in relation to JSFL Services, including provision of incorrect address or other personal details. Users also undertake to indemnify JSFL and their respective officers, directors, employees and agents on the happening of such an event (including without limitation cost of attorney, legal charges etc.) on full indemnity basis for any loss/damage suffered by JSFL on account of such act on the part of the Users.\n" +
            "•\tUsers shall indemnify, defend, and hold JSFL harmless from any third party/entity/organization claims arising from or related to such User's engagement with the JSFL or participation in any League. In no event shall JSFL be liable to any User for acts or omissions arising out of or related to User's engagement with the JSFL or his/her participation in any League(s).\n" +
            "•\tIn consideration of JSFL allowing Users to access the JSFL Services hosted on the JSFL, to the maximum extent permitted by law, the Users waive and release each and every right or claim, all actions, causes of actions (present or future) each of them has or may have against JSFL, its respective agents, directors, officers, business associates, group companies, sponsors, employees, or representatives for all and any injuries, accidents, or mishaps (whether known or unknown) or (whether anticipated or unanticipated) arising out of the provision of JSFL Services or related to the Leagues or the prizes of the Leagues.\n" +
            "19. Disclaimers\n" +
            "•\tTo the extent permitted under law, neither JSFL nor its parent/holding company, subsidiaries, affiliates, directors, officers, professional advisors, employees shall be responsible for the deletion, the failure to store, the mis-delivery, or the untimely delivery of any information or material.\n" +
            "•\tTo the extent permitted under law, JSFL shall not be responsible for any harm resulting from downloading or accessing any information or material, the quality of servers, games, products, JSFL services or sites, cancellation of competition and prizes. JSFL disclaims any responsibility for, and if a User pays for access to one of JSFL's Services the User will not be entitled to a refund as a result of, any inaccessibility that is caused by JSFL's maintenance on the servers or the technology that underlies our sites, failures of JSFL's service providers (including telecommunications, hosting, and power providers), computer viruses, natural disasters or other destruction or damage of our facilities, acts of nature, war, civil disturbance, or any other cause beyond our reasonable control. In addition, JSFL does not provide any warranty as to the content on the JSFL(s). JSFL(s) content is distributed on an \"as is, as available\" basis.\n" +
            "•\tAny material accessed, downloaded or otherwise obtained through JSFL is done at the User's discretion, competence, acceptance and risk, and the User will be solely responsible for any potential damage to User's computer system or loss of data that results from a User's download of any such material.\n" +
            "•\tJSFL shall make best endeavours to ensure that the JSFL(s) is error-free and secure, however, neither JSFL nor any of its partners, licensors or associates makes any warranty that:\n" +
            "\uF0A7\tthe JSFL(s) will meet Users' requirements,\n" +
            "\uF0A7\tJSFL(s) will be uninterrupted, timely, secure, or error free\n" +
            "\uF0A7\tthe results that may be obtained from the use of JSFL(s) will be accurate or reliable; and\n" +
            "\uF0A7\tthe quality of any products, JSFL Services, information, or other material that Users purchase or obtain through JSFLcom(s) will meet Users' expectations.\n" +
            "•\tIn case JSFL discovers any error, including any error in the determination of Winners or in the transfer of amounts to a User's account, JSFL reserves the right (exercisable at its discretion) to rectify the error in such manner as it deems fit, including through a set-off of the erroneous payment from amounts due to the User or deduction from the User's account of the amount of erroneous payment. In case of exercise of remedies in accordance with this clause, JSFL agrees to notify the User of the error and of the exercise of the remedy(ies) to rectify the same.\n" +
            "•\tTo the extent permitted under law, neither JSFL nor its partners, licensors or associates shall be liable for any direct, indirect, incidental, special, or consequential damages arising out of the use of or inability to use our sites, even if we have been advised of the possibility of such damages.\n" +
            "•\tAny JSFL Services, events or League(s) being hosted or provided, or intended to be hosted or provided by JSFL and requiring specific permission or authority from any statutory authority or any state or the central government, or the board of directors shall be deemed cancelled or terminated, if such permission or authority is either not obtained or denied either before or after the availability of the relevant JSFL Services, events or League(s) are hosted or provided.\n" +
            "•\tTo the extent permitted under law, in the event of suspension or closure of any Services, events or Leagues, Users (including Participants) shall not be entitled to make any demands, claims, on any nature whatsoever.\n" +
            "20. Miscellaneous\n" +
            "•\tJSFL may be required under certain legislations, to notify User(s) of certain events. User(s) hereby acknowledge and consent that such notices will be effective upon JSFL posting them on the JSFL or delivering them to the User through the email address provided by the User at the time of registration. User(s) may update their email address by logging into their account on the JSFL. If they do not provide JSFL with accurate information, JSFL cannot be held liable for failure to notify the User.\n" +
            "•\tJSFL shall not be liable for any delay or failure to perform resulting from causes outside its reasonable control, including but not limited to any failure to perform due to unforeseen circumstances or cause beyond JSFL's control such as acts of God, war, terrorism, riots, embargoes, acts of civil or military authorities, fire, floods, accidents, network infrastructure failures, strikes, or shortages of transportation facilities, fuel, energy, labor or materials or any cancellation of any cricket/football/kabaddi match to which a League relates. In such circumstances, JSFL shall also be entitled to cancel any related League(s) and to process an appropriate refund for all Participants.\n" +
            "•\tJSFL's failure to exercise or enforce any right or provision of these Terms and Conditions shall not constitute a waiver of such right or provision.\n" +
            "•\tUsers agree that regardless of any statute or law to the contrary, any claim or cause of action arising out of or related to use of the JSFL or these Terms must be filed within thirty (30) days of such claim or cause of action arising or be forever barred.\n" +
            "•\tThese Terms and Conditions, including all terms, conditions, and policies that are incorporated herein by reference, constitute the entire agreement between the User(s) and JSFL.\n" +
            "•\tIf any part of these Terms and Conditions is determined to be indefinite, invalid, or otherwise unenforceable, the rest of these Terms and Conditions shall continue in full force.\n" +
            "•\tJSFL reserves the right to moderate, restrict or ban the use of the JSFL, specifically to any User, or generally, in accordance with JSFL's policy/policies from time to time, at its sole and absolute discretion and without any notice.\n" +
            "•\tJSFL may, at its sole and absolute discretion, permanently close or temporarily suspend any JSFL Services (including any League(s)).\n" +
            "\n";
    public static String getWS(){
        File file = new File(Environment.getExternalStorageDirectory()+"/ACL","IP.txt");
        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);

            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            e.printStackTrace();
        }
        Log.wtf("Config---->",text.toString());
        return text.toString();

    }
    public static User getCurrentUser(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF, ctx.MODE_PRIVATE);
        User f = new User();
        f.setEmail(sharedPreferences.getString(EMAIL, ""));
        f.setPicture(sharedPreferences.getString(PICTURE, ""));
        f.setCellNo(sharedPreferences.getString(CELL_NO, ""));

        return f;
    }

    public static List<TeamVO> GET_TEAMS_ID() {
        List<TeamVO> list = new ArrayList<TeamVO>();
        TeamVO team = new TeamVO();
        team.setId("1");
        team.setTeam_id("1001");
        team.setTeam_name("Islmabad United");
        team.setShortname("IU");
        list.add(team);

        team = new TeamVO();
        team.setId("2");
        team.setTeam_id("1002");
        team.setTeam_name("karachi Kings");
        team.setShortname("KK");
        list.add(team);

        team = new TeamVO();
        team.setId("3");
        team.setTeam_id("1003");
        team.setTeam_name("Lahore Qalandars");
        team.setShortname("LQ");
        list.add(team);

        team = new TeamVO();
        team.setId("4");
        team.setTeam_id("1004");
        team.setTeam_name("Multan Sultan");
        team.setShortname("MS");
        list.add(team);

        team = new TeamVO();
        team.setId("5");
        team.setTeam_id("1005");
        team.setTeam_name("Peshawar Zaklmi");
        team.setShortname("PZ");
        list.add(team);

        team = new TeamVO();
        team.setId("6");
        team.setTeam_id("1006");
        team.setTeam_name("Quetta Gladiators");
        team.setShortname("QG");
        list.add(team);

        return list;
    }

    public static void getAlert(Context context, String message, String title) {
        InfoDialog alert = new InfoDialog(context, message, title);
        alert.show();
    }

    public static void getAlert(Context context, String message) {
        InfoDialog alert = new InfoDialog(context, message);
        alert.show();
    }

    public static void getAlert(Context context, String message, String title, boolean finish) {
        InfoDialog alert = new InfoDialog(context, message, title, finish);
        alert.show();
    }

    public static String format(String n)
    {
        try {
            double num = Double.parseDouble(n);
           String retunVal= formatter.format(num);
           return  retunVal;
        }
        catch (Exception e)
        {
            return "";
        }

    }

    public static void PopulateHeader(final Context ctx, View view) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        ImageView btnCreate = (ImageView) view.findViewById(R.id.btn_create);

        ImageView iv_profile = (ImageView) view.findViewById(R.id.ivUserImage);
        iv_profile.bringToFront();

        TextView team_name = (TextView) view.findViewById(R.id.tv_name);
        TextView user_name = (TextView) view.findViewById(R.id.user_field);
        user_name.bringToFront();
        TextView rank_field = (TextView) view.findViewById(R.id.star_field);
        TextView coins_field = (TextView) view.findViewById(R.id.coin_field);
        String name = sharedPreferences.getString(Config.NAME, "");
        if (name.equals(""))
            name = sharedPreferences.getString(Config.FIRST_NAME, "") + " " + sharedPreferences.getString(Config.LAST_NAME, "");
        user_name.setText(name);
        //rank_field.setVisibility(View.VISIBLE);//.setText(sharedPreferences.getString(Config.FIRST_NAME, "") + " " + sharedPreferences.getString(Config.LAST_NAME, ""));//setVisibility(View.INVISIBLE);//setText(sharedPreferences.getString(Config.USER_RANK, "100"));

      try {
         String budget= format(sharedPreferences.getString(Config.USER_BUDGET, "0"));
          coins_field.setText(budget);
        String rank=sharedPreferences.getString(Config.USER_RANK, "");
          rank_field.setText(rank);
      }
      catch (Exception e){
          e.printStackTrace();
      }
        Typeface custom_font = Typeface.createFromAsset(ctx.getAssets(), "fonts/Montserrat-Black.ttf");
        user_name.setTypeface(custom_font);
        rank_field.setTypeface(custom_font);
        coins_field.setTypeface(custom_font);
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Profile();
                FragmentManager fragmentManager = ((FragmentActivity) ctx).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        try {
            String fb_image = sharedPreferences.getString(Config.PICTURE, "");
            String base64 = sharedPreferences.getString(Config.IMAGE_DATA, "");
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            if (decodedByte != null) {
                iv_profile.setBackground(null);
                iv_profile.setImageBitmap(decodedByte);

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) user_name.getLayoutParams();
                params.bottomMargin = 30;
                user_name.setLayoutParams(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // team_name.setText(sharedPreferences.getString(Config.TEAM_NAME, "No Team"));

    }

    public static void PopulateHeader2(final Context ctx, View view) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        ImageView btnCreate = (ImageView) view.findViewById(R.id.btn_create);

        ImageView iv_profile = (ImageView) view.findViewById(R.id.ivUserImage);
        iv_profile.bringToFront();

        TextView team_name = (TextView) view.findViewById(R.id.tv_name);
        TextView user_name = (TextView) view.findViewById(R.id.user_field);
        user_name.bringToFront();
        TextView rank_field = (TextView) view.findViewById(R.id.star_field);
        TextView coins_field = (TextView) view.findViewById(R.id.coin_field);
        String name = sharedPreferences.getString(Config.NAME, "");
        if (name.equals(""))
            name = sharedPreferences.getString(Config.FIRST_NAME, "") + " " + sharedPreferences.getString(Config.LAST_NAME, "");
        user_name.setText(name);
        rank_field.setText(sharedPreferences.getString(Config.FIRST_NAME, "") + " " + sharedPreferences.getString(Config.LAST_NAME, ""));//setVisibility(View.INVISIBLE);//setText(sharedPreferences.getString(Config.USER_RANK, "100"));
        coins_field.setText(sharedPreferences.getString(Config.JS_Mobile_Number, ""));
        if (sharedPreferences.getString(Config.JS_Mobile_Number, "").equals("")) {
            rank_field.setVisibility(View.GONE);
            coins_field.setVisibility(View.GONE);
        }

        Typeface custom_font = Typeface.createFromAsset(ctx.getAssets(), "fonts/Montserrat-Black.ttf");
        user_name.setTypeface(custom_font);
        rank_field.setTypeface(custom_font);
        rank_field.setTextSize(12);
        coins_field.setTextSize(12);
        coins_field.setTypeface(custom_font);
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Profile();
                FragmentManager fragmentManager = ((FragmentActivity) ctx).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        try {
            //String fb_image = sharedPreferences.getString(Config.PICTURE, "");
            String base64 = sharedPreferences.getString(Config.IMAGE_DATA, "");
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            if (decodedByte != null) {
                iv_profile.setBackground(null);
                iv_profile.setImageBitmap(decodedByte);

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) user_name.getLayoutParams();
                params.bottomMargin = 30;
                user_name.setLayoutParams(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // team_name.setText(sharedPreferences.getString(Config.TEAM_NAME, "No Team"));
    }

    public static String getTeamName(String team_id)
    {
        String team_name = "";
        try {
            if(team_id !=null) {
                if (team_id.equalsIgnoreCase("1001")) {
                    team_name = "/ISB";
                } else if (team_id.equalsIgnoreCase("1002")) {
                    team_name = "/KHI";
                } else if (team_id.equalsIgnoreCase("1003")) {
                    team_name = "/LHR";
                } else if (team_id.equalsIgnoreCase("1004")) {
                    team_name = "/MUL";
                } else if (team_id.equalsIgnoreCase("1005")) {
                    team_name = "/PSH";
                } else if (team_id.equalsIgnoreCase("1006")) {
                    team_name = "/QTA";
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return team_name;
    }

    public static String readFile(Context context, String filename) {
        String buffer_row = "";
        try {
            AssetManager am = context.getAssets();
            java.io.InputStream fs = am.open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fs));
            String datarow = "";

            while ((datarow = reader.readLine()) != null) {
                buffer_row += datarow + "\n";

            }

            //getAlert(buffer_row);
            reader.close();

        } catch (Exception e) {

        }
        return buffer_row;
    }

    public static  String w5 = "https://api.jsbl.com";
    public static  String w3 = "YuvttXDI9TCbEbrKztBfAKv0MYYX89Bn";
    public static  String w4 = "HNQrkcsrd4OnbeU6";
    //public static  String w0 = "http://"+getWS()+"/";
    public static  String w0 = "http://104.41.140.154/PSL/";
//    public static String w0 = "http://172.28.28.58:82/";
    public static String w1 = "CJy1//PsvDYeMNZ/UTERPg==";
    public static String w2 = "12U/jYHQ4WkeMNZ/UTERPg==";


    public static long getPostDifference(Context context) {
        long minutes_difference = 0;
        try {
            String fixturesTeam = "";
            Date date1 = null;
            Date date2 = null; //new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2012-06-20 16:00:00");
            DatabaseHandler dbHandler = new DatabaseHandler(context);
            List<FixturesVO> fixturesList = dbHandler.getFixtures();
            String match_date = "";
            String dateAfterConvertMonth = "";
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = df.format(new Date());
            long difference = 0;
            for (int i = 0; i < fixturesList.size(); i++) {
                match_date = fixturesList.get(i).getDate();
                dateAfterConvertMonth = match_date.split(" ")[0] + "-" + replaceWithNumber(match_date.split(" ")[1]) + "-" + match_date.split(" ")[2] + " " + match_date.split(" ")[3];
                //dateAfterConvertMonth = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(match_date);
                date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(currentDateTime);
                date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateAfterConvertMonth);

                //seconds_countdown = (date2.getTime() - date1.getTime());

                if (date2.after(date1)) {
                    int k = 0;
                    k = i - 1;
                    if (k >= 0) {

                        match_date = fixturesList.get(k).getDate();
                        dateAfterConvertMonth = match_date.split(" ")[0] + "-" + replaceWithNumber(match_date.split(" ")[1]) + "-" + match_date.split(" ")[2] + " " + match_date.split(" ")[3];

                        fixturesTeam = fixturesList.get(k).getMatch();
                        String team1 = fixturesTeam.split("VS")[0].trim();
                        String team2 = fixturesTeam.split("VS")[1].trim();
                        date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(currentDateTime);
                        date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateAfterConvertMonth);

                        long diff = date1.getTime() - date2.getTime();
                        long seconds = diff / 1000;
                        minutes_difference = seconds / 60;

                        return Math.abs(minutes_difference);
                    }
                }
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return Math.abs(minutes_difference);
    }

    public static long datesDifference(Context context) {
        long minutes_difference = 0;
        try {
            String fixturesTeam = "";
            Date date1 = null;
            Date date2 = null; //new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2012-06-20 16:00:00");
            DatabaseHandler dbHandler = new DatabaseHandler(context);
            List<FixturesVO> fixturesList = dbHandler.getFixtures();
            //ImageView iv_Teamone = (ImageView) myView.findViewById(R.id.team_one);
            //ImageView iv_teamtwo = (ImageView) myView.findViewById(R.id.team_two);
            String match_date = "";
            String dateAfterConvertMonth = "";
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = df.format(new Date());
            long difference = 0;
            for (int i = 0; i < fixturesList.size(); i++) {
                match_date = fixturesList.get(i).getDate();
                dateAfterConvertMonth = match_date.split(" ")[0] + "-" + replaceWithNumber(match_date.split(" ")[1]) + "-" + match_date.split(" ")[2] + " " + match_date.split(" ")[3];
                //dateAfterConvertMonth = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(match_date);
                date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(currentDateTime);
                date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateAfterConvertMonth);

                //seconds_countdown = (date2.getTime() - date1.getTime());

                if (date2.after(date1)) {
                    fixturesTeam = fixturesList.get(i).getMatch();
                    String team1 = fixturesTeam.split("VS")[0].trim();
                    String team2 = fixturesTeam.split("VS")[1].trim();

                    long diff = date2.getTime() - date1.getTime();
                    long seconds = diff / 1000;
                    minutes_difference = seconds / 60;

                    return minutes_difference;
                }
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return minutes_difference;
    }

    static String replaceWithNumber(String string) {
        String result = "";
        try {

            result = string.replace("Jan", "01").replace("Feb", "02").replace("Mar", "03").replace("Apr", "04").replace("May", "05").replace("Jun", "06").replace("Jul", "07").replace("Aug", "08").replace("Sep", "09").replace("Oct", "10").replace("Nov", "11").replace("Dec", "12");

        } catch (Exception e) {
        }
        return result;
    }

    public static String getDecimeBudget(String amount)
    {
        try {
            if(amount !=null) {
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                return formatter.format(Integer.parseInt(amount));
            }
        }catch (Exception e)
        {
            e.printStackTrace();

        }
        return "0";
    }

}