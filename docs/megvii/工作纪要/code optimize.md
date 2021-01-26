```java
package com.megvii.ebg.pangu.facade.web.service.middle_end.event;

import com.megvii.ebg.pangu.facade.web.dto.event.request.DeviceListReq;
import com.megvii.ebg.pangu.facade.web.dto.event.request.EventInvokeListReq;
import com.megvii.ebg.pangu.facade.web.dto.event.response.DeviceListResp;
import com.megvii.ebg.pangu.facade.web.dto.event.response.EventInvokeListResp;
import com.megvii.ebg.pangu.facade.web.dto.event.response.GetServerHostResp;
import com.megvii.ebg.pangu.facade.web.dto.request.device.DeviceControlReqDto;
import com.megvii.ebg.pangu.facade.web.dto.request.door.DoorDeviceListReqDto;
import com.megvii.ebg.pangu.facade.web.dto.response.device.DeviceControlListRespDto;
import com.megvii.ebg.pangu.facade.web.dto.response.door.DoorDeviceListRespDto;
import com.megvii.ebg.pangu.facade.web.entity.User;
import com.megvii.ebg.pangu.facade.web.service.middle_end.device.DeviceService;
import com.megvii.ebg.pangu.facade.web.service.middle_end.door.DoorService;
import com.megvii.ebg.pangu.ms.base.api.EventInvokeApi;
import com.megvii.ebg.pangu.ms.base.api.dto.resp.GetServerHostRespDTO;
import com.megvii.ebg.pangu.ms.base.api.enums.EventTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.megvii.ebg.pangu.ms.base.api.enums.EventTypeEnum.PERSON;
import static com.megvii.ebg.pangu.ms.base.api.enums.EventTypeEnum.PERSON__STRANGE;
import static java.util.stream.Collectors.toList;

/**
 * 描述： 事件联动服务
 * Author：liuxing
 * Date：2020/4/2
 */
@Service
@Slf4j
public class EventInvokeService {


    @Resource
    private EventInvokeApi eventInvokeApi;


    @Resource
    private DeviceService deviceService;


    @Resource
    private DoorService doorService;

    /**
     * 查询事件联动类型列表
     *
     * @param req
     * @return
     */
    public List<EventInvokeListResp> list(EventInvokeListReq req) {
        return eventInvokeApi.list(req.toReqDTO()).stream().map(EventInvokeListResp::fromRespDTO).collect(toList());
    }

    /**
     * 获取联动设备列表
     *
     * @return
     */
    public List<DeviceListResp> deviceList(DeviceListReq deviceListReq, User user) {
        EventTypeEnum eventTypeEnum = EventTypeEnum.getTypeById(deviceListReq.getEventType(), deviceListReq.getZoneId());

        // 门禁设备
        if (EventTypeEnum.DOOR_CONTROL.getId().equals(eventTypeEnum.getParentId()) || EventTypeEnum.DOOR_CONTROL.getId().equals(eventTypeEnum.getId()) || EventTypeEnum.IN_OUT.getId().equals(eventTypeEnum.getParentId()) || EventTypeEnum.IN_OUT.getId().equals(eventTypeEnum.getId())) {
            DoorDeviceListReqDto reqDto = new DoorDeviceListReqDto();
            reqDto.setPassModel(false);
            List<DoorDeviceListRespDto> doorDeviceListRespDto = doorService.queryDoorList(reqDto, user);
            return convertDoorDeviceForUI(doorDeviceListRespDto);
        }

        // 安防设备
        DeviceControlReqDto deviceControlReqDto = new DeviceControlReqDto();
        if (PERSON.getId().equals(eventTypeEnum.getParentId()) || PERSON.getId().equals(eventTypeEnum.getId())) {
            deviceControlReqDto.setDeviceModes(Arrays.asList(1, 4));
            if (PERSON__STRANGE.getId().equals(eventTypeEnum.getId())) { // 陌生人
                deviceControlReqDto.setQueryType(1);
            } else {
                deviceControlReqDto.setQueryType(2);
            }

        } else if (EventTypeEnum.VEHICLE.getId().equals(eventTypeEnum.getParentId()) || EventTypeEnum.VEHICLE.getId().equals(eventTypeEnum.getId())) {
            // 车辆
            deviceControlReqDto.setDeviceModes(Arrays.asList(2));
        } else if (EventTypeEnum.AREA_WARN.getId().equals(eventTypeEnum.getParentId()) || EventTypeEnum.AREA_WARN.getId().equals(eventTypeEnum.getId())) {
            // 告警
            deviceControlReqDto.setDeviceModes(Arrays.asList(3));
        }

        List<DeviceControlListRespDto> deviceControlListRespDtos = deviceService.queryDeviceControl(deviceControlReqDto, user);
        return convertPersonDeviceForUI(deviceControlListRespDtos);
    }

    /**
     * 获取服务器信息
     *
     * @return
     */
    public GetServerHostResp getInvokeServerInfo() {
        GetServerHostRespDTO respDTO = eventInvokeApi.getInvokeServerInfo();
        return GetServerHostResp.fromRespDTO(respDTO);
    }

    /**
     * 把通行设备转换适合前台展示的
     *
     * @param baseDeviceResps
     * @return
     */
    private List<DeviceListResp> convertDoorDeviceForUI(List<DoorDeviceListRespDto> baseDeviceResps) {
        return convertDeviceInfoForUI(baseDeviceResps, ((doorDevice, device) -> {
            device.setName(doorDevice.getName());
            device.setId(doorDevice.getId());
        }));
    }

    /**
     * 转换人员管控设备到前台展示
     *
     * @param baseDeviceResps
     * @return
     */
    private List<DeviceListResp> convertPersonDeviceForUI(List<DeviceControlListRespDto> baseDeviceResps) {
        return convertDeviceInfoForUI(baseDeviceResps, ((baseDevice, device) -> {
            device.setName(baseDevice.getName());
            device.setId(baseDevice.getId());
        }));
    }

    /**
     * 把设备信息转成适合前台展示的
     *
     * @param originDeviceInfoList
     * @param biConsumer
     * @param <T>
     * @return
     */
    private <T> List<DeviceListResp> convertDeviceInfoForUI(List<T> origi nDeviceInfoList, BiConsumer<T, DeviceListResp> biConsumer) {
        return CollectionUtils.isEmpty(originDeviceInfoList) ? new ArrayList<>() : originDeviceInfoList.stream().map(baseDevice -> {
            DeviceListResp device = new DeviceListResp();
            biConsumer.accept(baseDevice, device);
            return device;
        }).collect(Collectors.toList());
    }
}
```



1. 相同数据出现在不同模块的时候，更新不同步（用不用webSocket）；
2. 

