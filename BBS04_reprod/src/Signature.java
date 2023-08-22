import it.unisa.dia.gas.jpbc.Element;

import java.util.Objects;

public class Signature {
    Element T1;
    Element T2;
    Element T3;
    Element c;
    Element sAlpha;
    Element sBeta;
    Element sX;
    Element sDelta1;
    Element sDelta2;

    public Signature(Element T1, Element T2, Element T3, Element c, Element sAlpha,
                     Element sBeta, Element sX, Element sDelta1, Element sDelta2) {
        this.T1 = T1;
        this.T2 = T2;
        this.T3 = T3;
        this.c = c;
        this.sAlpha = sAlpha;
        this.sBeta = sBeta;
        this.sX = sX;
        this.sDelta1 = sDelta1;
        this.sDelta2 = sDelta2;
    }

    public  Signature(){}

    public Element getT1() {
        return T1;
    }

    public void setT1(Element T1) {
        T1 = T1;
    }

    public Element getT2() {
        return T2;
    }

    public void setT2(Element T2) {
        T2 = T2;
    }

    public Element getT3() {
        return T3;
    }

    public void setT3(Element T3) {
        T3 = T3;
    }

    public Element getc() {
        return c;
    }

    public void setc(Element c) {
        this.c = c;
    }

    public Element getsAlpha() {
        return sAlpha;
    }

    public void setsAlpha(Element sAlpha) {
        this.sAlpha = sAlpha;
    }

    public Element getsBeta() {
        return sBeta;
    }

    public void setsBeta(Element sBeta) {
        this.sBeta = sBeta;
    }

    public Element getsX() {
        return sX;
    }

    public void setsX(Element sX) {
        this.sX = sX;
    }

    public Element getsDelta1() {
        return sDelta1;
    }

    public void setsDelta1(Element sDelta1) {
        this.sDelta1 = sDelta1;
    }

    public Element getsDelta2() {
        return sDelta2;
    }

    public void setsDelta2(Element sDelta2) {
        this.sDelta2 = sDelta2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signature signature = (Signature) o;
        return Objects.equals(T1, signature.T1) && Objects.equals(T2, signature.T2) && Objects.equals(T3, signature.T3) && Objects.equals(c, signature.c) && Objects.equals(sAlpha, signature.sAlpha) && Objects.equals(sBeta, signature.sBeta) && Objects.equals(sX, signature.sX) && Objects.equals(sDelta1, signature.sDelta1) && Objects.equals(sDelta2, signature.sDelta2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(T1, T2, T3, c, sAlpha, sBeta, sX, sDelta1, sDelta2);
    }
}
