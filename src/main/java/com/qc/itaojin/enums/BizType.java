package com.qc.itaojin.enums;

/**
 * @desc y业务类型
 * @author fuqinqin
 * @date 2018-07-09
 */
public enum BizType {
    PAY("pay", "支付系统"),
    AI("ai", "语音系统"),
    BENCH("bench", "平台系统"),
    TJK("tjk", "淘金客"),
    ;

    private String code;
    private String text;

    BizType(String code, String text){
        this.code = code;
        this.text = text;
    }

    public String code(){
        return this.code;
    }

}
