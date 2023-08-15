package com.stockexchangeapi.base;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;

public abstract class TestBase {

    protected final EnhancedRandom dataGenerator = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .objectPoolSize(100)
            .stringLengthRange(4, 10)
            .collectionSizeRange(1, 10)
            .scanClasspathForConcreteTypes(true)
            .build();
}
