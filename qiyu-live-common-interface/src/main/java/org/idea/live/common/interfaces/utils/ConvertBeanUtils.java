package org.idea.live.common.interfaces.utils;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * ConvertBeanUtils是一个用于对象属性转换的工具类
 * 提供了将一个对象转换为另一个对象类型的方法
 */
public class ConvertBeanUtils {
    /**
     * 将源对象转换为目标类型的对象
     * @param source 源对象，包含需要转换的数据
     * @param target 目标对象的Class对象，用于确定转换后的类型
     * @param <T> 目标对象的泛型类型
     * @return 转换后的目标对象，如果源对象为null则返回null
     */
    public static <T> T convert(Object source, Class<T> target) {
        if(source == null) {  // 检查源对象是否为null，为null则直接返回null
            return null;
        }
        T t = newInstance(target);  // 创建目标类型的新实例
        BeanUtils.copyProperties(source, t);  // 将源对象的属性复制到目标对象中
        return t;
    }

/**
 * 通用列表转换方法，将一个类型的List转换为另一个类型的List
 * @param source 源List列表
 * @param target 目标类型Class对象
 * @param <K> 源列表中元素的类型
 * @param <T> 目标列表中元素的类型
 * @return 转换后的目标类型List，如果源列表为null则返回null
 */
    public static <K, T> List<T> convertList(List<K> source, Class<T> target) {
        // 检查源列表是否为null，如果是则直接返回null
        if(source == null) {
            return null;
        }
        // 初始化目标列表，初始容量设置为源列表大小的1.33倍（除以0.75）加1，以减少扩容操作
        List targetList = new ArrayList((int)(source.size() / 0.75)+1);
        // 遍历源列表中的每个元素
        for (K k : source) {
            // 将每个元素转换为目标类型并添加到目标列表中
            // 注意：这里调用的convert方法可能存在问题，参数应该是k而不是source
            targetList.add(convert(k, target));
        }
        return targetList;
    }

    /**
     * 创建目标类型的实例
     * @param target 目标对象的Class对象
     * @param <T> 目标对象的泛型类型
     * @return 新创建的目标对象实例
     * @throws BeanInstantiationException 如果实例创建失败则抛出此异常
     */
    private static <T> T newInstance(Class<T> target) {
        try {
//            Constructor<T> constructor = target.getDeclaredConstructor();
//            return constructor.newInstance();
            return target.newInstance();  // 使用反射创建目标类型的新实例
        } catch (Exception e) {
            throw new BeanInstantiationException(target, "instantiation error", e);  // 捕获异常并转换为BeanInstantiationException抛出
        }
    }
}
