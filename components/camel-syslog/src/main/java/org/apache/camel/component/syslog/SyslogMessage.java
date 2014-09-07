begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_class
DECL|class|SyslogMessage
specifier|public
class|class
name|SyslogMessage
block|{
DECL|field|facility
specifier|private
name|SyslogFacility
name|facility
decl_stmt|;
DECL|field|severity
specifier|private
name|SyslogSeverity
name|severity
decl_stmt|;
DECL|field|remoteAddress
specifier|private
name|String
name|remoteAddress
decl_stmt|;
DECL|field|localAddress
specifier|private
name|String
name|localAddress
decl_stmt|;
DECL|field|hostname
specifier|private
name|String
name|hostname
decl_stmt|;
DECL|field|logMessage
specifier|private
name|String
name|logMessage
decl_stmt|;
DECL|field|timestamp
specifier|private
name|Calendar
name|timestamp
decl_stmt|;
DECL|method|getLogMessage ()
specifier|public
name|String
name|getLogMessage
parameter_list|()
block|{
return|return
name|logMessage
return|;
block|}
DECL|method|setLogMessage (String logMessage)
specifier|public
name|void
name|setLogMessage
parameter_list|(
name|String
name|logMessage
parameter_list|)
block|{
name|this
operator|.
name|logMessage
operator|=
name|logMessage
expr_stmt|;
block|}
DECL|method|getLocalAddress ()
specifier|public
name|String
name|getLocalAddress
parameter_list|()
block|{
return|return
name|localAddress
return|;
block|}
DECL|method|setLocalAddress (String localAddress)
specifier|public
name|void
name|setLocalAddress
parameter_list|(
name|String
name|localAddress
parameter_list|)
block|{
name|this
operator|.
name|localAddress
operator|=
name|localAddress
expr_stmt|;
block|}
DECL|method|getFacility ()
specifier|public
name|SyslogFacility
name|getFacility
parameter_list|()
block|{
return|return
name|facility
return|;
block|}
DECL|method|setFacility (SyslogFacility facility)
specifier|public
name|void
name|setFacility
parameter_list|(
name|SyslogFacility
name|facility
parameter_list|)
block|{
name|this
operator|.
name|facility
operator|=
name|facility
expr_stmt|;
block|}
DECL|method|getTimestamp ()
specifier|public
name|Calendar
name|getTimestamp
parameter_list|()
block|{
return|return
name|timestamp
return|;
block|}
DECL|method|setTimestamp (Calendar timestamp)
specifier|public
name|void
name|setTimestamp
parameter_list|(
name|Calendar
name|timestamp
parameter_list|)
block|{
name|this
operator|.
name|timestamp
operator|=
name|timestamp
expr_stmt|;
block|}
DECL|method|getSeverity ()
specifier|public
name|SyslogSeverity
name|getSeverity
parameter_list|()
block|{
return|return
name|severity
return|;
block|}
DECL|method|setSeverity (SyslogSeverity severity)
specifier|public
name|void
name|setSeverity
parameter_list|(
name|SyslogSeverity
name|severity
parameter_list|)
block|{
name|this
operator|.
name|severity
operator|=
name|severity
expr_stmt|;
block|}
DECL|method|getRemoteAddress ()
specifier|public
name|String
name|getRemoteAddress
parameter_list|()
block|{
return|return
name|remoteAddress
return|;
block|}
DECL|method|setRemoteAddress (String remoteAddress)
specifier|public
name|void
name|setRemoteAddress
parameter_list|(
name|String
name|remoteAddress
parameter_list|)
block|{
name|this
operator|.
name|remoteAddress
operator|=
name|remoteAddress
expr_stmt|;
block|}
DECL|method|getHostname ()
specifier|public
name|String
name|getHostname
parameter_list|()
block|{
return|return
name|hostname
return|;
block|}
DECL|method|setHostname (String hostname)
specifier|public
name|void
name|setHostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
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
literal|"SyslogMessage{"
operator|+
literal|"content='"
operator|+
name|logMessage
operator|+
literal|'\''
operator|+
literal|", facility="
operator|+
name|facility
operator|+
literal|", severity="
operator|+
name|severity
operator|+
literal|", remoteAddress='"
operator|+
name|remoteAddress
operator|+
literal|'\''
operator|+
literal|", localAddress='"
operator|+
name|localAddress
operator|+
literal|'\''
operator|+
literal|", hostname='"
operator|+
name|hostname
operator|+
literal|'\''
operator|+
literal|", messageTime="
operator|+
name|timestamp
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

