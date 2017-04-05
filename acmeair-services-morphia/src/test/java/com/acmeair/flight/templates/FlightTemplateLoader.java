package com.acmeair.flight.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.acmeair.morphia.entities.FlightImpl;
import com.acmeair.morphia.entities.FlightSegmentImpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import static com.seanyinx.github.unit.scaffolding.Randomness.nextId;
import static com.seanyinx.github.unit.scaffolding.Randomness.uniquify;

public class FlightTemplateLoader implements TemplateLoader {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void load() {
        Fixture.of(FlightSegmentImpl.class).addTemplate("valid", new Rule() {{
            add("_id", someId());
            add("originPort", uniquify("originPort"));
            add("destPort", uniquify("destPort"));
            add("miles", random(Integer.class));
        }});

        Fixture.of(FlightImpl.class).addTemplate("valid", new Rule() {{
            add("_id", someId());
            add("flightSegmentId", someId());
            add("scheduledDepartureTime", beforeDate("2017-04-01", dateFormat));
            add("scheduledArrivalTime", afterDate("2017-04-01", dateFormat));
            add("firstClassBaseCost", random(BigDecimal.class));
            add("economyClassBaseCost", random(BigDecimal.class));
            add("numFirstClassSeats", random(Integer.class));
            add("numEconomyClassSeats", random(Integer.class));
            add("airplaneTypeId", someId());
            add("flightSegment", one(FlightSegmentImpl.class, "valid"));
        }});
    }

    private String someId() {
        return String.valueOf(nextId());
    }
}
