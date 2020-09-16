package test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 * Author：liuxing
 * Date：2020-09-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceReq {
    private String name;
    private Integer pageNum;
    private Integer deviceType;
    private Integer pageSize;
}
