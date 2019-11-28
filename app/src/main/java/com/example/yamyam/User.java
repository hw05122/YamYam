package com.example.yamyam;

class User {
        String userNickname;
        String userGender;
        String userStar;
        String userState;
        String userHashtag1;
        String userHashtag2;
        String userHashtag3;

        public String getUserNickname() {
                return userNickname;
        }

        public void setUserNickname(String userNickname) {
                this.userNickname = userNickname;
        }

        public String getUserGender() {
                return userGender;
        }

        public void setUserGender(String userGender) {
                this.userGender = userGender;
        }

        public String getUserStar() {
                return userStar;
        }

        public void setUserStar(String userStar) {
                this.userStar = userStar;
        }

        public String getUserState() { return userState; }

        public void setUserState(String userState) {
                this.userState = userState;
        }

        public String getUserHashtag1() {
                return userHashtag1;
        }

        public void setUserHashtag1(String userHashtag1) {
                this.userHashtag1 = userHashtag1;
        }

        public String getUserHashtag2() {
                return userHashtag2;
        }

        public void setUserHashtag2(String userHashtag2) {
                this.userHashtag2 = userHashtag2;
        }

        public String getUserHashtag3() {
                return userHashtag3;
        }

        public void setUserHashtag3(String userHashtag3) {
                this.userHashtag3 = userHashtag3;
        }

        public User(String userNickname, String userGender, String userStar, String userState, String userHashtag1, String userHashtag2, String userHashtag3) {
                this.userNickname = userNickname;
                this.userGender = userGender;
                this.userStar = userStar;
                this.userState = userState;
                this.userHashtag1 = userHashtag1;
                this.userHashtag2 = userHashtag2;
                this.userHashtag3 = userHashtag3;
        }
}