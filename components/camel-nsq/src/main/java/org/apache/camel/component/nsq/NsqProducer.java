begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nsq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nsq
package|;
end_package

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|brainlag
operator|.
name|nsq
operator|.
name|NSQProducer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|brainlag
operator|.
name|nsq
operator|.
name|ServerAddress
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
name|support
operator|.
name|DefaultProducer
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

begin_comment
comment|/**  * The nsq producer.  */
end_comment

begin_class
DECL|class|NsqProducer
specifier|public
class|class
name|NsqProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|NsqProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|producer
specifier|private
name|NSQProducer
name|producer
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|NsqConfiguration
name|configuration
decl_stmt|;
DECL|method|NsqProducer (NsqEndpoint endpoint)
specifier|public
name|NsqProducer
parameter_list|(
name|NsqEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpoint
operator|.
name|getNsqConfiguration
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|NsqEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|NsqEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|topic
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|NsqConstants
operator|.
name|NSQ_MESSAGE_TOPIC
argument_list|,
name|configuration
operator|.
name|getTopic
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Publishing to topic: {}"
argument_list|,
name|topic
argument_list|)
expr_stmt|;
name|byte
index|[]
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|producer
operator|.
name|produce
argument_list|(
name|topic
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting NSQ Producer"
argument_list|)
expr_stmt|;
name|NsqConfiguration
name|config
init|=
name|getEndpoint
argument_list|()
operator|.
name|getNsqConfiguration
argument_list|()
decl_stmt|;
name|producer
operator|=
operator|new
name|NSQProducer
argument_list|()
expr_stmt|;
for|for
control|(
name|ServerAddress
name|server
range|:
name|config
operator|.
name|getServerAddresses
argument_list|()
control|)
block|{
name|producer
operator|.
name|addAddress
argument_list|(
name|server
operator|.
name|getHost
argument_list|()
argument_list|,
name|server
operator|.
name|getPort
argument_list|()
operator|==
literal|0
condition|?
name|config
operator|.
name|getPort
argument_list|()
else|:
name|server
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|producer
operator|.
name|setConfig
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getNsqConfig
argument_list|()
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping NSQ Producer"
argument_list|)
expr_stmt|;
if|if
condition|(
name|producer
operator|!=
literal|null
condition|)
block|{
name|producer
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

