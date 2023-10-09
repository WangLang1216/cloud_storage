package com.hq.minio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author damon
 * @data 2023/8/10 16:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilesAndSonlogsDto {
    private String fileName;
    private String sonlog;
}
