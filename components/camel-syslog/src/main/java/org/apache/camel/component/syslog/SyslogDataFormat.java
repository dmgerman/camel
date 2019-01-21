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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|spi
operator|.
name|DataFormat
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
name|spi
operator|.
name|DataFormatName
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
name|spi
operator|.
name|annotations
operator|.
name|Dataformat
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
name|support
operator|.
name|service
operator|.
name|ServiceSupport
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
name|support
operator|.
name|ExchangeHelper
import|;
end_import

begin_class
annotation|@
name|Dataformat
argument_list|(
literal|"syslog"
argument_list|)
DECL|class|SyslogDataFormat
specifier|public
class|class
name|SyslogDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
block|{
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"syslog"
return|;
block|}
annotation|@
name|Override
DECL|method|marshal (Exchange exchange, Object body, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|SyslogMessage
name|message
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|SyslogMessage
operator|.
name|class
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|stream
operator|.
name|write
argument_list|(
name|SyslogConverter
operator|.
name|toString
argument_list|(
name|message
argument_list|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unmarshal (Exchange exchange, InputStream inputStream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|body
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|inputStream
argument_list|)
decl_stmt|;
name|SyslogMessage
name|message
init|=
name|SyslogConverter
operator|.
name|parseMessage
argument_list|(
name|body
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SyslogConstants
operator|.
name|SYSLOG_FACILITY
argument_list|,
name|message
operator|.
name|getFacility
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SyslogConstants
operator|.
name|SYSLOG_SEVERITY
argument_list|,
name|message
operator|.
name|getSeverity
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SyslogConstants
operator|.
name|SYSLOG_HOSTNAME
argument_list|,
name|message
operator|.
name|getHostname
argument_list|()
argument_list|)
expr_stmt|;
comment|// use java.util.Date as timestamp
name|Date
name|time
init|=
name|message
operator|.
name|getTimestamp
argument_list|()
operator|!=
literal|null
condition|?
name|message
operator|.
name|getTimestamp
argument_list|()
operator|.
name|getTime
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|time
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SyslogConstants
operator|.
name|SYSLOG_TIMESTAMP
argument_list|,
name|time
argument_list|)
expr_stmt|;
block|}
comment|// Since we are behind the fact of being in an Endpoint...
comment|// We need to pull in the remote/local via either Mina or Netty.
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelMinaLocalAddress"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setLocalAddress
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelMinaLocalAddress"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SyslogConstants
operator|.
name|SYSLOG_LOCAL_ADDRESS
argument_list|,
name|message
operator|.
name|getLocalAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelMinaRemoteAddress"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setRemoteAddress
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelMinaRemoteAddress"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SyslogConstants
operator|.
name|SYSLOG_REMOTE_ADDRESS
argument_list|,
name|message
operator|.
name|getRemoteAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelNettyLocalAddress"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setLocalAddress
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelNettyLocalAddress"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SyslogConstants
operator|.
name|SYSLOG_LOCAL_ADDRESS
argument_list|,
name|message
operator|.
name|getLocalAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelNettyRemoteAddress"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setRemoteAddress
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelNettyRemoteAddress"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SyslogConstants
operator|.
name|SYSLOG_REMOTE_ADDRESS
argument_list|,
name|message
operator|.
name|getRemoteAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|message
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

