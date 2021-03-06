begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.splunk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|splunk
package|;
end_package

begin_import
import|import
name|com
operator|.
name|splunk
operator|.
name|Args
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
name|splunk
operator|.
name|event
operator|.
name|SplunkEvent
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
name|splunk
operator|.
name|support
operator|.
name|DataWriter
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
name|splunk
operator|.
name|support
operator|.
name|StreamDataWriter
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
name|splunk
operator|.
name|support
operator|.
name|SubmitDataWriter
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
name|splunk
operator|.
name|support
operator|.
name|TcpDataWriter
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
comment|/**  * The Splunk producer.  */
end_comment

begin_class
DECL|class|SplunkProducer
specifier|public
class|class
name|SplunkProducer
extends|extends
name|DefaultProducer
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
name|SplunkProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|SplunkEndpoint
name|endpoint
decl_stmt|;
DECL|field|dataWriter
specifier|private
name|DataWriter
name|dataWriter
decl_stmt|;
DECL|method|SplunkProducer (SplunkEndpoint endpoint, ProducerType producerType)
specifier|public
name|SplunkProducer
parameter_list|(
name|SplunkEndpoint
name|endpoint
parameter_list|,
name|ProducerType
name|producerType
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|createWriter
argument_list|(
name|producerType
argument_list|)
expr_stmt|;
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
try|try
block|{
if|if
condition|(
operator|!
name|dataWriter
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|dataWriter
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isRaw
argument_list|()
condition|)
block|{
name|dataWriter
operator|.
name|write
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dataWriter
operator|.
name|write
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|SplunkEvent
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|reset
argument_list|(
name|e
argument_list|)
condition|)
block|{
name|dataWriter
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
throw|throw
name|e
throw|;
block|}
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
name|dataWriter
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|createWriter (ProducerType producerType)
specifier|private
name|void
name|createWriter
parameter_list|(
name|ProducerType
name|producerType
parameter_list|)
block|{
switch|switch
condition|(
name|producerType
condition|)
block|{
case|case
name|TCP
case|:
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating TcpDataWriter"
argument_list|)
expr_stmt|;
name|dataWriter
operator|=
operator|new
name|TcpDataWriter
argument_list|(
name|endpoint
argument_list|,
name|buildSplunkArgs
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|TcpDataWriter
operator|)
name|dataWriter
operator|)
operator|.
name|setPort
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTcpReceiverPort
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"TcpDataWriter created for endpoint {}"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|SUBMIT
case|:
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating SubmitDataWriter"
argument_list|)
expr_stmt|;
name|dataWriter
operator|=
operator|new
name|SubmitDataWriter
argument_list|(
name|endpoint
argument_list|,
name|buildSplunkArgs
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SubmitDataWriter
operator|)
name|dataWriter
operator|)
operator|.
name|setIndex
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"SubmitDataWriter created for endpoint {}"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|STREAM
case|:
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating StreamDataWriter"
argument_list|)
expr_stmt|;
name|dataWriter
operator|=
operator|new
name|StreamDataWriter
argument_list|(
name|endpoint
argument_list|,
name|buildSplunkArgs
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|StreamDataWriter
operator|)
name|dataWriter
operator|)
operator|.
name|setIndex
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"StreamDataWriter created for endpoint {}"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
break|break;
block|}
default|default:
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"unknown producerType"
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|buildSplunkArgs ()
specifier|private
name|Args
name|buildSplunkArgs
parameter_list|()
block|{
name|Args
name|args
init|=
operator|new
name|Args
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSourceType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|args
operator|.
name|put
argument_list|(
literal|"sourcetype"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSourceType
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSource
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|args
operator|.
name|put
argument_list|(
literal|"source"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getEventHost
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|args
operator|.
name|put
argument_list|(
literal|"host"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getEventHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|args
return|;
block|}
DECL|method|getDataWriter ()
specifier|protected
name|DataWriter
name|getDataWriter
parameter_list|()
block|{
return|return
name|dataWriter
return|;
block|}
block|}
end_class

end_unit

