begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.freemarker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|freemarker
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|freemarker
operator|.
name|cache
operator|.
name|NullCacheStorage
import|;
end_import

begin_import
import|import
name|freemarker
operator|.
name|cache
operator|.
name|URLTemplateLoader
import|;
end_import

begin_import
import|import
name|freemarker
operator|.
name|template
operator|.
name|Configuration
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
name|annotations
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
name|support
operator|.
name|DefaultComponent
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
name|support
operator|.
name|ResourceHelper
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
comment|/**  * Freemarker component.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"freemarker"
argument_list|)
DECL|class|FreemarkerComponent
specifier|public
class|class
name|FreemarkerComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|Configuration
name|configuration
decl_stmt|;
DECL|field|noCacheConfiguration
specifier|private
name|Configuration
name|noCacheConfiguration
decl_stmt|;
DECL|method|FreemarkerComponent ()
specifier|public
name|FreemarkerComponent
parameter_list|()
block|{     }
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
comment|// should we use regular configuration or no cache (content cache is default true)
name|Configuration
name|config
decl_stmt|;
name|String
name|encoding
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"encoding"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|cache
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"contentCache"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
decl_stmt|;
name|int
name|templateUpdateDelay
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"templateUpdateDelay"
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
condition|)
block|{
name|config
operator|=
name|getConfiguration
argument_list|()
expr_stmt|;
if|if
condition|(
name|templateUpdateDelay
operator|>
literal|0
condition|)
block|{
name|config
operator|.
name|setTemplateUpdateDelay
argument_list|(
name|templateUpdateDelay
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|config
operator|=
name|getNoCacheConfiguration
argument_list|()
expr_stmt|;
block|}
name|FreemarkerEndpoint
name|endpoint
init|=
operator|new
name|FreemarkerEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
name|endpoint
operator|.
name|setEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|.
name|setContentCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setConfiguration
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setTemplateUpdateDelay
argument_list|(
name|templateUpdateDelay
argument_list|)
expr_stmt|;
comment|// if its a http resource then append any remaining parameters and update the resource uri
if|if
condition|(
name|ResourceHelper
operator|.
name|isHttpUri
argument_list|(
name|remaining
argument_list|)
condition|)
block|{
name|remaining
operator|=
name|ResourceHelper
operator|.
name|appendParameters
argument_list|(
name|remaining
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setResourceUri
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
specifier|synchronized
name|Configuration
name|getConfiguration
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
operator|new
name|Configuration
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|setTemplateLoader
argument_list|(
operator|new
name|URLTemplateLoader
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|URL
name|getURL
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
return|return
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsUrl
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// freemarker prefers to ask for locale first (eg xxx_en_GB, xxX_en), and then fallback without locale
comment|// so we should return null to signal the resource could not be found
return|return
literal|null
return|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|Configuration
operator|)
name|configuration
operator|.
name|clone
argument_list|()
return|;
block|}
comment|/**      * To use an existing {@link freemarker.template.Configuration} instance as the configuration.      */
DECL|method|setConfiguration (Configuration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getNoCacheConfiguration ()
specifier|private
specifier|synchronized
name|Configuration
name|getNoCacheConfiguration
parameter_list|()
block|{
if|if
condition|(
name|noCacheConfiguration
operator|==
literal|null
condition|)
block|{
comment|// create a clone of the regular configuration
name|noCacheConfiguration
operator|=
operator|(
name|Configuration
operator|)
name|getConfiguration
argument_list|()
operator|.
name|clone
argument_list|()
expr_stmt|;
comment|// set this one to not use cache
name|noCacheConfiguration
operator|.
name|setCacheStorage
argument_list|(
operator|new
name|NullCacheStorage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|noCacheConfiguration
return|;
block|}
block|}
end_class

end_unit

