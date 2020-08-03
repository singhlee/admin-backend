package org.singhlee.admin.modules.sys.entity;

import lombok.Data;

import java.util.List;


@Data
public class GenConfig {
    private String mainPath;
    private String packagePath;
    private String moduleName;
    private String author;
    private String email;
    private List<String> genTable;
}
