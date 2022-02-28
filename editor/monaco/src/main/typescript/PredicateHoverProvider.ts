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

import {PathResolvingProvider} from "./PathResolvingProvider";
import * as monaco from "monaco-editor/esm/vs/editor/editor.api";
import {CollectionDomainType, EntityDomainType, EnumDomainType} from "blaze-domain";
import {symbolTables} from "./EditorFactory";

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class PredicateHoverProvider extends PathResolvingProvider implements monaco.languages.HoverProvider {

    constructor(templateMode: boolean, identifierStart: RegExp = /[a-zA-Z_$\u0080-\ufffe]/, identifier: RegExp = /[a-zA-Z_$0-9\u0080-\ufffe]/, pathOperators: string[] = ['.']) {
        super(templateMode, identifierStart, identifier, pathOperators);
    }

    provideHover(model: monaco.editor.ITextModel, position: monaco.Position, token: monaco.CancellationToken): monaco.languages.ProviderResult<monaco.languages.Hover> {
        let pathResult = this.analyzePathAroundCursor(model, position);
        let path = pathResult.text;
        let symbolTable = symbolTables[model.id];
        let completionItem: monaco.languages.CompletionItem = null;
        if (path.length != 0 && path.charAt(0).match(this.identifierStart)) {
            let parts = path.split(this.pathOperators);
            if (parts.length == 1) {
                if (pathResult.offendingChar == "(") {
                    let domainFunction = symbolTable.model.domainModel.getFunction(path);
                    if (domainFunction != null) {
                        completionItem = this.functionSuggestion(domainFunction);
                    }
                } else {
                    let variable = symbolTable.variables[path];
                    if (variable != null) {
                        completionItem = this.varSuggestion(variable);
                    } else {
                        let type = symbolTable.model.domainModel.getType(path);
                        if (type != null) {
                            completionItem = this.typeSuggestion(type);
                        }
                    }
                }
            } else {
                let lastIdx = parts.length - 1;
                let basePath = path.substring(0, path.length - parts[lastIdx].length - 1);
                if (parts.length == 2) {
                    let type = symbolTable.model.domainModel.getType(basePath.trim());
                    if (type instanceof EnumDomainType) {
                        let enumValue = type.enumValues[parts[lastIdx]];
                        if (enumValue != null) {
                            completionItem = this.enumSuggestion(type, enumValue);
                        }
                    }
                }
                if (completionItem == null) {
                    let domainType = symbolTable.model.resolveType(basePath, symbolTable);
                    if (domainType instanceof CollectionDomainType) {
                        domainType = domainType.elementType;
                    }
                    if (domainType instanceof EntityDomainType) {
                        let attribute = domainType.attributes[parts[lastIdx]];
                        if (attribute != null) {
                            completionItem = this.attrSuggestion(attribute);
                        }
                    }
                }
            }
        }
        if (completionItem != null) {
            let docVal = "";
            let doc: string | monaco.IMarkdownString = completionItem.documentation;
            if (doc != null) {
                if (typeof doc === 'string' || doc instanceof String) {
                    docVal = "\n\n" + doc;
                } else {
                    docVal = "\n\n" + doc.value;
                }
            }
            return {
                contents: [ { value: "```\n" + completionItem.detail + "\n```" + docVal, isTrusted: true } ]
            };
        }
        return null;
    }

}