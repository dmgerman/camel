begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api.entity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|entity
package|;
end_package

begin_enum
DECL|enum|DispositionMode
specifier|public
enum|enum
name|DispositionMode
block|{
DECL|enumConstant|MANUAL_ACTION_MDN_SENT_MANUALLY
name|MANUAL_ACTION_MDN_SENT_MANUALLY
argument_list|(
literal|"manual-action"
argument_list|,
literal|"MDN-sent-manually"
argument_list|)
block|,
DECL|enumConstant|MANUAL_ACTION_MDN_SENT_AUTOMATICALLY
name|MANUAL_ACTION_MDN_SENT_AUTOMATICALLY
argument_list|(
literal|"manual-action"
argument_list|,
literal|"MDN-sent-automatically"
argument_list|)
block|,
DECL|enumConstant|AUTOMATIC_ACTION_MDN_SENT_MANUALLY
name|AUTOMATIC_ACTION_MDN_SENT_MANUALLY
argument_list|(
literal|"automatic-action"
argument_list|,
literal|"MDN-sent-manually"
argument_list|)
block|,
DECL|enumConstant|AUTOMATIC_ACTION_MDN_SENT_AUTOMATICALLY
name|AUTOMATIC_ACTION_MDN_SENT_AUTOMATICALLY
argument_list|(
literal|"automatic-action"
argument_list|,
literal|"MDN-sent-automatically"
argument_list|)
block|;
DECL|field|actionMode
specifier|private
name|String
name|actionMode
decl_stmt|;
DECL|field|sendingMode
specifier|private
name|String
name|sendingMode
decl_stmt|;
DECL|method|DispositionMode (String actionMode, String sendingMode)
specifier|private
name|DispositionMode
parameter_list|(
name|String
name|actionMode
parameter_list|,
name|String
name|sendingMode
parameter_list|)
block|{
name|this
operator|.
name|actionMode
operator|=
name|actionMode
expr_stmt|;
name|this
operator|.
name|sendingMode
operator|=
name|sendingMode
expr_stmt|;
block|}
DECL|method|getActionMode ()
specifier|public
name|String
name|getActionMode
parameter_list|()
block|{
return|return
name|actionMode
return|;
block|}
DECL|method|getSendingMode ()
specifier|public
name|String
name|getSendingMode
parameter_list|()
block|{
return|return
name|sendingMode
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|actionMode
operator|+
literal|"/"
operator|+
name|sendingMode
return|;
block|}
DECL|method|parseDispositionMode (String dispositionModeString)
specifier|public
specifier|static
name|DispositionMode
name|parseDispositionMode
parameter_list|(
name|String
name|dispositionModeString
parameter_list|)
block|{
switch|switch
condition|(
name|dispositionModeString
condition|)
block|{
case|case
literal|"manual-action/MDN-sent-manually"
case|:
return|return
name|MANUAL_ACTION_MDN_SENT_MANUALLY
return|;
case|case
literal|"manual-actionMDN-sent-automatically"
case|:
return|return
name|MANUAL_ACTION_MDN_SENT_AUTOMATICALLY
return|;
case|case
literal|"automatic-action/MDN-sent-manually"
case|:
return|return
name|AUTOMATIC_ACTION_MDN_SENT_MANUALLY
return|;
case|case
literal|"automatic-action/MDN-sent-automatically"
case|:
return|return
name|AUTOMATIC_ACTION_MDN_SENT_AUTOMATICALLY
return|;
default|default:
return|return
literal|null
return|;
block|}
block|}
block|}
end_enum

end_unit

