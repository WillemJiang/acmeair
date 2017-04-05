package com.acmeair.booking.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.acmeair.morphia.entities.BookingImpl;

import static com.seanyinx.github.unit.scaffolding.Randomness.nextId;

public class BookingTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(BookingImpl.class).addTemplate("valid", new Rule() {{
            add("_id", someId());
            add("flightId", someId());
            add("customerId", someId());
            add("dateOfBooking", instant("now"));
        }});
    }

    private String someId() {
        return String.valueOf(nextId());
    }
}
