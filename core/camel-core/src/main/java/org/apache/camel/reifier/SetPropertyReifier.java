begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Expression
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|ExpressionBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|ProcessorDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|SetPropertyDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|SetPropertyProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|RouteContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|SetPropertyReifier
class|class
name|SetPropertyReifier
extends|extends
name|ExpressionReifier
argument_list|<
name|SetPropertyDefinition
argument_list|>
block|{
DECL|method|SetPropertyReifier (ProcessorDefinition<?> definition)
name|SetPropertyReifier
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|SetPropertyDefinition
operator|)
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|definition
operator|.
name|getPropertyName
argument_list|()
argument_list|,
literal|"propertyName"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|Expression
name|expr
init|=
name|definition
operator|.
name|getExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|Expression
name|nameExpr
init|=
name|ExpressionBuilder
operator|.
name|parseSimpleOrFallbackToConstantExpression
argument_list|(
name|definition
operator|.
name|getPropertyName
argument_list|()
argument_list|,
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|SetPropertyProcessor
argument_list|(
name|nameExpr
argument_list|,
name|expr
argument_list|)
return|;
block|}
block|}
end_class

end_unit
