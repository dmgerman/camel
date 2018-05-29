begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.service
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|service
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
name|cloud
operator|.
name|ServiceDefinition
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
name|cloud
operator|.
name|DefaultServiceDefinition
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

begin_class
DECL|class|ServiceParameters
specifier|public
class|class
name|ServiceParameters
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|meta
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|meta
decl_stmt|;
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|setPort (String port)
specifier|public
name|void
name|setPort
parameter_list|(
name|String
name|port
parameter_list|)
block|{
name|setPort
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getMeta ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getMeta
parameter_list|()
block|{
return|return
name|meta
return|;
block|}
DECL|method|setMeta (Map<String, String> meta)
specifier|public
name|void
name|setMeta
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|meta
parameter_list|)
block|{
name|this
operator|.
name|meta
operator|=
name|meta
expr_stmt|;
block|}
DECL|method|addMeta (String key, String value)
specifier|public
name|void
name|addMeta
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|meta
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|meta
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|meta
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|addAllMeta (Map<String, String> meta)
specifier|public
name|void
name|addAllMeta
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|meta
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|meta
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|meta
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|meta
operator|.
name|putAll
argument_list|(
name|meta
argument_list|)
expr_stmt|;
block|}
DECL|method|enrich (CamelContext context, ServiceDefinition definition)
specifier|public
name|ServiceDefinition
name|enrich
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ServiceDefinition
name|definition
parameter_list|)
block|{
specifier|final
name|DefaultServiceDefinition
operator|.
name|Builder
name|builder
init|=
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|ifNotEmpty
argument_list|(
name|definition
argument_list|,
name|builder
operator|::
name|from
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|ifNotEmpty
argument_list|(
name|id
argument_list|,
name|builder
operator|::
name|withId
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|ifNotEmpty
argument_list|(
name|name
argument_list|,
name|builder
operator|::
name|withName
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|ifNotEmpty
argument_list|(
name|meta
argument_list|,
name|builder
operator|::
name|addAllMeta
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|ifNotEmpty
argument_list|(
name|host
argument_list|,
name|builder
operator|::
name|withHost
argument_list|)
expr_stmt|;
if|if
condition|(
name|port
operator|>
literal|0
condition|)
block|{
name|builder
operator|.
name|withPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
comment|// if the service does not have an id, we can auto-generate it
if|if
condition|(
name|builder
operator|.
name|id
argument_list|()
operator|==
literal|null
condition|)
block|{
name|builder
operator|.
name|withId
argument_list|(
name|context
operator|.
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

