/*
 * Copyright 2019 - 2022 Blazebit.
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

/**
 * The literal kind.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export enum LiteralKind {
    /**
     * Boolean literal.
     */
    BOOLEAN,
    /**
     * Integer literal.
     */
    INTEGER,
    /**
     * Numeric literal.
     */
    NUMERIC,
    /**
     * String literal.
     */
    STRING,
    /**
     * Timestamp literal.
     */
    TIMESTAMP,
    /**
     * Interval literal.
     */
    INTERVAL,
    /**
     * Entity literal.
     */
    ENTITY,
    /**
     * Enum literal.
     */
    ENUM,
    /**
     * Collection literal.
     */
    COLLECTION
}