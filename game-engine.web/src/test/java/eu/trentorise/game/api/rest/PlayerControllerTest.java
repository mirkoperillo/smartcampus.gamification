package eu.trentorise.game.api.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.bean.ChallengeAssignmentDTO;
import eu.trentorise.game.bean.PlayerStateDTO;
import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.config.NoSecurityConfig;
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.Inventory.ItemChoice;
import eu.trentorise.game.model.Inventory.ItemChoice.ChoiceType;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.model.Level.Config;
import eu.trentorise.game.model.Level.Threshold;
import eu.trentorise.game.model.LevelInstance;
import eu.trentorise.game.model.PlayerLevel;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.core.ArchivedConcept;
import eu.trentorise.game.model.core.ChallengeAssignment;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("no-sec")
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class, NoSecurityConfig.class,
        TestMVCConfiguration.class},
        loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
public class PlayerControllerTest {

    @Autowired
    private GameService gameSrv;

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mocker;

    private ObjectMapper mapper;

    private static final String GAME = "gameTest";
    private static final String ACTION = "save_itinerary";

    @PostConstruct
    public void init() {
        mocker = MockMvcBuilders.webAppContextSetup(wac).build();
        mapper = new ObjectMapper();
    }

    @Before
    public void cleanDB() {
        // clean mongo
        mongo.getDb().dropDatabase();
    }

    private Game defineGame() {
        Game game = new Game();

        game.setId(GAME);
        game.setName(GAME);

        game.setActions(new HashSet<String>());
        game.getActions().add(ACTION);
        game.getActions().add("classification");

        game.setConcepts(new HashSet<GameConcept>());

        game.setTasks(new HashSet<GameTask>());

        return game;
    }

    @Test
    public void createPlayer() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        try {
            PlayerStateDTO player = new PlayerStateDTO();
            player.setPlayerId("10001");
            RequestBuilder builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}", game.getId(),
                            "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(player));

