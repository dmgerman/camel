begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.flink
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flink
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|apache
operator|.
name|flink
operator|.
name|streaming
operator|.
name|api
operator|.
name|datastream
operator|.
name|DataStream
import|;
end_import

begin_class
DECL|class|DataStreamFlinkProducer
specifier|public
class|class
name|DataStreamFlinkProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|DataStreamFlinkProducer (FlinkEndpoint endpoint)
specifier|public
name|DataStreamFlinkProducer
parameter_list|(
name|FlinkEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
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
name|DataStream
name|ds
init|=
name|resolveDataStream
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|DataStreamCallback
name|dataStreamCallback
init|=
name|resolveDataStreamCallback
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|body
operator|instanceof
name|List
condition|?
name|dataStreamCallback
operator|.
name|onDataStream
argument_list|(
name|ds
argument_list|,
operator|(
operator|(
name|List
operator|)
name|body
operator|)
operator|.
name|toArray
argument_list|(
operator|new
name|Object
index|[
literal|0
index|]
argument_list|)
argument_list|)
else|:
name|dataStreamCallback
operator|.
name|onDataStream
argument_list|(
name|ds
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|collectResults
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|FlinkEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|FlinkEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|collectResults (Exchange exchange, Object result)
specifier|protected
name|void
name|collectResults
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|result
parameter_list|)
block|{
if|if
condition|(
name|result
operator|instanceof
name|DataStream
condition|)
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isCollect
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"collect mode not supported for Flink DataStreams."
argument_list|)
throw|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|FlinkConstants
operator|.
name|FLINK_DATASTREAM_HEADER
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|resolveDataStream (Exchange exchange)
specifier|protected
name|DataStream
name|resolveDataStream
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|FlinkConstants
operator|.
name|FLINK_DATASTREAM_HEADER
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|DataStream
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|FlinkConstants
operator|.
name|FLINK_DATASTREAM_HEADER
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getDataStream
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getDataStream
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No DataStream defined"
argument_list|)
throw|;
block|}
block|}
DECL|method|resolveDataStreamCallback (Exchange exchange)
specifier|protected
name|DataStreamCallback
name|resolveDataStreamCallback
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|FlinkConstants
operator|.
name|FLINK_DATASTREAM_CALLBACK_HEADER
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|DataStreamCallback
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|FlinkConstants
operator|.
name|FLINK_DATASTREAM_CALLBACK_HEADER
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getDataStreamCallback
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getDataStreamCallback
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot resolve DataStream callback."
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

