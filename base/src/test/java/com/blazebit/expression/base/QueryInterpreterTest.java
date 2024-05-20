package com.blazebit.expression.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import com.blazebit.domain.Domain;
import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.domain.runtime.model.EnumDomainType;
import com.blazebit.domain.runtime.model.EnumDomainTypeValue;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionInterpreterContext;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.Expressions;
import com.blazebit.expression.base.function.FunctionInvokerMetadataDefinition;
import com.blazebit.expression.spi.AttributeAccessor;
import com.blazebit.expression.spi.DataFetcher;
import com.blazebit.expression.spi.TypeAdapter;

import static org.junit.Assert.assertEquals;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class QueryInterpreterTest {

    private final DomainModel domainModel;
    private final ExpressionService expressionService;
    private final ExpressionCompiler compiler;
    private final ExpressionInterpreter interpreter;
    private final List<User> users = new ArrayList<>();
    private final List<Person> persons = new ArrayList<>();

    public class User extends Person {
        String status;
        Locale language;
        Currency currency;
        public User(Long id, String name, Boolean status, Locale language, Currency currency) {
            super(id, name);
            this.status = status.toString();
            this.language = language;
            this.currency = currency;
        }
        public String getStatus() {
            return status;
        }
        public Locale getLanguage() {
            return language;
        }
        public Currency getCurrency() {
            return currency;
        }
    }
    public class Person {
        Long id;
        String name;
        public Person(Long id, String name) {
            this.id = id;
            this.name = name;
        }
        public Long getId() {
            return id;
        }
        public String getName() {
            return name;
        }
    }
    public static class PersonIdAttributeAccessor implements MetadataDefinition<AttributeAccessor>, AttributeAccessor {
        public PersonIdAttributeAccessor() {
        }
        @Override
        public Object getAttribute(ExpressionInterpreter.Context context, Object value, EntityDomainTypeAttribute attribute) {
            return ((Person) value).getId();
        }
        @Override
        public Class<AttributeAccessor> getJavaType() {
            return AttributeAccessor.class;
        }
        @Override
        public AttributeAccessor build(MetadataDefinitionHolder definitionHolder) {
            return this;
        }
    }
    public static class PersonNameAttributeAccessor implements MetadataDefinition<AttributeAccessor>, AttributeAccessor {
        public PersonNameAttributeAccessor() {
        }
        @Override
        public Object getAttribute(ExpressionInterpreter.Context context, Object value, EntityDomainTypeAttribute attribute) {
            return ((Person) value).getName();
        }
        @Override
        public Class<AttributeAccessor> getJavaType() {
            return AttributeAccessor.class;
        }
        @Override
        public AttributeAccessor build(MetadataDefinitionHolder definitionHolder) {
            return this;
        }
    }
    public static class UserStatusAttributeAccessor implements MetadataDefinition<AttributeAccessor>, AttributeAccessor {
        public UserStatusAttributeAccessor() {
        }
        @Override
        public Object getAttribute(ExpressionInterpreter.Context context, Object value, EntityDomainTypeAttribute attribute) {
            return ((User) value).getStatus();
        }
        @Override
        public Class<AttributeAccessor> getJavaType() {
            return AttributeAccessor.class;
        }
        @Override
        public AttributeAccessor build(MetadataDefinitionHolder definitionHolder) {
            return this;
        }
    }
    public static class UserLanguageAttributeAccessor implements MetadataDefinition<AttributeAccessor>, AttributeAccessor {
        public UserLanguageAttributeAccessor() {
        }
        @Override
        public Object getAttribute(ExpressionInterpreter.Context context, Object value, EntityDomainTypeAttribute attribute) {
            return ((User) value).getLanguage();
        }
        @Override
        public Class<AttributeAccessor> getJavaType() {
            return AttributeAccessor.class;
        }
        @Override
        public AttributeAccessor build(MetadataDefinitionHolder definitionHolder) {
            return this;
        }
    }
    public static class UserCurrencyAttributeAccessor implements MetadataDefinition<AttributeAccessor>, AttributeAccessor {
        public UserCurrencyAttributeAccessor() {
        }
        @Override
        public Object getAttribute(ExpressionInterpreter.Context context, Object value, EntityDomainTypeAttribute attribute) {
            return ((User) value).getCurrency();
        }
        @Override
        public Class<AttributeAccessor> getJavaType() {
            return AttributeAccessor.class;
        }
        @Override
        public AttributeAccessor build(MetadataDefinitionHolder definitionHolder) {
            return this;
        }
    }
    private static class BooleanTypeAdapter implements TypeAdapter<String, Boolean> {
        public static final BooleanTypeAdapter INSTANCE = new BooleanTypeAdapter();

        @Override
        public Boolean toInternalType(ExpressionInterpreter.Context context, String value, DomainType domainType) {
            return value == null || value.isEmpty() ? null : Boolean.valueOf(value);
        }

        @Override
        public String toModelType(ExpressionInterpreter.Context context, Boolean value, DomainType domainType) {
            return value == null ? null : value.toString();
        }
    }
    private static class TypeAdapterMetadataDefinition implements MetadataDefinition<TypeAdapter<?, ?>> {
        private final TypeAdapter<?, ?> typeAdapter;

        public TypeAdapterMetadataDefinition(TypeAdapter<?, ?> typeAdapter) {
            this.typeAdapter = typeAdapter;
        }

        @Override
        public Class<TypeAdapter<?, ?>> getJavaType() {
            return (Class<TypeAdapter<?, ?>>) (Class<?>) TypeAdapter.class;
        }

        @Override
        public TypeAdapter<?, ?> build(MetadataDefinitionHolder definitionHolder) {
            return typeAdapter;
        }
    }

    public QueryInterpreterTest() {
        MetadataDefinition[] idAttributeMetadata = new MetadataDefinition[1];
        idAttributeMetadata[0] = new PersonIdAttributeAccessor();
        MetadataDefinition[] nameAttributeMetadata = new MetadataDefinition[1];
        nameAttributeMetadata[0] = new PersonNameAttributeAccessor();
        MetadataDefinition[] languageAttributeMetadata = new MetadataDefinition[1];
        languageAttributeMetadata[0] = new UserLanguageAttributeAccessor();
        MetadataDefinition[] currencyAttributeMetadata = new MetadataDefinition[2];
        currencyAttributeMetadata[0] = new UserCurrencyAttributeAccessor();
        currencyAttributeMetadata[1] = new TypeAdapterMetadataDefinition(new TypeAdapter<Currency, EnumDomainTypeValue>() {
            @Override
            public EnumDomainTypeValue toInternalType(ExpressionInterpreter.Context context, Currency value, DomainType domainType) {
                return ((EnumDomainType) domainType).getEnumValues().get(value.getCurrencyCode());
            }

            @Override
            public Currency toModelType(ExpressionInterpreter.Context context, EnumDomainTypeValue value, DomainType domainType) {
                return Currency.getInstance(value.getValue());
            }
        });

        MetadataDefinition[] statusAttributeMetadata = new MetadataDefinition[2];
        statusAttributeMetadata[0] = new UserStatusAttributeAccessor();
        statusAttributeMetadata[1] = new TypeAdapterMetadataDefinition( BooleanTypeAdapter.INSTANCE);

        DomainBuilder domainBuilder = Domain.getDefaultProvider().createEmptyBuilder();
        domainBuilder.withDefaults();
        domainBuilder.createBasicType("Language");
        domainBuilder.createEnumType("Currency")
            .withValue("EUR")
            .withValue("USD")
            .build();
        StringlyTypeUtils.registerStringlyType(domainBuilder, "Language", Locale::new);
        StringlyTypeUtils.registerStringlyType(domainBuilder, "Currency", Currency::getInstance);
        domainBuilder.createFunction("is_true")
            .withMetadata(new FunctionInvokerMetadataDefinition((context, function, arguments) -> arguments.getValue(0)))
            .withArgument("value", BaseContributor.BOOLEAN_TYPE_NAME)
            .withResultType(BaseContributor.BOOLEAN_TYPE_NAME)
            .build();
        domainBuilder.createEntityType("person")
                .withMetadata( new DataFetcherMetadataDefinition( new DataFetcher() {
                    @Override
                    public List<?> fetch(ExpressionInterpreter.Context context) {
                        return persons;
                    }
                } ) )
                .addAttribute( "id", BaseContributor.NUMERIC_TYPE_NAME, idAttributeMetadata )
                .addAttribute( "name", BaseContributor.STRING_TYPE_NAME, nameAttributeMetadata )
                .build();
        domainBuilder.createEntityType("user")
                .withMetadata( new DataFetcherMetadataDefinition( new DataFetcher() {
                    @Override
                    public List<?> fetch(ExpressionInterpreter.Context context) {
                        return users;
                    }
                } ) )
                .addAttribute( "id", BaseContributor.NUMERIC_TYPE_NAME, idAttributeMetadata )
                .addAttribute( "name", BaseContributor.STRING_TYPE_NAME, nameAttributeMetadata )
                .addAttribute("status", BaseContributor.BOOLEAN_TYPE_NAME, statusAttributeMetadata)
                .addAttribute("language", "Language", languageAttributeMetadata)
                .addAttribute("currency", "Currency", currencyAttributeMetadata)
                .build();
        this.domainModel = domainBuilder.build();
        this.expressionService = Expressions.forModel(domainModel);
        this.compiler = expressionService.createCompiler();
        this.interpreter = expressionService.createInterpreter();
        this.users.add(new User(1L, "Max", true, new Locale("de"), Currency.getInstance("EUR")));
        this.users.add(new User(2L, "John", false, new Locale("en"), Currency.getInstance("USD")));
        this.users.add(new User(3L, "Pierre", false, new Locale("fr"), Currency.getInstance("EUR")));
        this.persons.add( new Person( 1L, "Max" ) );
        this.persons.add( new Person( 2L, "Tony" ) );
    }

    private ExpressionInterpreter.Context createInterpreterContext() {
        ExpressionInterpreterContext<ExpressionInterpreter.Context> context = ExpressionInterpreterContext.create(expressionService);
        return context;
    }
    private <T> List<T> testQuery(String query) {
        return interpreter.evaluate(
                compiler.createQuery(query, compiler.createContext( Collections.emptyMap() )),
                createInterpreterContext());
    }

    @Test
    public void testFilter() {
        List<User> userList = testQuery("from user u where u.language = 'de'");
        assertEquals(1, userList.size());
    }

    @Test
    public void testFilterEmpty() {
        List<User> userList = testQuery("from user u where u.language is null");
        assertEquals(0, userList.size());
    }

    @Test
    public void testProjection() {
        List<Currency> userList = testQuery("select u.currency from user u");
        assertEquals(3, userList.size());
    }

    @Test
    public void testDistinct() {
        List<Currency> userList = testQuery("select distinct u.currency from user u");
        assertEquals(2, userList.size());
    }

    @Test
    public void testJoin() {
        List<Object[]> userList = testQuery("select u.language, u2.language from user u join user u2 on u.currency = u2.currency and u.id != u2.id");
        assertEquals(2, userList.size());
    }

    @Test
    public void testLeftJoin() {
        List<Object[]> userList = testQuery("select u, p from user u left join person p on u.name = p.name");
        assertEquals(3, userList.size());
    }

    @Test
    public void testRightJoin() {
        List<Object[]> userList = testQuery("select u, p from user u right join person p on u.name = p.name");
        assertEquals(2, userList.size());
    }

    @Test
    public void testFullJoin() {
        List<Object[]> userList = testQuery("select u, p from user u full join person p on u.name = p.name");
        assertEquals(4, userList.size());
    }

    @Test
    public void testJoinReverse() {
        List<Object[]> userList = testQuery("select u, p from person p join user u on p.name = u.name");
        assertEquals(1, userList.size());
    }

    @Test
    public void testLeftJoinReverse() {
        List<Object[]> userList = testQuery("select u, p from person p left join user u on p.name = u.name");
        assertEquals(2, userList.size());
    }

    @Test
    public void testRightJoinReverse() {
        List<Object[]> userList = testQuery("select u, p from person p right join user u on p.name = u.name");
        assertEquals(3, userList.size());
    }

    @Test
    public void testFullJoinReverse() {
        List<Object[]> userList = testQuery("select u, p from person p full join user u on p.name = u.name");
        assertEquals(4, userList.size());
    }

}
