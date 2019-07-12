package com.zlw.generator.db;

import java.io.Serializable;
import java.util.Objects;

public class ModuleBean implements Serializable {

    private String serial;
    private String moduleName;
    private String fileExt;
    private String templateFile;
    private Boolean isEdit;

    public ModuleBean() {
    }

    public ModuleBean(String moduleName, String fileExt, String templateFile, Boolean isEdit) {
        this.moduleName = moduleName;
        this.fileExt = fileExt;
        this.templateFile = templateFile;
        this.isEdit = isEdit;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }


    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }


    public Boolean getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(Boolean edit) {
        isEdit = edit;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModuleBean that = (ModuleBean) o;
        return Objects.equals(moduleName, that.moduleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleName);
    }
}
