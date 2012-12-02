package cz.fi.muni.eshop.testpackage;

import javax.ejb.Stateless;

@Stateless
public class Dummy {

    private String dummyText = "this is dummy text";

    public Dummy() {
        super();
    }

    public String getDummyText() {
        return dummyText;
    }
}
