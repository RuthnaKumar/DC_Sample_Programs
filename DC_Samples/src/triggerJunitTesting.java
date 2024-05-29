import com.me.test.test_framework.api.v2.controller.JunitRunnerController;

import javax.ws.rs.core.Response;

public class triggerJunitTesting {
    public static void main(String args[]){
        JunitRunnerController junitRunnerController = new JunitRunnerController();
        Response response = junitRunnerController.triggerJunitTest("{\"include_tag_expression_list\":[\"SGS\"],\"class_name\":\"com.me.sgs.preliminary.test.configuration.SGSBuildConfigurationTest\"}");
        System.out.println("Response : "+response);
    }
}
