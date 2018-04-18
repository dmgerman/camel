begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.routebox.direct
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|routebox
operator|.
name|direct
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|routebox
operator|.
name|RouteboxConfiguration
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
name|routebox
operator|.
name|RouteboxEndpoint
import|;
end_import

begin_class
DECL|class|RouteboxDirectEndpoint
specifier|public
class|class
name|RouteboxDirectEndpoint
extends|extends
name|RouteboxEndpoint
block|{
DECL|field|consumers
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|RouteboxDirectConsumer
argument_list|>
name|consumers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Deprecated
DECL|method|RouteboxDirectEndpoint (String endpointUri)
specifier|public
name|RouteboxDirectEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
DECL|method|RouteboxDirectEndpoint (String endpointUri, Component component)
specifier|public
name|RouteboxDirectEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|RouteboxDirectEndpoint (String uri, Component component, RouteboxConfiguration config)
specifier|public
name|RouteboxDirectEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|RouteboxConfiguration
name|config
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteboxDirectProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|RouteboxDirectConsumer
name|answer
init|=
operator|new
name|RouteboxDirectConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|addConsumer (RouteboxDirectConsumer consumer)
specifier|public
name|void
name|addConsumer
parameter_list|(
name|RouteboxDirectConsumer
name|consumer
parameter_list|)
block|{
name|String
name|key
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointKey
argument_list|()
decl_stmt|;
name|consumers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|removeConsumer (RouteboxDirectConsumer consumer)
specifier|public
name|void
name|removeConsumer
parameter_list|(
name|RouteboxDirectConsumer
name|consumer
parameter_list|)
block|{
name|String
name|key
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointKey
argument_list|()
decl_stmt|;
name|consumers
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
DECL|method|hasConsumer (RouteboxDirectConsumer consumer)
specifier|public
name|boolean
name|hasConsumer
parameter_list|(
name|RouteboxDirectConsumer
name|consumer
parameter_list|)
block|{
name|String
name|key
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointKey
argument_list|()
decl_stmt|;
return|return
name|consumers
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|getConsumer ()
specifier|public
name|RouteboxDirectConsumer
name|getConsumer
parameter_list|()
block|{
name|String
name|key
init|=
name|getEndpointKey
argument_list|()
decl_stmt|;
return|return
name|consumers
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
end_class

end_unit

