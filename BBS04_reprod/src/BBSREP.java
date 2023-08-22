import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Element;

import java.util.HashMap;
import java.util.Map;

public class BBSREP {
    public static void main(String[] args) {
        //===KeyGen===
        Pairing bp = PairingFactory.getPairing("f.properties");
        Field G1 = bp.getG1();
        Field G2 = bp.getG2();
        Field Zr = bp.getZr();

        //群成员个数n
        String[] users = new String[3];
        users[0] = "张";
        users[1] = "刘";
        users[2] = "王";
        int n = users.length;

        Element g1 = G1.newRandomElement().getImmutable();
        Element g2 = G2.newRandomElement().getImmutable();
        Element h = G1.newRandomElement().getImmutable();
        Element eps1 = Zr.newRandomElement().getImmutable();
        Element eps2 = Zr.newRandomElement().getImmutable();
        //计算u,v
        Element u = h.powZn(eps1.invert()).getImmutable();
        Element v = h.powZn(eps2.invert()).getImmutable();

        Element gamma = Zr.newRandomElement().getImmutable();
        //计算omega
        Element omega = g2.powZn(gamma).getImmutable();

        //群公钥为(g1,g2,h,u,v,omega)，群私钥为(gamma,eps1,eps2)

        //生成群成员私钥
        Map<String, User_key> usr_key_map = new HashMap<>(n);
        for (int i = 0; i < n; i++) {
            Element x = Zr.newRandomElement().getImmutable();
            Element A = g1.powZn(gamma.add(x).invert()).getImmutable();
            User_key urs = new User_key(x, A);
            usr_key_map.put(users[i], urs);
        }
        //System.out.println(usr_key_map.get("刘").getx().toString());

        //===Sign===假如是王进行签名
        Element x = usr_key_map.get("王").getx();
        Element A = usr_key_map.get("王").getA();
        Element alpha = Zr.newRandomElement().getImmutable();
        Element beta = Zr.newRandomElement().getImmutable();
        //计算T1,T2,T3;
        Element T1 = u.powZn(alpha).getImmutable();
        Element T2 = v.powZn(beta).getImmutable();
        Element T3 = A.mul(h.powZn(alpha.add(beta))).getImmutable();
        //计算helper值
        Element delta1 = x.mul(alpha).getImmutable();
        Element delta2 = x.mul(beta).getImmutable();

        Element rAlpha = Zr.newRandomElement().getImmutable();
        Element rBeta = Zr.newRandomElement().getImmutable();
        Element rX = Zr.newRandomElement().getImmutable();
        Element rDelta1 = Zr.newRandomElement().getImmutable();
        Element rDelta2 = Zr.newRandomElement().getImmutable();

        //计算R1到R5
        Element R1 = u.powZn(rAlpha).getImmutable();
        Element R2 = v.powZn(rBeta).getImmutable();
        Element R3P1 = bp.pairing(T3, g2).powZn(rX).getImmutable();
        Element R3P2 = bp.pairing(h, omega).getImmutable().
                powZn(rAlpha.negate().add(rBeta.negate())).getImmutable();
        Element R3P3 = bp.pairing(h, g2).getImmutable().
                powZn(rDelta1.negate().add(rDelta2.negate())).getImmutable();
        Element R3 = ((R3P1.mul(R3P2)).mul(R3P3)).getImmutable();
        Element R4 = (T1.powZn(rX)).mul(u.powZn(rDelta1.negate())).getImmutable();
        Element R5 = (T2.powZn(rX)).mul(v.powZn(rDelta2.negate())).getImmutable();

        String M = "HELLO";
        String M_con = M + T1 + T2 + T3 + R1 + R2 + R3 + R4 + R5;
        int c_ = M_con.hashCode();
        byte[] c_sign_byte = Integer.toString(c_).getBytes();
        //哈希值c
        //从哈希映射回Zr群，转换为Element类型
        Element c = (Zr.newElementFromHash(c_sign_byte, 0, c_sign_byte.length)).getImmutable();
        //计算s
        Element sAlpha = rAlpha.add(c.mul(alpha)).getImmutable();
        Element sBeta = rBeta.add(c.mul(beta)).getImmutable();
        Element sX = rX.add(c.mul(x)).getImmutable();
        Element sDelta1 = rDelta1.add(c.mul(delta1)).getImmutable();
        Element sDelta2 = rDelta2.add(c.mul(delta2)).getImmutable();

        Signature sign = new Signature(T1, T2, T3, c, sAlpha, sBeta, sX, sDelta1, sDelta2);

        //===Verify===
        Element R1_ = (u.powZn(sAlpha)).mul(T1.powZn(c.negate())).getImmutable();
        Element R2_ = (v.powZn(sBeta)).mul(T2.powZn(c.negate())).getImmutable();
        Element R4_ = (T1.powZn(sX)).mul(u.powZn(sDelta1.negate())).getImmutable();
        Element R5_ = (T2.powZn(sX)).mul(v.powZn(sDelta2.negate())).getImmutable();
        Element R3_P1 = ((bp.pairing(T3, g2)).powZn(sX)).getImmutable();
        Element R3_P2 = (bp.pairing(h, omega)).powZn((sAlpha.negate()).add(sBeta.negate())).getImmutable();
        Element R3_P3 = (bp.pairing(h, g2)).powZn((sDelta1.negate()).add(sDelta2.negate())).getImmutable();
        Element R3_P4 = ((bp.pairing(T3, omega)).div(bp.pairing(g1, g2))).powZn(c).getImmutable();
        Element R3_ = R3_P1.mul(R3_P2.mul(R3_P3.mul(R3_P4))).getImmutable();

        String M_ver = M + T1 + T2 + T3 + R1_ + R2_ + R3_ + R4_ + R5_;
        int c_ver = M_ver.hashCode();
        byte[] c_ver_byte = Integer.toString(c_ver).getBytes();
        Element cV = (Zr.newElementFromHash(c_ver_byte, 0, c_ver_byte.length)).getImmutable();
        System.out.println("c :" + c);
        System.out.println("cV:" + cV);
        if (c.isEqual(cV)) {
            System.out.println("验证成功");
        } else {
            System.out.println("验证失败");
        }

        //===Open===
        Element Av = T3.div((T1.powZn(eps1)).mul(T2.powZn(eps2))).getImmutable();
        String open_person = "";
        for (Map.Entry<String, User_key> entry : usr_key_map.entrySet()) {
            Element tmp = entry.getValue().getA();
            if (tmp.isEqual(Av)) {
                System.out.println("A :" + tmp);
                System.out.println("Av:" + Av);
                open_person = entry.getKey();
                break;
            }
        }
        System.out.println("签名者为：" + open_person);
    }
}

