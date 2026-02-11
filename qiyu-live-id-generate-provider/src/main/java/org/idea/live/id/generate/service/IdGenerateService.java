package org.idea.live.id.generate.service;

public interface IdGenerateService {
    /**
     * 根据整数ID获取对应的序列ID
     * @param id 整数ID，作为输入参数
     * @return 返回对应的Long类型序列ID
     */
    Long getSeqId(Integer id);

    /**
     * 获取无序ID的方法
     * @param id 一个整型参数，用于获取对应的无序ID
     * @return 返回一个Long类型的无序ID
     */
    Long getUnSeqId(Integer id);
}
