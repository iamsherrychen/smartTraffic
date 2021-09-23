package com.wistron.swpc.wismarttrafficlight.config;

import com.wistron.swpc.wismarttrafficlight.entity.CarDelay;
import com.wistron.swpc.wismarttrafficlight.entity.TrafficTrend;
import com.wistron.swpc.wismarttrafficlight.entity.TrafficTrendGoogle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.stereotype.Component;

@Component
public class MongoInitConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void initIndicesAfterStartup() {
        IndexOperations indexOps = mongoTemplate.indexOps(TrafficTrend.class);
        indexOps.ensureIndex(new Index().named("create_at").on("create_at", Sort.Direction.DESC).expire(1209600));
        IndexOperations indexOpsGoogle = mongoTemplate.indexOps(TrafficTrendGoogle.class);
        indexOpsGoogle.ensureIndex(new Index().named("create_at").on("create_at", Sort.Direction.DESC).expire(1209600));
        IndexOperations indexOpsCarDelay = mongoTemplate.indexOps(CarDelay.class);
        indexOpsCarDelay.ensureIndex(new Index().named("create_at").on("create_at", Sort.Direction.DESC).expire(1209600));
    }

}
