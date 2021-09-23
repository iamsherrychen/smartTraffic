package com.wistron.swpc.wismarttrafficlight.constant;

/**
 * traffic box 信息交互常量
 */
public class TrafficBoxConst {

    /**
     * 时序状态 固定
     */
    public static final String CONTROL_STRATEGY_FIXED = "0x05";

    /**
     * 时序状态 动态
     */
    public static final String CONTROL_STRATEGY_AUTO = "0x10";


    /**
     * 时序状态 路口手动
     */
    public static final String CONTROL_STRATEGY_MANUAL = "0x04";

    /**
     * 获取 traffic box 信息
     */
    public static final String MSG_TYPE_GET = "GET";

    /**
     * 设置 traffic box 信息
     */
    public static final String MSG_TYPE_SET = "SET";

    /**
     * 接收 traffic box 信息
     */
    public static final String MSG_TYPE_RESPONSE = "RESPONSE";

    /**
     * 获取十字路口信号灯状态
     */
    public static final String GET_TRAFFICBOX_CONFIG = "GET_TRAFFICBOX_CONFIG";

    /**
     * 获取十字路口信号灯控制策略
     */
    public static final String GET_CONTROL_STRATEGY = "GET_CONTROL_STRATEGY";

    /**
     * 设置十字路口信号灯控制策略
     */
    public static final String SET_CONTROL_STRATEGY = "SET_CONTROL_STRATEGY";

    public static final String SUB_PHASE_ONE = "1";

    /**
     * 获取转向量
     */
    public static final String GET_INTERSECTION_FLOW_V2 = "GET_Intersection_FlowV2";

    /**
     * 获取转向量
     */
    public static final String GET_Intersection_VQ = "GET_Intersection_VQ";

}
