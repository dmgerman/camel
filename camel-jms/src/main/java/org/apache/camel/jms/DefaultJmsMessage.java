begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jms
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
name|impl
operator|.
name|DefaultMessage
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
name|Headers
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultJmsMessage
specifier|public
class|class
name|DefaultJmsMessage
extends|extends
name|DefaultMessage
implements|implements
name|JmsMessage
block|{
DECL|field|jmsMessage
specifier|private
name|Message
name|jmsMessage
decl_stmt|;
DECL|method|DefaultJmsMessage ()
specifier|public
name|DefaultJmsMessage
parameter_list|()
block|{     }
DECL|method|DefaultJmsMessage (Message jmsMessage)
specifier|public
name|DefaultJmsMessage
parameter_list|(
name|Message
name|jmsMessage
parameter_list|)
block|{
name|this
operator|.
name|jmsMessage
operator|=
name|jmsMessage
expr_stmt|;
block|}
DECL|method|getJmsMessage ()
specifier|public
name|Message
name|getJmsMessage
parameter_list|()
block|{
return|return
name|jmsMessage
return|;
block|}
DECL|method|setJmsMessage (Message jmsMessage)
specifier|public
name|void
name|setJmsMessage
parameter_list|(
name|Message
name|jmsMessage
parameter_list|)
block|{
name|this
operator|.
name|jmsMessage
operator|=
name|jmsMessage
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createHeaders ()
specifier|protected
name|Headers
name|createHeaders
parameter_list|()
block|{
return|return
operator|new
name|JmsHeaders
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

