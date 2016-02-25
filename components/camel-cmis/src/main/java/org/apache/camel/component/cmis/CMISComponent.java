begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Represents the component that manages {@link CMISComponent}.  */
end_comment

begin_class
DECL|class|CMISComponent
specifier|public
class|class
name|CMISComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|method|CMISComponent ()
specifier|public
name|CMISComponent
parameter_list|()
block|{
name|super
argument_list|(
name|CMISEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
name|boolean
name|queryMode
init|=
name|removeQueryMode
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
name|CMISSessionFacade
name|sessionFacade
init|=
operator|new
name|CMISSessionFacade
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|sessionFacade
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
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
operator|new
name|CMISSessionFacadeFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|CMISSessionFacade
name|create
parameter_list|()
throws|throws
name|Exception
block|{
name|CMISSessionFacade
name|sessionFacade
init|=
operator|new
name|CMISSessionFacade
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|sessionFacade
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|sessionFacade
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setQueryMode
argument_list|(
name|queryMode
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|removeQueryMode (Map<String, Object> parameters)
specifier|private
name|boolean
name|removeQueryMode
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"queryMode"
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|remove
argument_list|(
literal|"queryMode"
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

