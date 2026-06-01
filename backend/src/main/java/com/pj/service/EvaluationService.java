package com.pj.service;

import com.pj.domain.AutomataType;
import com.pj.domain.EvaluationResult;
import com.pj.domain.EvaluationScenario;
import com.pj.domain.NeighborhoodType;
import com.pj.domain.RuleConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationService {
    private final SimulationService simulationService;

    public EvaluationService(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    public List<EvaluationScenario> listScenarios() {
        return List.of(
                buildStillLifeScenario(),
                buildOscillatorScenario(),
                buildGliderScenario(),
                buildRule30Scenario(),
                buildRule110Scenario()
        );
    }

    public EvaluationResult evaluate(String scenarioId, int[][] answerGrid) {
        EvaluationScenario scenario = listScenarios().stream()
                .filter(item -> item.getId().equals(scenarioId))
                .findFirst()
                .orElse(null);
        if (scenario == null) {
            return new EvaluationResult(false, "场景不存在", Integer.MAX_VALUE);
        }

        int diff = simulationService.calculateDiffCount(scenario.getExpectedGrid(), answerGrid);
        if (diff == 0) {
            return new EvaluationResult(true, "结果正确，继续保持。", 0);
        }
        return new EvaluationResult(false, "结果与期望有偏差，请检查规则或步数。", diff);
    }

    private EvaluationScenario buildStillLifeScenario() {
        RuleConfig config = defaultLifeConfig();
        int[][] start = new int[6][6];
        start[2][2] = 1;
        start[2][3] = 1;
        start[3][2] = 1;
        start[3][3] = 1;

        return createScenario("eval-1", "静态结构", "方块结构演化 3 步后应保持不变。", start, config, 3);
    }

    private EvaluationScenario buildOscillatorScenario() {
        RuleConfig config = defaultLifeConfig();
        int[][] start = new int[6][6];
        start[2][1] = 1;
        start[2][2] = 1;
        start[2][3] = 1;
        return createScenario("eval-2", "振荡结构", "Blinker 演化 2 步后应回到初态。", start, config, 2);
    }

    private EvaluationScenario buildGliderScenario() {
        RuleConfig config = defaultLifeConfig();
        int[][] start = new int[8][8];
        start[1][2] = 1;
        start[2][3] = 1;
        start[3][1] = 1;
        start[3][2] = 1;
        start[3][3] = 1;
        return createScenario("eval-3", "滑翔机位移", "滑翔机演化 4 步后应向右下移动。", start, config, 4);
    }

    private EvaluationScenario buildRule30Scenario() {
        RuleConfig config = ruleConfig(AutomataType.RULE_30_1D, 30);
        int[][] start = new int[1][21];
        start[0][10] = 1;
        return createScenario("eval-4", "Rule30 基础", "中心单点演化 5 步，观察左侧混沌扩散。", start, config, 5);
    }

    private EvaluationScenario buildRule110Scenario() {
        RuleConfig config = ruleConfig(AutomataType.RULE_110_1D, 110);
        int[][] start = new int[1][21];
        start[0][10] = 1;
        start[0][9] = 1;
        return createScenario("eval-5", "Rule110 基础", "双点初始演化 4 步，验证模式生成。", start, config, 4);
    }

    private EvaluationScenario createScenario(String id, String title, String question,
                                              int[][] initialGrid, RuleConfig config, int steps) {
        EvaluationScenario scenario = new EvaluationScenario();
        scenario.setId(id);
        scenario.setTitle(title);
        scenario.setQuestion(question);
        scenario.setInitialGrid(initialGrid);
        scenario.setRuleConfig(config);
        scenario.setSteps(steps);
        scenario.setExpectedGrid(simulationService.runSteps(initialGrid, config, steps));
        return scenario;
    }

    private RuleConfig defaultLifeConfig() {
        RuleConfig config = new RuleConfig();
        config.setAutomataType(AutomataType.LIFE_GAME_2D);
        config.setNeighborhoodType(NeighborhoodType.MOORE);
        config.setSurviveMin(2);
        config.setSurviveMax(3);
        config.setBirthValue(3);
        return config;
    }

    private RuleConfig ruleConfig(AutomataType automataType, int ruleNumber) {
        RuleConfig config = new RuleConfig();
        config.setAutomataType(automataType);
        config.setNeighborhoodType(NeighborhoodType.MOORE);
        config.setRuleNumber(ruleNumber);
        return config;
    }
}
