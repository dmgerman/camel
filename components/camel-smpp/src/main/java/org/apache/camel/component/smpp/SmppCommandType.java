begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.smpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|smpp
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

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|session
operator|.
name|SMPPSession
import|;
end_import

begin_enum
DECL|enum|SmppCommandType
specifier|public
enum|enum
name|SmppCommandType
block|{
DECL|enumConstant|SUBMIT_SM
name|SUBMIT_SM
argument_list|(
literal|"SubmitSm"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|SmppSubmitSmCommand
name|createCommand
parameter_list|(
name|SMPPSession
name|session
parameter_list|,
name|SmppConfiguration
name|config
parameter_list|)
block|{
return|return
operator|new
name|SmppSubmitSmCommand
argument_list|(
name|session
argument_list|,
name|config
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|REPLACE_SM
name|REPLACE_SM
argument_list|(
literal|"ReplaceSm"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|SmppReplaceSmCommand
name|createCommand
parameter_list|(
name|SMPPSession
name|session
parameter_list|,
name|SmppConfiguration
name|config
parameter_list|)
block|{
return|return
operator|new
name|SmppReplaceSmCommand
argument_list|(
name|session
argument_list|,
name|config
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|QUERY_SM
name|QUERY_SM
argument_list|(
literal|"QuerySm"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|SmppQuerySmCommand
name|createCommand
parameter_list|(
name|SMPPSession
name|session
parameter_list|,
name|SmppConfiguration
name|config
parameter_list|)
block|{
return|return
operator|new
name|SmppQuerySmCommand
argument_list|(
name|session
argument_list|,
name|config
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|SUBMIT_MULTI
name|SUBMIT_MULTI
argument_list|(
literal|"SubmitMulti"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|SmppSubmitMultiCommand
name|createCommand
parameter_list|(
name|SMPPSession
name|session
parameter_list|,
name|SmppConfiguration
name|config
parameter_list|)
block|{
return|return
operator|new
name|SmppSubmitMultiCommand
argument_list|(
name|session
argument_list|,
name|config
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|CANCEL_SM
name|CANCEL_SM
argument_list|(
literal|"CancelSm"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|SmppCancelSmCommand
name|createCommand
parameter_list|(
name|SMPPSession
name|session
parameter_list|,
name|SmppConfiguration
name|config
parameter_list|)
block|{
return|return
operator|new
name|SmppCancelSmCommand
argument_list|(
name|session
argument_list|,
name|config
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|DATA_SHORT_MESSAGE
name|DATA_SHORT_MESSAGE
argument_list|(
literal|"DataSm"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|SmppDataSmCommand
name|createCommand
parameter_list|(
name|SMPPSession
name|session
parameter_list|,
name|SmppConfiguration
name|config
parameter_list|)
block|{
return|return
operator|new
name|SmppDataSmCommand
argument_list|(
name|session
argument_list|,
name|config
argument_list|)
return|;
block|}
block|}
block|;
DECL|field|commandName
specifier|private
name|String
name|commandName
decl_stmt|;
DECL|method|SmppCommandType (String commandName)
specifier|private
name|SmppCommandType
parameter_list|(
name|String
name|commandName
parameter_list|)
block|{
name|this
operator|.
name|commandName
operator|=
name|commandName
expr_stmt|;
block|}
DECL|method|getCommandName ()
specifier|public
name|String
name|getCommandName
parameter_list|()
block|{
return|return
name|commandName
return|;
block|}
DECL|method|createCommand (SMPPSession session, SmppConfiguration config)
specifier|public
specifier|abstract
name|SmppCommand
name|createCommand
parameter_list|(
name|SMPPSession
name|session
parameter_list|,
name|SmppConfiguration
name|config
parameter_list|)
function_decl|;
comment|/**      * Tries to return an instance of {@link SmppCommandType} using      * {@link SmppConstants#COMMAND} header of the incoming message.      *<p/>      * Returns {@link #SUBMIT_SM} if there is no {@link SmppConstants#COMMAND}      * header in the incoming message or value of such a header cannot be      * recognized.      *<p/>      * The valid values for the {@link SmppConstants#COMMAND} header are:<span      * style="font: bold;">SubmitSm</span><span      * style="font: bold;">ReplaceSm</span>,<span      * style="font: bold;">QuerySm</span>,<span      * style="font: bold;">SubmitMulti</span>,<span      * style="font: bold;">CancelSm</span>,<span      * style="font: bold;">DataSm</span>.      *       * @param exchange      *            an exchange to get an incoming message from      * @return an instance of {@link SmppCommandType}      */
DECL|method|fromExchange (Exchange exchange)
specifier|public
specifier|static
name|SmppCommandType
name|fromExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|commandName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|COMMAND
argument_list|)
condition|)
block|{
name|commandName
operator|=
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|SmppCommandType
name|commandType
init|=
name|SUBMIT_SM
decl_stmt|;
for|for
control|(
name|SmppCommandType
name|nextCommandType
range|:
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|nextCommandType
operator|.
name|commandName
operator|.
name|equals
argument_list|(
name|commandName
argument_list|)
condition|)
block|{
name|commandType
operator|=
name|nextCommandType
expr_stmt|;
break|break;
block|}
block|}
return|return
name|commandType
return|;
block|}
block|}
end_enum

end_unit

