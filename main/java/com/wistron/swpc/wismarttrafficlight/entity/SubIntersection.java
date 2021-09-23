package com.wistron.swpc.wismarttrafficlight.entity;

public class SubIntersection {

    private String uuid;

    private String id;

    private String intersectionUuid;

    /**
     * 子路口连接到的 路口 id
     */
    private String connectTo;

    private String name;

    private String direction;

    private String description;

    private String createdAt;

    private String updatedAt;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getConnectTo() {
        return connectTo;
    }

    public void setConnectTo(String connectTo) {
        this.connectTo = connectTo;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntersectionUuid() {
        return intersectionUuid;
    }

    public void setIntersectionUuid(String intersectionUuid) {
        this.intersectionUuid = intersectionUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "SubIntersection{" +
                "uuid='" + uuid + '\'' +
                ", id='" + id + '\'' +
                ", intersectionUuid='" + intersectionUuid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
