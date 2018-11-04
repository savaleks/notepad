import java.util.Scanner;

public class Words {
    public static void main(String args[]) {
        int x = 0;
        if(x > 0) x = 1;
        switch(x) {
            case 1: System.out.println(1);
            case 0: System.out.println(0);
            case 2: System.out.println(2);
                break;
            case 3: System.out.println(3);
            default: System.out.println(4);
                break;
        }
    }
}
