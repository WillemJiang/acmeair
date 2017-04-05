package com.acmeair.booking.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.acmeair.web.dto.BookingReceiptInfo;

import static com.seanyinx.github.unit.scaffolding.Randomness.nextId;
import static com.seanyinx.github.unit.scaffolding.Randomness.uniquify;

public class BookingDtoTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(BookingReceiptInfo.class).addTemplate("valid", new Rule() {{
            add("departBookingId", uniquify("departBookingId"));
            add("returnBookingId", uniquify("returnBookingId"));
            add("oneWay", false);
        }});
    }

    private String someId() {
        return String.valueOf(nextId());
    }
}
