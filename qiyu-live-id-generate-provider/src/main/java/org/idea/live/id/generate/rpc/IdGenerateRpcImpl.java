package org.idea.live.id.generate.rpc;

import org.apache.dubbo.config.annotation.DubboService;
import org.idea.live.id.generate.interfaces.IdGenerateRpc;
import org.idea.live.id.generate.service.IdGenerateService;

import jakarta.annotation.Resource;

@DubboService
public class IdGenerateRpcImpl implements IdGenerateRpc {

    @Resource
    private IdGenerateService idGenerateService;

    @Override
    public Long getSeqId(Integer id) {
        return idGenerateService.getSeqId(id);
    }

    @Override
    public Long getUnSeqId(Integer id) {
        return idGenerateService.getUnSeqId(id);
    }
}
