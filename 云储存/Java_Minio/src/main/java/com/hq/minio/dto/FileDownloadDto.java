package com.hq.minio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author damon
 * @data 2023/8/18 10:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileDownloadDto {
    private List<String> file;

}
