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
package com.blazebit.expression.azure.vm;

import java.util.Map;

import org.junit.Test;

import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionInterpreterContext;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.Expressions;
import com.blazebit.expression.azure.subscription.AzureSubscriptionModel;
import com.blazebit.expression.azure.subscription.model.Subscription;

import static org.junit.Assert.assertEquals;

public class AzureSubscriptionExpressionTest {

	@Test
	public void testExpressionInterpretation() {
		DomainModel domainModel = AzureSubscriptionModel.createConfiguration().createDomainModel();
		ExpressionService expressionService = Expressions.forModel( domainModel );
		ExpressionCompiler compiler = expressionService.createCompiler();
		ExpressionCompiler.Context compileContext = compiler.createContext(
				Map.of( "s", domainModel.getType( "Subscription" ) ),
				(pathParts, ctx) -> "s"
		);

		ExpressionInterpreter interpreter = expressionService.createInterpreter();
		ExpressionInterpreterContext interpreterContext = ExpressionInterpreterContext.create(expressionService);

		Subscription subscription = new Subscription();
		subscription.setAuthorizationSource("123");
		interpreterContext.withRoot("s", subscription);
		String result = interpreter.evaluate(compiler.createExpression("authorizationSource", compileContext), interpreterContext);
		assertEquals(subscription.getAuthorizationSource(), result);
	}
}
