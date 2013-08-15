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

begin_class
DECL|class|SyslogConstants
specifier|public
specifier|final
class|class
name|SyslogConstants
block|{
comment|/**      * The socket address of local machine that received the message.      */
DECL|field|SYSLOG_LOCAL_ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|SYSLOG_LOCAL_ADDRESS
init|=
literal|"CamelSyslogLocalAddress"
decl_stmt|;
comment|/**      * The socket address of the remote machine that send the message.      */
DECL|field|SYSLOG_REMOTE_ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|SYSLOG_REMOTE_ADDRESS
init|=
literal|"CamelSyslogRemoteAddress"
decl_stmt|;
comment|/**      * The Sylog message Facility      */
DECL|field|SYSLOG_FACILITY
specifier|public
specifier|static
specifier|final
name|String
name|SYSLOG_FACILITY
init|=
literal|"CamelSyslogFacility"
decl_stmt|;
comment|/**      * The Syslog severity      */
DECL|field|SYSLOG_SEVERITY
specifier|public
specifier|static
specifier|final
name|String
name|SYSLOG_SEVERITY
init|=
literal|"CamelSyslogSeverity"
decl_stmt|;
comment|/**      * The hostname in the syslog message      */
DECL|field|SYSLOG_HOSTNAME
specifier|public
specifier|static
specifier|final
name|String
name|SYSLOG_HOSTNAME
init|=
literal|"CamelSyslogHostname"
decl_stmt|;
comment|/**      * The syslog timestamp      */
DECL|field|SYSLOG_TIMESTAMP
specifier|public
specifier|static
specifier|final
name|String
name|SYSLOG_TIMESTAMP
init|=
literal|"CamelSyslogTimestamp"
decl_stmt|;
DECL|method|SyslogConstants ()
specifier|private
name|SyslogConstants
parameter_list|()
block|{
comment|// Utility class
block|}
block|}
end_class

end_unit

