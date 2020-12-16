package com.ruoyi.framework.web.service;

import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.domain.TInterfaceInfo;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysDictTypeService;
import com.ruoyi.system.service.ITInterfaceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RuoYi首创 html调用 thymeleaf 实现字典读取
 *
 * @author ruoyi
 */
@Service("interfaceInfo")
public class InterfaceInfoService {
    @Autowired
    private ITInterfaceInfoService interfaceInfoService;


    /**
     * 查询接口信息
     *
     * @return 接口列表信息
     */
    public List<TInterfaceInfo> getInterfaceInfo() {
        List<TInterfaceInfo> tInterfaceInfos = interfaceInfoService.selectTInterfaceInfoList(new TInterfaceInfo());
        for (TInterfaceInfo interfaceInfo : tInterfaceInfos) {
            interfaceInfo.setServerView(interfaceInfo.getServerCode() + "-" + interfaceInfo.getServerView());
        }
        return tInterfaceInfos;
    }

    public static void main(String[] args) {


    }

}
