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

import {SymbolTable} from "./../../main/typescript/SymbolTable";

export class UserModel {
    static getSymbolTable(): SymbolTable {
        return SymbolTable.parse(JSON.stringify({
            domain: {
                types: [
                    {
                        name: "UserView", kind: "E", preds: ["N", "E"], attrs: [
                            {name: "name", type: "String"},
                            {name: "sameAgeIds", type: "Collection[Integer]"},
                            {name: "oldestNamedAge", type: "Integer"},
                            {name: "id", type: "Integer"},
                            {name: "age", type: "Integer"},
                            {name: "parent", type: "UserView"}
                        ]
                    },
                    {
                        name: "Time",
                        kind: "B",
                        ops: ["+", "-"],
                        preds: ["N", "R", "E"],
                        meta: [{doc: "A time without a timezone in the ISO-8601 calendar system."}]
                    },
                    {
                        name: "String",
                        kind: "B",
                        ops: ["+"],
                        preds: ["N", "R", "E"],
                        meta: [{doc: "Arbitrary length text."}]
                    },
                    {
                        name: "Timestamp",
                        kind: "B",
                        ops: ["+", "-"],
                        preds: ["N", "R", "E"],
                        meta: [{doc: "An instant in time as timestamp."}]
                    },
                    {
                        name: "Integer",
                        kind: "B",
                        ops: ["P", "M", "+", "-", "*", "/", "%"],
                        preds: ["N", "R", "E"],
                        meta: [{doc: "Arbitrary precision integer number."}]
                    },
                    {
                        name: "Numeric",
                        kind: "B",
                        ops: ["P", "M", "+", "-", "*", "/", "%"],
                        preds: ["N", "R", "E"],
                        meta: [{doc: "Arbitrary precision decimal number."}]
                    },
                    {
                        name: "Boolean",
                        kind: "B",
                        ops: ["!"],
                        preds: ["N", "E"],
                        meta: [{doc: "A truth value that can be true or false"}]
                    },
                    {name: "Collection[Timestamp]", kind: "C", preds: ["C"]},
                    {name: "Collection[Boolean]", kind: "C", preds: ["C"]},
                    {name: "Collection[Time]", kind: "C", preds: ["C"]},
                    {name: "Collection[UserView]", kind: "C", preds: ["C"]},
                    {name: "Collection[String]", kind: "C", preds: ["C"]},
                    {name: "Collection[Integer]", kind: "C", preds: ["C"]},
                    {name: "Collection[Numeric]", kind: "C", preds: ["C"]}
                ],
                "funcs": [
                    {
                        "name": "ABS",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "NthArgumentDomainFunctionTypeResolver": [
                                0
                            ]
                        },
                        "args": [
                            {
                                "name": "number",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "The number argument whose absolute value is to be determined"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the absolute value of the number argument."
                            }
                        ]
                    },
                    {
                        "name": "ACOS",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Numeric"
                            ]
                        },
                        "args": [
                            {
                                "name": "number",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "The value whose arc cosine is to be determined"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the arc cosine of a value."
                            }
                        ]
                    },
                    {
                        "name": "ASIN",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Numeric"
                            ]
                        },
                        "args": [
                            {
                                "name": "number",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "The value whose arc sine is to be determined"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the arc sine of a value."
                            }
                        ]
                    },
                    {
                        "name": "ATAN",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Numeric"
                            ]
                        },
                        "args": [
                            {
                                "name": "number",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "The value whose arc tangent is to be determined"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the arc tangent of a value."
                            }
                        ]
                    },
                    {
                        "name": "ATAN2",
                        "volatility": "I",
                        "argCount": 2,
                        "minArgCount": 2,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Numeric"
                            ]
                        },
                        "args": [
                            {
                                "name": "y",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "The ordinate coordinate"
                                    }
                                ]
                            },
                            {
                                "name": "x",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "The abscissa coordinate"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Computes the ATAN2 value from the given arguments."
                            }
                        ]
                    },
                    {
                        "name": "CEIL",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Integer"
                            ]
                        },
                        "args": [
                            {
                                "name": "number",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "The number argument whose ceil value is to be determined"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the smallest numeric value that is greater than or equal to the argument and is equal to a mathematical integer."
                            }
                        ]
                    },
                    {
                        "name": "COS",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Numeric"
                            ]
                        },
                        "args": [
                            {
                                "name": "number",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "An angle in radians"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the trigonometric cosine of an angle."
                            }
                        ]
                    },
                    {
                        "name": "CURRENT_DATE",
                        "volatility": "S",
                        "argCount": 0,
                        "minArgCount": 0,
                        "type": "Timestamp",
                        "args": [],
                        "meta": [
                            {
                                "doc": "Returns the current date at midnight as timestamp."
                            }
                        ]
                    },
                    {
                        "name": "CURRENT_TIME",
                        "volatility": "S",
                        "argCount": 0,
                        "minArgCount": 0,
                        "type": "Time",
                        "args": [],
                        "meta": [
                            {
                                "doc": "Returns the current time."
                            }
                        ]
                    },
                    {
                        "name": "CURRENT_TIMESTAMP",
                        "volatility": "S",
                        "argCount": 0,
                        "minArgCount": 0,
                        "type": "Timestamp",
                        "args": [],
                        "meta": [
                            {
                                "doc": "Returns the current timestamp."
                            }
                        ]
                    },
                    {
                        "name": "DEGREES",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Numeric"
                            ]
                        },
                        "args": [
                            {
                                "name": "number",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "An angle in radians"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Converts an angle in radians to degrees."
                            }
                        ]
                    },
                    {
                        "name": "ENDS_WITH",
                        "volatility": "I",
                        "argCount": 2,
                        "minArgCount": 2,
                        "type": "Boolean",
                        "args": [
                            {
                                "name": "string",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The string to check"
                                    }
                                ]
                            },
                            {
                                "name": "substring",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The substring to search for"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns if string ends with substring."
                            }
                        ]
                    },
                    {
                        "name": "EXP",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Numeric"
                            ]
                        },
                        "args": [
                            {
                                "name": "number",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "The exponent to raise e to."
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns Euler's number e raised to the power of a value."
                            }
                        ]
                    },
                    {
                        "name": "FLOOR",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Integer"
                            ]
                        },
                        "args": [
                            {
                                "name": "number",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "The number argument whose floor value is to be determined"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the largest numeric value that is less than or equal to the argument and is equal to a mathematical integer."
                            }
                        ]
                    },
                    {
                        "name": "GREATEST",
                        "volatility": "I",
                        "argCount": 3,
                        "minArgCount": 2,
                        "typeResolver": {
                            "WidestDomainFunctionTypeResolver": [
                                [
                                    "Numeric"
                                ]
                            ]
                        },
                        "args": [
                            {
                                "name": "first",
                                "meta": [
                                    {
                                        "doc": "The first value"
                                    }
                                ]
                            },
                            {
                                "name": "second",
                                "meta": [
                                    {
                                        "doc": "The second value"
                                    }
                                ]
                            },
                            {
                                "name": "other",
                                "meta": [
                                    {
                                        "doc": "Other values"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the greatest of the given arguments."
                            }
                        ]
                    },
                    {
                        "name": "LEAST",
                        "volatility": "I",
                        "argCount": 3,
                        "minArgCount": 2,
                        "typeResolver": {
                            "WidestDomainFunctionTypeResolver": [
                                [
                                    "Numeric"
                                ]
                            ]
                        },
                        "args": [
                            {
                                "name": "first",
                                "meta": [
                                    {
                                        "doc": "The first value"
                                    }
                                ]
                            },
                            {
                                "name": "second",
                                "meta": [
                                    {
                                        "doc": "The second value"
                                    }
                                ]
                            },
                            {
                                "name": "other",
                                "meta": [
                                    {
                                        "doc": "Other values"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the smallest of the given arguments."
                            }
                        ]
                    },
                    {
                        "name": "LENGTH",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "type": "Integer",
                        "args": [
                            {
                                "name": "string",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The string whose length is to be determined"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the length of the given string."
                            }
                        ]
                    },
                    {
                        "name": "LOCATE",
                        "volatility": "I",
                        "argCount": 3,
                        "minArgCount": 2,
                        "type": "Integer",
                        "args": [
                            {
                                "name": "substring",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The substring to locate"
                                    }
                                ]
                            },
                            {
                                "name": "string",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The string in which to locate the substring"
                                    }
                                ]
                            },
                            {
                                "name": "start",
                                "type": "Integer",
                                "meta": [
                                    {
                                        "doc": "The 1-based start index at which to start searching for the substring. The default value is 1."
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the 1-based index of the first occurrence of the specified substring within the given string, starting at the specified index. Returns 0 if the substring was not found. By default, the start index is 1 i.e. it starts to search at the beginning."
                            }
                        ]
                    },
                    {
                        "name": "LOCATE_LAST",
                        "volatility": "I",
                        "argCount": 3,
                        "minArgCount": 2,
                        "type": "Integer",
                        "args": [
                            {
                                "name": "substring",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The substring to locate"
                                    }
                                ]
                            },
                            {
                                "name": "string",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The string in which to locate the substring"
                                    }
                                ]
                            },
                            {
                                "name": "start",
                                "type": "Integer",
                                "meta": [
                                    {
                                        "doc": "The 1-based start index at which to start searching for the substring. The default value is the length of the string."
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the 1-based index of the last occurrence of the specified substring within the given string, starting at the specified index. Returns 0 if the substring was not found. By default, the start index the last index i.e. it starts to search at the end."
                            }
                        ]
                    },
                    {
                        "name": "LOG",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Numeric"
                            ]
                        },
                        "args": [
                            {
                                "name": "number",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "The value whose logarithm is to be determined"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the natural logarithm (base e) of a value."
                            }
                        ]
                    },
                    {
                        "name": "LOWER",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "type": "String",
                        "args": [
                            {
                                "name": "string",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The string to be lower-cased"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Converts all of the characters of the given string to lower case."
                            }
                        ]
                    },
                    {
                        "name": "LTRIM",
                        "volatility": "I",
                        "argCount": 2,
                        "minArgCount": 1,
                        "type": "String",
                        "args": [
                            {
                                "name": "string",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The string to trim"
                                    }
                                ]
                            },
                            {
                                "name": "character",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The character to trim. By default this is a whitespace."
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the specified string having any leading characters that match the specified character removed. By default, the character to trim is a whitespace."
                            }
                        ]
                    },
                    {
                        "name": "POW",
                        "volatility": "I",
                        "argCount": 2,
                        "minArgCount": 2,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Numeric"
                            ]
                        },
                        "args": [
                            {
                                "name": "base",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "The base"
                                    }
                                ]
                            },
                            {
                                "name": "power",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "The exponent"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the value of the first argument raised to the power of the second argument."
                            }
                        ]
                    },
                    {
                        "name": "RADIANS",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Numeric"
                            ]
                        },
                        "args": [
                            {
                                "name": "number",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "An angle in degrees"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Converts an angle in degrees to radians."
                            }
                        ]
                    },
                    {
                        "name": "RANDOM",
                        "volatility": "V",
                        "argCount": 0,
                        "minArgCount": 0,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Numeric"
                            ]
                        },
                        "args": [],
                        "meta": [
                            {
                                "doc": "Returns a random number between 0.0 and less than 1.0."
                            }
                        ]
                    },
                    {
                        "name": "REPLACE",
                        "volatility": "I",
                        "argCount": 3,
                        "minArgCount": 3,
                        "type": "String",
                        "args": [
                            {
                                "name": "string",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The string for which to do the replacement"
                                    }
                                ]
                            },
                            {
                                "name": "target",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The target string to replace"
                                    }
                                ]
                            },
                            {
                                "name": "replacement",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The replacement string"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns a string where each substring of the specified string that matches the specified target string is replaced with the specified replacement string."
                            }
                        ]
                    },
                    {
                        "name": "ROUND",
                        "volatility": "I",
                        "argCount": 2,
                        "minArgCount": 1,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Numeric"
                            ]
                        },
                        "args": [
                            {
                                "name": "value",
                                "type": "Numeric",
                                "meta": [
                                    {
                                        "doc": "The value to round"
                                    }
                                ]
                            },
                            {
                                "name": "precision",
                                "type": "Integer",
                                "meta": [
                                    {
                                        "doc": "The precision the value is to be rounded to"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns a number rounded according to the precision."
                            }
                        ]
                    },
                    {
                        "name": "RTRIM",
                        "volatility": "I",
                        "argCount": 2,
                        "minArgCount": 1,
                        "type": "String",
                        "args": [
                            {
                                "name": "string",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The string to trim"
                                    }
                                ]
                            },
                            {
                                "name": "character",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The character to trim. By default this is a whitespace."
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the specified string having any trailing characters that match the specified character removed. By default, the character to trim is a whitespace."
                            }
                        ]
                    },
                    {
                        "name": "SELF",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "NthArgumentDomainFunctionTypeResolver": [
                                0
                            ]
                        },
                        "args": [
                            {
                                "name": "object"
                            }
                        ]
                    },
                    {
                        "name": "SIN",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Numeric"
                            ]
                        },
                        "args": [
                            {
                                "name": "number",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "An angle in radians"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the trigonometric sine of an angle."
                            }
                        ]
                    },
                    {
                        "name": "SIZE",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "type": "Integer",
                        "args": [
                            {
                                "name": "collection",
                                "type": "Collection",
                                "meta": [
                                    {
                                        "doc": "The collection whose size is to be determined"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the size of a collection."
                            }
                        ]
                    },
                    {
                        "name": "SQRT",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Numeric"
                            ]
                        },
                        "args": [
                            {
                                "name": "number",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "The number argument whose square root value is to be determined"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the square root of a value."
                            }
                        ]
                    },
                    {
                        "name": "STARTS_WITH",
                        "volatility": "I",
                        "argCount": 3,
                        "minArgCount": 2,
                        "type": "Boolean",
                        "args": [
                            {
                                "name": "string",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The string to check"
                                    }
                                ]
                            },
                            {
                                "name": "substring",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The substring to search for"
                                    }
                                ]
                            },
                            {
                                "name": "startIndex",
                                "type": "Integer",
                                "meta": [
                                    {
                                        "doc": "The 1-based start index at which to start searching"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns if string starts with substring, beginning at the 1-based start index. By default the start index is at beginning of the string."
                            }
                        ]
                    },
                    {
                        "name": "SUBSTRING",
                        "volatility": "I",
                        "argCount": 3,
                        "minArgCount": 2,
                        "type": "String",
                        "args": [
                            {
                                "name": "string",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The string whose substring is to be determined"
                                    }
                                ]
                            },
                            {
                                "name": "start",
                                "type": "Integer",
                                "meta": [
                                    {
                                        "doc": "The 1-based index at which to start for the returned substring"
                                    }
                                ]
                            },
                            {
                                "name": "count",
                                "type": "Integer",
                                "meta": [
                                    {
                                        "doc": "The amount of characters to copy from the string to the substring."
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns a string that is a substring of the given string, starting at the given 1-based start index. The maximum size of the substring is denoted by the count argument. By default, all remaining characters after the start index are considered."
                            }
                        ]
                    },
                    {
                        "name": "TAN",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "FixedDomainFunctionTypeResolver": [
                                "Numeric"
                            ]
                        },
                        "args": [
                            {
                                "name": "number",
                                "type": "Numeric|Integer",
                                "meta": [
                                    {
                                        "doc": "An angle in radians"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the trigonometric tangent of an angle."
                            }
                        ]
                    },
                    {
                        "name": "THE SELF",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "typeResolver": {
                            "NthArgumentDomainFunctionTypeResolver": [
                                0
                            ]
                        },
                        "args": [
                            {
                                "name": "object"
                            }
                        ]
                    },
                    {
                        "name": "TO_STRING",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "type": "String",
                        "args": [
                            {
                                "name": "value",
                                "meta": [
                                    {
                                        "doc": "The value for which to create the string representation."
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the string representation of the given value."
                            }
                        ]
                    },
                    {
                        "name": "TRIM",
                        "volatility": "I",
                        "argCount": 2,
                        "minArgCount": 1,
                        "type": "String",
                        "args": [
                            {
                                "name": "string",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The string to trim"
                                    }
                                ]
                            },
                            {
                                "name": "character",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The character to trim. By default this is a whitespace."
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Returns the specified string having any leading and trailing characters that match the specified character removed. By default, the character to trim is a whitespace."
                            }
                        ]
                    },
                    {
                        "name": "UPPER",
                        "volatility": "I",
                        "argCount": 1,
                        "minArgCount": 1,
                        "type": "String",
                        "args": [
                            {
                                "name": "string",
                                "type": "String",
                                "meta": [
                                    {
                                        "doc": "The string to be upper-cased"
                                    }
                                ]
                            }
                        ],
                        "meta": [
                            {
                                "doc": "Converts all of the characters of the given string to upper case."
                            }
                        ]
                    }
                ],
                opResolvers: [
                    {resolver: {"FixedDomainOperationTypeResolver": ["Boolean"]}, typeOps: {"Boolean": ["!"]}},
                    {resolver: {"FixedDomainOperationTypeResolver": ["Time"]}, typeOps: {"Time": ["+", "-"]}},
                    {resolver: {"FixedDomainOperationTypeResolver": ["Timestamp"]}, typeOps: {"Timestamp": ["+", "-"]}},
                    {resolver: {"FixedDomainOperationTypeResolver": ["String"]}, typeOps: {"String": ["+"]}},
                    {
                        resolver: {"FixedDomainOperationTypeResolver": ["Numeric"]},
                        typeOps: {"Integer": ["/"], "Numeric": ["P", "M", "/"]}
                    },
                    {
                        resolver: {"FixedDomainOperationTypeResolver": ["Integer"]},
                        typeOps: {"Integer": ["P", "M", "%"], "Numeric": ["%"]}
                    },
                    {
                        resolver: {"WidestDomainOperationTypeResolver": ["Numeric", "Integer"]},
                        typeOps: {"Integer": ["+", "-", "*"], "Numeric": ["+", "-", "*"]}
                    }
                ],
                predResolvers: [
                    {
                        resolver: {"FixedDomainPredicateTypeResolver": ["Boolean"]}
                    }
                ]
            },
            booleanLiteralResolver: "BooleanLiteralResolver",
            numericLiteralResolver: "NumericLiteralResolver",
            stringLiteralResolver: "StringLiteralResolver",
            temporalLiteralResolver: "TemporalLiteralResolver",
            symbols: {
                u: {type: "UserView"},
                "the user": {type: "UserView"}
            }
        }));
    }
}