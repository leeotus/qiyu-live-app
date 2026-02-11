package org.idea.live.id.generate.interfaces;

public interface IdGenerateRpc {

    /**
     * 获取自增ID
     * 该方法用于根据传入的整数参数生成一个自增的长整型ID
     *
     * @param id 整数类型的ID参数，作为生成自增ID的基础
     * @return 返回一个长整型(Long)的自增ID
     */
    Long getSeqId(Integer id);


    /**
     * 获取一个无序ID
     * @param id 一个整数类型的ID参数
     * @return 返回一个Long类型的ID
     */
    Long getUnSeqId(Integer id);
}
