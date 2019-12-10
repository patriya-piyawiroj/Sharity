package itp341.piyawiroj.patriya.sharity.models;

import android.location.Address;

import java.util.ArrayList;
import java.util.Locale;

public class TestCenters {
    public ArrayList<DonationCenter> centers;

    public ArrayList<DonationCenter> getCenters() {
        return centers;
    }

    public TestCenters() {
        centers = new ArrayList<>();
        Address a = new Address(Locale.US);
        Address[] as = new Address[]{a};
        a.setAddressLine(0,"442 S. Pedro Street");
        a.setSubAdminArea("Los Angeles");
        a.setAdminArea("CA");
        a.setPostalCode("90013");
        BusinessHour b = new BusinessHour();
        String[] s = new String[2];
        DonationCenter c = new DonationCenter();
        c.setName("Downtown Women's Center");
        c.setAcceptedItems(s);
        c.setHours(b);
        c.setNotice("notice");
        c.setDescription("description");
        c.setAcceptedItems(s);
        c.setNotAcceptedItems(s);
        c.setPhoneNumber("2136800600");
        c.setEmail("Info@downtownwomencenter.org");
        c.setWebsite("downtownwomencenter.org");
        c.setInstructions("instructions");
        c.setOtherLocations(as);
        c.setAddress(a);
        c.setAcceptedItemsDetails("Clothing & Accessories:\n\n" +
                "*Socks - new only" +
                "\n*Adult Diapers and Pull-Ups - all sizes" +
                "\n*Underwear/bras - new only" +
                "\n*Women's cloting and shoes"+
                "\n*Plus-sized women's career clothing" +
                "\n*Sun hats and baseball caps" +
                "\n\nClothing must be new or gently used and freshly laundered. Shoes should be bagged or tied together");

        centers.add(c);
        centers.add(c);
        centers.add(c);
    }
}
