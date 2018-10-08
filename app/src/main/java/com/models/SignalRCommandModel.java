package com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SignalRCommandModel {

    @SerializedName("H")
    @Expose
    private String h;
    @SerializedName("M")
    @Expose
    private String m;
    @SerializedName("A")
    @Expose
    private List<A> a = null;

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public List<A> getA() {
        return a;
    }

    public void setA(List<A> a) {
        this.a = a;
    }

    public class A {

        @SerializedName("Serials")
        @Expose
        private String serials;
        @SerializedName("Command")
        @Expose
        private Integer command;
        @SerializedName("Param1")
        @Expose
        private String param1;
        @SerializedName("Param2")
        @Expose
        private String param2;
        @SerializedName("Param3")
        @Expose
        private String param3;

        public String getSerials() {
            return serials;
        }

        public void setSerials(String serials) {
            this.serials = serials;
        }

        public Integer getCommand() {
            return command;
        }

        public void setCommand(Integer command) {
            this.command = command;
        }

        public String getParam1() {
            return param1;
        }

        public void setParam1(String param1) {
            this.param1 = param1;
        }

        public String getParam2() {
            return param2;
        }

        public void setParam2(String param2) {
            this.param2 = param2;
        }

        public String getParam3() {
            return param3;
        }

        public void setParam3(String param3) {
            this.param3 = param3;
        }

    }
}