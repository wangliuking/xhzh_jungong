package T;

import lombok.Data;

@Data
public class MutiModel<T> {
    private boolean status;
    private Exception e;
    private T data;

    public MutiModel() {
    }

    public MutiModel(boolean status) {
        this.status = status;
    }

    public MutiModel(Exception e) {
        this.status = false;
        this.e = e;
    }

    public MutiModel(T t) {
        this.status = true;
        this.data = t;
    }

    public static void main(String[] args) throws Exception {
        boolean b = false;
        Exception exception = new Exception("有异常");
        MutiModel mutiModel = new MutiModel(b);
        System.out.println(mutiModel);
        throw exception;
    }
}
