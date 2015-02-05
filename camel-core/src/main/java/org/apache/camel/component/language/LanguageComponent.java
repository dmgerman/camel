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
name|net
operator|.
name|URLDecoder
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
name|impl
operator|.
name|UriEndpointComponent
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
comment|/**  * The<a href="http://camel.apache.org/language-component.html">Language component</a> enables sending  * {@link org.apache.camel.Exchange}s to a given language in order to have a script executed.  *  * @version   */
end_comment

begin_class
DECL|class|LanguageComponent
specifier|public
class|class
name|LanguageComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|RESOURCE
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE
init|=
literal|"resource:"
decl_stmt|;
DECL|method|LanguageComponent ()
specifier|public
name|LanguageComponent
parameter_list|()
block|{
name|super
argument_list|(
name|LanguageEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|name
init|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|String
name|script
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
comment|// no script then remaining is the language name
if|if
condition|(
name|name
operator|==
literal|null
operator|&&
name|script
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|remaining
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Illegal syntax. Name of language not given in uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|Language
name|language
init|=
name|getCamelContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|String
name|resourceUri
init|=
literal|null
decl_stmt|;
name|String
name|resource
init|=
name|script
decl_stmt|;
if|if
condition|(
name|resource
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|resource
operator|.
name|startsWith
argument_list|(
name|RESOURCE
argument_list|)
condition|)
block|{
name|resource
operator|=
name|resource
operator|.
name|substring
argument_list|(
name|RESOURCE
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ResourceHelper
operator|.
name|hasScheme
argument_list|(
name|resource
argument_list|)
condition|)
block|{
comment|// the script is a uri for a resource
name|resourceUri
operator|=
name|resource
expr_stmt|;
comment|// then the script should be null
name|script
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
comment|// the script is provided as text in the uri, so decode to utf-8
name|script
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|script
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
comment|// then the resource should be null
name|resourceUri
operator|=
literal|null
expr_stmt|;
block|}
block|}
name|LanguageEndpoint
name|endpoint
init|=
operator|new
name|LanguageEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|language
argument_list|,
literal|null
argument_list|,
name|resourceUri
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setScript
argument_list|(
name|script
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

