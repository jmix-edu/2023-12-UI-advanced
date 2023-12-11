package com.company.jmixpm.component;

import io.jmix.ui.xml.layout.loader.AbstractFieldLoader;

public class ColorFieldLoader extends AbstractFieldLoader<ColorField> {
    @Override
    public void createComponent() {
        resultComponent = factory.create(ColorField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();
    }
}
