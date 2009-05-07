begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quickfix
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quickfix
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Application
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|DoNotSend
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|FieldNotFound
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|IncorrectDataFormat
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|IncorrectTagValue
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|RejectLogon
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|SessionID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|UnsupportedMessageType
import|;
end_import

begin_comment
comment|/**  * QuickfixApplication is the root for application logic.  *  * @author Anton Arhipov  * @see quickfix.Application  */
end_comment

begin_class
DECL|class|QuickfixApplication
specifier|public
class|class
name|QuickfixApplication
implements|implements
name|Application
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|QuickfixApplication
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|QuickfixEndpoint
name|endpoint
decl_stmt|;
DECL|method|QuickfixApplication (QuickfixEndpoint endpoint)
specifier|public
name|QuickfixApplication
parameter_list|(
name|QuickfixEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|QuickfixEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|setEndpoint (QuickfixEndpoint endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|QuickfixEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|onCreate (SessionID sessionID)
specifier|public
name|void
name|onCreate
parameter_list|(
name|SessionID
name|sessionID
parameter_list|)
block|{
name|endpoint
operator|.
name|setSessionID
argument_list|(
name|sessionID
argument_list|)
expr_stmt|;
block|}
DECL|method|onLogon (SessionID sessionID)
specifier|public
name|void
name|onLogon
parameter_list|(
name|SessionID
name|sessionID
parameter_list|)
block|{
name|endpoint
operator|.
name|setSessionID
argument_list|(
name|sessionID
argument_list|)
expr_stmt|;
block|}
DECL|method|onLogout (SessionID sessionID)
specifier|public
name|void
name|onLogout
parameter_list|(
name|SessionID
name|sessionID
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"logging out from session "
operator|+
name|sessionID
argument_list|)
expr_stmt|;
block|}
DECL|method|toAdmin (Message message, SessionID sessionID)
specifier|public
name|void
name|toAdmin
parameter_list|(
name|Message
name|message
parameter_list|,
name|SessionID
name|sessionID
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"to admin: ["
operator|+
name|sessionID
operator|+
literal|"]: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|fromAdmin (Message message, SessionID sessionID)
specifier|public
name|void
name|fromAdmin
parameter_list|(
name|Message
name|message
parameter_list|,
name|SessionID
name|sessionID
parameter_list|)
throws|throws
name|FieldNotFound
throws|,
name|IncorrectDataFormat
throws|,
name|IncorrectTagValue
throws|,
name|RejectLogon
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"to admin: ["
operator|+
name|sessionID
operator|+
literal|"]: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|toApp (Message message, SessionID sessionID)
specifier|public
name|void
name|toApp
parameter_list|(
name|Message
name|message
parameter_list|,
name|SessionID
name|sessionID
parameter_list|)
throws|throws
name|DoNotSend
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"to app: ["
operator|+
name|sessionID
operator|+
literal|"]: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|fromApp (Message message, SessionID sessionID)
specifier|public
name|void
name|fromApp
parameter_list|(
name|Message
name|message
parameter_list|,
name|SessionID
name|sessionID
parameter_list|)
throws|throws
name|FieldNotFound
throws|,
name|IncorrectDataFormat
throws|,
name|IncorrectTagValue
throws|,
name|UnsupportedMessageType
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"from app: ["
operator|+
name|sessionID
operator|+
literal|"]: "
operator|+
name|message
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|onMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

