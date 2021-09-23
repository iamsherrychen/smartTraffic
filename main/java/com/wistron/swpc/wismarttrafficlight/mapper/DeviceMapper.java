package com.wistron.swpc.wismarttrafficlight.mapper;

import com.wistron.swpc.wismarttrafficlight.entity.Device;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeviceMapper {

    List<Device> selectByIntersectionId(String intersectionId);

}
