package com.company.jmixpm.screen.city;

import io.jmix.ui.screen.*;
import com.company.jmixpm.entity.City;

@UiController("CitySimple.browse")
@UiDescriptor("city-browse-simple.xml")
@LookupComponent("citiesTable")
public class CityBrowseSimple extends StandardLookup<City> {
}