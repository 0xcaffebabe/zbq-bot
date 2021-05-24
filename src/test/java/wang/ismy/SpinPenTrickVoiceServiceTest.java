package wang.ismy;

import static org.junit.jupiter.api.Assertions.*;

class SpinPenTrickVoiceServiceTest {

    public  static void test(){
        byte[] bytes = new SpinPenTrickVoiceService().speakTrick("peem's trick");
        System.out.println(bytes.length);
    }

    public static void main(String[] args) {
        test();
    }
}