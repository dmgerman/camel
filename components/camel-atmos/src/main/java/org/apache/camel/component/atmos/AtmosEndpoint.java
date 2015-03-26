begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atmos
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atmos
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
name|atmos
operator|.
name|integration
operator|.
name|consumer
operator|.
name|AtmosScheduledPollConsumer
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
name|atmos
operator|.
name|integration
operator|.
name|consumer
operator|.
name|AtmosScheduledPollGetConsumer
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
name|atmos
operator|.
name|integration
operator|.
name|producer
operator|.
name|AtmosDelProducer
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
name|atmos
operator|.
name|integration
operator|.
name|producer
operator|.
name|AtmosGetProducer
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
name|atmos
operator|.
name|integration
operator|.
name|producer
operator|.
name|AtmosMoveProducer
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
name|atmos
operator|.
name|integration
operator|.
name|producer
operator|.
name|AtmosPutProducer
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
name|atmos
operator|.
name|util
operator|.
name|AtmosException
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
name|atmos
operator|.
name|util
operator|.
name|AtmosOperation
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|component
operator|.
name|atmos
operator|.
name|util
operator|.
name|AtmosConstants
operator|.
name|POLL_CONSUMER_DELAY
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"atmos"
argument_list|,
name|title
operator|=
literal|"Atmos"
argument_list|,
name|syntax
operator|=
literal|"atmos:name/operation"
argument_list|,
name|consumerClass
operator|=
name|AtmosScheduledPollConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"file,cloud"
argument_list|)
DECL|class|AtmosEndpoint
specifier|public
class|class
name|AtmosEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AtmosEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|AtmosConfiguration
name|configuration
decl_stmt|;
DECL|method|AtmosEndpoint ()
specifier|public
name|AtmosEndpoint
parameter_list|()
block|{     }
DECL|method|AtmosEndpoint (String uri, AtmosComponent component, AtmosConfiguration configuration)
specifier|public
name|AtmosEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|AtmosComponent
name|component
parameter_list|,
name|AtmosConfiguration
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
DECL|method|AtmosEndpoint (String endpointUri)
specifier|public
name|AtmosEndpoint
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
DECL|method|getConfiguration ()
specifier|public
name|AtmosConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (AtmosConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|AtmosConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
comment|/**      * Create one of the camel producer available based on the configuration      *      * @return the camel producer      * @throws Exception      */
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"resolve producer atmos endpoint {"
operator|+
name|configuration
operator|.
name|getOperation
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"}"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"resolve producer atmos attached client: "
operator|+
name|configuration
operator|.
name|getClient
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getOperation
argument_list|()
operator|==
name|AtmosOperation
operator|.
name|put
condition|)
block|{
return|return
operator|new
name|AtmosPutProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|.
name|configuration
operator|.
name|getOperation
argument_list|()
operator|==
name|AtmosOperation
operator|.
name|del
condition|)
block|{
return|return
operator|new
name|AtmosDelProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|.
name|configuration
operator|.
name|getOperation
argument_list|()
operator|==
name|AtmosOperation
operator|.
name|get
condition|)
block|{
return|return
operator|new
name|AtmosGetProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|.
name|configuration
operator|.
name|getOperation
argument_list|()
operator|==
name|AtmosOperation
operator|.
name|move
condition|)
block|{
return|return
operator|new
name|AtmosMoveProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|AtmosException
argument_list|(
literal|"operation specified is not valid for producer!"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Create one of the camel consumer available based on the configuration      *      * @param processor the given processor      * @return the camel consumer      * @throws Exception      */
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"resolve consumer atmos endpoint {"
operator|+
name|configuration
operator|.
name|getOperation
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"}"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"resolve consumer atmos attached client:"
operator|+
name|configuration
operator|.
name|getClient
argument_list|()
argument_list|)
expr_stmt|;
name|AtmosScheduledPollConsumer
name|consumer
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|configuration
operator|.
name|getOperation
argument_list|()
operator|==
name|AtmosOperation
operator|.
name|get
condition|)
block|{
name|consumer
operator|=
operator|new
name|AtmosScheduledPollGetConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setDelay
argument_list|(
name|POLL_CONSUMER_DELAY
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|AtmosException
argument_list|(
literal|"operation specified is not valid for consumer!"
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
block|}
end_class

end_unit

