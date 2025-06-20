package org.example.chocoloop;

public class Casa {
    private int casaNum;
    private String casaLetra, casaCluster;

    public Casa(int casaNum, String casaLetra, String casaCluster) {
        this.casaNum = casaNum;
        this.casaLetra = casaLetra;
        this.casaCluster = casaCluster;
    }

    public String getCasaLetra() {
        return casaLetra;
    }

    public String getCasaCluster() {
        return casaCluster;
    }

    public int getCasaNum() {
        return casaNum;
    }
}
