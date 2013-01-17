begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.jdbc
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
name|jdbc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|impl
operator|.
name|DefaultExchange
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
name|DefaultExchangeHolder
import|;
end_import

begin_comment
comment|/**  * Adapted from HawtDBCamelCodec  */
end_comment

begin_class
DECL|class|JdbcCamelCodec
specifier|public
class|class
name|JdbcCamelCodec
block|{
DECL|method|marshallExchange (CamelContext camelContext, Exchange exchange)
specifier|public
name|byte
index|[]
name|marshallExchange
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Exchange
name|exchange
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
name|encode
argument_list|(
name|pe
argument_list|)
return|;
block|}
DECL|method|unmarshallExchange (CamelContext camelContext, byte[] buffer)
specifier|public
name|Exchange
name|unmarshallExchange
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|byte
index|[]
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
name|decode
argument_list|(
name|camelContext
argument_list|,
name|buffer
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
DECL|method|encode (Object object)
specifier|private
name|byte
index|[]
name|encode
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
name|byte
index|[]
name|data
init|=
name|bytesOut
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
return|return
name|data
return|;
block|}
DECL|method|decode (CamelContext camelContext, byte[] dataIn)
specifier|private
name|DefaultExchangeHolder
name|decode
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|byte
index|[]
name|dataIn
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|ByteArrayInputStream
name|bytesIn
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|dataIn
argument_list|)
decl_stmt|;
name|ObjectInputStream
name|objectIn
init|=
operator|new
name|ClassLoadingAwareObjectInputStream
argument_list|(
name|camelContext
argument_list|,
name|bytesIn
argument_list|)
decl_stmt|;
name|Object
name|obj
init|=
name|objectIn
operator|.
name|readObject
argument_list|()
decl_stmt|;
return|return
operator|(
name|DefaultExchangeHolder
operator|)
name|obj
return|;
block|}
block|}
end_class

end_unit

