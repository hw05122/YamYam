package com.example.yamyam;

class PointList {
    String userNickname;
    String getPoint;
    String userPointResult1;
    String userPointResult2;

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getPoint() {
        return getPoint;
    }

    public void setPoint(String getPoint) {
        this.getPoint = getPoint;
    }

    public String getUserPointResult1() {
        return userPointResult1;
    }

    public void setUserPointResult(String userPointResult1) {
        this.userPointResult1 = userPointResult1;
    }

    public String getUserPointResult2() {
        return userPointResult2;
    }

    public void setUserPointResult2(String userPointResult2) {
        this.userPointResult2 = userPointResult2;
    }


    public PointList(String userPointResult1, String userPointResult2) {
        this.userPointResult1 = userPointResult1;
        this.userPointResult2 = userPointResult2;
//        this.getPoint = getPoint;
//        this.userNickname = userNickname;
    }
}
