package com.zlw.generator.ui;

public interface TextDAO {
    void    create(String file);
    void    save(String s, String file);
    String  read(String file);
}
