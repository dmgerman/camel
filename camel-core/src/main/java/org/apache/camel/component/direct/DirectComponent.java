begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.direct
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|Metadata
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
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * The<a href="http://camel.apache.org/direct.html">Direct Component</a> manages {@link DirectEndpoint} and holds the list of named direct endpoints.  *  * @version  */
end_comment

begin_class
DECL|class|DirectComponent
specifier|public
class|class
name|DirectComponent
extends|extends
name|UriEndpointComponent
block|{
comment|// must keep a map of consumers on the component to ensure endpoints can lookup old consumers
comment|// later in case the DirectEndpoint was re-created due the old was evicted from the endpoints LRUCache
comment|// on DefaultCamelContext
DECL|field|consumers
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DirectConsumer
argument_list|>
name|consumers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|DirectConsumer
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|block
specifier|private
name|boolean
name|block
init|=
literal|true
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"30000"
argument_list|)
DECL|field|timeout
specifier|private
name|long
name|timeout
init|=
literal|30000L
decl_stmt|;
DECL|method|DirectComponent ()
specifier|public
name|DirectComponent
parameter_list|()
block|{
name|super
argument_list|(
name|DirectEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
name|DirectEndpoint
name|endpoint
init|=
operator|new
name|DirectEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|consumers
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setBlock
argument_list|(
name|block
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
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
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|consumers
argument_list|)
expr_stmt|;
name|consumers
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
DECL|method|isBlock ()
specifier|public
name|boolean
name|isBlock
parameter_list|()
block|{
return|return
name|block
return|;
block|}
comment|/**      * If sending a message to a direct endpoint which has no active consumer,      * then we can tell the producer to block and wait for the consumer to become active.      */
DECL|method|setBlock (boolean block)
specifier|public
name|void
name|setBlock
parameter_list|(
name|boolean
name|block
parameter_list|)
block|{
name|this
operator|.
name|block
operator|=
name|block
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
comment|/**      * The timeout value to use if block is enabled.      */
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
block|}
end_class

end_unit

