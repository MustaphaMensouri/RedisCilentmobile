package com.example.redis_v5.data;

public class TerminalData {
    private String ip;
    private String port;
    private String command;
    private String result;


    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getCommand() {
        return command;
    }

    public String getResult() {
        return result;
    }

    public TerminalData(String ip, String port, String command, String result) {
        this.ip = ip;
        this.port = port;
        this.command = command;
        this.result = result;
    }
}
