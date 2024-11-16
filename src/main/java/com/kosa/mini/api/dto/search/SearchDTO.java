package com.kosa.mini.api.dto.search;

import lombok.Data;

@Data
public class SearchDTO {
    private String query;
    private String sort = "latest";
}
