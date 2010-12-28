begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.routebox
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|BlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|LinkedBlockingQueue
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
name|Exchange
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
name|direct
operator|.
name|RouteboxDirectEndpoint
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
name|seda
operator|.
name|RouteboxSedaEndpoint
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
name|DefaultComponent
import|;
end_import

begin_class
DECL|class|RouteboxComponent
specifier|public
class|class
name|RouteboxComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|config
specifier|final
name|RouteboxConfiguration
name|config
decl_stmt|;
DECL|field|queues
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|queues
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|RouteboxComponent ()
specifier|public
name|RouteboxComponent
parameter_list|()
block|{
name|config
operator|=
operator|new
name|RouteboxConfiguration
argument_list|()
expr_stmt|;
block|}
DECL|method|RouteboxComponent (CamelContext context)
specifier|public
name|RouteboxComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|config
operator|=
operator|new
name|RouteboxConfiguration
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
name|RouteboxEndpoint
name|blackboxRouteEndpoint
init|=
literal|null
decl_stmt|;
name|config
operator|.
name|parseURI
argument_list|(
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
argument_list|,
name|parameters
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getInnerProtocol
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"direct"
argument_list|)
condition|)
block|{
name|blackboxRouteEndpoint
operator|=
operator|new
name|RouteboxDirectEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|blackboxRouteEndpoint
operator|.
name|getConfig
argument_list|()
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|baseUri
init|=
name|getQueueKey
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|blackboxRouteEndpoint
operator|=
operator|new
name|RouteboxSedaEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|,
name|createQueue
argument_list|(
name|baseUri
argument_list|,
name|parameters
argument_list|)
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|blackboxRouteEndpoint
operator|.
name|getConfig
argument_list|()
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
return|return
name|blackboxRouteEndpoint
return|;
block|}
DECL|method|createQueue (String uri, Map<String, Object> parameters)
specifier|public
specifier|synchronized
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|createQueue
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
block|{
if|if
condition|(
name|queues
operator|.
name|containsKey
argument_list|(
name|uri
argument_list|)
condition|)
block|{
return|return
name|queues
operator|.
name|get
argument_list|(
name|uri
argument_list|)
return|;
block|}
comment|// create queue
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
decl_stmt|;
name|Integer
name|size
init|=
name|config
operator|.
name|getQueueSize
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|!=
literal|null
operator|&&
name|size
operator|>
literal|0
condition|)
block|{
name|queue
operator|=
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|queue
operator|=
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Exchange
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|queues
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|queue
argument_list|)
expr_stmt|;
return|return
name|queue
return|;
block|}
DECL|method|getQueueKey (String uri)
specifier|protected
name|String
name|getQueueKey
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
comment|// strip parameters
name|uri
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|uri
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|uri
return|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|queues
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

