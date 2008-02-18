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
name|io
operator|.
name|IOException
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|Conduit
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
name|transport
operator|.
name|Destination
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
name|transport
operator|.
name|MessageObserver
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
import|;
end_import

begin_class
DECL|class|NullDestination
specifier|public
class|class
name|NullDestination
implements|implements
name|Destination
block|{
DECL|field|messageObserver
name|MessageObserver
name|messageObserver
decl_stmt|;
DECL|method|NullDestination ()
specifier|public
name|NullDestination
parameter_list|()
block|{
comment|// do nothing here
block|}
DECL|method|getAddress ()
specifier|public
name|EndpointReferenceType
name|getAddress
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
DECL|method|getBackChannel (Message inMessage, Message partialResponse, EndpointReferenceType address)
specifier|public
name|Conduit
name|getBackChannel
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|Message
name|partialResponse
parameter_list|,
name|EndpointReferenceType
name|address
parameter_list|)
throws|throws
name|IOException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
DECL|method|getMessageObserver ()
specifier|public
name|MessageObserver
name|getMessageObserver
parameter_list|()
block|{
return|return
name|messageObserver
return|;
block|}
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|messageObserver
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|setMessageObserver (MessageObserver observer)
specifier|public
name|void
name|setMessageObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{
name|messageObserver
operator|=
name|observer
expr_stmt|;
block|}
block|}
end_class

end_unit

