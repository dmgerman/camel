begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
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
package|;
end_package

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
name|Set
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
comment|/**  * The binding/mapping of Camel messages to Apache CXF and back again  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfBinding
specifier|public
specifier|final
class|class
name|CxfBinding
block|{
DECL|method|CxfBinding ()
specifier|private
name|CxfBinding
parameter_list|()
block|{
comment|// Helper class
block|}
DECL|method|extractBodyFromCxf (CxfExchange exchange, Message message)
specifier|public
specifier|static
name|Object
name|extractBodyFromCxf
parameter_list|(
name|CxfExchange
name|exchange
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
comment|// TODO how do we choose a format?
return|return
name|getBody
argument_list|(
name|message
argument_list|)
return|;
block|}
DECL|method|getBody (Message message)
specifier|protected
specifier|static
name|Object
name|getBody
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|contentFormats
init|=
name|message
operator|.
name|getContentFormats
argument_list|()
decl_stmt|;
if|if
condition|(
name|contentFormats
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|contentFormat
range|:
name|contentFormats
control|)
block|{
name|Object
name|answer
init|=
name|message
operator|.
name|getContent
argument_list|(
name|contentFormat
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|createCxfMessage (CxfExchange exchange)
specifier|public
specifier|static
name|Message
name|createCxfMessage
parameter_list|(
name|CxfExchange
name|exchange
parameter_list|)
block|{
name|Message
name|answer
init|=
name|exchange
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
comment|// CXF uses StAX which is based on the stream API to parse the XML,
comment|// so the CXF transport is also based on the stream API.
comment|// And the interceptors are also based on the stream API,
comment|// so let's use an InputStream to host the CXF on wire message.
name|CxfMessage
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Object
name|body
init|=
name|in
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
name|body
operator|=
name|in
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|body
operator|instanceof
name|InputStream
condition|)
block|{
name|answer
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|body
argument_list|)
expr_stmt|;
comment|// we need copy context
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|List
condition|)
block|{
comment|// just set the operation's parameter
name|answer
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|body
argument_list|)
expr_stmt|;
comment|// just set the method name
name|answer
operator|.
name|put
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
operator|(
name|String
operator|)
name|in
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAMESPACE
argument_list|,
operator|(
name|String
operator|)
name|in
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAMESPACE
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|storeCxfResponse (CxfExchange exchange, Message response)
specifier|public
specifier|static
name|void
name|storeCxfResponse
parameter_list|(
name|CxfExchange
name|exchange
parameter_list|,
name|Message
name|response
parameter_list|)
block|{
comment|// no need to process headers as we use the CXF message
name|CxfMessage
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|setMessage
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|out
operator|.
name|setBody
argument_list|(
name|response
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|storeCxfResponse (CxfExchange exchange, Object response)
specifier|public
specifier|static
name|void
name|storeCxfResponse
parameter_list|(
name|CxfExchange
name|exchange
parameter_list|,
name|Object
name|response
parameter_list|)
block|{
name|CxfMessage
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|setBody
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|storeCxfFault (CxfExchange exchange, Message message)
specifier|public
specifier|static
name|void
name|storeCxfFault
parameter_list|(
name|CxfExchange
name|exchange
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|CxfMessage
name|fault
init|=
name|exchange
operator|.
name|getFault
argument_list|()
decl_stmt|;
if|if
condition|(
name|fault
operator|!=
literal|null
condition|)
block|{
name|fault
operator|.
name|setBody
argument_list|(
name|getBody
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

