package org.idea.live.id.generate.rpc;

import org.apache.dubbo.config.annotation.DubboService;
import org.idea.live.id.generate.interfaces.IdGenerateRpc;

@DubboService
public class IdGenerateRpcImpl implements IdGenerateRpc {


    @Override
    public Long getSeqId(Integer id) {
        // TODO
        return 0L;
    }

    @Override
    public Long getUnSeqId(Integer id) {
        // TODO
        return 0L;
    }
}
