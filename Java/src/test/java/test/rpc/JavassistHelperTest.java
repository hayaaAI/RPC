package test.rpc;

import hayaa.rpc.client.JavassistHelper;
import org.junit.Test;



public class JavassistHelperTest {
    @Test
    public void createTest(){

        MyRpc myRpc=(MyRpc)JavassistHelper.createClass("test.rpc.MyRpc");
    }
}
