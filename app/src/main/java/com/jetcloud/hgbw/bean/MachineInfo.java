package com.jetcloud.hgbw.bean;

/**
 * Created by Cqing on 2016/12/12.
 */

public class MachineInfo {
    private String id;
    private String name;
    private boolean selected ;
    private boolean isEditor;

    public String getId() {
        return id;
    }

    public MachineInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isEditor() {
        return isEditor;
    }

    public void setEditor(boolean editor) {
        isEditor = editor;
    }
}
