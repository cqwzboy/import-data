package com.qc.itaojin.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by fuqinqin on 2018/6/26.
 */
public enum KeyType {
    NONE("无主键"),
    PRIMARY_KEY("单主键"),
    COMBINE_KEY("联合主键"),
    ;

    private String text;

    KeyType(String text){
        this.text = text;
    }

    public static KeyType nameOf(String name){
        if(StringUtils.isBlank(name)){
            return null;
        }

        name = name.toUpperCase();

        for (KeyType keyType : KeyType.values()) {
            if(keyType.name().equals(name)){
                return keyType;
            }
        }

        return null;
    }

    public boolean equalsTo(KeyType type){
        if(type == null){
            return false;
        }

        if(type == this){
            return true;
        }

        if(type.text.equals(this.text) && type.name().equals(this.name())){
            return true;
        }

        return false;
    }

}
