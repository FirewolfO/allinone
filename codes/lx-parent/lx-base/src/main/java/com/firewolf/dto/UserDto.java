package com.firewolf.dto;

import com.firewolf.domain.ResponseEnum;
import com.firewolf.exception.BusinessException;
import com.firewolf.entity.User;
import com.firewolf.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Author: liuxing
 * Date: 2020/3/3 9:56
 */
@Slf4j
public class UserDto {
    public static User tansVo2PO(UserVO userVO) {
        User user = new User();
        try {
            BeanUtils.copyProperties(user, userVO);
        } catch (Exception e) {
            log.error("tansVo2PO error", e);
            throw new BusinessException(ResponseEnum.FAILED.getCode(), ResponseEnum.FAILED.getMsg());
        }
        return user;
    }
}
