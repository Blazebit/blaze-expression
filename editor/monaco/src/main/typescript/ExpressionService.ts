/*
 * Copyright 2019 - 2025 Blazebit.
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

import {DomainModel, DomainType, EntityDomainType, EnumDomainType} from "blaze-domain";
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
import {TypeResolvingBlazeExpressionParserVisitor} from "./TypeResolvingBlazeExpressionParserVisitor";
import {Token} from "antlr4ts/Token";

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

    templateLexerFactory: (string) => Lexer;

    parserFactory: (Lexer) => Parser;

    parseRuleInvoker: (Parser) => ParserRuleContext;

    parseTemplateRuleInvoker: (Parser) => ParserRuleContext;

    inTemplateExpressionContextChecker: (string) => boolean;

    identifierRenderer: (string) => string;

    typeResolver: (tree: ParserRuleContext, symbolTable: SymbolTable) => DomainType;

    constructor(domainModel: DomainModel, booleanLiteralResolver: LiteralResolver, numericLiteralResolver: LiteralResolver, stringLiteralResolver: LiteralResolver, temporalLiteralResolver: LiteralResolver, entityLiteralResolver: LiteralResolver, enumLiteralResolver: LiteralResolver, collectionLiteralResolver: LiteralResolver, lexerFactory: (string) => Lexer, templateLexerFactory: (string) => Lexer, parserFactory: (Lexer) => Parser, parseRuleInvoker: (Parser) => ParserRuleContext, parseTemplateRuleInvoker: (Parser) => ParserRuleContext, inTemplateExpressionContextChecker: (string) => boolean, identifierRenderer: (string) => string, typeResolver: (tree: ParserRuleContext, symbolTable: SymbolTable) => DomainType) {
        this.domainModel = domainModel;
        this.booleanLiteralResolver = booleanLiteralResolver;
        this.numericLiteralResolver = numericLiteralResolver;
        this.stringLiteralResolver = stringLiteralResolver;
        this.temporalLiteralResolver = temporalLiteralResolver;
        this.entityLiteralResolver = entityLiteralResolver;
        this.enumLiteralResolver = enumLiteralResolver;
        this.collectionLiteralResolver = collectionLiteralResolver;
        this.lexerFactory = lexerFactory;
        this.templateLexerFactory = templateLexerFactory;
        this.parserFactory = parserFactory;
        this.parseRuleInvoker = parseRuleInvoker;
        this.parseTemplateRuleInvoker = parseTemplateRuleInvoker;
        this.inTemplateExpressionContextChecker = inTemplateExpressionContextChecker;
        this.identifierRenderer = identifierRenderer;
        this.typeResolver = typeResolver;
        let types = domainModel.getTypes();
        for (let name in types) {
            let type = types[name];
            (type as any).identifier = identifierRenderer(type.name);
            if (type instanceof EnumDomainType) {
                for (let valueName in type.enumValues) {
                    let value = type.enumValues[valueName];
                    (value as any).identifier = identifierRenderer(value.value);
                }
            } else if (type instanceof EntityDomainType) {
                for (let attributeName in type.attributes) {
                    let attribute = type.attributes[attributeName];
                    (attribute as any).identifier = identifierRenderer(attribute.name);
                }
            }
        }
        let funcs = domainModel.getFunctions();
        for (let name in funcs) {
            let f = funcs[name];
            (f as any).identifier = identifierRenderer(f.name);
            if (f.arguments.length != 0) {
                for (var i = 0; i < f.arguments.length; i++) {
                    let argument = f.arguments[i];
                    (argument as any).identifier = identifierRenderer(argument.name);
                }
            }
        }
    }

    createLexer(input: string): Lexer {
        return this.lexerFactory(input);
    }

    createTemplateLexer(input: string): Lexer {
        return this.templateLexerFactory(input);
    }

    inTemplateExpressionContext(input: string): boolean {
        return this.inTemplateExpressionContextChecker(input);
    }

    parseTree(input: string, templateMode: boolean = false): ParserRuleContext {
        const lexer = templateMode ? this.createTemplateLexer(input) : this.createLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ConsoleErrorListener());

        const parser = this.parserFactory(lexer);
        parser.removeErrorListeners();
        parser.errorHandler = new BlazeExpressionErrorStrategy();
        if (templateMode) {
            return this.parseTemplateRuleInvoker(parser);
        } else {
            return this.parseRuleInvoker(parser);
        }
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
                    return domainModel.getType('Boolean');
                }};
        });
        registerIfAbsent("NumericLiteralResolver", function(): LiteralResolver {
            return { resolveLiteral(domainModel: DomainModel, kind: LiteralKind, value: boolean | string | EntityLiteral | EnumLiteral | CollectionLiteral): DomainType {
                    if (kind == LiteralKind.NUMERIC) {
                        return domainModel.getType('Numeric');
                    } else {
                        return domainModel.getType('Integer');
                    }
                }};
        });
        registerIfAbsent("StringLiteralResolver", function(): LiteralResolver {
            return { resolveLiteral(domainModel: DomainModel, kind: LiteralKind, value: boolean | string | EntityLiteral | EnumLiteral | CollectionLiteral): DomainType {
                    return domainModel.getType('String');
                }};
        });
        registerIfAbsent("TemporalLiteralResolver", function(): LiteralResolver {
            return { resolveLiteral(domainModel: DomainModel, kind: LiteralKind, value: boolean | string | EntityLiteral | EnumLiteral | CollectionLiteral): DomainType {
                    switch (kind) {
                        case LiteralKind.INTERVAL:
                            return domainModel.getType('Interval');
                        case LiteralKind.DATE:
                            return domainModel.getType('Date');
                        case LiteralKind.TIME:
                            return domainModel.getType('Time');
                        default:
                            return domainModel.getType('Timestamp');
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
            input => {
                let lexer = new BlazeExpressionLexer(CharStreams.fromString(input));
                lexer.pushMode(BlazeExpressionLexer.TEMPLATE);
                return lexer;
            },
            lexer => new BlazeExpressionParser(new CommonTokenStream(lexer)),
            parser => parser.parseExpressionOrPredicate(),
            parser => parser.parseTemplate(),
            input => {
                let lexer = new BlazeExpressionLexer(CharStreams.fromString(input));
                lexer.pushMode(BlazeExpressionLexer.TEMPLATE);
                let text = true;
                let token;
                while ((token = lexer.nextToken()).type != Token.EOF) {
                    text = token.type == BlazeExpressionLexer.TEXT || token.type == BlazeExpressionLexer.EXPRESSION_END;
                }
                return !text;
            },
            identifier => {
                if (identifier == null || identifier.length == 0 || /[a-zA-Z_$\u0080-\ufffe]/.test(identifier.charAt(0)) && (identifier.length == 1 || /^[a-zA-Z_$0-9\u0080-\ufffe]+$/.test(identifier.substring(1)))) {
                    return identifier;
                }
                return '`' + identifier + '`';
            },
            (tree, symbolTable) => tree.accept(new TypeResolvingBlazeExpressionParserVisitor(symbolTable))
        );
    }
}