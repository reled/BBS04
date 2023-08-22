import it.unisa.dia.gas.jpbc.Element;

public class User_key {
    Element x;
    Element A;

    public User_key(){}

    public User_key(Element x, Element A) {
        this.x = x;
        this.A = A;
    }

    public Element getx() {
        return x;
    }

    public void setx(Element x) {
        this.x = x;
    }

    public Element getA() {
        return A;
    }

    public void setA(Element A) {
        A = A;
    }
}
