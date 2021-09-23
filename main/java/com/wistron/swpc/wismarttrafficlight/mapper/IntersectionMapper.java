package com.wistron.swpc.wismarttrafficlight.mapper;

import com.wistron.swpc.wismarttrafficlight.entity.Intersection;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IntersectionMapper {

    List<Intersection> selectAll();

    Intersection selectById(String id);

}
