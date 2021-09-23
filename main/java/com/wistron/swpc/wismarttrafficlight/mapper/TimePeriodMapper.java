package com.wistron.swpc.wismarttrafficlight.mapper;

import java.util.List;
import com.wistron.swpc.wismarttrafficlight.entity.TimePeriod;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TimePeriodMapper {
    /**
     * 依據路口抓取時制表
     * @param intersectionId
     * @return
     */
    List<TimePeriod> selectTimePeriodByIntersectionId(String intersectionId);
}