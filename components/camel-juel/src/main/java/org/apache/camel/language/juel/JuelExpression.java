begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.juel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|juel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|el
operator|.
name|ArrayELResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|el
operator|.
name|CompositeELResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|el
operator|.
name|ELContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|el
operator|.
name|ELResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|el
operator|.
name|ExpressionFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|el
operator|.
name|ListELResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|el
operator|.
name|MapELResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|el
operator|.
name|ResourceBundleELResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|el
operator|.
name|ValueExpression
import|;
end_import

begin_import
import|import
name|de
operator|.
name|odysseus
operator|.
name|el
operator|.
name|util
operator|.
name|SimpleContext
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
name|Message
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
name|ExpressionSupport
import|;
end_import

begin_comment
comment|/**  * The<a href="http://activemq.apache.org/camel/el.html">EL Language from JSP and JSF</a>  * using the<a href="http://activemq.apache.org/camel/juel.html">JUEL library</a>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JuelExpression
specifier|public
class|class
name|JuelExpression
extends|extends
name|ExpressionSupport
block|{
DECL|field|expression
specifier|private
specifier|final
name|String
name|expression
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|field|expressionFactory
specifier|private
name|ExpressionFactory
name|expressionFactory
decl_stmt|;
DECL|field|expressionFactoryProperties
specifier|private
name|Properties
name|expressionFactoryProperties
decl_stmt|;
DECL|method|JuelExpression (String expression, Class<?> type)
specifier|public
name|JuelExpression
parameter_list|(
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|el (String expression)
specifier|public
specifier|static
name|JuelExpression
name|el
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
operator|new
name|JuelExpression
argument_list|(
name|expression
argument_list|,
name|Object
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|evaluate (Exchange exchange)
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// TODO we could use caching here but then we'd have possible concurrency issues
comment|// so lets assume that the provider caches
name|ELContext
name|context
init|=
name|populateContext
argument_list|(
name|createContext
argument_list|()
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|ValueExpression
name|valueExpression
init|=
name|getExpressionFactory
argument_list|()
operator|.
name|createValueExpression
argument_list|(
name|context
argument_list|,
name|expression
argument_list|,
name|type
argument_list|)
decl_stmt|;
return|return
name|valueExpression
operator|.
name|getValue
argument_list|(
name|context
argument_list|)
return|;
block|}
DECL|method|getExpressionFactory ()
specifier|public
name|ExpressionFactory
name|getExpressionFactory
parameter_list|()
block|{
if|if
condition|(
name|expressionFactory
operator|==
literal|null
condition|)
block|{
name|Properties
name|properties
init|=
name|getExpressionFactoryProperties
argument_list|()
decl_stmt|;
name|expressionFactory
operator|=
name|ExpressionFactory
operator|.
name|newInstance
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
return|return
name|expressionFactory
return|;
block|}
DECL|method|setExpressionFactory (ExpressionFactory expressionFactory)
specifier|public
name|void
name|setExpressionFactory
parameter_list|(
name|ExpressionFactory
name|expressionFactory
parameter_list|)
block|{
name|this
operator|.
name|expressionFactory
operator|=
name|expressionFactory
expr_stmt|;
block|}
DECL|method|getExpressionFactoryProperties ()
specifier|public
name|Properties
name|getExpressionFactoryProperties
parameter_list|()
block|{
if|if
condition|(
name|expressionFactoryProperties
operator|==
literal|null
condition|)
block|{
name|expressionFactoryProperties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|populateDefaultExpressionProperties
argument_list|(
name|expressionFactoryProperties
argument_list|)
expr_stmt|;
block|}
return|return
name|expressionFactoryProperties
return|;
block|}
DECL|method|setExpressionFactoryProperties (Properties expressionFactoryProperties)
specifier|public
name|void
name|setExpressionFactoryProperties
parameter_list|(
name|Properties
name|expressionFactoryProperties
parameter_list|)
block|{
name|this
operator|.
name|expressionFactoryProperties
operator|=
name|expressionFactoryProperties
expr_stmt|;
block|}
DECL|method|populateContext (ELContext context, Exchange exchange)
specifier|protected
name|ELContext
name|populateContext
parameter_list|(
name|ELContext
name|context
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|setVariable
argument_list|(
name|context
argument_list|,
literal|"exchange"
argument_list|,
name|exchange
argument_list|,
name|Exchange
operator|.
name|class
argument_list|)
expr_stmt|;
name|setVariable
argument_list|(
name|context
argument_list|,
literal|"in"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|Message
operator|.
name|class
argument_list|)
expr_stmt|;
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|setVariable
argument_list|(
name|context
argument_list|,
literal|"out"
argument_list|,
name|out
argument_list|,
name|Message
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
comment|/**      * A Strategy Method to populate the default properties used to create the expression factory      */
DECL|method|populateDefaultExpressionProperties (Properties properties)
specifier|protected
name|void
name|populateDefaultExpressionProperties
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
comment|// lets enable method invocations
name|properties
operator|.
name|setProperty
argument_list|(
literal|"javax.el.methodInvocations"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
DECL|method|setVariable (ELContext context, String name, Object value, Class<?> type)
specifier|protected
name|void
name|setVariable
parameter_list|(
name|ELContext
name|context
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|ValueExpression
name|valueExpression
init|=
name|getExpressionFactory
argument_list|()
operator|.
name|createValueExpression
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|SimpleContext
name|simpleContext
init|=
operator|(
name|SimpleContext
operator|)
name|context
decl_stmt|;
name|simpleContext
operator|.
name|setVariable
argument_list|(
name|name
argument_list|,
name|valueExpression
argument_list|)
expr_stmt|;
block|}
comment|/**      * Factory method to create the EL context      */
DECL|method|createContext ()
specifier|protected
name|ELContext
name|createContext
parameter_list|()
block|{
name|ELResolver
name|resolver
init|=
operator|new
name|CompositeELResolver
argument_list|()
block|{
block|{
comment|//add(methodResolver);
name|add
argument_list|(
operator|new
name|ArrayELResolver
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|add
argument_list|(
operator|new
name|ListELResolver
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|add
argument_list|(
operator|new
name|MapELResolver
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|add
argument_list|(
operator|new
name|ResourceBundleELResolver
argument_list|()
argument_list|)
expr_stmt|;
name|add
argument_list|(
operator|new
name|BeanAndMethodELResolver
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
return|return
operator|new
name|SimpleContext
argument_list|(
name|resolver
argument_list|)
return|;
block|}
DECL|method|assertionFailureMessage (Exchange exchange)
specifier|protected
name|String
name|assertionFailureMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|expression
return|;
block|}
block|}
end_class

end_unit

