package com.force.librarybase.base;

/**
 * @author Hugh
 * @version v1.0
 * @Package com.daydao.caterers.fastfood.base
 * @Description:
 * @date 17:28
 */
public interface BaseProcessorCallback {
    /**
     * 过程成功
     */
    public void successProcessor(BaseProcessorInfo processorInfo);

    /**
     * 过程失败
     */
    public void failProcessor(BaseProcessorInfo processorInfo);

    /**
     * 过程取消
     */
    public void cancelProcessor(BaseProcessorInfo processorInfo);
}
