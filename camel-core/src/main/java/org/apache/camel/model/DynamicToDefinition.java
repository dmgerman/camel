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
name|XmlAccessType
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
name|XmlAccessorType
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
name|XmlAttribute
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ExchangePattern
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
name|NoSuchLanguageException
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
name|processor
operator|.
name|DynamicSendProcessor
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
name|Metadata
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

begin_comment
comment|/**  * Sends the message to a dynamic endpoint (uri supports languages)  *<p/>  * You can specify multiple languages in the uri separated by the plus sign, such as<tt>mock:+xpath:/order/@uri</tt>  * where<tt>mock:</tt> would be a prefix to a xpath expression.  *<p/>  * For more dynamic behavior use<a href="http://camel.apache.org/recipient-list.html">Recipient List</a> or  *<a href="http://camel.apache.org/dynamic-router.html">Dynamic Router</a> EIP instead.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,endpoint,routing"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"toD"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|DynamicToDefinition
specifier|public
class|class
name|DynamicToDefinition
extends|extends
name|NoOutputDefinition
argument_list|<
name|DynamicToDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|pattern
specifier|private
name|ExchangePattern
name|pattern
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|cacheSize
specifier|private
name|Integer
name|cacheSize
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ignoreInvalidEndpoint
specifier|private
name|Boolean
name|ignoreInvalidEndpoint
decl_stmt|;
DECL|method|DynamicToDefinition ()
specifier|public
name|DynamicToDefinition
parameter_list|()
block|{     }
DECL|method|DynamicToDefinition (String uri)
specifier|public
name|DynamicToDefinition
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
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
name|notEmpty
argument_list|(
name|uri
argument_list|,
literal|"uri"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Expression
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Expression
argument_list|>
argument_list|()
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|uri
operator|.
name|split
argument_list|(
literal|"\\+"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
comment|// the part may have optional language to use, so you can mix languages
name|String
name|before
init|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|part
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|String
name|after
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|part
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|before
operator|!=
literal|null
operator|&&
name|after
operator|!=
literal|null
condition|)
block|{
comment|// maybe its a language
try|try
block|{
name|Language
name|partLanguage
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
name|before
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|partLanguage
operator|.
name|createExpression
argument_list|(
name|after
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|exp
argument_list|)
expr_stmt|;
continue|continue;
block|}
catch|catch
parameter_list|(
name|NoSuchLanguageException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
comment|// fallback and use simple language
name|Language
name|lan
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
name|Expression
name|exp
init|=
name|lan
operator|.
name|createExpression
argument_list|(
name|part
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|exp
argument_list|)
expr_stmt|;
block|}
name|Expression
name|exp
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|exp
operator|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exp
operator|=
name|ExpressionBuilder
operator|.
name|concatExpression
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
name|DynamicSendProcessor
name|processor
init|=
operator|new
name|DynamicSendProcessor
argument_list|(
name|uri
argument_list|,
name|exp
argument_list|)
decl_stmt|;
name|processor
operator|.
name|setPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
if|if
condition|(
name|cacheSize
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|setCacheSize
argument_list|(
name|cacheSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ignoreInvalidEndpoint
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|setIgnoreInvalidEndpoint
argument_list|(
name|ignoreInvalidEndpoint
argument_list|)
expr_stmt|;
block|}
return|return
name|processor
return|;
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
literal|"DynamicTo["
operator|+
name|getLabel
argument_list|()
operator|+
literal|"]"
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the optional {@link ExchangePattern} used to invoke this endpoint      */
DECL|method|pattern (ExchangePattern pattern)
specifier|public
name|DynamicToDefinition
name|pattern
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|setPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the maximum size used by the {@link org.apache.camel.impl.ConsumerCache} which is used to cache and reuse producers.      *      * @param cacheSize  the cache size, use<tt>0</tt> for default cache size, or<tt>-1</tt> to turn cache off.      * @return the builder      */
DECL|method|cacheSize (int cacheSize)
specifier|public
name|DynamicToDefinition
name|cacheSize
parameter_list|(
name|int
name|cacheSize
parameter_list|)
block|{
name|setCacheSize
argument_list|(
name|cacheSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Ignore the invalidate endpoint exception when try to create a producer with that endpoint      *      * @return the builder      */
DECL|method|ignoreInvalidEndpoint ()
specifier|public
name|DynamicToDefinition
name|ignoreInvalidEndpoint
parameter_list|()
block|{
name|setIgnoreInvalidEndpoint
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
comment|/**      * The uri of the endpoint to send to. The uri can be dynamic computed using the {@link org.apache.camel.language.simple.SimpleLanguage} expression.      */
DECL|method|setUri (String uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|getPattern ()
specifier|public
name|ExchangePattern
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
DECL|method|setPattern (ExchangePattern pattern)
specifier|public
name|void
name|setPattern
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
DECL|method|getCacheSize ()
specifier|public
name|Integer
name|getCacheSize
parameter_list|()
block|{
return|return
name|cacheSize
return|;
block|}
DECL|method|setCacheSize (Integer cacheSize)
specifier|public
name|void
name|setCacheSize
parameter_list|(
name|Integer
name|cacheSize
parameter_list|)
block|{
name|this
operator|.
name|cacheSize
operator|=
name|cacheSize
expr_stmt|;
block|}
DECL|method|getIgnoreInvalidEndpoint ()
specifier|public
name|Boolean
name|getIgnoreInvalidEndpoint
parameter_list|()
block|{
return|return
name|ignoreInvalidEndpoint
return|;
block|}
DECL|method|setIgnoreInvalidEndpoint (Boolean ignoreInvalidEndpoint)
specifier|public
name|void
name|setIgnoreInvalidEndpoint
parameter_list|(
name|Boolean
name|ignoreInvalidEndpoint
parameter_list|)
block|{
name|this
operator|.
name|ignoreInvalidEndpoint
operator|=
name|ignoreInvalidEndpoint
expr_stmt|;
block|}
block|}
end_class

end_unit

