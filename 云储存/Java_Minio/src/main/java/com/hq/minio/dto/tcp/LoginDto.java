package com.hq.minio.dto.tcp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author damon
 * @data 2023/8/22 14:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginDto {
    private String order;
    private String userName;
    private String password;
}
