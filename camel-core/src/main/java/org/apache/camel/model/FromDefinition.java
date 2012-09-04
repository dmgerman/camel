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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|Required
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
comment|/**  * Represents an XML&lt;from/&gt; element  *  * @version   */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"from"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|FromDefinition
specifier|public
class|class
name|FromDefinition
extends|extends
name|OptionalIdentifiedDefinition
argument_list|<
name|FromDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ref
specifier|private
name|String
name|ref
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|method|FromDefinition ()
specifier|public
name|FromDefinition
parameter_list|()
block|{     }
DECL|method|FromDefinition (String uri)
specifier|public
name|FromDefinition
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
DECL|method|FromDefinition (Endpoint endpoint)
specifier|public
name|FromDefinition
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|setEndpoint
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
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
literal|"From["
operator|+
name|getLabel
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"from"
return|;
block|}
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
name|description
argument_list|(
name|getUri
argument_list|()
argument_list|,
name|getRef
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
return|;
block|}
DECL|method|resolveEndpoint (RouteContext context)
specifier|public
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|RouteContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
return|return
name|context
operator|.
name|resolveEndpoint
argument_list|(
name|getUri
argument_list|()
argument_list|,
name|getRef
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|endpoint
return|;
block|}
block|}
comment|// Properties
comment|// -----------------------------------------------------------------------
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
return|return
name|uri
return|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
return|return
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Sets the URI of the endpoint to use      *      * @param uri the endpoint URI to use      */
annotation|@
name|Required
DECL|method|setUri (String uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|getRef ()
specifier|public
name|String
name|getRef
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
comment|/**      * Sets the name of the endpoint within the registry (such as the Spring      * ApplicationContext or JNDI) to use      *      * @param ref the reference name to use      */
DECL|method|setRef (String ref)
specifier|public
name|void
name|setRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
comment|/**      * Gets tne endpoint if an {@link Endpoint} instance was set.      *<p/>      * This implementation may return<tt>null</tt> which means you need to use      * {@link #getRef()} or {@link #getUri()} to get information about the endpoint.      *      * @return the endpoint instance, or<tt>null</tt>      */
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|setEndpoint (Endpoint endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|uri
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|uri
operator|=
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns the endpoint URI or the name of the reference to it      */
DECL|method|getUriOrRef ()
specifier|public
name|Object
name|getUriOrRef
parameter_list|()
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|uri
argument_list|)
condition|)
block|{
return|return
name|uri
return|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
return|return
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
return|;
block|}
return|return
name|ref
return|;
block|}
comment|// Implementation methods
comment|// -----------------------------------------------------------------------
DECL|method|description (String uri, String ref, Endpoint endpoint)
specifier|protected
specifier|static
name|String
name|description
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|ref
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
return|return
literal|"ref:"
operator|+
name|ref
return|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
return|return
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
return|return
name|uri
return|;
block|}
else|else
block|{
return|return
literal|"no uri or ref supplied!"
return|;
block|}
block|}
DECL|method|clear ()
specifier|protected
name|void
name|clear
parameter_list|()
block|{
name|this
operator|.
name|endpoint
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|ref
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|uri
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

