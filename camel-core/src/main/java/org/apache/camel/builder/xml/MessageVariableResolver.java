begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathVariableResolver
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

begin_import
import|import static
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
name|Namespaces
operator|.
name|ENVIRONMENT_VARIABLES
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
name|builder
operator|.
name|xml
operator|.
name|Namespaces
operator|.
name|EXCHANGE_PROPERTY
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
name|builder
operator|.
name|xml
operator|.
name|Namespaces
operator|.
name|IN_NAMESPACE
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
name|builder
operator|.
name|xml
operator|.
name|Namespaces
operator|.
name|OUT_NAMESPACE
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
name|builder
operator|.
name|xml
operator|.
name|Namespaces
operator|.
name|SYSTEM_PROPERTIES_NAMESPACE
import|;
end_import

begin_comment
comment|/**  * A variable resolver for XPath expressions which support properties on the  * message, exchange as well as making system properties and environment  * properties available.  *<p/>  * Implementations of this resolver must be thread safe  *  * @version   */
end_comment

begin_class
DECL|class|MessageVariableResolver
specifier|public
class|class
name|MessageVariableResolver
implements|implements
name|XPathVariableResolver
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MessageVariableResolver
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|variables
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|variables
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|ThreadLocal
argument_list|<
name|Exchange
argument_list|>
name|exchange
decl_stmt|;
DECL|method|MessageVariableResolver (ThreadLocal<Exchange> exchange)
specifier|public
name|MessageVariableResolver
parameter_list|(
name|ThreadLocal
argument_list|<
name|Exchange
argument_list|>
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
DECL|method|resolveVariable (QName name)
specifier|public
name|Object
name|resolveVariable
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|String
name|uri
init|=
name|name
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|localPart
init|=
name|name
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|Object
name|answer
init|=
literal|null
decl_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|get
argument_list|()
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|==
literal|null
operator|||
name|uri
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|answer
operator|=
name|variables
operator|.
name|get
argument_list|(
name|localPart
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|Message
name|message
init|=
name|in
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|message
operator|.
name|getHeader
argument_list|(
name|localPart
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|exchange
operator|.
name|get
argument_list|()
operator|.
name|getProperty
argument_list|(
name|localPart
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|equals
argument_list|(
name|SYSTEM_PROPERTIES_NAMESPACE
argument_list|)
condition|)
block|{
try|try
block|{
name|answer
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|localPart
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Security exception evaluating system property: "
operator|+
name|localPart
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|equals
argument_list|(
name|ENVIRONMENT_VARIABLES
argument_list|)
condition|)
block|{
name|answer
operator|=
name|System
operator|.
name|getenv
argument_list|()
operator|.
name|get
argument_list|(
name|localPart
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|equals
argument_list|(
name|EXCHANGE_PROPERTY
argument_list|)
condition|)
block|{
name|answer
operator|=
name|exchange
operator|.
name|get
argument_list|()
operator|.
name|getProperty
argument_list|(
name|localPart
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|equals
argument_list|(
name|IN_NAMESPACE
argument_list|)
condition|)
block|{
name|answer
operator|=
name|in
operator|.
name|getHeader
argument_list|(
name|localPart
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|localPart
operator|.
name|equals
argument_list|(
literal|"body"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|in
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|equals
argument_list|(
name|OUT_NAMESPACE
argument_list|)
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|get
argument_list|()
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|get
argument_list|()
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|answer
operator|=
name|out
operator|.
name|getHeader
argument_list|(
name|localPart
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|localPart
operator|.
name|equals
argument_list|(
literal|"body"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|out
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|// if we can't find an answer we must return an empty String.
comment|// if we return null, then the JDK default XPathEngine will throw an exception
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
else|else
block|{
return|return
name|answer
return|;
block|}
block|}
DECL|method|addVariable (String localPart, Object value)
specifier|public
name|void
name|addVariable
parameter_list|(
name|String
name|localPart
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|variables
operator|.
name|put
argument_list|(
name|localPart
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

