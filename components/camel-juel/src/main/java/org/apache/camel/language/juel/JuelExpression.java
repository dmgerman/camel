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
name|io
operator|.
name|IOException
import|;
end_import

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
name|ExpressionFactoryImpl
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
name|CamelContext
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
name|FactoryFinder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * The<a href="http://camel.apache.org/el.html">EL Language from JSP and JSF</a>  * using the<a href="http://camel.apache.org/juel.html">JUEL library</a>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JuelExpression
specifier|public
class|class
name|JuelExpression
extends|extends
name|ExpressionSupport
block|{
DECL|field|DEFAULT_EXPRESSION_FACTORY_IMPL_CLASS
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_EXPRESSION_FACTORY_IMPL_CLASS
init|=
literal|"de.odysseus.el.ExpressionFactoryImpl"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|JuelExpression
operator|.
name|class
argument_list|)
decl_stmt|;
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
DECL|method|evaluate (Exchange exchange, Class<T> tClass)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|tClass
parameter_list|)
block|{
comment|// TODO we could use caching here but then we'd have possible concurrency issues
comment|// so lets assume that the provider caches
comment|// Create (if needed) the ExpressionFactory first from the CamelContext using FactoryFinder
name|ExpressionFactory
name|factory
init|=
name|getExpressionFactory
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
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
name|factory
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
name|Object
name|value
init|=
name|valueExpression
operator|.
name|getValue
argument_list|(
name|context
argument_list|)
decl_stmt|;
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|tClass
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|getExpressionFactory (CamelContext context)
specifier|public
name|ExpressionFactory
name|getExpressionFactory
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|expressionFactory
operator|==
literal|null
operator|&&
name|context
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|FactoryFinder
name|finder
init|=
name|context
operator|.
name|getFactoryFinder
argument_list|(
literal|"META-INF/services/org/apache/camel/language/"
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|finder
operator|.
name|findClass
argument_list|(
literal|"el"
argument_list|,
literal|"impl."
argument_list|)
decl_stmt|;
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
name|expressionFactory
operator|=
operator|(
name|ExpressionFactory
operator|)
name|clazz
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"'impl.class' not found"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No impl class for juel ExpressionFactory defined in 'META-INF/services/org/apache/camel/language/el'"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Failed to instantiate juel ExpressionFactory implementation class."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Failed to instantiate juel ExpressionFactory implementation class."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|getExpressionFactory
argument_list|()
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
name|expressionFactory
operator|=
operator|new
name|ExpressionFactoryImpl
argument_list|()
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
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|setVariable
argument_list|(
name|context
argument_list|,
literal|"out"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
name|Message
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|context
return|;
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

