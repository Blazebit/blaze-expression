/*
 * Copyright 2019 - 2024 Blazebit.
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

package com.blazebit.expression.impl;

import org.junit.Test;

import com.blazebit.expression.Query;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class QueryCompilerTest extends AbstractExpressionCompilerTest {

    @Test
    public void testSimple() {
        Query query = parseQuery( "FROM user u");
        assertEquals(
                asList( path( "user", "u" )),
                query.getSelectItems()
        );
        assertEquals(
                asList(fromItem("user", "u")),
                query.getFromItems()
        );
        assertNull( query.getWherePredicate() );
    }

    @Test
    public void testPredicate() {
        Query query = parseQuery( "FROM user u WHERE 1 > 2");
        assertEquals(
                asList( path( "user", "u" )),
                query.getSelectItems()
        );
        assertEquals(
                asList(fromItem("user", "u")),
                query.getFromItems()
        );
        assertEquals(
                gt(pos(number(1L)), pos(number(2L))),
                query.getWherePredicate()
        );
    }

    @Test
    public void testSelectAttribute() {
        Query query = parseQuery( "SELECT u.id FROM user u");
        assertEquals(
                asList(path( "user", "u", "id" )),
                query.getSelectItems()
        );
        assertEquals(
                asList(fromItem("user", "u")),
                query.getFromItems()
        );
        assertNull( query.getWherePredicate() );
    }

    @Test
    public void testSelectDistinct() {
        Query query = parseQuery( "SELECT DISTINCT u FROM user u");
        assertEquals(
                asList(path( "user", "u" )),
                query.getSelectItems()
        );
        assertEquals(
                asList(fromItem("user", "u")),
                query.getFromItems()
        );
        assertNull( query.getWherePredicate() );
    }

    @Test
    public void testJoin() {
        Query query = parseQuery( "FROM user u JOIN user u2 ON u.id = u2.id");
        assertEquals(
                asList( path( "user", "u" )),
                query.getSelectItems()
        );
        assertEquals(
                asList(
                        fromItem(
                                "user",
                                "u",
                                join( "user", "u2", eq( path( "user", "u", "id" ), path( "user", "u2", "id" ) ) )
                        )
                ),
                query.getFromItems()
        );
        assertNull( query.getWherePredicate() );
    }

    @Test
    public void testLeftJoin() {
        Query query = parseQuery( "FROM user u LEFT JOIN user u2 ON u.id = u2.id");
        assertEquals(
                asList( path( "user", "u" )),
                query.getSelectItems()
        );
        assertEquals(
                asList(
                        fromItem(
                                "user",
                                "u",
                                leftJoin( "user", "u2", eq( path( "user", "u", "id" ), path( "user", "u2", "id" ) ) )
                        )
                ),
                query.getFromItems()
        );
        assertNull( query.getWherePredicate() );
    }

    @Test
    public void testRightJoin() {
        Query query = parseQuery( "FROM user u RIGHT JOIN user u2 ON u.id = u2.id");
        assertEquals(
                asList( path( "user", "u" )),
                query.getSelectItems()
        );
        assertEquals(
                asList(
                        fromItem(
                                "user",
                                "u",
                                rightJoin( "user", "u2", eq( path( "user", "u", "id" ), path( "user", "u2", "id" ) ) )
                        )
                ),
                query.getFromItems()
        );
        assertNull( query.getWherePredicate() );
    }

    @Test
    public void testFullJoin() {
        Query query = parseQuery( "FROM user u FULL JOIN user u2 ON u.id = u2.id");
        assertEquals(
                asList( path( "user", "u" )),
                query.getSelectItems()
        );
        assertEquals(
                asList(
                        fromItem(
                                "user",
                                "u",
                                fullJoin( "user", "u2", eq( path( "user", "u", "id" ), path( "user", "u2", "id" ) ) )
                        )
                ),
                query.getFromItems()
        );
        assertNull( query.getWherePredicate() );
    }
}
