begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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

begin_comment
comment|/**  * A base class for implementation inheritence providing the core {@link Message} body  * handling features but letting the derived class deal with headers.  *  * Unless a specific provider wishes to do something particularly clever with headers you probably  * want to just derive from {@link DefaultMessage}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MessageSupport
specifier|public
specifier|abstract
class|class
name|MessageSupport
implements|implements
name|Message
block|{
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
DECL|field|body
specifier|private
name|Object
name|body
decl_stmt|;
DECL|method|getBody ()
specifier|public
name|Object
name|getBody
parameter_list|()
block|{
return|return
name|body
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
DECL|method|getBody (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getBody
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Exchange
name|e
init|=
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
return|return
name|e
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|getBody
argument_list|()
argument_list|)
return|;
block|}
return|return
operator|(
name|T
operator|)
name|getBody
argument_list|()
return|;
block|}
DECL|method|setBody (Object body)
specifier|public
name|void
name|setBody
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
block|}
DECL|method|setBody (Object body, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|setBody
parameter_list|(
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Exchange
name|e
init|=
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|T
name|value
init|=
name|e
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|body
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|body
operator|=
name|value
expr_stmt|;
block|}
block|}
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|copy ()
specifier|public
name|Message
name|copy
parameter_list|()
block|{
name|Message
name|answer
init|=
name|newInstance
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setBody
argument_list|(
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getExchange ()
specifier|public
name|Exchange
name|getExchange
parameter_list|()
block|{
return|return
name|exchange
return|;
block|}
DECL|method|setExchange (Exchange exchange)
specifier|public
name|void
name|setExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
comment|/**      * Returns a new instance      *      * @return      */
DECL|method|newInstance ()
specifier|public
specifier|abstract
name|Message
name|newInstance
parameter_list|()
function_decl|;
block|}
end_class

end_unit

