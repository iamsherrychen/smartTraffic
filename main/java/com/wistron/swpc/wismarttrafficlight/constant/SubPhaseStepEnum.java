package com.wistron.swpc.wismarttrafficlight.constant;

public enum SubPhaseStepEnum {
    STEP1("1"),
    STEP2("2"),
    STEP3("3"),
    STEP4("4"),
    STEP5("5"),
    FLASH("0xBF");

    private String currentStep;

    SubPhaseStepEnum(String currentStep) {
        this.currentStep = currentStep;
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public static SubPhaseStepEnum getInstance(String id) {
        SubPhaseStepEnum instance = null;
        for (SubPhaseStepEnum item : values()) {
            if (item.currentStep.equals(id)) {
                instance = item;
                break;
            }
        }
        return instance;
    }
}