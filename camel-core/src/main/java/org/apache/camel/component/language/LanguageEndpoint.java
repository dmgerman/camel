begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|language
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLEncoder
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
name|Component
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
name|Consumer
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
name|Producer
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
name|RuntimeCamelException
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|component
operator|.
name|ResourceEndpoint
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
name|UriParam
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
name|IOHelper
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ResourceHelper
import|;
end_import

begin_comment
comment|/**  * Language endpoint.  *  * @version   */
end_comment

begin_class
DECL|class|LanguageEndpoint
specifier|public
class|class
name|LanguageEndpoint
extends|extends
name|ResourceEndpoint
block|{
DECL|field|language
specifier|private
name|Language
name|language
decl_stmt|;
DECL|field|expression
specifier|private
name|Expression
name|expression
decl_stmt|;
annotation|@
name|UriParam
DECL|field|languageName
specifier|private
name|String
name|languageName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|script
specifier|private
name|String
name|script
decl_stmt|;
annotation|@
name|UriParam
DECL|field|transform
specifier|private
name|boolean
name|transform
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|contentResolvedFromResource
specifier|private
name|boolean
name|contentResolvedFromResource
decl_stmt|;
annotation|@
name|UriParam
DECL|field|cacheScript
specifier|private
name|boolean
name|cacheScript
decl_stmt|;
DECL|method|LanguageEndpoint ()
specifier|public
name|LanguageEndpoint
parameter_list|()
block|{
comment|// enable cache by default
name|setContentCache
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|LanguageEndpoint (String endpointUri, Component component, Language language, Expression expression, String resourceUri)
specifier|public
name|LanguageEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|Language
name|language
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|String
name|resourceUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|language
operator|=
name|language
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
comment|// enable cache by default
name|setContentCache
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"CamelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|language
operator|==
literal|null
operator|&&
name|languageName
operator|!=
literal|null
condition|)
block|{
name|language
operator|=
name|getCamelContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
name|languageName
argument_list|)
expr_stmt|;
block|}
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|language
argument_list|,
literal|"language"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|cacheScript
operator|&&
name|expression
operator|==
literal|null
operator|&&
name|script
operator|!=
literal|null
condition|)
block|{
name|script
operator|=
name|resolveScript
argument_list|(
name|script
argument_list|)
expr_stmt|;
name|expression
operator|=
name|language
operator|.
name|createExpression
argument_list|(
name|script
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|LanguageProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot consume to a LanguageEndpoint: "
operator|+
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
block|}
comment|/**      * Resolves the script.      *      * @param script script or uri for a script to load      * @return the script      * @throws IOException is thrown if error loading the script      */
DECL|method|resolveScript (String script)
specifier|protected
name|String
name|resolveScript
parameter_list|(
name|String
name|script
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|answer
decl_stmt|;
if|if
condition|(
name|ResourceHelper
operator|.
name|hasScheme
argument_list|(
name|script
argument_list|)
condition|)
block|{
name|InputStream
name|is
init|=
name|loadResource
argument_list|(
name|script
argument_list|)
decl_stmt|;
name|answer
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|script
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
name|String
name|s
init|=
name|script
decl_stmt|;
try|try
block|{
name|s
operator|=
name|URLEncoder
operator|.
name|encode
argument_list|(
name|s
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|languageName
operator|+
literal|":"
operator|+
name|s
return|;
block|}
DECL|method|getLanguage ()
specifier|public
name|Language
name|getLanguage
parameter_list|()
block|{
return|return
name|language
return|;
block|}
DECL|method|getExpression ()
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
if|if
condition|(
name|isContentResolvedFromResource
argument_list|()
operator|&&
name|isContentCacheCleared
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|expression
return|;
block|}
DECL|method|setExpression (Expression expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|isTransform ()
specifier|public
name|boolean
name|isTransform
parameter_list|()
block|{
return|return
name|transform
return|;
block|}
comment|/**      * Whether or not the result of the script should be used as message body.      *<p/>      * This options is default<tt>true</tt>.      *      * @param transform<tt>true</tt> to use result as new message body,<tt>false</tt> to keep the existing message body      */
DECL|method|setTransform (boolean transform)
specifier|public
name|void
name|setTransform
parameter_list|(
name|boolean
name|transform
parameter_list|)
block|{
name|this
operator|.
name|transform
operator|=
name|transform
expr_stmt|;
block|}
comment|/**      * Sets the name of the language to use      *      * @param languageName the name of the language      */
DECL|method|setLanguageName (String languageName)
specifier|public
name|void
name|setLanguageName
parameter_list|(
name|String
name|languageName
parameter_list|)
block|{
name|this
operator|.
name|languageName
operator|=
name|languageName
expr_stmt|;
block|}
comment|/**      * Sets the script to execute      *      * @param script the script      */
DECL|method|setScript (String script)
specifier|public
name|void
name|setScript
parameter_list|(
name|String
name|script
parameter_list|)
block|{
name|this
operator|.
name|script
operator|=
name|script
expr_stmt|;
block|}
DECL|method|getScript ()
specifier|public
name|String
name|getScript
parameter_list|()
block|{
return|return
name|script
return|;
block|}
DECL|method|isContentResolvedFromResource ()
specifier|public
name|boolean
name|isContentResolvedFromResource
parameter_list|()
block|{
return|return
name|contentResolvedFromResource
return|;
block|}
DECL|method|setContentResolvedFromResource (boolean contentResolvedFromResource)
specifier|public
name|void
name|setContentResolvedFromResource
parameter_list|(
name|boolean
name|contentResolvedFromResource
parameter_list|)
block|{
name|this
operator|.
name|contentResolvedFromResource
operator|=
name|contentResolvedFromResource
expr_stmt|;
block|}
DECL|method|isCacheScript ()
specifier|public
name|boolean
name|isCacheScript
parameter_list|()
block|{
return|return
name|cacheScript
return|;
block|}
comment|/**      * Whether to cache the compiled script and reuse      *<p/>      * Notice reusing the script can cause side effects from processing one Camel      * {@link org.apache.camel.Exchange} to the next {@link org.apache.camel.Exchange}.      */
DECL|method|setCacheScript (boolean cacheScript)
specifier|public
name|void
name|setCacheScript
parameter_list|(
name|boolean
name|cacheScript
parameter_list|)
block|{
name|this
operator|.
name|cacheScript
operator|=
name|cacheScript
expr_stmt|;
block|}
DECL|method|clearContentCache ()
specifier|public
name|void
name|clearContentCache
parameter_list|()
block|{
name|super
operator|.
name|clearContentCache
argument_list|()
expr_stmt|;
comment|// must also clear expression and script
name|expression
operator|=
literal|null
expr_stmt|;
name|script
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

