package com.pj.controller;

import com.pj.domain.EvaluationCheckRequest;
import com.pj.domain.EvaluationResult;
import com.pj.domain.EvaluationScenario;
import com.pj.domain.PresetModel;
import com.pj.domain.SimulationRecord;
import com.pj.domain.SimulationRequest;
import com.pj.domain.SimulationStepResponse;
import com.pj.mapper.SimulationRecordMapper;
import com.pj.service.EvaluationService;
import com.pj.service.PresetService;
import com.pj.service.SimulationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SimulationController {
    private final SimulationService simulationService;
    private final PresetService presetService;
    private final EvaluationService evaluationService;
    private final SimulationRecordMapper recordMapper;

    public SimulationController(SimulationService simulationService,
                                PresetService presetService,
                                EvaluationService evaluationService,
                                SimulationRecordMapper recordMapper) {
        this.simulationService = simulationService;
        this.presetService = presetService;
        this.evaluationService = evaluationService;
        this.recordMapper = recordMapper;
    }

    @PostMapping("/simulation/step")
    public SimulationStepResponse nextStep(@Valid @RequestBody SimulationRequest request) {
        return simulationService.nextStep(request.getGrid(), request.getRuleConfig());
    }

    @GetMapping("/presets")
    public List<PresetModel> listPresets() {
        return presetService.listPresets();
    }

    @GetMapping("/evaluation/scenarios")
    public List<EvaluationScenario> listScenarios() {
        return evaluationService.listScenarios();
    }

    @PostMapping("/evaluation/check")
    public EvaluationResult checkAnswer(@Valid @RequestBody EvaluationCheckRequest request) {
        return evaluationService.evaluate(request.getScenarioId(), request.getAnswerGrid());
    }

    @PostMapping("/records")
    public Map<String, Object> saveRecord(@RequestBody SimulationRecord record) {
        int inserted = recordMapper.insert(record);
        return Map.of(
                "success", inserted > 0,
                "id", record.getId() == null ? -1 : record.getId()
        );
    }
}
