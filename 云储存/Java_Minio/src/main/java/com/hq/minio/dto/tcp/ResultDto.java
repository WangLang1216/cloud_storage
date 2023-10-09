package com.hq.minio.dto.tcp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author damon
 * @data 2023/8/22 14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResultDto {
    private String result;
    private String userName;
    private String password;
    private String order;
}
