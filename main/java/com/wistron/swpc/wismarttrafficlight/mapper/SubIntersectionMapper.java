package com.wistron.swpc.wismarttrafficlight.mapper;

import com.wistron.swpc.wismarttrafficlight.entity.SubIntersection;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubIntersectionMapper {

    List<SubIntersection> selectByIntersectionId(String intersectionId);

}
