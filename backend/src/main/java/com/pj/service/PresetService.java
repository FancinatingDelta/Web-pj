package com.pj.service;

import com.pj.domain.AutomataType;
import com.pj.domain.NeighborhoodType;
import com.pj.domain.PresetModel;
import com.pj.domain.RuleConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PresetService {

    public List<PresetModel> listPresets() {
        List<PresetModel> list = new ArrayList<>();
        // 生灵 (Still Life)
        list.add(buildBlock());
        list.add(buildBeehive());
        list.add(buildLoaf());
        list.add(buildBoat());
        // 振荡器 (Oscillator)
        list.add(buildBlinker());
        list.add(buildToad());
        list.add(buildBeacon());
        list.add(buildPulsar());
        // 飞船 (Spaceship)
        list.add(buildGlider());
        list.add(buildLWSS());
        list.add(buildMWSS());
        list.add(buildHWSS());
        // 繁衍器 (Gun)
        list.add(buildGosperGliderGun());
        // 集合 (Methuselah)
        list.add(buildRPentomino());
        list.add(buildDiehard());
        list.add(buildAcorn());
        // 创造 (Constructor)
        list.add(buildGliderSynthesis());
        list.add(buildReflector());
        // 射线 (Wave)
        list.add(buildPuffer());
        // 电路 (Circuit)
        list.add(buildEater());
        list.add(buildGliderDuplicator());
        // 1D 规则
        list.add(buildRule30());
        list.add(buildRule110());
        return list;
    }

    /* ---- helpers ---- */

    private RuleConfig lifeConfig() {
        RuleConfig c = new RuleConfig();
        c.setAutomataType(AutomataType.LIFE_GAME_2D);
        c.setNeighborhoodType(NeighborhoodType.MOORE);
        c.setSurviveMin(2);
        c.setSurviveMax(3);
        c.setBirthValue(3);
        c.setRuleNumber(0);
        c.setProbability(1.0);
        return c;
    }

    private int[][] blank(int rows, int cols) {
        return new int[rows][cols];
    }

    private void set(int[][] g, int r, int c, int v) {
        g[r][c] = v;
    }

    /* ================================================================
       生灵 (Still Life) — 静态稳定结构
       ================================================================ */

    private PresetModel buildBlock() {
        int[][] g = blank(20, 20);
        set(g, 9, 9, 1); set(g, 9, 10, 1);
        set(g, 10, 9, 1); set(g, 10, 10, 1);
        return preset("still-block", "方块 (Block)", "生灵",
            "最简单的静态结构，由 2×2 四个活细胞组成。没有任何邻居变化能使它改变，是所有静态生命中最小且最稳定的形态。",
            lifeConfig(), g);
    }

    private PresetModel buildBeehive() {
        int[][] g = blank(20, 20);
        set(g, 8, 10, 1); set(g, 8, 11, 1);
        set(g, 9, 9, 1);  set(g, 9, 12, 1);
        set(g, 10, 10, 1); set(g, 10, 11, 1);
        return preset("still-beehive", "蜂巢 (Beehive)", "生灵",
            "由 6 个细胞组成的稳定结构，因其形状似蜂巢而得名。在康威生命游戏中是最常见的自然生成的静态模式之一。",
            lifeConfig(), g);
    }

    private PresetModel buildLoaf() {
        int[][] g = blank(20, 20);
        set(g, 7, 10, 1); set(g, 7, 11, 1);
        set(g, 8, 9, 1);  set(g, 8, 12, 1);
        set(g, 9, 10, 1); set(g, 9, 13, 1);
        set(g, 10, 11, 1); set(g, 10, 12, 1);
        return preset("still-loaf", "面包 (Loaf)", "生灵",
            "由 7 个细胞组成的稳定结构，形状像一块面包。是常见的静态结构之一。",
            lifeConfig(), g);
    }

    private PresetModel buildBoat() {
        int[][] g = blank(20, 20);
        set(g, 8, 9, 1); set(g, 8, 10, 1);
        set(g, 9, 9, 1); set(g, 9, 11, 1);
        set(g, 10, 10, 1);
        return preset("still-boat", "小船 (Boat)", "生灵",
            "由 5 个细胞组成的稳定结构，外形酷似一艘小船。是基础静态模式之一。",
            lifeConfig(), g);
    }

    /* ================================================================
       振荡器 (Oscillator) — 周期性重复
       ================================================================ */

    private PresetModel buildBlinker() {
        int[][] g = blank(20, 20);
        set(g, 10, 9, 1); set(g, 10, 10, 1); set(g, 10, 11, 1);
        return preset("osc-blinker", "闪光灯 (Blinker)", "振荡器",
            "周期为 2 的振荡器。3 个水平细胞与 3 个垂直细胞交替切换，是最简单的振荡器。也被称为'交通灯'。",
            lifeConfig(), g);
    }

    private PresetModel buildToad() {
        int[][] g = blank(20, 20);
        set(g, 9, 9, 1); set(g, 9, 10, 1); set(g, 9, 11, 1);
        set(g, 10, 8, 1); set(g, 10, 9, 1); set(g, 10, 10, 1);
        return preset("osc-toad", "蟾蜍 (Toad)", "振荡器",
            "周期为 2 的振荡器，由两行各 3 个细胞错位组成。每一代在两相之间来回切换，外形类似蟾蜍。",
            lifeConfig(), g);
    }

    private PresetModel buildBeacon() {
        int[][] g = blank(20, 20);
        set(g, 8, 8, 1); set(g, 8, 9, 1);
        set(g, 9, 8, 1); set(g, 9, 9, 1);
        set(g, 10, 10, 1); set(g, 10, 11, 1);
        set(g, 11, 10, 1); set(g, 11, 11, 1);
        return preset("osc-beacon", "信标 (Beacon)", "振荡器",
            "周期为 2 的振荡器，由两个对角的 2×2 方块组成。两个方块交替闪烁，如同信标灯。",
            lifeConfig(), g);
    }

    private PresetModel buildPulsar() {
        int[][] g = blank(20, 20);
        int[][] coords = {
            {4,6},{4,7},{4,12},{4,13}, {5,6},{5,7},{5,12},{5,13},
            {6,4},{6,6},{6,8},{6,10},{6,12},{6,14},
            {7,4},{7,6},{7,8},{7,10},{7,12},{7,14},
            {8,4},{8,6},{8,8},{8,10},{8,12},{8,14},
            {9,6},{9,7},{9,12},{9,13},
            {10,6},{10,7},{10,12},{10,13},
            {11,4},{11,6},{11,8},{11,10},{11,12},{11,14},
            {12,4},{12,6},{12,8},{12,10},{12,12},{12,14},
            {13,4},{13,6},{13,8},{13,10},{13,12},{13,14},
            {14,6},{14,7},{14,12},{14,13}, {15,6},{15,7},{15,12},{15,13}
        };
        for (int[] c : coords) set(g, c[0], c[1], 1);
        return preset("osc-pulsar", "脉冲星 (Pulsar)", "振荡器",
            "周期为 3 的振荡器，是生命游戏中最著名的大型振荡器之一。由 John Conway 于 1970 年发现，具有高度对称的结构。",
            lifeConfig(), g);
    }

    /* ================================================================
       飞船 (Spaceship) — 在网格中平移
       ================================================================ */

    private PresetModel buildGlider() {
        int[][] g = blank(20, 20);
        set(g, 8, 9, 1);
        set(g, 9, 10, 1);
        set(g, 10, 8, 1); set(g, 10, 9, 1); set(g, 10, 10, 1);
        return preset("ship-glider", "滑翔机 (Glider)", "飞船",
            "最著名的移动模式，由 5 个活细胞组成。每 4 代循环一次并沿对角线平移一格，是生命游戏信息传递的基本单元。",
            lifeConfig(), g);
    }

    private PresetModel buildLWSS() {
        int[][] g = blank(30, 30);
        set(g, 12, 13, 1);
        set(g, 12, 16, 1);
        set(g, 13, 12, 1);
        set(g, 14, 12, 1); set(g, 14, 16, 1);
        set(g, 15, 12, 1); set(g, 15, 13, 1); set(g, 15, 14, 1); set(g, 15, 15, 1);
        return preset("ship-lwss", "轻型飞船 (LWSS)", "飞船",
            "轻型飞船（Lightweight Spaceship），每 4 代水平移动 2 格，是除滑翔机外最小的飞船。",
            lifeConfig(), g);
    }

    private PresetModel buildMWSS() {
        int[][] g = blank(30, 30);
        set(g, 12, 14, 1);
        set(g, 12, 17, 1);
        set(g, 13, 12, 1);
        set(g, 14, 12, 1); set(g, 14, 17, 1);
        set(g, 15, 12, 1); set(g, 15, 13, 1); set(g, 15, 14, 1); set(g, 15, 15, 1); set(g, 15, 16, 1);
        return preset("ship-mwss", "中型飞船 (MWSS)", "飞船",
            "中型飞船（Middleweight Spaceship），比 LWSS 稍大，同样每 4 代水平移动 2 格。",
            lifeConfig(), g);
    }

    private PresetModel buildHWSS() {
        int[][] g = blank(30, 30);
        set(g, 12, 15, 1);
        set(g, 12, 18, 1);
        set(g, 13, 12, 1);
        set(g, 14, 12, 1); set(g, 14, 18, 1);
        set(g, 15, 12, 1); set(g, 15, 13, 1); set(g, 15, 14, 1); set(g, 15, 15, 1); set(g, 15, 16, 1); set(g, 15, 17, 1);
        return preset("ship-hwss", "重型飞船 (HWSS)", "飞船",
            "重型飞船（Heavyweight Spaceship），是最重的标准飞船，每 4 代水平移动 2 格。",
            lifeConfig(), g);
    }

    /* ================================================================
       繁衍器 (Gun) — 持续产生飞船
       ================================================================ */

    private PresetModel buildGosperGliderGun() {
        int[][] g = blank(40, 40);
        int[][] cells = {
            {5,24},{5,25},{6,23},{6,25},{7,13},{7,14},{7,23},{7,24},
            {8,12},{8,16},{8,23},{8,24},
            {9,1},{9,2},{9,11},{9,17},{9,23},{9,24},
            {10,1},{10,2},{10,11},{10,15},{10,17},{10,18},{10,22},{10,24},
            {11,11},{11,17},{11,24},
            {12,12},{12,16},
            {13,13},{13,14},
            {16,0},{16,1},{16,8},{16,9},{16,10},
            {17,0},{17,1},{17,7},{17,11},
            {18,10},{18,11},{18,22},{18,23},
            {19,12},{19,13},{19,22},{19,23},
            {22,3},{22,4},{22,13},{22,14},{22,20},{22,21},
            {23,2},{23,5},{23,13},{23,14},
            {24,2},{24,5},{24,12},{24,16},{24,20},{24,21},
            {25,3},{25,4},{25,12},{25,16},{25,20},{25,21},
            {26,13},{26,14},
            {27,14}
        };
        for (int[] c : cells) set(g, c[0], c[1], 1);
        return preset("gun-gosper", "高斯帕滑翔机枪 (Gosper Glider Gun)", "繁衍器",
            "由 Bill Gosper 于 1970 年发现，是生命游戏中第一个已知的滑翔机枪。每 30 代发射一架滑翔机，证明了生命游戏可以无限增长。这一发现为康威生命游戏赢得 50 美元奖金。",
            lifeConfig(), g);
    }

    /* ================================================================
       集合 (Methuselah) — 小初始状态，极长演化
       ================================================================ */

    private PresetModel buildRPentomino() {
        int[][] g = blank(30, 30);
        set(g, 14, 14, 1); set(g, 14, 15, 1);
        set(g, 15, 13, 1); set(g, 15, 14, 1);
        set(g, 16, 14, 1);
        return preset("meth-r-pentomino", "R-pentomino", "集合",
            "由 5 个细胞组成的小型初始模式，需要 1103 代才能完全稳定。最终产生 116 个活细胞（包括 6 架滑翔机），是新用户探索生命游戏复杂性的经典起点。",
            lifeConfig(), g);
    }

    private PresetModel buildDiehard() {
        int[][] g = blank(30, 30);
        set(g, 13, 18, 1);
        set(g, 14, 12, 1); set(g, 14, 13, 1);
        set(g, 15, 13, 1); set(g, 15, 17, 1); set(g, 15, 18, 1); set(g, 15, 19, 1);
        return preset("meth-diehard", "Diehard", "集合",
            "由 7 个细胞组成，演化 130 代后所有细胞完全消失。是少有的最终完全消亡的初始模式，展示了生命游戏的不可预测性。",
            lifeConfig(), g);
    }

    private PresetModel buildAcorn() {
        int[][] g = blank(50, 50);
        set(g, 22, 25, 1);
        set(g, 23, 23, 1); set(g, 23, 25, 1);
        set(g, 24, 13, 1); set(g, 24, 14, 1); set(g, 24, 23, 1); set(g, 24, 24, 1); set(g, 24, 25, 1); set(g, 24, 27, 1);
        return preset("meth-acorn", "橡子 (Acorn)", "集合",
            "由 7 个细胞组成的初始模式，需要 5206 代才能稳定。最终产生 633 个细胞，其中包括至少 13 架滑翔机。是生命游戏中演化时间最长的玛土撒拉之一。",
            lifeConfig(), g);
    }

    /* ================================================================
       创造 (Constructor) — 构造其他图案
       ================================================================ */

    private PresetModel buildGliderSynthesis() {
        int[][] g = blank(30, 30);
        // glider 1 from top-left
        set(g, 4, 4, 1);
        set(g, 5, 5, 1);
        set(g, 6, 3, 1); set(g, 6, 4, 1); set(g, 6, 5, 1);
        // glider 2 from top-right (mirrored approach)
        set(g, 4, 18, 1);
        set(g, 5, 16, 1); set(g, 5, 17, 1); set(g, 5, 18, 1);
        set(g, 6, 17, 1);
        return preset("constr-synthesis", "滑翔机合成 (Glider Synthesis)", "创造",
            "两架滑翔机从不同方向碰撞，最终形成稳定的方块结构。这种用滑翔机碰撞来构造目标图案的技术称为滑翔机合成，是构造器理论的基础。",
            lifeConfig(), g);
    }

    private PresetModel buildReflector() {
        int[][] g = blank(30, 30);
        // eater-like reflector structure (simplified)
        set(g, 8, 14, 1); set(g, 8, 15, 1);
        set(g, 9, 13, 1); set(g, 9, 15, 1);
        set(g, 10, 13, 1); set(g, 10, 14, 1);
        // incoming glider
        set(g, 12, 8, 1);
        set(g, 13, 9, 1);
        set(g, 14, 7, 1); set(g, 14, 8, 1); set(g, 14, 9, 1);
        return preset("constr-reflector", "反射器 (Reflector)", "创造",
            "演示一个静态结构（上方）与一架滑翔机（下方）的碰撞。滑翔机到达后会改变方向 90 度反射出去，展示了生命游戏中信息路由的基本原理。",
            lifeConfig(), g);
    }

    /* ================================================================
       射线 (Wave) — 沿方向传播
       ================================================================ */

    private PresetModel buildPuffer() {
        int[][] g = blank(40, 40);
        // simplified puffer engine
        set(g, 18, 10, 1); set(g, 18, 11, 1); set(g, 18, 12, 1);
        set(g, 19, 9, 1);  set(g, 19, 12, 1); set(g, 19, 13, 1);
        set(g, 20, 10, 1); set(g, 20, 11, 1); set(g, 20, 12, 1);
        set(g, 21, 10, 1); set(g, 21, 11, 1); set(g, 21, 12, 1);
        return preset("wave-puffer", "喷烟者 (Puffer)", "射线",
            "一种移动的同时在身后留下静态结构（如方块）的引擎，像火车喷烟一样。演化后会在网格上留下一串轨迹，展示了生命游戏中移动与建造的结合。",
            lifeConfig(), g);
    }

    /* ================================================================
       电路 (Circuit) — 逻辑门和信号处理
       ================================================================ */

    private PresetModel buildEater() {
        int[][] g = blank(30, 30);
        // eater structure
        set(g, 10, 14, 1); set(g, 10, 15, 1);
        set(g, 11, 14, 1); set(g, 11, 16, 1);
        set(g, 12, 16, 1); set(g, 12, 17, 1);
        set(g, 13, 17, 1);
        // incoming glider
        set(g, 16, 6, 1);
        set(g, 17, 7, 1);
        set(g, 18, 5, 1); set(g, 18, 6, 1); set(g, 18, 7, 1);
        return preset("circuit-eater", "吞噬者 (Eater)", "电路",
            "一架滑翔机飞向一个静止的吞噬者结构。吞噬者会'吃掉'滑翔机并恢复原状，如同电路中的信号终端。这是生命游戏电路的基本元件。",
            lifeConfig(), g);
    }

    private PresetModel buildGliderDuplicator() {
        int[][] g = blank(30, 30);
        // A simple breeder-like setup: glider stream splits
        // block
        set(g, 8, 18, 1); set(g, 8, 19, 1);
        set(g, 9, 18, 1); set(g, 9, 19, 1);
        // incoming glider from left
        set(g, 14, 8, 1);
        set(g, 15, 9, 1);
        set(g, 16, 7, 1); set(g, 16, 8, 1); set(g, 16, 9, 1);
        return preset("circuit-duplicator", "信号分流器 (Glider Duplicator)", "电路",
            "一架滑翔机飞向一个方块。碰撞后滑翔机被吸收，同时方块移位产生额外的变化，展示了生命游戏中信号与静态结构交互的基本原理。",
            lifeConfig(), g);
    }

    /* ================================================================
       1D 经典规则
       ================================================================ */

    private PresetModel buildRule30() {
        RuleConfig config = new RuleConfig();
        config.setAutomataType(AutomataType.RULE_30_1D);
        config.setNeighborhoodType(NeighborhoodType.MOORE);
        config.setRuleNumber(30);
        config.setProbability(1.0);

        int[][] grid = new int[1][61];
        grid[0][30] = 1;

        PresetModel model = new PresetModel();
        model.setId("1d-rule30");
        model.setTitle("Rule 30 - 单点初始");
        model.setDescription("Rule 30 由 Stephen Wolfram 于 1983 年提出，以产生混沌图样著称。中心列呈现高度随机性，被用于 Mathematica 的随机数生成器。");
        model.setCategory("1D 经典");
        model.setRuleConfig(config);
        model.setInitialGrid(grid);
        return model;
    }

    private PresetModel buildRule110() {
        RuleConfig config = new RuleConfig();
        config.setAutomataType(AutomataType.RULE_110_1D);
        config.setNeighborhoodType(NeighborhoodType.MOORE);
        config.setRuleNumber(110);
        config.setProbability(1.0);

        int[][] grid = new int[1][61];
        grid[0][30] = 1;
        grid[0][29] = 1;

        PresetModel model = new PresetModel();
        model.setId("1d-rule110");
        model.setTitle("Rule 110 - 双点初始");
        model.setDescription("Rule 110 是唯一被证明具有图灵完备性的初等元胞自动机。Matthew Cook 于 2004 年完成证明，该规则可执行任何可计算函数。");
        model.setCategory("1D 经典");
        model.setRuleConfig(config);
        model.setInitialGrid(grid);
        return model;
    }

    /* ---- factory ---- */

    private PresetModel preset(String id, String title, String category,
                                String description, RuleConfig config, int[][] grid) {
        PresetModel m = new PresetModel();
        m.setId(id);
        m.setTitle(title);
        m.setCategory(category);
        m.setDescription(description);
        m.setRuleConfig(config);
        m.setInitialGrid(grid);
        return m;
    }
}
