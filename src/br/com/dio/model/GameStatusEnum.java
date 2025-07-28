package br.com.dio.model;

public enum GameStatusEnum {

    COMPLETE("completo"),
    INCOMPLETE("incompleto"),
    NON_STARTED("não iniciado");

    private String label;

    GameStatusEnum(final String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