            mocker.perform(builder).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

        } catch (Exception e) {
            Assert.fail("exception " + e.getMessage());
        }
    }

    @Test
    public void propose_a_challenge() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        ChallengeAssignmentDTO assignment = new ChallengeAssignmentDTO();
        assignment.setInstanceName("new-instance");
        assignment.setModelName("model_1");
        assignment.setState("proposed");


        RequestBuilder builder;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/challenges", game.getId(), "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(assignment));
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));
            
            PlayerState player = playerSrv.loadState(game.getId(), "10001", false);
            ChallengeConcept challenge =
                    (ChallengeConcept) player.getState().stream().findFirst().get();
            assertThat(challenge.getState(), is(ChallengeState.PROPOSED));
        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }

    }
    
    
    @Test
    public void assign_a_challenge() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        ChallengeAssignmentDTO assignment = new ChallengeAssignmentDTO();
        assignment.setInstanceName("new-instance");
        assignment.setModelName("model_1");


        RequestBuilder builder;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/challenges", game.getId(), "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(assignment));
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));
            
            PlayerState player = playerSrv.loadState(game.getId(), "10001", false);
            ChallengeConcept challenge =
                    (ChallengeConcept) player.getState().stream().findFirst().get();
            assertThat(challenge.getState(), is(ChallengeState.ASSIGNED));
        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }
    }

    @Test
    public void propose_a_challenge_with_priority_5() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        ChallengeAssignmentDTO assignment = new ChallengeAssignmentDTO();
        assignment.setInstanceName("new-instance");
        assignment.setModelName("model_1");
        assignment.setState("proposed");
        assignment.setPriority(5);


        RequestBuilder builder;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/challenges", game.getId(), "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(assignment));
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            PlayerState player = playerSrv.loadState(game.getId(), "10001", false);
            ChallengeConcept challenge =
                    (ChallengeConcept) player.getState().stream().findFirst().get();
            assertThat(challenge.getState(), is(ChallengeState.PROPOSED));
            assertThat(challenge.getPriority(), is(5));
        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }

    }

    @Test
    public void assign_invalid_challenge() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        ChallengeAssignmentDTO assignment = new ChallengeAssignmentDTO();
        assignment.setInstanceName("new-instance");
        assignment.setModelName("model_1");
        assignment.setState("dummie");


        RequestBuilder builder;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/challenges", game.getId(), "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(assignment));
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(400));

        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }

    }

    @Test
    public void assign_challenge_without_declare_type_of_assignment() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        ChallengeAssignmentDTO assignment = new ChallengeAssignmentDTO();
        assignment.setInstanceName("new-instance");
        assignment.setModelName("model_1");

        RequestBuilder builder;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/challenges", game.getId(), "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(assignment));
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            PlayerState player = playerSrv.loadState(game.getId(), "10001", false);
            ChallengeConcept challenge =
                    (ChallengeConcept) player.getState().stream().findFirst().get();
            assertThat(challenge.getState(), is(ChallengeState.ASSIGNED));
        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }
    }

    @Test
    public void set_origin_for_challenge_assignment() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        ChallengeAssignmentDTO assignment = new ChallengeAssignmentDTO();
        assignment.setInstanceName("new-instance");
        assignment.setModelName("model_1");
        assignment.setOrigin("MY_SYSTEM");

        RequestBuilder builder;
        try {
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/challenges", game.getId(), "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(assignment));
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            PlayerState player = playerSrv.loadState(game.getId(), "10001", false);
            ChallengeConcept challenge =
                    (ChallengeConcept) player.getState().stream().findFirst().get();
            assertThat(challenge.getOrigin(), is("MY_SYSTEM"));
        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }
    }

    // test reading player state data


    @Test
    public void read_inventory() {
        Game game = defineGame();

        Level levelDefinition = new Level("greener","green leaves");
        Threshold beginner = new Threshold("beginner", 0);
        Config config = new Config();
        config.setChoices(1);
        config.getAvailableModels().addAll(Arrays.asList("model1", "model2"));
        beginner.setConfig(config);
        levelDefinition.getThresholds().add(beginner);
        
        game.getLevels().add(levelDefinition);
        gameSrv.saveGameDefinition(game);
        
        PlayerState player = playerSrv.loadState(game.getId(), "10001", true);
        player.getLevels().add(new PlayerLevel(levelDefinition, 10d));
        player.updateInventory(game, Arrays.asList(new LevelInstance("greener", "beginner")));
        playerSrv.saveState(player);

        RequestBuilder builder = null;
        try {
            builder = MockMvcRequestBuilders
                    .get("/data/game/{gameId}/player/{playerId}/inventory", game.getId(), "10001");
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.challengeChoices", hasSize(2)))
                    .andExpect(jsonPath("$.challengeChoices[0].modelName", is("model1")))
                    .andExpect(jsonPath("$.challengeChoices[0].state", is("AVAILABLE")));

        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }
    }

    @Test
    public void read_empty_inventory() {
        Game game = defineGame();

        Level levelDefinition = new Level("greener", "green leaves");
        Threshold beginner = new Threshold("beginner", 0);
        levelDefinition.getThresholds().add(beginner);

        game.getLevels().add(levelDefinition);
        gameSrv.saveGameDefinition(game);

        PlayerState player = playerSrv.loadState(game.getId(), "10001", true);
        player.getLevels().add(new PlayerLevel(levelDefinition, 10d));
        player.updateInventory(game, null);
        playerSrv.saveState(player);

        RequestBuilder builder = null;
        try {
            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/player/{playerId}/inventory",
                    game.getId(), "10001");
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.challengeChoices", hasSize(0)));

        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }
    }


    @Test
    public void activate_a_choice() {
        Game game = defineGame();
        Level levelDefinition = new Level("greener", "green leaves");
        Threshold beginner = new Threshold("beginner", 0);
        Config config = new Config();
        config.setChoices(1);
        config.getAvailableModels().add("model1");
        beginner.setConfig(config);
        levelDefinition.getThresholds().add(beginner);

        game.getLevels().add(levelDefinition);
        gameSrv.saveGameDefinition(game);



        RequestBuilder builder = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            ItemChoice choice = new ItemChoice(ChoiceType.CHALLENGE_MODEL, "model1");

            PlayerState player = playerSrv.loadState(game.getId(), "10001", true);

            player.updateInventory(game, Arrays.asList(new LevelInstance("greener", "beginner")));
            playerSrv.saveState(player);

            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/inventory/activate", game.getId(),
                            "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(choice));

            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.challengeActivationActions", is(0)))
                    .andExpect(jsonPath("$.challengeChoices[0].state", is("ACTIVE")));

        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }
    }

    @Test
    public void no_more_activation_actions() {
        Game game = defineGame();
        Level levelDefinition = new Level("greener", "green leaves");
        Threshold beginner = new Threshold("beginner", 0);
        Config config = new Config();
        config.setChoices(1);
        config.getAvailableModels().add("model1");
        config.getAvailableModels().add("model2");
        beginner.setConfig(config);
        levelDefinition.getThresholds().add(beginner);

        game.getLevels().add(levelDefinition);
        gameSrv.saveGameDefinition(game);

        RequestBuilder builder = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            ItemChoice choice = new ItemChoice(ChoiceType.CHALLENGE_MODEL, "model1");

            PlayerState player = playerSrv.loadState(game.getId(), "10001", true);

            player.updateInventory(game, Arrays.asList(new LevelInstance("greener", "beginner")));
            playerSrv.saveState(player);

            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/inventory/activate", game.getId(),
                            "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(choice));

            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$.challengeActivationActions", is(0)))
                    .andExpect(jsonPath("$.challengeChoices[0].state", is("ACTIVE")));

            ItemChoice choice1 = new ItemChoice(ChoiceType.CHALLENGE_MODEL, "model1");
            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/inventory/activate", game.getId(),
                            "10001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(choice1));
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(400));

        } catch (Exception e) {
            Assert.fail("Exception " + e.getMessage());
        }
    }

    @Test
    public void accept_challenge() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);
        


        RequestBuilder builder = null;
        try {
            PlayerState player = playerSrv.loadState(game.getId(), "player", true);
            ChallengeAssignment assignment = new ChallengeAssignment("model_1", "instance_name",
                    new HashMap<>(), "PROPOSED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment);

            builder = MockMvcRequestBuilders
                    .post("/data/game/{gameId}/player/{playerId}/challenges/{challengeName}/accept",
                            game.getId(),
                            "player", "instance_name");
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));
            PlayerState loaded = playerSrv.loadState(game.getId(), "player", false);

            ChallengeConcept challenge = loaded.challenges().stream()
                    .filter(ch -> ch.getName().equals("instance_name")).findFirst()
                    .get();
            assertThat(challenge.getState(), is(ChallengeState.ASSIGNED));

        } catch (Exception e) {

        }

    }

    @Test
    public void try_to_accept_a_completed_challenge() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);



        RequestBuilder builder = null;
        try {
            PlayerState player = playerSrv.loadState(game.getId(), "player", true);
            ChallengeAssignment assignment = new ChallengeAssignment("model_1", "instance_name",
                    new HashMap<>(), "COMPLETED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment);

            builder = MockMvcRequestBuilders.post(
                    "/data/game/{gameId}/player/{playerId}/challenges/{challengeName}/accept",
                    game.getId(), "player", "instance_name");
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(400));

        } catch (Exception e) {

        }

    }

    @Test
    public void try_to_accept_a_not_existent_challenge() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        RequestBuilder builder = null;
        try {
            PlayerState player = playerSrv.loadState(game.getId(), "player", true);
            ChallengeAssignment assignment = new ChallengeAssignment("model_1", "instance_name",
                    new HashMap<>(), "PROPOSED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment);

            builder = MockMvcRequestBuilders.post(
                    "/data/game/{gameId}/player/{playerId}/challenges/{challengeName}/accept",
                    game.getId(), "player", "notExistentInstanceName");
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(400));

        } catch (Exception e) {

        }
    }

    @Test
    public void accept_challenge_and_clean_others_proposed() {

        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);

        RequestBuilder builder = null;
        try {
            PlayerState player = playerSrv.loadState(game.getId(), "player", true);
            ChallengeAssignment assignment = new ChallengeAssignment("model_1", "instance_name",
                    new HashMap<>(), "PROPOSED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment);
            ChallengeAssignment assignment2 = new ChallengeAssignment("model_1", "instance_name_1",
                    new HashMap<>(), "PROPOSED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment2);
            ChallengeAssignment assignment3 = new ChallengeAssignment("model_1", "instance_name_2",
                    new HashMap<>(), "PROPOSED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment3);

            builder = MockMvcRequestBuilders.post(
                    "/data/game/{gameId}/player/{playerId}/challenges/{challengeName}/accept",
                    game.getId(), "player", "instance_name_1");
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            PlayerState loaded = playerSrv.loadState(game.getId(), "player", false);
            List<ChallengeConcept> proposed = loaded.challenges().stream()
                    .filter(ch -> ch.getState() == ChallengeState.PROPOSED)
                    .collect(Collectors.toList());

            assertThat(proposed, hasSize(0));

        } catch (Exception e) {

        }

    }

    @Test
    public void accept_challenge_and_clean_only_proposed() {

        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        ChallengeModel model = new ChallengeModel();
        model.setGameId(game.getId());
        model.setName("model_1");
        gameSrv.saveChallengeModel(game.getId(), model);
        ChallengeModel model2 = new ChallengeModel();
        model2.setGameId(game.getId());
        model2.setName("model_2");
        gameSrv.saveChallengeModel(game.getId(), model2);

        RequestBuilder builder = null;
        try {
            PlayerState player = playerSrv.loadState(game.getId(), "player", true);
            ChallengeAssignment assignment = new ChallengeAssignment("model_1", "instance_name",
                    new HashMap<>(), "PROPOSED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment);
            ChallengeAssignment assignment2 = new ChallengeAssignment("model_1", "instance_name_1",
                    new HashMap<>(), "PROPOSED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment2);
            ChallengeAssignment assignment3 = new ChallengeAssignment("model_1", "instance_name_2",
                    new HashMap<>(), "COMPLETED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment3);
            ChallengeAssignment assignment4 = new ChallengeAssignment("model_2", "instance_name_3",
                    new HashMap<>(), "ACTIVE", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment4);
            ChallengeAssignment assignment5 = new ChallengeAssignment("model_2", "instance_name_4",
                    new HashMap<>(), "FAILED", null, null);
            playerSrv.assignChallenge(game.getId(), "player", assignment5);

            builder = MockMvcRequestBuilders.post(
                    "/data/game/{gameId}/player/{playerId}/challenges/{challengeName}/accept",
                    game.getId(), "player", "instance_name_1");
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200));

            PlayerState loaded = playerSrv.loadState(game.getId(), "player", false);
            List<ChallengeConcept> proposed = loaded.challenges().stream()
                    .filter(ch -> ch.getState() == ChallengeState.PROPOSED)
                    .collect(Collectors.toList());

            assertThat(proposed, hasSize(0));
            assertThat(loaded.getState(), hasSize(4));
        } catch (Exception e) {

        }
    }

    @Test
    public void read_empty_archive() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);

        RequestBuilder builder = null;
        try {

            builder = MockMvcRequestBuilders.get(
                    "/data/game/{gameId}/archive",
                    game.getId());
            mocker.perform(builder).andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$", hasSize(0)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void read_archive() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        ArchivedConcept archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        mongo.save(archived);

        RequestBuilder builder = null;
        try {

            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/archive", game.getId());
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$", hasSize(1)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void read_archive_given_a_state() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        ArchivedConcept archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        mongo.save(archived);

        RequestBuilder builder = null;
        try {

            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/archive", game.getId())
                    .param("state", "AUTO_DISCARDED");
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$", hasSize(0)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void read_archive_given_a_player() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        ArchivedConcept archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        mongo.save(archived);

        archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_2");
        challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        mongo.save(archived);

        RequestBuilder builder = null;
        try {

            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/archive", game.getId())
                    .param("playerId", "player_1");
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$", hasSize(1)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void read_archive_from_a_date() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        ArchivedConcept archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        archived.setArchivingDate(date("2018-11-06T05:00:00"));
        mongo.save(archived);

        archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        archived.setArchivingDate(date("2018-11-07T10:00:00"));
        mongo.save(archived);

        RequestBuilder builder = null;
        try {

            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/archive", game.getId())
                    .param("playerId", "player_1")
                    .param("from", "" + date("2018-11-07T02:00:00").getTime());
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$", hasSize(1)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }


    @Test
    public void read_archive_to_a_date() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        ArchivedConcept archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        archived.setArchivingDate(date("2018-11-06T05:00:00"));
        mongo.save(archived);

        archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        archived.setArchivingDate(date("2018-11-07T10:00:00"));
        mongo.save(archived);

        RequestBuilder builder = null;
        try {

            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/archive", game.getId())
                    .param("playerId", "player_1")
                    .param("to", "" + date("2018-11-07T02:00:00").getTime());
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$", hasSize(1)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void read_archive_between_a_date() {
        Game game = defineGame();
        gameSrv.saveGameDefinition(game);
        ArchivedConcept archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        ChallengeConcept challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        archived.setArchivingDate(date("2018-11-06T05:00:00"));
        mongo.save(archived);

        archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        archived.setArchivingDate(date("2018-11-07T10:00:00"));

        archived = new ArchivedConcept();
        archived.setGameId(game.getId());
        archived.setPlayerId("player_1");
        challenge = new ChallengeConcept();
        challenge.setModelName("model1");
        challenge.updateState(ChallengeState.REFUSED);
        archived.setChallenge(challenge);
        archived.setArchivingDate(date("2018-11-07T11:00:00"));
        mongo.save(archived);

        RequestBuilder builder = null;
        try {

            builder = MockMvcRequestBuilders.get("/data/game/{gameId}/archive", game.getId())
                    .param("playerId", "player_1")
                    .param("from", "" + date("2018-11-07T01:00:00").getTime())
                    .param("to", "" + date("2018-11-07T12:00:00").getTime());
            mocker.perform(builder).andDo(print()).andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(jsonPath("$", hasSize(1)));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }


    private Date date(String isoDate) {
        return LocalDateTime.parse(isoDate).toDate();
    }
}
