begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.docker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|docker
package|;
end_package

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
name|docker
operator|.
name|consumer
operator|.
name|DockerEventsConsumer
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
name|docker
operator|.
name|consumer
operator|.
name|DockerStatsConsumer
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
name|docker
operator|.
name|exception
operator|.
name|DockerException
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
name|docker
operator|.
name|producer
operator|.
name|DockerProducer
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
name|DefaultEndpoint
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
name|UriEndpoint
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
name|UriParam
import|;
end_import

begin_comment
comment|/**  * Represents a Docker endpoint.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"docker"
argument_list|,
name|title
operator|=
literal|"Docker"
argument_list|,
name|syntax
operator|=
literal|"docker:operation"
argument_list|,
name|consumerClass
operator|=
name|DockerEventsConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"container,cloud,platform"
argument_list|)
DECL|class|DockerEndpoint
specifier|public
class|class
name|DockerEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|DockerConfiguration
name|configuration
decl_stmt|;
DECL|method|DockerEndpoint ()
specifier|public
name|DockerEndpoint
parameter_list|()
block|{     }
DECL|method|DockerEndpoint (String uri, DockerComponent component, DockerConfiguration configuration)
specifier|public
name|DockerEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|DockerComponent
name|component
parameter_list|,
name|DockerConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|DockerEndpoint (String endpointUri)
specifier|public
name|DockerEndpoint
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
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|DockerOperation
name|operation
init|=
name|configuration
operator|.
name|getOperation
argument_list|()
decl_stmt|;
if|if
condition|(
name|operation
operator|!=
literal|null
operator|&&
name|operation
operator|.
name|canProduce
argument_list|()
condition|)
block|{
return|return
operator|new
name|DockerProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|DockerException
argument_list|(
name|operation
operator|+
literal|" is not a valid producer operation"
argument_list|)
throw|;
block|}
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
name|DockerOperation
name|operation
init|=
name|configuration
operator|.
name|getOperation
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|EVENTS
case|:
return|return
operator|new
name|DockerEventsConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
case|case
name|STATS
case|:
return|return
operator|new
name|DockerStatsConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|DockerException
argument_list|(
name|operation
operator|+
literal|" is not a valid consumer operation"
argument_list|)
throw|;
block|}
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
DECL|method|getConfiguration ()
specifier|public
name|DockerConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
annotation|@
name|Override
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

