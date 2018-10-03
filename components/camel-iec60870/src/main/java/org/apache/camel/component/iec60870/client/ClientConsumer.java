begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.iec60870.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|iec60870
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
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
name|Message
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
name|component
operator|.
name|iec60870
operator|.
name|ObjectAddress
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
name|DefaultConsumer
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
name|DefaultMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|asdu
operator|.
name|types
operator|.
name|Value
import|;
end_import

begin_class
DECL|class|ClientConsumer
specifier|public
class|class
name|ClientConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|connection
specifier|private
specifier|final
name|ClientConnection
name|connection
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|ClientEndpoint
name|endpoint
decl_stmt|;
DECL|method|ClientConsumer (final ClientEndpoint endpoint, final Processor processor, final ClientConnection connection)
specifier|public
name|ClientConsumer
parameter_list|(
specifier|final
name|ClientEndpoint
name|endpoint
parameter_list|,
specifier|final
name|Processor
name|processor
parameter_list|,
specifier|final
name|ClientConnection
name|connection
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|connection
operator|=
name|connection
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
name|this
operator|.
name|connection
operator|.
name|setListener
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|,
name|this
operator|::
name|updateValue
argument_list|)
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
name|this
operator|.
name|connection
operator|.
name|setListener
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|updateValue (final ObjectAddress address, final Value<?> value)
specifier|private
name|void
name|updateValue
parameter_list|(
specifier|final
name|ObjectAddress
name|address
parameter_list|,
specifier|final
name|Value
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
comment|// Note: we hold the sync lock for the connection
try|try
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|mapMessage
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Failed to process message"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|mapMessage (final Value<?> value)
specifier|private
name|Message
name|mapMessage
parameter_list|(
specifier|final
name|Value
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
specifier|final
name|DefaultMessage
name|message
init|=
operator|new
name|DefaultMessage
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"value"
argument_list|,
name|value
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"timestamp"
argument_list|,
name|Instant
operator|.
name|ofEpochMilli
argument_list|(
name|value
operator|.
name|getTimestamp
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"quality"
argument_list|,
name|value
operator|.
name|getQualityInformation
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"overflow"
argument_list|,
name|value
operator|.
name|isOverflow
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
block|}
end_class

end_unit

