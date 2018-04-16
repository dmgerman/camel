begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|Arrays
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
name|LoggingLevel
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
name|builder
operator|.
name|xml
operator|.
name|XPathBuilder
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
name|ModelCamelContext
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
name|ExchangePropertyExpression
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
name|HeaderExpression
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
name|JsonPathExpression
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Base class for implementation inheritance for different clauses in the<a  * href="http://camel.apache.org/dsl.html">Java DSL</a>  *  * @version  */
end_comment

begin_class
DECL|class|BuilderSupport
specifier|public
specifier|abstract
class|class
name|BuilderSupport
block|{
DECL|field|context
specifier|private
name|ModelCamelContext
name|context
decl_stmt|;
DECL|field|errorHandlerBuilder
specifier|private
name|ErrorHandlerBuilder
name|errorHandlerBuilder
decl_stmt|;
DECL|method|BuilderSupport ()
specifier|protected
name|BuilderSupport
parameter_list|()
block|{     }
DECL|method|BuilderSupport (CamelContext context)
specifier|protected
name|BuilderSupport
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
operator|!=
literal|null
condition|?
name|context
operator|.
name|adapt
argument_list|(
name|ModelCamelContext
operator|.
name|class
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
comment|// Builder methods
comment|// -------------------------------------------------------------------------
comment|/**      * Returns a value builder for the given header      */
DECL|method|header (String name)
specifier|public
name|ValueBuilder
name|header
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Expression
name|exp
init|=
operator|new
name|HeaderExpression
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      *      * Returns a value builder for the given exchange property      * @deprecated use {@link #exchangeProperty(String)} instead      */
annotation|@
name|Deprecated
DECL|method|property (String name)
specifier|public
name|ValueBuilder
name|property
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Expression
name|exp
init|=
operator|new
name|ExchangePropertyExpression
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * Returns a value builder for the given exchange property      */
DECL|method|exchangeProperty (String name)
specifier|public
name|ValueBuilder
name|exchangeProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Expression
name|exp
init|=
operator|new
name|ExchangePropertyExpression
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the inbound body on an exchange      */
DECL|method|body ()
specifier|public
name|ValueBuilder
name|body
parameter_list|()
block|{
return|return
name|Builder
operator|.
name|body
argument_list|()
return|;
block|}
comment|/**      * Returns a predicate and value builder for the inbound message body as a      * specific type      *      * @deprecated use {@link #bodyAs(Class)}      */
annotation|@
name|Deprecated
DECL|method|body (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ValueBuilder
name|body
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|bodyAs
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the inbound message body as a      * specific type      */
DECL|method|bodyAs (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ValueBuilder
name|bodyAs
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|bodyAs
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the outbound body on an      * exchange      *      * @deprecated use {@link #body()}      */
annotation|@
name|Deprecated
DECL|method|outBody ()
specifier|public
name|ValueBuilder
name|outBody
parameter_list|()
block|{
return|return
name|Builder
operator|.
name|outBody
argument_list|()
return|;
block|}
comment|/**      * Returns a predicate and value builder for the outbound message body as a      * specific type      *      * @deprecated use {@link #bodyAs(Class)}      */
annotation|@
name|Deprecated
DECL|method|outBody (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ValueBuilder
name|outBody
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|outBodyAs
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the fault body on an      * exchange      */
DECL|method|faultBody ()
specifier|public
name|ValueBuilder
name|faultBody
parameter_list|()
block|{
return|return
name|Builder
operator|.
name|faultBody
argument_list|()
return|;
block|}
comment|/**      * Returns a predicate and value builder for the fault message body as a      * specific type      *      * @deprecated use {@link #bodyAs(Class)}      */
annotation|@
name|Deprecated
DECL|method|faultBodyAs (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ValueBuilder
name|faultBodyAs
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|faultBodyAs
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**      * Returns a value builder for the given system property      */
DECL|method|systemProperty (String name)
specifier|public
name|ValueBuilder
name|systemProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|systemProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Returns a value builder for the given system property      */
DECL|method|systemProperty (String name, String defaultValue)
specifier|public
name|ValueBuilder
name|systemProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|systemProperty
argument_list|(
name|name
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
comment|/**      * Returns a constant expression value builder      */
DECL|method|constant (Object value)
specifier|public
name|ValueBuilder
name|constant
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|constant
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * Returns a JSonPath expression value builder      */
DECL|method|jsonpath (String value)
specifier|public
name|ValueBuilder
name|jsonpath
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|JsonPathExpression
name|exp
init|=
operator|new
name|JsonPathExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * Returns a JSonPath expression value builder      *      * @param value      The JSonPath expression      * @param resultType The result type that the JSonPath expression will return.      */
DECL|method|jsonpath (String value, Class<?> resultType)
specifier|public
name|ValueBuilder
name|jsonpath
parameter_list|(
name|String
name|value
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
name|JsonPathExpression
name|exp
init|=
operator|new
name|JsonPathExpression
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|exp
operator|.
name|setResultType
argument_list|(
name|resultType
argument_list|)
expr_stmt|;
return|return
operator|new
name|ValueBuilder
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * Returns a language expression value builder      */
DECL|method|language (String language, String expression)
specifier|public
name|ValueBuilder
name|language
parameter_list|(
name|String
name|language
parameter_list|,
name|String
name|expression
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|language
argument_list|(
name|language
argument_list|,
name|expression
argument_list|)
return|;
block|}
comment|/**      * Returns a simple expression value builder      */
DECL|method|simple (String value)
specifier|public
name|SimpleBuilder
name|simple
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|SimpleBuilder
operator|.
name|simple
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * Returns a simple expression value builder      */
DECL|method|simple (String value, Class<?> resultType)
specifier|public
name|SimpleBuilder
name|simple
parameter_list|(
name|String
name|value
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
return|return
name|SimpleBuilder
operator|.
name|simple
argument_list|(
name|value
argument_list|,
name|resultType
argument_list|)
return|;
block|}
comment|/**      * Returns a simple expression value builder, using String.format style      */
DECL|method|simpleF (String format, Object...values)
specifier|public
name|SimpleBuilder
name|simpleF
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
return|return
name|SimpleBuilder
operator|.
name|simpleF
argument_list|(
name|format
argument_list|,
name|values
argument_list|)
return|;
block|}
comment|/**      * Returns a simple expression value builder, using String.format style      */
DECL|method|simpleF (String format, Class<?> resultType, Object...values)
specifier|public
name|SimpleBuilder
name|simpleF
parameter_list|(
name|String
name|format
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
return|return
name|SimpleBuilder
operator|.
name|simpleF
argument_list|(
name|format
argument_list|,
name|resultType
argument_list|,
name|values
argument_list|)
return|;
block|}
comment|/**      * Returns a xpath expression value builder      *      * @param value the XPath expression      * @return the builder      */
DECL|method|xpath (String value)
specifier|public
name|XPathBuilder
name|xpath
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|XPathBuilder
operator|.
name|xpath
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * Returns a xpath expression value builder      *      * @param value      the XPath expression      * @param resultType the result type that the XPath expression will return.      * @return the builder      */
DECL|method|xpath (String value, Class<?> resultType)
specifier|public
specifier|static
name|XPathBuilder
name|xpath
parameter_list|(
name|String
name|value
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
return|return
name|XPathBuilder
operator|.
name|xpath
argument_list|(
name|value
argument_list|,
name|resultType
argument_list|)
return|;
block|}
comment|/**      * Returns a<a href="http://camel.apache.org/bean-language.html">method call expression</a>      * value builder      *<p/>      * This method accepts dual parameters. Either an bean instance or a reference to a bean (String).      *      * @param beanOrBeanRef  either an instanceof a bean or a reference to bean to lookup in the Registry      * @return the builder      * @deprecated use {@link #method(Object)} instead      */
annotation|@
name|Deprecated
DECL|method|bean (Object beanOrBeanRef)
specifier|public
name|ValueBuilder
name|bean
parameter_list|(
name|Object
name|beanOrBeanRef
parameter_list|)
block|{
return|return
name|bean
argument_list|(
name|beanOrBeanRef
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns a<a href="http://camel.apache.org/bean-language.html">method call expression</a>      * value builder      *<p/>      * This method accepts dual parameters. Either an bean instance or a reference to a bean (String).      *      * @param beanOrBeanRef  either an instanceof a bean or a reference to bean to lookup in the Registry      * @param method   name of method to invoke      * @return the builder      * @deprecated use {@link #method(Object, String)} instead      */
annotation|@
name|Deprecated
DECL|method|bean (Object beanOrBeanRef, String method)
specifier|public
name|ValueBuilder
name|bean
parameter_list|(
name|Object
name|beanOrBeanRef
parameter_list|,
name|String
name|method
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|bean
argument_list|(
name|beanOrBeanRef
argument_list|,
name|method
argument_list|)
return|;
block|}
comment|/**      * Returns a<a href="http://camel.apache.org/bean-language.html">method call expression</a>      * value builder      *      * @param beanType the Class of the bean which we want to invoke      * @return the builder      * @deprecated use {@link #method(Class)} instead      */
annotation|@
name|Deprecated
DECL|method|bean (Class<?> beanType)
specifier|public
name|ValueBuilder
name|bean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanType
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|bean
argument_list|(
name|beanType
argument_list|)
return|;
block|}
comment|/**      * Returns a<a href="http://camel.apache.org/bean-language.html">method call expression</a>      * value builder      *      * @param beanType the Class of the bean which we want to invoke      * @param method   name of method to invoke      * @return the builder      * @deprecated use {@link #method(Class, String)} instead      */
annotation|@
name|Deprecated
DECL|method|bean (Class<?> beanType, String method)
specifier|public
name|ValueBuilder
name|bean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanType
parameter_list|,
name|String
name|method
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|bean
argument_list|(
name|beanType
argument_list|,
name|method
argument_list|)
return|;
block|}
comment|/**      * Returns a<a href="http://camel.apache.org/bean-language.html">method call expression</a>      * value builder      *<p/>      * This method accepts dual parameters. Either an bean instance or a reference to a bean (String).      *      * @param beanOrBeanRef  either an instanceof a bean or a reference to bean to lookup in the Registry      * @return the builder      */
DECL|method|method (Object beanOrBeanRef)
specifier|public
name|ValueBuilder
name|method
parameter_list|(
name|Object
name|beanOrBeanRef
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|beanOrBeanRef
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns a<a href="http://camel.apache.org/bean-language.html">method call expression</a>      * value builder      *<p/>      * This method accepts dual parameters. Either an bean instance or a reference to a bean (String).      *      * @param beanOrBeanRef  either an instanceof a bean or a reference to bean to lookup in the Registry      * @param method   name of method to invoke      * @return the builder      */
DECL|method|method (Object beanOrBeanRef, String method)
specifier|public
name|ValueBuilder
name|method
parameter_list|(
name|Object
name|beanOrBeanRef
parameter_list|,
name|String
name|method
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|bean
argument_list|(
name|beanOrBeanRef
argument_list|,
name|method
argument_list|)
return|;
block|}
comment|/**      * Returns a<a href="http://camel.apache.org/bean-language.html">method call expression</a>      * value builder      *      * @param beanType the Class of the bean which we want to invoke      * @return the builder      */
DECL|method|method (Class<?> beanType)
specifier|public
name|ValueBuilder
name|method
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanType
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|bean
argument_list|(
name|beanType
argument_list|)
return|;
block|}
comment|/**      * Returns a<a href="http://camel.apache.org/bean-language.html">method call expression</a>      * value builder      *      * @param beanType the Class of the bean which we want to invoke      * @param method   name of method to invoke      * @return the builder      */
DECL|method|method (Class<?> beanType, String method)
specifier|public
name|ValueBuilder
name|method
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanType
parameter_list|,
name|String
name|method
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|bean
argument_list|(
name|beanType
argument_list|,
name|method
argument_list|)
return|;
block|}
comment|/**      * Returns an expression processing the exchange to the given endpoint uri      *      * @param uri endpoint uri to send the exchange to      * @return the builder      * @deprecated not in use, and not available in XML DSL      */
annotation|@
name|Deprecated
DECL|method|sendTo (String uri)
specifier|public
name|ValueBuilder
name|sendTo
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|sendTo
argument_list|(
name|uri
argument_list|)
return|;
block|}
comment|/**      * Returns an expression value builder that replaces all occurrences of the      * regular expression with the given replacement      */
DECL|method|regexReplaceAll (Expression content, String regex, String replacement)
specifier|public
name|ValueBuilder
name|regexReplaceAll
parameter_list|(
name|Expression
name|content
parameter_list|,
name|String
name|regex
parameter_list|,
name|String
name|replacement
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|regexReplaceAll
argument_list|(
name|content
argument_list|,
name|regex
argument_list|,
name|replacement
argument_list|)
return|;
block|}
comment|/**      * Returns an expression value builder that replaces all occurrences of the      * regular expression with the given replacement      */
DECL|method|regexReplaceAll (Expression content, String regex, Expression replacement)
specifier|public
name|ValueBuilder
name|regexReplaceAll
parameter_list|(
name|Expression
name|content
parameter_list|,
name|String
name|regex
parameter_list|,
name|Expression
name|replacement
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|regexReplaceAll
argument_list|(
name|content
argument_list|,
name|regex
argument_list|,
name|replacement
argument_list|)
return|;
block|}
comment|/**      * Returns a exception expression value builder      */
DECL|method|exceptionMessage ()
specifier|public
name|ValueBuilder
name|exceptionMessage
parameter_list|()
block|{
return|return
name|Builder
operator|.
name|exceptionMessage
argument_list|()
return|;
block|}
comment|/**      * Resolves the given URI to an endpoint      *      * @param uri  the uri to resolve      * @throws NoSuchEndpointException if the endpoint URI could not be resolved      * @return the endpoint      */
DECL|method|endpoint (String uri)
specifier|public
name|Endpoint
name|endpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|NoSuchEndpointException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|uri
argument_list|,
literal|"uri"
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|getContext
argument_list|()
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
return|return
name|endpoint
return|;
block|}
comment|/**      * Resolves the given URI to an endpoint of the specified type      *      * @param uri  the uri to resolve      * @param type the excepted type of the endpoint      * @throws NoSuchEndpointException if the endpoint URI could not be resolved      * @return the endpoint      */
DECL|method|endpoint (String uri, Class<T> type)
specifier|public
parameter_list|<
name|T
extends|extends
name|Endpoint
parameter_list|>
name|T
name|endpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|NoSuchEndpointException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|uri
argument_list|,
literal|"uri"
argument_list|)
expr_stmt|;
name|T
name|endpoint
init|=
name|getContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|,
name|type
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
return|return
name|endpoint
return|;
block|}
comment|/**      * Resolves the list of URIs into a list of {@link Endpoint} instances      *      * @param uris  list of endpoints to resolve      * @throws NoSuchEndpointException if an endpoint URI could not be resolved      * @return list of endpoints      */
DECL|method|endpoints (String... uris)
specifier|public
name|List
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
parameter_list|(
name|String
modifier|...
name|uris
parameter_list|)
throws|throws
name|NoSuchEndpointException
block|{
name|List
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|uri
range|:
name|uris
control|)
block|{
name|endpoints
operator|.
name|add
argument_list|(
name|endpoint
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoints
return|;
block|}
comment|/**      * Helper method to create a list of {@link Endpoint} instances      *      * @param endpoints  endpoints      * @return list of the given endpoints      */
DECL|method|endpoints (Endpoint... endpoints)
specifier|public
name|List
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
parameter_list|(
name|Endpoint
modifier|...
name|endpoints
parameter_list|)
block|{
name|List
argument_list|<
name|Endpoint
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|endpoints
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates a default<a href="http://camel.apache.org/error-handler.html">error handler</a>.      *      * @return the builder      */
DECL|method|defaultErrorHandler ()
specifier|public
name|DefaultErrorHandlerBuilder
name|defaultErrorHandler
parameter_list|()
block|{
return|return
operator|new
name|DefaultErrorHandlerBuilder
argument_list|()
return|;
block|}
comment|/**      * Creates a disabled<a href="http://camel.apache.org/error-handler.html">error handler</a>      * for removing the default error handler      *      * @return the builder      */
DECL|method|noErrorHandler ()
specifier|public
name|NoErrorHandlerBuilder
name|noErrorHandler
parameter_list|()
block|{
return|return
operator|new
name|NoErrorHandlerBuilder
argument_list|()
return|;
block|}
comment|/**      * Creates an<a href="http://camel.apache.org/error-handler.html">error handler</a>      * which just logs errors      *      * @return the builder      * @deprecated use dead letter channel with a log endpoint      */
annotation|@
name|Deprecated
DECL|method|loggingErrorHandler ()
specifier|public
name|LoggingErrorHandlerBuilder
name|loggingErrorHandler
parameter_list|()
block|{
return|return
operator|new
name|LoggingErrorHandlerBuilder
argument_list|()
return|;
block|}
comment|/**      * Creates an<a href="http://camel.apache.org/error-handler.html">error handler</a>      * which just logs errors      *      * @return the builder      * @deprecated use dead letter channel with a log endpoint      */
annotation|@
name|Deprecated
DECL|method|loggingErrorHandler (String log)
specifier|public
name|LoggingErrorHandlerBuilder
name|loggingErrorHandler
parameter_list|(
name|String
name|log
parameter_list|)
block|{
return|return
name|loggingErrorHandler
argument_list|(
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|log
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates an<a href="http://camel.apache.org/error-handler.html">error handler</a>      * which just logs errors      *      * @return the builder      * @deprecated use dead letter channel with a log endpoint      */
annotation|@
name|Deprecated
DECL|method|loggingErrorHandler (Logger log)
specifier|public
name|LoggingErrorHandlerBuilder
name|loggingErrorHandler
parameter_list|(
name|Logger
name|log
parameter_list|)
block|{
return|return
operator|new
name|LoggingErrorHandlerBuilder
argument_list|(
name|log
argument_list|)
return|;
block|}
comment|/**      * Creates an<a href="http://camel.apache.org/error-handler.html">error handler</a>      * which just logs errors      *      * @return the builder      * @deprecated use dead letter channel with a log endpoint      */
annotation|@
name|Deprecated
DECL|method|loggingErrorHandler (Logger log, LoggingLevel level)
specifier|public
name|LoggingErrorHandlerBuilder
name|loggingErrorHandler
parameter_list|(
name|Logger
name|log
parameter_list|,
name|LoggingLevel
name|level
parameter_list|)
block|{
return|return
operator|new
name|LoggingErrorHandlerBuilder
argument_list|(
name|log
argument_list|,
name|level
argument_list|)
return|;
block|}
comment|/**      *<a href="http://camel.apache.org/dead-letter-channel.html">Dead Letter Channel EIP:</a>      * is a error handler for handling messages that could not be delivered to it's intended destination.      *      * @param deadLetterUri  uri to the dead letter endpoint storing dead messages      * @return the builder      */
DECL|method|deadLetterChannel (String deadLetterUri)
specifier|public
name|DeadLetterChannelBuilder
name|deadLetterChannel
parameter_list|(
name|String
name|deadLetterUri
parameter_list|)
block|{
return|return
name|deadLetterChannel
argument_list|(
name|endpoint
argument_list|(
name|deadLetterUri
argument_list|)
argument_list|)
return|;
block|}
comment|/**      *<a href="http://camel.apache.org/dead-letter-channel.html">Dead Letter Channel EIP:</a>      * is a error handler for handling messages that could not be delivered to it's intended destination.      *      * @param deadLetterEndpoint  dead letter endpoint storing dead messages      * @return the builder      */
DECL|method|deadLetterChannel (Endpoint deadLetterEndpoint)
specifier|public
name|DeadLetterChannelBuilder
name|deadLetterChannel
parameter_list|(
name|Endpoint
name|deadLetterEndpoint
parameter_list|)
block|{
return|return
operator|new
name|DeadLetterChannelBuilder
argument_list|(
name|deadLetterEndpoint
argument_list|)
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getContext ()
specifier|public
name|ModelCamelContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|setContext (CamelContext context)
specifier|public
name|void
name|setContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|context
argument_list|,
literal|"CamelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
operator|.
name|adapt
argument_list|(
name|ModelCamelContext
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|setContext (ModelCamelContext context)
specifier|public
name|void
name|setContext
parameter_list|(
name|ModelCamelContext
name|context
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|context
argument_list|,
literal|"CamelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
DECL|method|getErrorHandlerBuilder ()
specifier|public
name|ErrorHandlerBuilder
name|getErrorHandlerBuilder
parameter_list|()
block|{
if|if
condition|(
name|errorHandlerBuilder
operator|==
literal|null
condition|)
block|{
name|errorHandlerBuilder
operator|=
name|createErrorHandlerBuilder
argument_list|()
expr_stmt|;
block|}
return|return
name|errorHandlerBuilder
return|;
block|}
DECL|method|createErrorHandlerBuilder ()
specifier|protected
name|ErrorHandlerBuilder
name|createErrorHandlerBuilder
parameter_list|()
block|{
return|return
operator|new
name|DefaultErrorHandlerBuilder
argument_list|()
return|;
block|}
comment|/**      * Sets the error handler to use with processors created by this builder      */
DECL|method|setErrorHandlerBuilder (ErrorHandlerBuilder errorHandlerBuilder)
specifier|public
name|void
name|setErrorHandlerBuilder
parameter_list|(
name|ErrorHandlerBuilder
name|errorHandlerBuilder
parameter_list|)
block|{
name|this
operator|.
name|errorHandlerBuilder
operator|=
name|errorHandlerBuilder
expr_stmt|;
block|}
block|}
end_class

end_unit

