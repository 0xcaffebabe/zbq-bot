package wang.ismy.service;

import cn.hutool.core.map.MapUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessManagerTest {

    @Test
    public void test(){
        DataAccessManager manager = new DataAccessManager();
        manager.save("test", MapUtil.builder("name", "cxk").build());
    }

    public static void main(String[] args) {
        new DataAccessManagerTest().test();
    }
}