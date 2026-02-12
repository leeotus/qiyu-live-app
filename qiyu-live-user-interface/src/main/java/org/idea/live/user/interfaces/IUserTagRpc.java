package org.idea.live.user.interfaces;

import org.idea.live.user.constants.UserTagsEnum;

public interface IUserTagRpc {
    
    /**
     * 设置用户标签
     *
     * @param userId [in] 用户id
     * @param userTagsEnum [in] 用户标签
     */
    boolean setTag(Long userId, UserTagsEnum userTagsEnum);
    
    /**
     * 取消用户的某个标签
     *
     * @param userId [in] 用户id
     * @param userTagsEnum [in] 要取消的用户标签
     */
    boolean cancelTag(Long userId, UserTagsEnum userTagsEnum);
    
    /**
     * 判断是否包含某个标签
     *
     * @param userId [in] 用户id
     * @param userTagsEnum [in] 是否包含该标签
     */
    boolean containTag(Long userId, UserTagsEnum userTagsEnum);
}
