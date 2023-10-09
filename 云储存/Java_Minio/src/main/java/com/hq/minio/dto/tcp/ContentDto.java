package com.hq.minio.dto.tcp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author damon
 * @data 2023/8/23 10:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContentDto {
    private String order;
    private String content;
}
