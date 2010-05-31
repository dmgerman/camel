begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|xml
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
name|XmlTransient
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
name|XmlType
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
name|CamelContextAware
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
name|model
operator|.
name|IdentifiedType
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

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|AbstractCamelEndpointFactoryBean
specifier|public
specifier|abstract
class|class
name|AbstractCamelEndpointFactoryBean
extends|extends
name|IdentifiedType
implements|implements
name|CamelContextAware
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Deprecated
DECL|field|singleton
specifier|private
name|Boolean
name|singleton
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|camelContextId
specifier|private
name|String
name|camelContextId
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|method|getObject ()
specifier|public
name|Object
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|endpoint
operator|==
literal|null
operator|||
operator|!
name|endpoint
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
if|if
condition|(
name|context
operator|==
literal|null
operator|&&
name|camelContextId
operator|!=
literal|null
condition|)
block|{
name|context
operator|=
name|getCamelContextWithId
argument_list|(
name|camelContextId
argument_list|)
expr_stmt|;
block|}
name|notNull
argument_list|(
name|context
argument_list|,
literal|"context"
argument_list|)
expr_stmt|;
name|notNull
argument_list|(
name|uri
argument_list|,
literal|"uri"
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
expr_stmt|;
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
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|getCamelContextWithId (String camelContextId)
specifier|protected
specifier|abstract
name|CamelContext
name|getCamelContextWithId
parameter_list|(
name|String
name|camelContextId
parameter_list|)
function_decl|;
DECL|method|getObjectType ()
specifier|public
name|Class
name|getObjectType
parameter_list|()
block|{
return|return
name|Endpoint
operator|.
name|class
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|setSingleton (boolean singleton)
specifier|public
name|void
name|setSingleton
parameter_list|(
name|boolean
name|singleton
parameter_list|)
block|{     }
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
comment|/**      * Sets the context to use to resolve endpoints      *      * @param context the context used to resolve endpoints      */
DECL|method|setCamelContext (CamelContext context)
specifier|public
name|void
name|setCamelContext
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
expr_stmt|;
block|}
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
comment|/**      * Sets the URI to use to resolve the endpoint      *      * @param uri the URI used to set the endpoint      */
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
DECL|method|getCamelContextId ()
specifier|public
name|String
name|getCamelContextId
parameter_list|()
block|{
return|return
name|camelContextId
return|;
block|}
DECL|method|setCamelContextId (String camelContextId)
specifier|public
name|void
name|setCamelContextId
parameter_list|(
name|String
name|camelContextId
parameter_list|)
block|{
name|this
operator|.
name|camelContextId
operator|=
name|camelContextId
expr_stmt|;
block|}
block|}
end_class

end_unit

