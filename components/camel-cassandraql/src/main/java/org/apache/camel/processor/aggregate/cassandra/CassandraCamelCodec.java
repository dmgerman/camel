begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.cassandra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
operator|.
name|cassandra
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
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
name|support
operator|.
name|DefaultExchangeHolder
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
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  * Marshall/unmarshall Exchange to/from a ByteBuffer.  * Inspired from JdbcCamelCodec.  */
end_comment

begin_class
DECL|class|CassandraCamelCodec
specifier|public
class|class
name|CassandraCamelCodec
block|{
DECL|method|marshallExchange (CamelContext camelContext, Exchange exchange, boolean allowSerializedHeaders)
specifier|public
name|ByteBuffer
name|marshallExchange
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|allowSerializedHeaders
parameter_list|)
throws|throws
name|IOException
block|{
comment|// use DefaultExchangeHolder to marshal to a serialized object
name|DefaultExchangeHolder
name|pe
init|=
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
comment|// add the aggregated size and timeout property as the only properties we want to retain
name|DefaultExchangeHolder
operator|.
name|addProperty
argument_list|(
name|pe
argument_list|,
name|Exchange
operator|.
name|AGGREGATED_SIZE
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_SIZE
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|DefaultExchangeHolder
operator|.
name|addProperty
argument_list|(
name|pe
argument_list|,
name|Exchange
operator|.
name|AGGREGATED_TIMEOUT
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_TIMEOUT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|// add the aggregated completed by property to retain
name|DefaultExchangeHolder
operator|.
name|addProperty
argument_list|(
name|pe
argument_list|,
name|Exchange
operator|.
name|AGGREGATED_COMPLETED_BY
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COMPLETED_BY
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|// add the aggregated correlation key property to retain
name|DefaultExchangeHolder
operator|.
name|addProperty
argument_list|(
name|pe
argument_list|,
name|Exchange
operator|.
name|AGGREGATED_CORRELATION_KEY
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_CORRELATION_KEY
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|// and a guard property if using the flexible toolbox aggregator
name|DefaultExchangeHolder
operator|.
name|addProperty
argument_list|(
name|pe
argument_list|,
name|Exchange
operator|.
name|AGGREGATED_COLLECTION_GUARD
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COLLECTION_GUARD
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|// persist the from endpoint as well
if|if
condition|(
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|DefaultExchangeHolder
operator|.
name|addProperty
argument_list|(
name|pe
argument_list|,
literal|"CamelAggregatedFromEndpoint"
argument_list|,
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|serialize
argument_list|(
name|pe
argument_list|)
argument_list|)
return|;
block|}
DECL|method|unmarshallExchange (CamelContext camelContext, ByteBuffer buffer)
specifier|public
name|Exchange
name|unmarshallExchange
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ByteBuffer
name|buffer
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|DefaultExchangeHolder
name|pe
init|=
operator|(
name|DefaultExchangeHolder
operator|)
name|deserialize
argument_list|(
operator|new
name|ByteBufferInputStream
argument_list|(
name|buffer
argument_list|)
argument_list|)
decl_stmt|;
name|Exchange
name|answer
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|DefaultExchangeHolder
operator|.
name|unmarshal
argument_list|(
name|answer
argument_list|,
name|pe
argument_list|)
expr_stmt|;
comment|// restore the from endpoint
name|String
name|fromEndpointUri
init|=
operator|(
name|String
operator|)
name|answer
operator|.
name|removeProperty
argument_list|(
literal|"CamelAggregatedFromEndpoint"
argument_list|)
decl_stmt|;
if|if
condition|(
name|fromEndpointUri
operator|!=
literal|null
condition|)
block|{
name|Endpoint
name|fromEndpoint
init|=
name|camelContext
operator|.
name|hasEndpoint
argument_list|(
name|fromEndpointUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|fromEndpoint
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setFromEndpoint
argument_list|(
name|fromEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|serialize (Object object)
specifier|private
name|byte
index|[]
name|serialize
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|bytesOut
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ObjectOutputStream
name|objectOut
init|=
operator|new
name|ObjectOutputStream
argument_list|(
name|bytesOut
argument_list|)
decl_stmt|;
name|objectOut
operator|.
name|writeObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|objectOut
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|bytesOut
operator|.
name|toByteArray
argument_list|()
return|;
block|}
DECL|method|deserialize (InputStream bytes)
specifier|private
name|Object
name|deserialize
parameter_list|(
name|InputStream
name|bytes
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|ObjectInputStream
name|objectIn
init|=
operator|new
name|ObjectInputStream
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|Object
name|object
init|=
name|objectIn
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|objectIn
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|object
return|;
block|}
DECL|class|ByteBufferInputStream
specifier|private
specifier|static
class|class
name|ByteBufferInputStream
extends|extends
name|InputStream
block|{
DECL|field|buffer
specifier|private
specifier|final
name|ByteBuffer
name|buffer
decl_stmt|;
DECL|method|ByteBufferInputStream (ByteBuffer buffer)
name|ByteBufferInputStream
parameter_list|(
name|ByteBuffer
name|buffer
parameter_list|)
block|{
name|this
operator|.
name|buffer
operator|=
name|buffer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|buffer
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|buffer
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|read (byte[] bytes, int off, int len)
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|buffer
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|len
operator|=
name|Math
operator|.
name|min
argument_list|(
name|len
argument_list|,
name|buffer
operator|.
name|remaining
argument_list|()
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|get
argument_list|(
name|bytes
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
return|return
name|len
return|;
block|}
block|}
block|}
end_class

end_unit

