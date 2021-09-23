package com.wistron.swpc.wismarttrafficlight.mapper;

import com.wistron.swpc.wismarttrafficlight.entity.SubPhase;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 时相数据库操作
 */
@Mapper
public interface SubPhaseMapper {

    /**
     * 依據時制uuid抓出時相
     * @param intersectionId
     * @return
     */
    List<SubPhase> selectByTimingUuid(String timing_uuid);

}
