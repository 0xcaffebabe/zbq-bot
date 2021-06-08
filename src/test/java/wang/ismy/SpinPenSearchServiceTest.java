package wang.ismy;

import org.junit.jupiter.api.Test;
import wang.ismy.service.SpinPenSearchService;

class SpinPenSearchServiceTest {

    @Test
    public void download(){
        System.out.println(new SpinPenSearchService().searchSpinPen("ivan mod").size());
    }

    public static void main(String[] args) {
        new SpinPenSearchServiceTest().download();
    }
}