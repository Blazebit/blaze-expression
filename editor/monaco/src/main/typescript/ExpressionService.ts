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

import {DomainModel, DomainType} from "blaze-domain";
import {LiteralResolver} from "./LiteralResolver";
import {LiteralKind} from "./LiteralKind";
import {EntityLiteral} from "./EntityLiteral";
import {EnumLiteral} from "./EnumLiteral";
import {CollectionLiteral} from "./CollectionLiteral";
import {Lexer} from "antlr4ts/Lexer";
import {Parser} from "antlr4ts/Parser";
import {ParserRuleContext} from "antlr4ts/ParserRuleContext";
import {BlazeExpressionLexer} from "./blaze-expression-predicate/BlazeExpressionLexer";
import {CharStreams, CommonTokenStream, ConsoleErrorListener} from "antlr4ts";
import {BlazeExpressionParser} from "./blaze-expression-predicate/BlazeExpressionParser";
import {SymbolTable} from "./SymbolTable";
import {BlazeExpressionErrorStrategy} from "./BlazeExpressionErrorStrategy";
import {MyBlazeExpressionParserVisitor} from "./MyBlazeExpressionParserVisitor";

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class ExpressionService {
    /**
     * The domain model.
     */
    domainModel: DomainModel;
    /**
     * Returns the resolver for boolean literals.
     */
    booleanLiteralResolver: LiteralResolver;
    /**
     * Returns the resolver for numeric literals.
     */
    numericLiteralResolver: LiteralResolver;
    /**
     * Returns the resolver for string literals.
     */
    stringLiteralResolver: LiteralResolver;
    /**
     * Returns the resolver for temporal literals.
     */
    temporalLiteralResolver: LiteralResolver;
    /**
     * Returns the resolver for entity literals.
     */
    entityLiteralResolver: LiteralResolver;
    /**
     * Returns the resolver for enum literals.
     */
    enumLiteralResolver: LiteralResolver;
    /**
     * Returns the resolver for collection literals.
     */
    collectionLiteralResolver: LiteralResolver;

    lexerFactory: (string) => Lexer;

    parserFactory: (Lexer) => Parser;

    parseRuleInvoker: (Parser) => ParserRuleContext;

    typeResolver: (tree: ParserRuleContext, symbolTable: SymbolTable) => DomainType;

    constructor(domainModel: DomainModel, booleanLiteralResolver: LiteralResolver, numericLiteralResolver: LiteralResolver, stringLiteralResolver: LiteralResolver, temporalLiteralResolver: LiteralResolver, entityLiteralResolver: LiteralResolver, enumLiteralResolver: LiteralResolver, collectionLiteralResolver: LiteralResolver, lexerFactory: (string) => Lexer, parserFactory: (Lexer) => Parser, parseRuleInvoker: (Parser) => ParserRuleContext, typeResolver: (tree: ParserRuleContext, symbolTable: SymbolTable) => DomainType) {
        this.domainModel = domainModel;
        this.booleanLiteralResolver = booleanLiteralResolver;
        this.numericLiteralResolver = numericLiteralResolver;
        this.stringLiteralResolver = stringLiteralResolver;
        this.temporalLiteralResolver = temporalLiteralResolver;
        this.entityLiteralResolver = entityLiteralResolver;
        this.enumLiteralResolver = enumLiteralResolver;
        this.collectionLiteralResolver = collectionLiteralResolver;
        this.lexerFactory = lexerFactory;
        this.parserFactory = parserFactory;
        this.parseRuleInvoker = parseRuleInvoker;
        this.typeResolver = typeResolver;
    }

    createLexer(input: string): Lexer {
        return this.lexerFactory(input);
    }

    parseTree(input: string): ParserRuleContext {
        const lexer = this.lexerFactory(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ConsoleErrorListener());

        const parser = this.parserFactory(lexer);
        parser.removeErrorListeners();
        parser.errorHandler = new BlazeExpressionErrorStrategy();
        return this.parseRuleInvoker(parser);
    }

    resolveType(input: string, symbolTable: SymbolTable) : DomainType {
        try {
            const tree = this.parseTree(input);
            return this.typeResolver(tree, symbolTable);
        } catch (e) {
            return null;
        }
    }

    static parse(input: string | object, baseModel?: ExpressionService | DomainModel, extension?: StringMap<Function>): ExpressionService {
        let json;
        if (typeof input === "string") {
            json = JSON.parse(input);
        } else {
            json = input;
        }
        let baseDomainModel: DomainModel;
        if (baseModel instanceof DomainModel) {
            baseDomainModel = baseModel;
        } else if (baseModel instanceof ExpressionService) {
            baseDomainModel = baseModel.domainModel;
            let setIfAbsent = function(o, k, v) {
                if (typeof o[k] === 'undefined') {
                    o[k] = v;
                }
            }
            setIfAbsent(json, 'booleanLiteralResolver', baseModel.booleanLiteralResolver);
            setIfAbsent(json, 'numericLiteralResolver', baseModel.numericLiteralResolver);
            setIfAbsent(json, 'stringLiteralResolver', baseModel.stringLiteralResolver);
            setIfAbsent(json, 'temporalLiteralResolver', baseModel.temporalLiteralResolver);
            setIfAbsent(json, 'entityLiteralResolver', baseModel.entityLiteralResolver);
            setIfAbsent(json, 'enumLiteralResolver', baseModel.enumLiteralResolver);
            setIfAbsent(json, 'collectionLiteralResolver', baseModel.collectionLiteralResolver);
        } else {
            baseDomainModel = null;
        }
        let domainModel: DomainModel;
        if (typeof json['domain'] === 'undefined') {
            domainModel = baseDomainModel;
        } else {
            domainModel = DomainModel.parse(json['domain'], baseDomainModel, extension);
        }
        return this.from(domainModel, json, extension);
    }

    static from(model: DomainModel, json: object, extensions?: StringMap<Function>): ExpressionService {
        if (typeof extensions === "undefined") {
            extensions = {};
        }
        let registerIfAbsent = function(k: string, f: Function) {
            if (!(extensions[k] instanceof Function)) {
                extensions[k] = f;
            }
        };
        let resolver = function(resolver): LiteralResolver {
            if (typeof resolver !== 'undefined') {
                if (resolver.resolveLiteral) {
                    return resolver;
                } else if (typeof extensions !== 'undefined') {
                    let typeResolver = null, args = [];
                    if (typeof resolver === "string") {
                        typeResolver = extensions[resolver];
                    } else {
                        for (let prop in resolver) {
                            if ((typeResolver = extensions[prop]) != null) {
                                args = resolver[prop];
                                break;
                            }
                        }
                    }
                    if (typeResolver != null) {
                        return typeResolver(...args);
                    }
                }
            }
            return null;
        };
        registerIfAbsent("BooleanLiteralResolver", function(): LiteralResolver {
            return { resolveLiteral(domainModel: DomainModel, kind: LiteralKind, value: boolean | string | EntityLiteral | EnumLiteral | CollectionLiteral): DomainType {
                    return domainModel.types['Boolean'];
                }};
        });
        registerIfAbsent("NumericLiteralResolver", function(): LiteralResolver {
            return { resolveLiteral(domainModel: DomainModel, kind: LiteralKind, value: boolean | string | EntityLiteral | EnumLiteral | CollectionLiteral): DomainType {
                    if (isNaN(parseInt(value as string))) {
                        return domainModel.types['Numeric'];
                    } else {
                        return domainModel.types['Integer'];
                    }
                }};
        });
        registerIfAbsent("StringLiteralResolver", function(): LiteralResolver {
            return { resolveLiteral(domainModel: DomainModel, kind: LiteralKind, value: boolean | string | EntityLiteral | EnumLiteral | CollectionLiteral): DomainType {
                    return domainModel.types['String'];
                }};
        });
        registerIfAbsent("TemporalLiteralResolver", function(): LiteralResolver {
            return { resolveLiteral(domainModel: DomainModel, kind: LiteralKind, value: boolean | string | EntityLiteral | EnumLiteral | CollectionLiteral): DomainType {
                    if (kind == LiteralKind.INTERVAL) {
                        return domainModel.types['Interval'];
                    } else {
                        return domainModel.types['Timestamp'];
                    }
                }};
        });
        registerIfAbsent("DelegatingEntityLiteralResolver", function(main: any, delegate: any): LiteralResolver {
            let mainResolver: LiteralResolver = resolver(main);
            let delegateResolver: LiteralResolver = null;
            if (delegate != null) {
                delegateResolver = resolver(delegate);
            }
            return { resolveLiteral(domainModel: DomainModel, kind: LiteralKind, value: boolean | string | EntityLiteral | EnumLiteral | CollectionLiteral): DomainType {
                    let result = mainResolver.resolveLiteral(domainModel, kind, value);
                    if (result == null) {
                        return delegateResolver == null ? null : delegateResolver.resolveLiteral(domainModel, kind, value);
                    }
                    return result;
                }};
        });
        registerIfAbsent("FixedEntityLiteralResolver", function(supportedEntityTypeIds: StringMap<string>): LiteralResolver {
            return { resolveLiteral(domainModel: DomainModel, kind: LiteralKind, value: boolean | string | EntityLiteral | EnumLiteral | CollectionLiteral): DomainType {
                    let entityLiteral = value as EntityLiteral;
                    let idName = supportedEntityTypeIds[entityLiteral.entityType.name]
                    if (idName == null || entityLiteral.attributeValues[idName] == null) {
                        return null;
                    }
                    return entityLiteral.entityType;
                }};
        });
        registerIfAbsent("SimpleEnumLiteralResolver", function(): LiteralResolver {
            return { resolveLiteral(domainModel: DomainModel, kind: LiteralKind, value: boolean | string | EntityLiteral | EnumLiteral | CollectionLiteral): DomainType {
                    let enumLiteral = value as EnumLiteral;
                    return enumLiteral.enumType;
                }};
        });
        return new ExpressionService(
            model,
            resolver(json['booleanLiteralResolver']),
            resolver(json['numericLiteralResolver']),
            resolver(json['stringLiteralResolver']),
            resolver(json['temporalLiteralResolver']),
            resolver(json['entityLiteralResolver']),
            resolver(json['enumLiteralResolver']),
            resolver(json['collectionLiteralResolver']),
            input => new BlazeExpressionLexer(CharStreams.fromString(input)),
            lexer => new BlazeExpressionParser(new CommonTokenStream(lexer)),
            parser => parser.parseExpressionOrPredicate(),
            (tree, symbolTable) => tree.accept(new MyBlazeExpressionParserVisitor(symbolTable))
        );
    }
}