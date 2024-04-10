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

import {SymbolTable} from "../../main/typescript";

export class PostModel {
    static getSymbolTable(): SymbolTable {
        return SymbolTable.parse(JSON.stringify({
            domain: {
                types: [
                    {
                        name: "Integer",
                        kind: "B",
                        ops: ["+", "-", "*", "/", "%", "M", "P"],
                        preds: ["R", "E", "N"],
                        meta: []
                    },
                    {
                        name: "Numeric",
                        kind: "B",
                        ops: ["+", "-", "*", "/", "%", "M", "P"],
                        preds: ["R", "E", "N"],
                        meta: []
                    },
                    {
                        name: "String",
                        kind: "B",
                        ops: ["+"],
                        preds: ["R", "E", "N"],
                        meta: []
                    },
                    {
                        name: "Timestamp",
                        kind: "B",
                        ops: ["+", "-"],
                        preds: ["N", "R", "E"],
                        meta: []
                    },
                    {
                        name: "Boolean",
                        kind: "B",
                        ops: ["!"],
                        preds: ["E", "N"],
                        meta: []
                    },
                    {
                        name: "User",
                        kind: "E",
                        preds: ["E", "N"],
                        meta: [],
                        attrs: [
                            {name: "id", type: "Integer", meta: []},
                            {name: "name", type: "String", meta: []}
                        ]
                    },
                    {
                        name: "Post",
                        kind: "E",
                        preds: ["E", "N"],
                        meta: [],
                        attrs: [
                            {name: "id", type: "Integer", meta: []},
                            {name: "name", type: "String", meta: []},
                            {name: "writer", type: "User", meta: []},
                            {name: "comments", type: "Collection[Comment]", meta: []}
                        ]
                    },
                    {
                        name: "Comment",
                        kind: "E",
                        preds: ["E", "N"],
                        meta: [],
                        attrs: [
                            {name: "id", type: "Integer", meta: []},
                            {name: "content", type: "String", meta: []},
                            {name: "writer", type: "User", meta: []}
                        ]
                    },
                    {
                        name: "Collection[Comment]",
                        kind: "C",
                        preds: ["C"],
                        meta: []
                    }
                ],
                funcs: [
                    {
                        name: "indexOf",
                        volatility: "S",
                        argCount: -1,
                        minArgCount: 2,
                        type: "Integer",
                        meta: [
                            {doc: "The indexOf function"}
                        ],
                        args: [
                            {
                                name: "string",
                                type: "String",
                                meta: [
                                    {doc: "The string in which to search"}
                                ]
                            },
                            {
                                name: "substring",
                                type: "String",
                                meta: [
                                    {doc: "The substring to search"}
                                ]
                            },
                            {
                                name: "startIndex",
                                type: "Integer",
                                meta: [
                                    {doc: "The index at which to start searching"}
                                ]
                            }
                        ]
                    },
                    {
                        name: "substring",
                        volatility: "S",
                        argCount: -1,
                        minArgCount: 2,
                        type: "String",
                        meta: [
                            {doc: "The substring function"}
                        ],
                        args: [
                            {
                                name: "string",
                                type: "String",
                                meta: [
                                    {doc: "The string for which to produce a substring"}
                                ]
                            },
                            {
                                name: "startIndex",
                                type: "Integer",
                                meta: [
                                    {doc: "The index at which to start the substring"}
                                ]
                            },
                            {
                                name: "endIndex",
                                type: "Integer",
                                meta: [
                                    {doc: "The index at which to end the substring"}
                                ]
                            }
                        ]
                    },
                    {
                        name: "startsWith",
                        volatility: "S",
                        argCount: 2,
                        minArgCount: 2,
                        type: "Boolean",
                        meta: [
                            {doc: "The startsWith function"}
                        ],
                        args: [
                            {
                                name: "string",
                                type: "String",
                                meta: [
                                    {doc: "The string in which to search"}
                                ]
                            },
                            {
                                name: "substring",
                                type: "String",
                                meta: [
                                    {doc: "The substring to search"}
                                ]
                            }
                        ]
                    }
                ],
                opResolvers: [
                    {
                        resolver: {"FixedDomainOperationTypeResolver": ["Integer"]},
                        typeOps: {"Integer": ["%", "M", "P"], "Numeric": ["%"]}
                    },
                    {
                        resolver: {"FixedDomainOperationTypeResolver": ["Numeric"]},
                        typeOps: {"Integer": ["/"], "Numeric": ["/", "%", "M", "P"]}
                    },
                    {
                        resolver: {"WidestDomainOperationTypeResolver": ["Numeric", "Integer"]},
                        typeOps: {"Integer": ["+", "-", "*"], "Numeric": ["+", "-", "*"]}
                    },
                    {resolver: {"FixedDomainOperationTypeResolver": ["String"]}, typeOps: {"String": ["+"]}},
                    {resolver: {"FixedDomainOperationTypeResolver": ["Boolean"]}, typeOps: {"Boolean": ["!"]}}
                ],
                predResolvers: []
            },
            booleanLiteralResolver: "BooleanLiteralResolver",
            numericLiteralResolver: "NumericLiteralResolver",
            stringLiteralResolver: "StringLiteralResolver",
            temporalLiteralResolver: "TemporalLiteralResolver",
            symbols: {
                p: {type: "Post"}
            }
        }));
    }
}