package com.acmeair.customer.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.acmeair.entities.Customer;
import com.acmeair.morphia.entities.CustomerAddressImpl;
import com.acmeair.morphia.entities.CustomerImpl;

import static com.seanyinx.github.unit.scaffolding.Randomness.nextId;
import static com.seanyinx.github.unit.scaffolding.Randomness.uniquify;

public class CustomerTemplateLoader implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(CustomerAddressImpl.class).addTemplate("valid", new Rule() {{
            add("streetAddress1", uniquify("broad way ave "));
            add("streetAddress2", uniquify("white plain street "));
            add("city", random(String.class, "Beijing", "NYC", "Chicago", "White Plains", "Boston"));
            add("stateProvince", random(String.class, "NY", "TX", "TK"));
            add("country", random(String.class, "USA", "China", "Canada"));
            add("postalCode", uniquify("60"));
        }});

        Fixture.of(CustomerImpl.class).addTemplate("valid", new Rule() {{
            add("_id", String.valueOf(nextId()));
            add("password", uniquify("password"));
            add("status", random(Customer.MemberShipStatus.class));
            add("total_miles", random(Integer.class, range(1, 100000)));
            add("miles_ytd", random(Integer.class, range(1, 100000)));
            add("address", one(CustomerAddressImpl.class, "valid"));
            add("phoneNumber", random(String.class, uniquify("086"), uniquify("065")));
            add("phoneNumberType", random(Customer.PhoneType.class));
        }});
    }
}
