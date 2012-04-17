begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
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
name|HasId
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
name|web
operator|.
name|util
operator|.
name|UriCharactersEncoder
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|XmlRootElement
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|EndpointLink
specifier|public
class|class
name|EndpointLink
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
DECL|field|href
specifier|private
name|String
name|href
decl_stmt|;
DECL|method|EndpointLink ()
specifier|public
name|EndpointLink
parameter_list|()
block|{     }
DECL|method|EndpointLink (Endpoint endpoint)
specifier|public
name|EndpointLink
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|load
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|EndpointLink (String key, Endpoint endpoint)
specifier|public
name|EndpointLink
parameter_list|(
name|String
name|key
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|load
argument_list|(
name|key
argument_list|,
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
literal|"EndpointLink{href='"
operator|+
name|href
operator|+
literal|"' uri='"
operator|+
name|uri
operator|+
literal|"'}"
return|;
block|}
DECL|method|load (Endpoint endpoint)
specifier|public
name|void
name|load
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
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
name|this
operator|.
name|href
operator|=
name|createHref
argument_list|(
name|uri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|load (String key, Endpoint endpoint)
specifier|public
name|void
name|load
parameter_list|(
name|String
name|key
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
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
name|this
operator|.
name|href
operator|=
name|createHref
argument_list|(
name|key
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|getHref ()
specifier|public
name|String
name|getHref
parameter_list|()
block|{
return|return
name|href
return|;
block|}
DECL|method|setHref (String href)
specifier|public
name|void
name|setHref
parameter_list|(
name|String
name|href
parameter_list|)
block|{
name|this
operator|.
name|href
operator|=
name|href
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
DECL|method|createHref (String uri, Endpoint endpoint)
specifier|protected
name|String
name|createHref
parameter_list|(
name|String
name|uri
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|instanceof
name|HasId
condition|)
block|{
name|HasId
name|hasId
init|=
operator|(
name|HasId
operator|)
name|endpoint
decl_stmt|;
name|String
name|id
init|=
name|hasId
operator|.
name|getId
argument_list|()
decl_stmt|;
return|return
literal|"/endpoints/"
operator|+
name|id
return|;
block|}
else|else
block|{
comment|// must not include :// in endpoint link
comment|// TODO: might need to use org.apache.camel.util.UnsafeUriCharactersEncoder to safely encode URI for the web
return|return
literal|"/endpoints/"
operator|+
name|UriCharactersEncoder
operator|.
name|encode
argument_list|(
name|uri
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

