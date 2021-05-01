/*
 * Copyright 2019 - 2021 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blazebit.expression.declarative.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines that the domain type can make use of entity literals.
 * A {@link EntityLiteralPersistenceRestrictionProvider} can optionally be provided,
 * which can restrict the elements that can be accessed.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface EntityLiteralPersistenceCapable {

    /**
     * The entity literal persistence restriction provider.
     *
     * @return the entity literal persistence restriction provider
     */
    Class<? extends EntityLiteralPersistenceRestrictionProvider> value() default EntityLiteralPersistenceRestrictionProvider.class;
}
