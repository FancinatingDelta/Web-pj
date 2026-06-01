package com.pj.mapper;

import com.pj.domain.SimulationRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SimulationRecordMapper {
    int insert(SimulationRecord record);
}
