begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

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
name|ArrayList
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
name|NoSuchEndpointException
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
name|Injector
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
name|Language
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
name|Registry
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_comment
comment|/**  * A number of helper methods  *  * @version $Revision: $  */
end_comment

begin_class
DECL|class|CamelContextHelper
specifier|public
class|class
name|CamelContextHelper
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|CamelContextHelper ()
specifier|private
name|CamelContextHelper
parameter_list|()
block|{     }
comment|/**      * Returns the mandatory endpoint for the given URI or the      * {@link org.apache.camel.NoSuchEndpointException} is thrown      *      * @param camelContext      * @param uri      * @return      */
DECL|method|getMandatoryEndpoint (CamelContext camelContext, String uri)
specifier|public
specifier|static
name|Endpoint
name|getMandatoryEndpoint
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|NoSuchEndpointException
block|{
name|Endpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchEndpointException
argument_list|(
name|uri
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|endpoint
return|;
block|}
block|}
comment|/**      * Returns a list of all endpoints of the given type      *      * @param camelContext      * @param type the type of the endpoints requested      * @return a list which may be empty of all the endpoint instances of the given type      */
DECL|method|getSingletonEndpoints (CamelContext camelContext, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|getSingletonEndpoints
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|List
argument_list|<
name|T
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
init|=
name|camelContext
operator|.
name|getSingletonEndpoints
argument_list|()
decl_stmt|;
for|for
control|(
name|Endpoint
name|endpoint
range|:
name|endpoints
control|)
block|{
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|endpoint
argument_list|)
condition|)
block|{
name|T
name|value
init|=
name|type
operator|.
name|cast
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Converts the given value to the requested type      */
DECL|method|convertTo (CamelContext context, Class<T> type, Object value)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|notNull
argument_list|(
name|context
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
return|return
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * Converts the given value to the specified type throwing an {@link IllegalArgumentException}      * if the value could not be converted to a non null value      */
DECL|method|mandatoryConvertTo (CamelContext context, Class<T> type, Object value)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|T
name|answer
init|=
name|convertTo
argument_list|(
name|context
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value "
operator|+
name|value
operator|+
literal|" converted to "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" cannot be null"
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Creates a new instance of the given type using the {@link Injector} on the given      * {@link CamelContext}      */
DECL|method|newInstance (CamelContext context, Class<T> beanType)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|beanType
parameter_list|)
block|{
return|return
name|context
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|beanType
argument_list|)
return|;
block|}
comment|/**      * Look up the given named bean in the {@link Registry} on the      * {@link CamelContext}      */
DECL|method|lookup (CamelContext context, String name)
specifier|public
specifier|static
name|Object
name|lookup
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Look up the given named bean of the given type in the {@link Registry} on the      * {@link CamelContext}      */
DECL|method|lookup (CamelContext context, String name, Class<T> beanType)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|beanType
parameter_list|)
block|{
return|return
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|name
argument_list|,
name|beanType
argument_list|)
return|;
block|}
comment|/**      * Look up the given named bean in the {@link Registry} on the      * {@link CamelContext} or throws      */
DECL|method|mandatoryLookup (CamelContext context, String name)
specifier|public
specifier|static
name|Object
name|mandatoryLookup
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Object
name|answer
init|=
name|lookup
argument_list|(
name|context
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|notNull
argument_list|(
name|answer
argument_list|,
literal|"registry entry called "
operator|+
name|name
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Look up the given named bean of the given type in the {@link Registry} on the      * {@link CamelContext}      */
DECL|method|mandatoryLookup (CamelContext context, String name, Class<T> beanType)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryLookup
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|beanType
parameter_list|)
block|{
name|T
name|answer
init|=
name|lookup
argument_list|(
name|context
argument_list|,
name|name
argument_list|,
name|beanType
argument_list|)
decl_stmt|;
name|notNull
argument_list|(
name|answer
argument_list|,
literal|"registry entry called "
operator|+
name|name
operator|+
literal|" of type "
operator|+
name|beanType
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Resolves the given language name into a {@link Language} or throws an exception if it could not be converted      *      * @param camelContext      * @param languageName      * @return      */
DECL|method|resolveMandatoryLanguage (CamelContext camelContext, String languageName)
specifier|public
specifier|static
name|Language
name|resolveMandatoryLanguage
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|languageName
parameter_list|)
block|{
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|notNull
argument_list|(
name|languageName
argument_list|,
literal|"languageName"
argument_list|)
expr_stmt|;
name|Language
name|language
init|=
name|camelContext
operator|.
name|resolveLanguage
argument_list|(
name|languageName
argument_list|)
decl_stmt|;
if|if
condition|(
name|language
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Could not resolve language: "
operator|+
name|languageName
argument_list|)
throw|;
block|}
return|return
name|language
return|;
block|}
comment|/**      * Resolves the mandatory language name and expression text into a {@link Expression} instance      * throwing an exception if it could not be created      */
DECL|method|resolveMandatoryExpression (CamelContext camelContext, String languageName, String expressionText)
specifier|public
specifier|static
name|Expression
name|resolveMandatoryExpression
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|languageName
parameter_list|,
name|String
name|expressionText
parameter_list|)
block|{
name|notNull
argument_list|(
name|expressionText
argument_list|,
literal|"expressionText"
argument_list|)
expr_stmt|;
name|Language
name|language
init|=
name|resolveMandatoryLanguage
argument_list|(
name|camelContext
argument_list|,
name|languageName
argument_list|)
decl_stmt|;
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|expression
init|=
name|language
operator|.
name|createExpression
argument_list|(
name|expressionText
argument_list|)
decl_stmt|;
if|if
condition|(
name|expression
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Could not create expression: "
operator|+
name|expressionText
operator|+
literal|" with language: "
operator|+
name|language
argument_list|)
throw|;
block|}
return|return
name|expression
return|;
block|}
block|}
end_class

end_unit

