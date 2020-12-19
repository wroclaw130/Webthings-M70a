public class SingleDataInput {

    SingleDataInput(String tK_temp, int tK_value){
        this.tK_temp = tK_temp;
        this.tK_value = tK_value;
    }

    String tK_temp;
    int tK_value;

    String gettK_temp() {
        return tK_temp;
    }

    int gettK_value() {
        return tK_value;
    }

    @Override
    public String toString() {
        return "SingleDataInput{" +
                "tK_temp='" + tK_temp + '\'' +
                ", tK_value=" + tK_value +
                '}';
    }
}
