begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|MessageImpl
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
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * The binding of how Camel messages get mapped to Apache CXF and back again  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfBinding
specifier|public
class|class
name|CxfBinding
block|{
DECL|method|extractBodyFromCxf (CxfExchange exchange, Message message)
specifier|public
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
comment|//  TODO how do we choose a format?
return|return
name|getBody
argument_list|(
name|message
argument_list|)
return|;
block|}
DECL|method|getBody (Message message)
specifier|protected
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
return|return
literal|null
return|;
block|}
DECL|method|createCxfMessage (CxfExchange exchange)
specifier|public
name|MessageImpl
name|createCxfMessage
parameter_list|(
name|CxfExchange
name|exchange
parameter_list|)
block|{
name|MessageImpl
name|answer
init|=
operator|(
name|MessageImpl
operator|)
name|exchange
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
comment|// TODO is InputStream the best type to give to CXF?
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
comment|// no need to process headers as we reuse the CXF message
comment|/*         // set the headers         Set<Map.Entry<String, Object>> entries = in.getHeaders().entrySet();         for (Map.Entry<String, Object> entry : entries) {             answer.put(entry.getKey(), entry.getValue());         }         */
return|return
name|answer
return|;
block|}
DECL|method|storeCxfResponse (CxfExchange exchange, Message response)
specifier|public
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
name|getBody
argument_list|(
name|response
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|storeCxfResponse (CxfExchange exchange, Object response)
specifier|public
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
block|}
end_class

end_unit

