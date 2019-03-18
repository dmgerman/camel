begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.mvel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|mvel
package|;
end_package

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
name|Message
import|;
end_import

begin_class
DECL|class|RootObject
specifier|public
class|class
name|RootObject
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|method|RootObject (Exchange exchange)
specifier|public
name|RootObject
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
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
return|;
block|}
DECL|method|getException ()
specifier|public
name|Throwable
name|getException
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getException
argument_list|()
return|;
block|}
DECL|method|getExchangeId ()
specifier|public
name|String
name|getExchangeId
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getExchangeId
argument_list|()
return|;
block|}
DECL|method|getRequest ()
specifier|public
name|Message
name|getRequest
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
return|;
block|}
DECL|method|getResponse ()
specifier|public
name|Message
name|getResponse
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getOut
argument_list|()
return|;
block|}
DECL|method|getProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getProperties
argument_list|()
return|;
block|}
DECL|method|getProperty (String name)
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|getProperty (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getProperty
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

