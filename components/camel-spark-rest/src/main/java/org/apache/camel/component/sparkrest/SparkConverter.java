begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sparkrest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sparkrest
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
name|Converter
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
name|spi
operator|.
name|TypeConverterRegistry
import|;
end_import

begin_import
import|import
name|spark
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|spark
operator|.
name|Response
import|;
end_import

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
DECL|class|SparkConverter
specifier|public
specifier|final
class|class
name|SparkConverter
block|{
DECL|method|SparkConverter ()
specifier|private
name|SparkConverter
parameter_list|()
block|{     }
comment|/**      * A fallback converter that allows us to easily call Java beans and use the raw Spark {@link Request} as parameter types.      */
annotation|@
name|Converter
argument_list|(
name|fallback
operator|=
literal|true
argument_list|)
DECL|method|convertToRequest (Class<?> type, Exchange exchange, Object value, TypeConverterRegistry registry)
specifier|public
specifier|static
name|Object
name|convertToRequest
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|,
name|TypeConverterRegistry
name|registry
parameter_list|)
block|{
comment|// if we want to covert to Request
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|Request
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
comment|// okay we may need to cheat a bit when we want to grab the HttpRequest as its stored on the NettyHttpMessage
comment|// so if the message instance is a NettyHttpMessage and its body is the value, then we can grab the
comment|// HttpRequest from the NettyHttpMessage
name|SparkMessage
name|msg
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|msg
operator|=
name|exchange
operator|.
name|getOut
argument_list|(
name|SparkMessage
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|=
name|exchange
operator|.
name|getIn
argument_list|(
name|SparkMessage
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|msg
operator|!=
literal|null
condition|)
block|{
return|return
name|msg
operator|.
name|getRequest
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * A fallback converter that allows us to easily call Java beans and use the raw Spark {@link Response} as parameter types.      */
annotation|@
name|Converter
argument_list|(
name|fallback
operator|=
literal|true
argument_list|)
DECL|method|convertToResponse (Class<?> type, Exchange exchange, Object value, TypeConverterRegistry registry)
specifier|public
specifier|static
name|Object
name|convertToResponse
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|,
name|TypeConverterRegistry
name|registry
parameter_list|)
block|{
comment|// if we want to covert to Response
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|Response
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
comment|// okay we may need to cheat a bit when we want to grab the HttpRequest as its stored on the NettyHttpMessage
comment|// so if the message instance is a NettyHttpMessage and its body is the value, then we can grab the
comment|// HttpRequest from the NettyHttpMessage
name|SparkMessage
name|msg
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|msg
operator|=
name|exchange
operator|.
name|getOut
argument_list|(
name|SparkMessage
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|=
name|exchange
operator|.
name|getIn
argument_list|(
name|SparkMessage
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|msg
operator|!=
literal|null
condition|)
block|{
return|return
name|msg
operator|.
name|getResponse
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

