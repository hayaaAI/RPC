package hayaa.rpc.common.protocol;

import hayaa.common.JsonHelper;
import hayaa.rpc.common.RpcDataHelper;

import java.io.Serializable;

/**
 * @author hsieh
 */
public class RpcDataValue implements Serializable {
    /**
     * 1-class
     * 2-string
     * 3-int
     * 4-float
     * 5-double
     * 传输的数据类型
     */
    private Integer dataType;
    /**
     * 参数json序列化后的存储字段
     */
    private String valContainer;
    /**
     * 参数的类型
     * 跨平台传输时只有类型1才有使用意义
     */
    private String className;

    /**
     *
     * @param arg 函数中的参数变量
     */
    public RpcDataValue(Object arg) {
        this.dataType = RpcDataHelper.parseDataType(arg);
        this.className = arg.getClass().getName();
        this.valContainer = JsonHelper.SerializeObject(arg);
    }

    public String getValContainer() {
        return valContainer;
    }

    public void setValContainer(String valContainer) {
        this.valContainer = valContainer;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }


}
