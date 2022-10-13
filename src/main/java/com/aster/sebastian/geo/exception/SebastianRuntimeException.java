package com.aster.sebastian.geo.exception;

/**
 * @author astercasc
 */
public class SebastianRuntimeException extends RuntimeException {

    /**
     * 生成异常
     */
    public SebastianRuntimeException(String message) {
        super(message);
    }

    /**
     * 已抓异常抛出
     */
    public SebastianRuntimeException(Throwable cause) {
        super(cause);
    }


}
