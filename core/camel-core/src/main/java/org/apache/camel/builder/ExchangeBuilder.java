begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|ExchangePattern
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
name|support
operator|.
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  * Builder to create {@link Exchange} and add headers and set body on the Exchange {@link Message}.  *<p/>  * Use the {@link #build()} method when done setting up the exchange.  */
end_comment

begin_class
DECL|class|ExchangeBuilder
specifier|public
specifier|final
class|class
name|ExchangeBuilder
block|{
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|field|pattern
specifier|private
name|ExchangePattern
name|pattern
decl_stmt|;
DECL|field|body
specifier|private
name|Object
name|body
decl_stmt|;
DECL|field|headers
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|properties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|ExchangeBuilder (CamelContext context)
specifier|public
name|ExchangeBuilder
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
comment|/**      * Create the exchange by setting the camel context      *      * @param context the camel context       * @return exchange builder      */
DECL|method|anExchange (CamelContext context)
specifier|public
specifier|static
name|ExchangeBuilder
name|anExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|ExchangeBuilder
argument_list|(
name|context
argument_list|)
return|;
block|}
comment|/**      * Set the in message body on the exchange      *      * @param body the body      * @return exchange builder      */
DECL|method|withBody (Object body)
specifier|public
name|ExchangeBuilder
name|withBody
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the message header of the in message on the exchange      *      * @param key the key of the header      * @param value the value of the header      * @return exchange builder      */
DECL|method|withHeader (String key, Object value)
specifier|public
name|ExchangeBuilder
name|withHeader
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the message exchange pattern on the exchange      *      * @param pattern exchange pattern      * @return exchange builder      */
DECL|method|withPattern (ExchangePattern pattern)
specifier|public
name|ExchangeBuilder
name|withPattern
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the exchange property      *      * @param key the key of the exchange property      * @param value the value of the exchange property      * @return exchange builder      */
DECL|method|withProperty (String key, Object value)
specifier|public
name|ExchangeBuilder
name|withProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Build up the exchange from the exchange builder      *      * @return exchange       */
DECL|method|build ()
specifier|public
name|Exchange
name|build
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
if|if
condition|(
name|headers
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|message
operator|.
name|setHeaders
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
comment|// setup the properties on the exchange
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pattern
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

