package com.imall.common;

public class Const {
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String TOKEN_PREFIX = "token_";

    public interface Role {
        int ROLE_ADMIN = 1;
        int ROLE_USER = 0;
    }

    public enum ProductStatusEnum {
        ON_SALE(1, "在售");

        private String status;
        private int code;

        ProductStatusEnum(int code, String status) {
            this.status = status;
            this.code = code;
        }


        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

}
