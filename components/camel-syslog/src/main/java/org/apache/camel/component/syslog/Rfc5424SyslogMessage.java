begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.syslog
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|syslog
package|;
end_package

begin_class
DECL|class|Rfc5424SyslogMessage
specifier|public
class|class
name|Rfc5424SyslogMessage
extends|extends
name|SyslogMessage
block|{
DECL|field|appName
specifier|private
name|String
name|appName
init|=
literal|"-"
decl_stmt|;
DECL|field|procId
specifier|private
name|String
name|procId
init|=
literal|"-"
decl_stmt|;
DECL|field|msgId
specifier|private
name|String
name|msgId
init|=
literal|"-"
decl_stmt|;
DECL|field|structuredData
specifier|private
name|String
name|structuredData
init|=
literal|"-"
decl_stmt|;
comment|/**      * @return the appName      */
DECL|method|getAppName ()
specifier|public
name|String
name|getAppName
parameter_list|()
block|{
return|return
name|appName
return|;
block|}
comment|/**      * @param appName the appName to set      */
DECL|method|setAppName (String appName)
specifier|public
name|void
name|setAppName
parameter_list|(
name|String
name|appName
parameter_list|)
block|{
name|this
operator|.
name|appName
operator|=
name|appName
expr_stmt|;
block|}
comment|/**      * @return the procId      */
DECL|method|getProcId ()
specifier|public
name|String
name|getProcId
parameter_list|()
block|{
return|return
name|procId
return|;
block|}
comment|/**      * @param procId the procId to set      */
DECL|method|setProcId (String procId)
specifier|public
name|void
name|setProcId
parameter_list|(
name|String
name|procId
parameter_list|)
block|{
name|this
operator|.
name|procId
operator|=
name|procId
expr_stmt|;
block|}
comment|/**      * @return the msgId      */
DECL|method|getMsgId ()
specifier|public
name|String
name|getMsgId
parameter_list|()
block|{
return|return
name|msgId
return|;
block|}
comment|/**      * @param msgId the msgId to set      */
DECL|method|setMsgId (String msgId)
specifier|public
name|void
name|setMsgId
parameter_list|(
name|String
name|msgId
parameter_list|)
block|{
name|this
operator|.
name|msgId
operator|=
name|msgId
expr_stmt|;
block|}
comment|/**      * @return the structuredData      */
DECL|method|getStructuredData ()
specifier|public
name|String
name|getStructuredData
parameter_list|()
block|{
return|return
name|structuredData
return|;
block|}
comment|/**      * @param structuredData the structuredData to set      */
DECL|method|setStructuredData (String structuredData)
specifier|public
name|void
name|setStructuredData
parameter_list|(
name|String
name|structuredData
parameter_list|)
block|{
name|this
operator|.
name|structuredData
operator|=
name|structuredData
expr_stmt|;
block|}
block|}
end_class

end_unit

