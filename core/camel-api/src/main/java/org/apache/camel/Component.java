begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

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
name|Collections
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
name|java
operator|.
name|util
operator|.
name|Optional
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
name|extension
operator|.
name|ComponentExtension
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
name|PropertyConfigurer
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/component.html">component</a> is  * a factory of {@link Endpoint} objects.  */
end_comment

begin_interface
DECL|interface|Component
specifier|public
interface|interface
name|Component
extends|extends
name|CamelContextAware
extends|,
name|Service
block|{
comment|/**      * Attempt to resolve an endpoint for the given URI if the component is      * capable of handling the URI.      *<p/>      * See {@link #useRawUri()} for controlling whether the passed in uri      * should be as-is (raw), or encoded (default).      *       * @param uri the URI to create; either raw or encoded (default)      * @return a newly created {@link Endpoint} or null if this component cannot create      *         {@link Endpoint} instances using the given uri      * @throws Exception is thrown if error creating the endpoint      * @see #useRawUri()      */
DECL|method|createEndpoint (String uri)
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Attempt to resolve an endpoint for the given URI if the component is      * capable of handling the URI.      *<p/>      * See {@link #useRawUri()} for controlling whether the passed in uri      * should be as-is (raw), or encoded (default).      *      * @param uri the URI to create; either raw or encoded (default)      * @param parameters the parameters for the endpoint      * @return a newly created {@link Endpoint} or null if this component cannot create      *         {@link Endpoint} instances using the given uri      * @throws Exception is thrown if error creating the endpoint      * @see #useRawUri()      */
DECL|method|createEndpoint (String uri, Map<String, Object> parameters)
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
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
function_decl|;
comment|/**      * Whether to use raw or encoded uri, when creating endpoints.      *<p/>      *<b>Notice:</b> When using raw uris, then the parameter values is raw as well.      *      * @return<tt>true</tt> to use raw uris,<tt>false</tt> to use encoded uris (default).      *      * @since Camel 2.11.0      */
DECL|method|useRawUri ()
name|boolean
name|useRawUri
parameter_list|()
function_decl|;
comment|/**      * Gets the component {@link PropertyConfigurer}.      *      * @return the configurer, or<tt>null</tt> if the component does not support using property configurer.      */
DECL|method|getComponentPropertyConfigurer ()
specifier|default
name|PropertyConfigurer
name|getComponentPropertyConfigurer
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Gets the endpoint {@link PropertyConfigurer}.      *      * @return the configurer, or<tt>null</tt> if the endpoint does not support using property configurer.      */
DECL|method|getEndpointPropertyConfigurer ()
specifier|default
name|PropertyConfigurer
name|getEndpointPropertyConfigurer
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Gets a list of supported extensions.      *      * @return the list of extensions.      */
DECL|method|getSupportedExtensions ()
specifier|default
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|ComponentExtension
argument_list|>
argument_list|>
name|getSupportedExtensions
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
comment|/**      * Gets the extension of the given type.      *      * @param extensionType tye type of the extensions      * @return an optional extension      */
DECL|method|getExtension (Class<T> extensionType)
specifier|default
parameter_list|<
name|T
extends|extends
name|ComponentExtension
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|getExtension
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|extensionType
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
comment|/**      * Set the {@link Component} context if the component is an instance of {@link ComponentAware}.      */
DECL|method|trySetComponent (T object, Component component)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|trySetComponent
parameter_list|(
name|T
name|object
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|ComponentAware
condition|)
block|{
operator|(
operator|(
name|ComponentAware
operator|)
name|object
operator|)
operator|.
name|setComponent
argument_list|(
name|component
argument_list|)
expr_stmt|;
block|}
return|return
name|object
return|;
block|}
block|}
end_interface

end_unit

