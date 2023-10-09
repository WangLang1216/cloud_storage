package com.hq.minio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author damon
 * @data 2023/8/16 9:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShareDto {
    private Long userId;
    private List<String> files;
}
