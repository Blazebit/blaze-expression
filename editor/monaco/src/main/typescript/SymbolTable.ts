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

import {ExpressionService} from "./ExpressionService";
import {DomainModel} from "blaze-domain";
import {Symbol} from "./Symbol";

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class SymbolTable {
    variables: StringMap<Symbol>;
    model: ExpressionService;

    static parse(input: string | object, baseModel?: ExpressionService | DomainModel, extension?: StringMap<Function>): SymbolTable {
        let json;
        if (typeof input === "string") {
            json = JSON.parse(input);
        } else {
            json = input;
        }
        let model = ExpressionService.parse(json, baseModel, extension);
        return { variables: this.parseSymbols(model, json['symbols']), model: model };
    }

    static from(model: ExpressionService, input: string | object): SymbolTable {
        let json;
        if (typeof input === "string") {
            json = JSON.parse(input);
        } else {
            json = input;
        }
        return { variables: this.parseSymbols(model, json['symbols']), model: model };
    }

    static parseSymbols(expressionService: ExpressionService, symbols: object): StringMap<Symbol> {
        const model = expressionService.domainModel;
        let vars: StringMap<Symbol> = {};
        for (let name in symbols) {
            let s = symbols[name];
            vars[name] = new Symbol(name, expressionService.identifierRenderer(name), model.getType(s['type']), s['doc']);
        }
        return vars;
    }
}