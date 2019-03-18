begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cmis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cmis
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

begin_comment
comment|/**  * Represents the component that manages {@link CMISComponent}.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"cmis"
argument_list|)
DECL|class|CMISComponent
specifier|public
class|class
name|CMISComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|sessionFacadeFactory
specifier|private
name|CMISSessionFacadeFactory
name|sessionFacadeFactory
decl_stmt|;
DECL|method|CMISComponent ()
specifier|public
name|CMISComponent
parameter_list|()
block|{     }
DECL|method|createEndpoint (String uri, final String remaining, final Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|remaining
parameter_list|,
specifier|final
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
name|CMISEndpoint
name|endpoint
init|=
operator|new
name|CMISEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
comment|// create a copy of parameters which we need to store on the endpoint which are in use from the session factory
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|copy
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setProperties
argument_list|(
name|copy
argument_list|)
expr_stmt|;
if|if
condition|(
name|sessionFacadeFactory
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setSessionFacadeFactory
argument_list|(
name|sessionFacadeFactory
argument_list|)
expr_stmt|;
block|}
comment|// create a dummy CMISSessionFacade which we set the properties on
comment|// so we can validate if they are all known options and fail fast if there are unknown options
name|CMISSessionFacade
name|dummy
init|=
operator|new
name|CMISSessionFacade
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|dummy
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// and the remainder options are for the endpoint
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
DECL|method|getSessionFacadeFactory ()
specifier|public
name|CMISSessionFacadeFactory
name|getSessionFacadeFactory
parameter_list|()
block|{
return|return
name|sessionFacadeFactory
return|;
block|}
comment|/**      * To use a custom CMISSessionFacadeFactory to create the CMISSessionFacade instances      */
DECL|method|setSessionFacadeFactory (CMISSessionFacadeFactory sessionFacadeFactory)
specifier|public
name|void
name|setSessionFacadeFactory
parameter_list|(
name|CMISSessionFacadeFactory
name|sessionFacadeFactory
parameter_list|)
block|{
name|this
operator|.
name|sessionFacadeFactory
operator|=
name|sessionFacadeFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

