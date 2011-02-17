begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|List
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
name|HeaderFilterStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|CastUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * Utility class to propagate headers to and from CXF message.  *  * @version   */
end_comment

begin_class
DECL|class|CxfHeaderHelper
specifier|public
specifier|final
class|class
name|CxfHeaderHelper
block|{
comment|/**      * Utility class does not have public constructor      */
DECL|method|CxfHeaderHelper ()
specifier|private
name|CxfHeaderHelper
parameter_list|()
block|{     }
comment|/**      * Propagates Camel headers to CXF message.      *      * @param strategy header filter strategy      * @param headers Camel header      * @param message CXF message      * @param exchange provides context for filtering      */
DECL|method|propagateCamelToCxf (HeaderFilterStrategy strategy, Map<String, Object> headers, Message message, Exchange exchange)
specifier|public
specifier|static
name|void
name|propagateCamelToCxf
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|Message
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|cxfHeaders
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cxfHeaders
operator|==
literal|null
condition|)
block|{
name|cxfHeaders
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|cxfHeaders
argument_list|)
expr_stmt|;
block|}
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
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|strategy
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToCamelHeaders
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
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
if|if
condition|(
name|Exchange
operator|.
name|CONTENT_TYPE
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Client
operator|.
name|REQUEST_CONTEXT
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|||
name|Client
operator|.
name|RESPONSE_CONTEXT
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|||
name|Message
operator|.
name|RESPONSE_CODE
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|message
operator|.
name|put
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
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|listValue
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|listValue
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|cxfHeaders
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|listValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|propagateCxfToCamel (HeaderFilterStrategy strategy, Message message, Map<String, Object> headers, Exchange exchange)
specifier|public
specifier|static
name|void
name|propagateCxfToCamel
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|Message
name|message
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|strategy
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// Copy the CXF protocol headers to the camel headers
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|cxfHeaders
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cxfHeaders
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|cxfHeaders
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|strategy
operator|.
name|applyFilterToExternalHeaders
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
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|headers
operator|.
name|put
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
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// propagate content type
name|String
name|key
init|=
name|Message
operator|.
name|CONTENT_TYPE
decl_stmt|;
name|Object
name|value
init|=
name|message
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|// propagate request context
name|key
operator|=
name|Client
operator|.
name|REQUEST_CONTEXT
expr_stmt|;
name|value
operator|=
name|message
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
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
block|}
comment|// propagate response context
name|key
operator|=
name|Client
operator|.
name|RESPONSE_CONTEXT
expr_stmt|;
name|value
operator|=
name|message
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
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
block|}
comment|// propagate response code
name|key
operator|=
name|Message
operator|.
name|RESPONSE_CODE
expr_stmt|;
name|value
operator|=
name|message
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

