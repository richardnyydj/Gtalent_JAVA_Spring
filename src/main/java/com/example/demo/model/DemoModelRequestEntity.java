package com.example.demo.model;

import jakarta.validation.constraints.NotNull;

public class DemoModelRequestEntity {
    @NotNull // 不可為空的標籤，如果為空則會報錯
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
