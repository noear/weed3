package org.noear.weed.cache;

/**
 * 缓存状态
 *
 * @author noear
 * @since 3.0
 */
public enum  CacheState {
    /// <summary>
    /// 不使用
    /// </summary>
    NonUsing,
    /// <summary>
    /// 使用
    /// </summary>
    Using,
    /// <summary>
    /// 刷新
    /// </summary>
    Refurbish,
    /// <summary>
    /// 移除
    /// </summary>
    Remove,
}
