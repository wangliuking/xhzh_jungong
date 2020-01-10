package serialize;

import lombok.Data;

import java.io.*;

public class SerializeAndDeserialize {

    public static void main(String[] args) {
        deserializeObj();
    }

    public static void serializeObj() {
        Demo demo = new Demo();
        demo.setName("wlk");
        demo.setVal(999);
        demo.setSSN(1111111);
        try {
            FileOutputStream fs = new FileOutputStream("/home/wlk/jdk/Demo.ser");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(demo);
            os.close();
            fs.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deserializeObj(){
        Demo demo = null;
        try {
            FileInputStream fs = new FileInputStream("/home/wlk/jdk/Demo.ser");
            ObjectInputStream os = new ObjectInputStream(fs);
            demo = (Demo)os.readObject();
            os.close();
            fs.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        System.out.println(demo);
    }
}

@Data
class Demo implements Serializable {
    private String name;
    private int val;
    private transient int SSN;
}
