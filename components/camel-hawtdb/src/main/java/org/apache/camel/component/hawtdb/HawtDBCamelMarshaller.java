begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hawtdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hawtdb
package|;
end_package

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
name|Serializable
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

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|hawtdb
operator|.
name|util
operator|.
name|buffer
operator|.
name|Buffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|hawtdb
operator|.
name|util
operator|.
name|buffer
operator|.
name|DataByteArrayInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|hawtdb
operator|.
name|util
operator|.
name|buffer
operator|.
name|DataByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|hawtdb
operator|.
name|util
operator|.
name|marshaller
operator|.
name|Marshaller
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|hawtdb
operator|.
name|util
operator|.
name|marshaller
operator|.
name|ObjectMarshaller
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|hawtdb
operator|.
name|util
operator|.
name|marshaller
operator|.
name|StringMarshaller
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|HawtDBCamelMarshaller
specifier|public
specifier|final
class|class
name|HawtDBCamelMarshaller
parameter_list|<
name|K
parameter_list|>
block|{
DECL|field|keyMarshaller
specifier|private
name|Marshaller
argument_list|<
name|K
argument_list|>
name|keyMarshaller
init|=
operator|new
name|ObjectMarshaller
argument_list|<
name|K
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|confirmKeyMarshaller
specifier|private
name|Marshaller
argument_list|<
name|String
argument_list|>
name|confirmKeyMarshaller
init|=
operator|new
name|StringMarshaller
argument_list|()
decl_stmt|;
DECL|field|exchangeMarshaller
specifier|private
name|Marshaller
argument_list|<
name|DefaultExchangeHolder
argument_list|>
name|exchangeMarshaller
init|=
operator|new
name|ObjectMarshaller
argument_list|<
name|DefaultExchangeHolder
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|marshallKey (K key)
specifier|public
name|Buffer
name|marshallKey
parameter_list|(
name|K
name|key
parameter_list|)
throws|throws
name|IOException
block|{
name|DataByteArrayOutputStream
name|baos
init|=
operator|new
name|DataByteArrayOutputStream
argument_list|()
decl_stmt|;
name|keyMarshaller
operator|.
name|writePayload
argument_list|(
name|key
argument_list|,
name|baos
argument_list|)
expr_stmt|;
return|return
name|baos
operator|.
name|toBuffer
argument_list|()
return|;
block|}
DECL|method|marshallConfirmKey (String exchangeId)
specifier|public
name|Buffer
name|marshallConfirmKey
parameter_list|(
name|String
name|exchangeId
parameter_list|)
throws|throws
name|IOException
block|{
name|DataByteArrayOutputStream
name|baos
init|=
operator|new
name|DataByteArrayOutputStream
argument_list|()
decl_stmt|;
name|confirmKeyMarshaller
operator|.
name|writePayload
argument_list|(
name|exchangeId
argument_list|,
name|baos
argument_list|)
expr_stmt|;
return|return
name|baos
operator|.
name|toBuffer
argument_list|()
return|;
block|}
DECL|method|unmarshallConfirmKey (Buffer buffer)
specifier|public
name|String
name|unmarshallConfirmKey
parameter_list|(
name|Buffer
name|buffer
parameter_list|)
throws|throws
name|IOException
block|{
name|DataByteArrayInputStream
name|bais
init|=
operator|new
name|DataByteArrayInputStream
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|String
name|key
init|=
name|confirmKeyMarshaller
operator|.
name|readPayload
argument_list|(
name|bais
argument_list|)
decl_stmt|;
return|return
name|key
return|;
block|}
DECL|method|marshallExchange (CamelContext camelContext, Exchange exchange)
specifier|public
name|Buffer
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
name|DataByteArrayOutputStream
name|baos
init|=
operator|new
name|DataByteArrayOutputStream
argument_list|()
decl_stmt|;
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
comment|// add the aggregated size property as the only property we want to retain
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
name|Serializable
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
name|exchangeMarshaller
operator|.
name|writePayload
argument_list|(
name|pe
argument_list|,
name|baos
argument_list|)
expr_stmt|;
return|return
name|baos
operator|.
name|toBuffer
argument_list|()
return|;
block|}
DECL|method|unmarshallExchange (CamelContext camelContext, Buffer buffer)
specifier|public
name|Exchange
name|unmarshallExchange
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Buffer
name|buffer
parameter_list|)
throws|throws
name|IOException
block|{
name|DataByteArrayInputStream
name|bais
init|=
operator|new
name|DataByteArrayInputStream
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|DefaultExchangeHolder
name|pe
init|=
name|exchangeMarshaller
operator|.
name|readPayload
argument_list|(
name|bais
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
block|}
end_class

end_unit

