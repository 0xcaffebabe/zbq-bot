package wang.ismy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpinPenSearchServiceTest {

    @Test
    public void download(){
        System.out.println(new SpinPenSearchService().searchSpinPen("ivan mod").size());
    }

    public static void main(String[] args) {
        new SpinPenSearchServiceTest().download();
    }
}