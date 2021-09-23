package com.wistron.swpc.wismarttrafficlight.service;

import com.wistron.swpc.wismarttrafficlight.entity.Device;
import com.wistron.swpc.wismarttrafficlight.mapper.DeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceMapper deviceDao;

    public List<Device> getDeviceByIntersectionId(String intersectionId) {
        return deviceDao.selectByIntersectionId(intersectionId);
    }

}
