begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElementRef
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|Endpoint
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
name|Exchange
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
name|Route
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
name|impl
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
name|model
operator|.
name|language
operator|.
name|ExpressionType
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
name|Resequencer
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"resequencer"
argument_list|)
DECL|class|ResequencerType
specifier|public
class|class
name|ResequencerType
extends|extends
name|ProcessorType
block|{
annotation|@
name|XmlElementRef
DECL|field|interceptors
specifier|private
name|List
argument_list|<
name|InterceptorType
argument_list|>
name|interceptors
init|=
operator|new
name|ArrayList
argument_list|<
name|InterceptorType
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|expressions
specifier|private
name|List
argument_list|<
name|ExpressionType
argument_list|>
name|expressions
init|=
operator|new
name|ArrayList
argument_list|<
name|ExpressionType
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|outputs
specifier|private
name|List
argument_list|<
name|ProcessorType
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorType
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|expressionList
specifier|private
name|List
argument_list|<
name|Expression
argument_list|>
name|expressionList
decl_stmt|;
DECL|method|ResequencerType ()
specifier|public
name|ResequencerType
parameter_list|()
block|{     }
DECL|method|ResequencerType (List<Expression> expressions)
specifier|public
name|ResequencerType
parameter_list|(
name|List
argument_list|<
name|Expression
argument_list|>
name|expressions
parameter_list|)
block|{
name|this
operator|.
name|expressionList
operator|=
name|expressions
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Resequencer[ "
operator|+
name|getExpressions
argument_list|()
operator|+
literal|" -> "
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|getExpressions ()
specifier|public
name|List
argument_list|<
name|ExpressionType
argument_list|>
name|getExpressions
parameter_list|()
block|{
return|return
name|expressions
return|;
block|}
DECL|method|getInterceptors ()
specifier|public
name|List
argument_list|<
name|InterceptorType
argument_list|>
name|getInterceptors
parameter_list|()
block|{
return|return
name|interceptors
return|;
block|}
DECL|method|setInterceptors (List<InterceptorType> interceptors)
specifier|public
name|void
name|setInterceptors
parameter_list|(
name|List
argument_list|<
name|InterceptorType
argument_list|>
name|interceptors
parameter_list|)
block|{
name|this
operator|.
name|interceptors
operator|=
name|interceptors
expr_stmt|;
block|}
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorType
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|outputs
return|;
block|}
DECL|method|setOutputs (List<ProcessorType> outputs)
specifier|public
name|void
name|setOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorType
argument_list|>
name|outputs
parameter_list|)
block|{
name|this
operator|.
name|outputs
operator|=
name|outputs
expr_stmt|;
block|}
DECL|method|addRoutes (RouteContext routeContext, Collection<Route> routes)
specifier|public
name|void
name|addRoutes
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
throws|throws
name|Exception
block|{
name|Endpoint
name|from
init|=
name|routeContext
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
specifier|final
name|Processor
name|processor
init|=
name|routeContext
operator|.
name|createProcessor
argument_list|(
name|this
argument_list|)
decl_stmt|;
specifier|final
name|Resequencer
name|resequencer
init|=
operator|new
name|Resequencer
argument_list|(
name|from
argument_list|,
name|processor
argument_list|,
name|resolveExpressionList
argument_list|(
name|routeContext
argument_list|)
argument_list|)
decl_stmt|;
name|Route
name|route
init|=
operator|new
name|Route
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|from
argument_list|,
name|resequencer
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ResequencerRoute["
operator|+
name|getEndpoint
argument_list|()
operator|+
literal|" -> "
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
block|}
decl_stmt|;
name|routes
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
DECL|method|resolveExpressionList (RouteContext routeContext)
specifier|private
name|List
argument_list|<
name|Expression
argument_list|>
name|resolveExpressionList
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|expressionList
operator|==
literal|null
condition|)
block|{
name|expressionList
operator|=
operator|new
name|ArrayList
argument_list|<
name|Expression
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|ExpressionType
name|expression
range|:
name|expressions
control|)
block|{
name|expressionList
operator|.
name|add
argument_list|(
name|expression
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|expressionList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No expressions configured for: "
operator|+
name|this
argument_list|)
throw|;
block|}
return|return
name|expressionList
return|;
block|}
block|}
end_class

end_unit

