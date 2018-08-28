package hayaa.rpc.common.protocol;

import hayaa.common.JsonHelper;
import hayaa.rpc.common.RpcDataHelper;

import java.io.Serializable;

/**
 * 由于java的Gson序列化对于大小写敏感，所以字段命名和其他平台一致
 * @author hsieh
 */
public class RpcDataValue implements Serializable {
    /**
     * 1-class 可定义类
     * 2-string
     * 3-bool
     * 4-byte
     * 5-short
     * 6-int
     * 7-long
     * 8-float
     * 9-double
     * 10-decimal
     * 传输的数据类型
     */
    private Integer DataType;
    /**
     * 参数json序列化后的存储字段
     */
    private String ValContainer;
    /**
     * 参数的类型
     * 跨平台传输时只有类型1才有使用意义
     */
    private String ClassName;
    /**
     * 参数索引号
     */
    private Integer ArgIndex;

    /**
     *Json序列化无法使用此构造函数
     * @param arg 函数中的参数变量
     */
    public RpcDataValue(Object arg,int argIndex) {
        this.DataType = RpcDataHelper.parseDataType(arg);
        this.ClassName = arg.getClass().getName();
        this.ValContainer = JsonHelper.SerializeObject(arg);
        this.ArgIndex=argIndex;
    }

    /**
     * 提供给Json序列化使用
     */
    public RpcDataValue() {

    }


    public String getValContainer() {
        return ValContainer;
    }

    public void setValContainer(String valContainer) {
        this.ValContainer = valContainer;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        this.ClassName = className;
    }

    public Integer getDataType() {
        return DataType;
    }

    public void setDataType(Integer dataType) {
        this.DataType = dataType;
    }

    public Integer getArgIndex() {
        return ArgIndex;
    }

    public void setArgIndex(Integer argIndex) {
        this.ArgIndex = argIndex;
    }
}
