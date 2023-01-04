package com.mycompany.doanlaptrinhmang;

public final class LapTrinhMang {

    private boolean pulseOximeter;
    private int SpO2;
    private double BPM;
    
    public LapTrinhMang(){
    }

    public LapTrinhMang(boolean pulseOximeter, int SpO2, double BPM) {
        this.pulseOximeter = pulseOximeter;
        this.SpO2 = SpO2;
        this.BPM = BPM;
    }
    
    public int getSpO2() {
        return SpO2;
    }

    public void setSpO2(int SpO2) {
        this.SpO2 = SpO2;
    }

    public double getBPM() {
        return BPM;
    }

    public void setBPM(double BPM) {
        this.BPM = BPM;
    }

    public boolean isPulseOximeter() {
        return pulseOximeter;
    }

    public void setPulseOximeter(boolean pulseOximeter) {
        this.pulseOximeter = pulseOximeter;
    }

    @Override
    public String toString() {
        return "{" + "pulseOximeter=" + pulseOximeter + ", SpO2=" + SpO2 + ", BPM=" + BPM + '}';
    }
}
