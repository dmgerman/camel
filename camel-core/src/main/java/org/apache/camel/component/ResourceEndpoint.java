begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|mbean
operator|.
name|ManagedResourceEndpointMBean
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
name|converter
operator|.
name|IOConverter
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
name|ProcessorEndpoint
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
name|ResourceHelper
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
comment|/**  * A useful base class for endpoints which depend on a resource  * such as things like Velocity or XQuery based components.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed ResourceEndpoint"
argument_list|)
DECL|class|ResourceEndpoint
specifier|public
specifier|abstract
class|class
name|ResourceEndpoint
extends|extends
name|ProcessorEndpoint
implements|implements
name|ManagedResourceEndpointMBean
block|{
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|buffer
specifier|private
specifier|volatile
name|byte
index|[]
name|buffer
decl_stmt|;
DECL|field|resourceUri
specifier|private
name|String
name|resourceUri
decl_stmt|;
annotation|@
name|UriParam
DECL|field|contentCache
specifier|private
name|boolean
name|contentCache
decl_stmt|;
DECL|method|ResourceEndpoint ()
specifier|public
name|ResourceEndpoint
parameter_list|()
block|{     }
DECL|method|ResourceEndpoint (String endpointUri, Component component, String resourceUri)
specifier|public
name|ResourceEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
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
argument_list|)
expr_stmt|;
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
block|}
comment|/**      * Gets the resource as an input stream considering the cache flag as well.      *<p/>      * If cache is enabled then the resource content is cached in an internal buffer and this content is      * returned to avoid loading the resource over and over again.      *      * @return the input stream      * @throws IOException is thrown if error loading the content of the resource to the local cache buffer      */
DECL|method|getResourceAsInputStream ()
specifier|public
name|InputStream
name|getResourceAsInputStream
parameter_list|()
throws|throws
name|IOException
block|{
comment|// try to get the resource input stream
name|InputStream
name|is
decl_stmt|;
if|if
condition|(
name|isContentCache
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|buffer
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Reading resource: {} into the content cache"
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
name|is
operator|=
name|getResourceAsInputStreamWithoutCache
argument_list|()
expr_stmt|;
name|buffer
operator|=
name|IOConverter
operator|.
name|toBytes
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|,
name|resourceUri
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Using resource: {} from the content cache"
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|buffer
argument_list|)
return|;
block|}
return|return
name|getResourceAsInputStreamWithoutCache
argument_list|()
return|;
block|}
DECL|method|getResourceAsInputStreamWithoutCache ()
specifier|protected
name|InputStream
name|getResourceAsInputStreamWithoutCache
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|loadResource
argument_list|(
name|resourceUri
argument_list|)
return|;
block|}
comment|/**      * Loads the given resource.      *      * @param uri uri of the resource.      * @return the loaded resource      * @throws IOException is thrown if resource is not found or cannot be loaded      */
DECL|method|loadResource (String uri)
specifier|protected
name|InputStream
name|loadResource
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|uri
argument_list|)
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether the resource is cached"
argument_list|)
DECL|method|isContentCache ()
specifier|public
name|boolean
name|isContentCache
parameter_list|()
block|{
return|return
name|contentCache
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Clears the cached resource, forcing to re-load the resource on next request"
argument_list|)
DECL|method|clearContentCache ()
specifier|public
name|void
name|clearContentCache
parameter_list|()
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Clearing resource: {} from the content cache"
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
name|buffer
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|isContentCacheCleared ()
specifier|public
name|boolean
name|isContentCacheCleared
parameter_list|()
block|{
return|return
name|buffer
operator|==
literal|null
return|;
block|}
comment|/**      * Sets whether to use resource content cache or not - default is<tt>false</tt>.      *      * @see #getResourceAsInputStream()      */
DECL|method|setContentCache (boolean contentCache)
specifier|public
name|void
name|setContentCache
parameter_list|(
name|boolean
name|contentCache
parameter_list|)
block|{
name|this
operator|.
name|contentCache
operator|=
name|contentCache
expr_stmt|;
block|}
DECL|method|getResourceUri ()
specifier|public
name|String
name|getResourceUri
parameter_list|()
block|{
return|return
name|resourceUri
return|;
block|}
DECL|method|setResourceUri (String resourceUri)
specifier|public
name|void
name|setResourceUri
parameter_list|(
name|String
name|resourceUri
parameter_list|)
block|{
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
block|}
block|}
end_class

end_unit

