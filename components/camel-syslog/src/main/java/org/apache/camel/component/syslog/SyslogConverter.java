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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|DatatypeConverter
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
name|Converter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
DECL|class|SyslogConverter
specifier|public
specifier|final
class|class
name|SyslogConverter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SyslogConverter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|enum|MONTHS
specifier|private
enum|enum
name|MONTHS
block|{
DECL|enumConstant|jan
DECL|enumConstant|feb
DECL|enumConstant|mar
DECL|enumConstant|apr
DECL|enumConstant|may
DECL|enumConstant|jun
DECL|enumConstant|jul
DECL|enumConstant|aug
DECL|enumConstant|sep
DECL|enumConstant|oct
DECL|enumConstant|nov
DECL|enumConstant|dec
name|jan
block|,
name|feb
block|,
name|mar
block|,
name|apr
block|,
name|may
block|,
name|jun
block|,
name|jul
block|,
name|aug
block|,
name|sep
block|,
name|oct
block|,
name|nov
block|,
name|dec
block|}
DECL|field|monthValueMap
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|MONTHS
argument_list|>
name|monthValueMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|MONTHS
argument_list|>
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
block|{
name|put
argument_list|(
literal|"jan"
argument_list|,
name|MONTHS
operator|.
name|jan
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"feb"
argument_list|,
name|MONTHS
operator|.
name|feb
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"mar"
argument_list|,
name|MONTHS
operator|.
name|mar
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"apr"
argument_list|,
name|MONTHS
operator|.
name|apr
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"may"
argument_list|,
name|MONTHS
operator|.
name|may
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"jun"
argument_list|,
name|MONTHS
operator|.
name|jun
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"jul"
argument_list|,
name|MONTHS
operator|.
name|jul
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"aug"
argument_list|,
name|MONTHS
operator|.
name|aug
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"sep"
argument_list|,
name|MONTHS
operator|.
name|sep
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"oct"
argument_list|,
name|MONTHS
operator|.
name|oct
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"nov"
argument_list|,
name|MONTHS
operator|.
name|nov
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"dec"
argument_list|,
name|MONTHS
operator|.
name|dec
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|method|SyslogConverter ()
specifier|private
name|SyslogConverter
parameter_list|()
block|{
comment|// Utility class
block|}
annotation|@
name|Converter
DECL|method|toString (SyslogMessage message)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|SyslogMessage
name|message
parameter_list|)
block|{
name|boolean
name|isRfc5424
init|=
name|message
operator|instanceof
name|Rfc5424SyslogMessage
decl_stmt|;
name|StringBuilder
name|sbr
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sbr
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|.
name|getFacility
argument_list|()
operator|==
literal|null
condition|)
block|{
name|message
operator|.
name|setFacility
argument_list|(
name|SyslogFacility
operator|.
name|USER
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|.
name|getSeverity
argument_list|()
operator|==
literal|null
condition|)
block|{
name|message
operator|.
name|setSeverity
argument_list|(
name|SyslogSeverity
operator|.
name|INFO
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|.
name|getHostname
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// This is massively ugly..
try|try
block|{
name|message
operator|.
name|setHostname
argument_list|(
name|InetAddress
operator|.
name|getLocalHost
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownHostException
name|e
parameter_list|)
block|{
name|message
operator|.
name|setHostname
argument_list|(
literal|"UNKNOWN_HOST"
argument_list|)
expr_stmt|;
block|}
block|}
name|sbr
operator|.
name|append
argument_list|(
name|message
operator|.
name|getFacility
argument_list|()
operator|.
name|ordinal
argument_list|()
operator|*
literal|8
operator|+
name|message
operator|.
name|getSeverity
argument_list|()
operator|.
name|ordinal
argument_list|()
argument_list|)
expr_stmt|;
name|sbr
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
comment|// version number
if|if
condition|(
name|isRfc5424
condition|)
block|{
name|sbr
operator|.
name|append
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|sbr
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|.
name|getTimestamp
argument_list|()
operator|==
literal|null
condition|)
block|{
name|message
operator|.
name|setTimestamp
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isRfc5424
condition|)
block|{
name|sbr
operator|.
name|append
argument_list|(
name|DatatypeConverter
operator|.
name|printDateTime
argument_list|(
name|message
operator|.
name|getTimestamp
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addRfc3164TimeStamp
argument_list|(
name|sbr
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
name|sbr
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|sbr
operator|.
name|append
argument_list|(
name|message
operator|.
name|getHostname
argument_list|()
argument_list|)
expr_stmt|;
name|sbr
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
if|if
condition|(
name|isRfc5424
condition|)
block|{
name|Rfc5424SyslogMessage
name|rfc5424SyslogMessage
init|=
operator|(
name|Rfc5424SyslogMessage
operator|)
name|message
decl_stmt|;
name|sbr
operator|.
name|append
argument_list|(
name|rfc5424SyslogMessage
operator|.
name|getAppName
argument_list|()
argument_list|)
expr_stmt|;
name|sbr
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|sbr
operator|.
name|append
argument_list|(
name|rfc5424SyslogMessage
operator|.
name|getProcId
argument_list|()
argument_list|)
expr_stmt|;
name|sbr
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|sbr
operator|.
name|append
argument_list|(
name|rfc5424SyslogMessage
operator|.
name|getMsgId
argument_list|()
argument_list|)
expr_stmt|;
name|sbr
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|sbr
operator|.
name|append
argument_list|(
name|rfc5424SyslogMessage
operator|.
name|getStructuredData
argument_list|()
argument_list|)
expr_stmt|;
name|sbr
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|sbr
operator|.
name|append
argument_list|(
name|message
operator|.
name|getLogMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sbr
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toSyslogMessage (String body)
specifier|public
specifier|static
name|SyslogMessage
name|toSyslogMessage
parameter_list|(
name|String
name|body
parameter_list|)
block|{
return|return
name|parseMessage
argument_list|(
name|body
operator|.
name|getBytes
argument_list|()
argument_list|)
return|;
block|}
DECL|method|parseMessage (byte[] bytes)
specifier|public
specifier|static
name|SyslogMessage
name|parseMessage
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|ByteBuffer
name|byteBuffer
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
name|bytes
operator|.
name|length
argument_list|)
decl_stmt|;
name|byteBuffer
operator|.
name|put
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|byteBuffer
operator|.
name|rewind
argument_list|()
expr_stmt|;
name|Character
name|charFound
init|=
operator|(
name|char
operator|)
name|byteBuffer
operator|.
name|get
argument_list|()
decl_stmt|;
name|SyslogFacility
name|foundFacility
init|=
literal|null
decl_stmt|;
name|SyslogSeverity
name|foundSeverity
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|charFound
operator|!=
literal|'<'
condition|)
block|{
comment|// Ignore noise in beginning of message.
name|charFound
operator|=
operator|(
name|char
operator|)
name|byteBuffer
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
name|char
name|priChar
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|charFound
operator|==
literal|'<'
condition|)
block|{
name|int
name|facility
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|Character
operator|.
name|isDigit
argument_list|(
name|priChar
operator|=
call|(
name|char
call|)
argument_list|(
name|byteBuffer
operator|.
name|get
argument_list|()
operator|&
literal|0xff
argument_list|)
argument_list|)
condition|)
block|{
name|facility
operator|*=
literal|10
expr_stmt|;
name|facility
operator|+=
name|Character
operator|.
name|digit
argument_list|(
name|priChar
argument_list|,
literal|10
argument_list|)
expr_stmt|;
block|}
name|foundFacility
operator|=
name|SyslogFacility
operator|.
name|values
argument_list|()
index|[
name|facility
operator|>>
literal|3
index|]
expr_stmt|;
name|foundSeverity
operator|=
name|SyslogSeverity
operator|.
name|values
argument_list|()
index|[
name|facility
operator|&
literal|0x07
index|]
expr_stmt|;
block|}
if|if
condition|(
name|priChar
operator|!=
literal|'>'
condition|)
block|{
comment|// Invalid character - this is not a well defined syslog message.
name|LOG
operator|.
name|error
argument_list|(
literal|"Invalid syslog message, missing a> in the Facility/Priority part"
argument_list|)
expr_stmt|;
block|}
name|SyslogMessage
name|syslogMessage
init|=
operator|new
name|SyslogMessage
argument_list|()
decl_stmt|;
name|boolean
name|isRfc5424
init|=
literal|false
decl_stmt|;
comment|// Read next character
name|charFound
operator|=
operator|(
name|char
operator|)
name|byteBuffer
operator|.
name|get
argument_list|()
expr_stmt|;
comment|// If next character is a 1, we have probably found an rfc 5424 message
comment|// message
if|if
condition|(
name|charFound
operator|==
literal|'1'
condition|)
block|{
name|syslogMessage
operator|=
operator|new
name|Rfc5424SyslogMessage
argument_list|()
expr_stmt|;
name|isRfc5424
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
comment|// go back one to parse the rfc3164 date
name|byteBuffer
operator|.
name|position
argument_list|(
name|byteBuffer
operator|.
name|position
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|syslogMessage
operator|.
name|setFacility
argument_list|(
name|foundFacility
argument_list|)
expr_stmt|;
name|syslogMessage
operator|.
name|setSeverity
argument_list|(
name|foundSeverity
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isRfc5424
condition|)
block|{
comment|// Parse rfc 3164 date
name|syslogMessage
operator|.
name|setTimestamp
argument_list|(
name|parseRfc3164Date
argument_list|(
name|byteBuffer
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|charFound
operator|=
operator|(
name|char
operator|)
name|byteBuffer
operator|.
name|get
argument_list|()
expr_stmt|;
if|if
condition|(
name|charFound
operator|!=
literal|' '
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Invalid syslog message, missing a mandatory space after version"
argument_list|)
expr_stmt|;
block|}
comment|// This should be the timestamp
name|StringBuilder
name|date
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
operator|(
name|charFound
operator|=
call|(
name|char
call|)
argument_list|(
name|byteBuffer
operator|.
name|get
argument_list|()
operator|&
literal|0xff
argument_list|)
operator|)
operator|!=
literal|' '
condition|)
block|{
name|date
operator|.
name|append
argument_list|(
name|charFound
argument_list|)
expr_stmt|;
block|}
name|syslogMessage
operator|.
name|setTimestamp
argument_list|(
name|DatatypeConverter
operator|.
name|parseDateTime
argument_list|(
name|date
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// The host is the char sequence until the next ' '
name|StringBuilder
name|host
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
operator|(
name|charFound
operator|=
call|(
name|char
call|)
argument_list|(
name|byteBuffer
operator|.
name|get
argument_list|()
operator|&
literal|0xff
argument_list|)
operator|)
operator|!=
literal|' '
condition|)
block|{
name|host
operator|.
name|append
argument_list|(
name|charFound
argument_list|)
expr_stmt|;
block|}
name|syslogMessage
operator|.
name|setHostname
argument_list|(
name|host
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isRfc5424
condition|)
block|{
name|Rfc5424SyslogMessage
name|rfc5424SyslogMessage
init|=
operator|(
name|Rfc5424SyslogMessage
operator|)
name|syslogMessage
decl_stmt|;
name|StringBuilder
name|appName
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
operator|(
name|charFound
operator|=
call|(
name|char
call|)
argument_list|(
name|byteBuffer
operator|.
name|get
argument_list|()
operator|&
literal|0xff
argument_list|)
operator|)
operator|!=
literal|' '
condition|)
block|{
name|appName
operator|.
name|append
argument_list|(
name|charFound
argument_list|)
expr_stmt|;
block|}
name|rfc5424SyslogMessage
operator|.
name|setAppName
argument_list|(
name|appName
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|StringBuilder
name|procId
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
operator|(
name|charFound
operator|=
call|(
name|char
call|)
argument_list|(
name|byteBuffer
operator|.
name|get
argument_list|()
operator|&
literal|0xff
argument_list|)
operator|)
operator|!=
literal|' '
condition|)
block|{
name|procId
operator|.
name|append
argument_list|(
name|charFound
argument_list|)
expr_stmt|;
block|}
name|rfc5424SyslogMessage
operator|.
name|setProcId
argument_list|(
name|procId
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|StringBuilder
name|msgId
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
operator|(
name|charFound
operator|=
call|(
name|char
call|)
argument_list|(
name|byteBuffer
operator|.
name|get
argument_list|()
operator|&
literal|0xff
argument_list|)
operator|)
operator|!=
literal|' '
condition|)
block|{
name|msgId
operator|.
name|append
argument_list|(
name|charFound
argument_list|)
expr_stmt|;
block|}
name|rfc5424SyslogMessage
operator|.
name|setMsgId
argument_list|(
name|msgId
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|StringBuilder
name|structuredData
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|inblock
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|(
operator|(
name|charFound
operator|=
call|(
name|char
call|)
argument_list|(
name|byteBuffer
operator|.
name|get
argument_list|()
operator|&
literal|0xff
argument_list|)
operator|)
operator|!=
literal|' '
operator|)
operator|||
name|inblock
condition|)
block|{
if|if
condition|(
name|charFound
operator|==
literal|'['
condition|)
block|{
name|inblock
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|charFound
operator|==
literal|']'
condition|)
block|{
name|inblock
operator|=
literal|false
expr_stmt|;
block|}
name|structuredData
operator|.
name|append
argument_list|(
name|charFound
argument_list|)
expr_stmt|;
block|}
name|rfc5424SyslogMessage
operator|.
name|setStructuredData
argument_list|(
name|structuredData
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
name|byteBuffer
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|charFound
operator|=
call|(
name|char
call|)
argument_list|(
name|byteBuffer
operator|.
name|get
argument_list|()
operator|&
literal|0xff
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|charFound
argument_list|)
expr_stmt|;
block|}
name|syslogMessage
operator|.
name|setLogMessage
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Syslog message : {}"
argument_list|,
name|syslogMessage
argument_list|)
expr_stmt|;
return|return
name|syslogMessage
return|;
block|}
DECL|method|addRfc3164TimeStamp (StringBuilder sbr, SyslogMessage message)
specifier|private
specifier|static
name|void
name|addRfc3164TimeStamp
parameter_list|(
name|StringBuilder
name|sbr
parameter_list|,
name|SyslogMessage
name|message
parameter_list|)
block|{
comment|// SDF isn't going to help much here.
name|Calendar
name|cal
init|=
name|message
operator|.
name|getTimestamp
argument_list|()
decl_stmt|;
name|String
name|firstLetter
init|=
name|MONTHS
operator|.
name|values
argument_list|()
index|[
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MONTH
argument_list|)
index|]
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
decl_stmt|;
comment|// Get
comment|// first
comment|// letter
name|String
name|remainder
init|=
name|MONTHS
operator|.
name|values
argument_list|()
index|[
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MONTH
argument_list|)
index|]
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// Get
comment|// remainder
comment|// of
comment|// word.
name|String
name|capitalized
init|=
name|firstLetter
operator|.
name|toUpperCase
argument_list|()
operator|+
name|remainder
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
name|sbr
operator|.
name|append
argument_list|(
name|capitalized
argument_list|)
expr_stmt|;
name|sbr
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
if|if
condition|(
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|DAY_OF_MONTH
argument_list|)
operator|<
literal|10
condition|)
block|{
name|sbr
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|DAY_OF_MONTH
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sbr
operator|.
name|append
argument_list|(
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|DAY_OF_MONTH
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sbr
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
if|if
condition|(
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|HOUR_OF_DAY
argument_list|)
operator|<
literal|10
condition|)
block|{
name|sbr
operator|.
name|append
argument_list|(
literal|"0"
argument_list|)
operator|.
name|append
argument_list|(
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|HOUR_OF_DAY
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sbr
operator|.
name|append
argument_list|(
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|HOUR_OF_DAY
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sbr
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
if|if
condition|(
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MINUTE
argument_list|)
operator|<
literal|10
condition|)
block|{
name|sbr
operator|.
name|append
argument_list|(
literal|"0"
argument_list|)
operator|.
name|append
argument_list|(
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MINUTE
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sbr
operator|.
name|append
argument_list|(
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MINUTE
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sbr
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
if|if
condition|(
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|)
operator|<
literal|10
condition|)
block|{
name|sbr
operator|.
name|append
argument_list|(
literal|"0"
argument_list|)
operator|.
name|append
argument_list|(
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sbr
operator|.
name|append
argument_list|(
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|parseRfc3164Date (ByteBuffer byteBuffer)
specifier|private
specifier|static
name|Calendar
name|parseRfc3164Date
parameter_list|(
name|ByteBuffer
name|byteBuffer
parameter_list|)
block|{
name|char
name|charFound
decl_stmt|;
comment|// Done parsing severity and facility
comment|//<169>Oct 22 10:52:01 TZ-6 scapegoat.dmz.example.org 10.1.2.3
comment|// sched[0]: That's All Folks!
comment|// Need to parse the date.
comment|/**          * The TIMESTAMP field is the local time and is in the format of          * "Mmm dd hh:mm:ss" (without the quote marks) where: Mmm is the English          * language abbreviation for the month of the year with the first          * character in uppercase and the other two characters in lowercase. The          * following are the only acceptable values: Jan, Feb, Mar, Apr, May,          * Jun, Jul, Aug, Sep, Oct, Nov, Dec dd is the day of the month. If the          * day of the month is less than 10, then it MUST be represented as a          * space and then the number. For example, the 7th day of August would          * be represented as "Aug  7", with two spaces between the "g" and the          * "7". hh:mm:ss is the local time. The hour (hh) is represented in a          * 24-hour format. Valid entries are between 00 and 23, inclusive. The          * minute (mm) and second (ss) entries are between 00 and 59 inclusive.          */
name|char
index|[]
name|month
init|=
operator|new
name|char
index|[
literal|3
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|month
index|[
name|i
index|]
operator|=
call|(
name|char
call|)
argument_list|(
name|byteBuffer
operator|.
name|get
argument_list|()
operator|&
literal|0xff
argument_list|)
expr_stmt|;
block|}
name|charFound
operator|=
operator|(
name|char
operator|)
name|byteBuffer
operator|.
name|get
argument_list|()
expr_stmt|;
if|if
condition|(
name|charFound
operator|!=
literal|' '
condition|)
block|{
comment|// Invalid Message - missing mandatory space.
name|LOG
operator|.
name|error
argument_list|(
literal|"Invalid syslog message, missing a mandatory space after month"
argument_list|)
expr_stmt|;
block|}
name|charFound
operator|=
call|(
name|char
call|)
argument_list|(
name|byteBuffer
operator|.
name|get
argument_list|()
operator|&
literal|0xff
argument_list|)
expr_stmt|;
name|int
name|day
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|charFound
operator|==
literal|' '
condition|)
block|{
comment|// Extra space for the day - this is okay.
comment|// Just ignored per the spec.
block|}
else|else
block|{
name|day
operator|*=
literal|10
expr_stmt|;
name|day
operator|+=
name|Character
operator|.
name|digit
argument_list|(
name|charFound
argument_list|,
literal|10
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|Character
operator|.
name|isDigit
argument_list|(
name|charFound
operator|=
call|(
name|char
call|)
argument_list|(
name|byteBuffer
operator|.
name|get
argument_list|()
operator|&
literal|0xff
argument_list|)
argument_list|)
condition|)
block|{
name|day
operator|*=
literal|10
expr_stmt|;
name|day
operator|+=
name|Character
operator|.
name|digit
argument_list|(
name|charFound
argument_list|,
literal|10
argument_list|)
expr_stmt|;
block|}
name|int
name|hour
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|Character
operator|.
name|isDigit
argument_list|(
name|charFound
operator|=
call|(
name|char
call|)
argument_list|(
name|byteBuffer
operator|.
name|get
argument_list|()
operator|&
literal|0xff
argument_list|)
argument_list|)
condition|)
block|{
name|hour
operator|*=
literal|10
expr_stmt|;
name|hour
operator|+=
name|Character
operator|.
name|digit
argument_list|(
name|charFound
argument_list|,
literal|10
argument_list|)
expr_stmt|;
block|}
name|int
name|minute
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|Character
operator|.
name|isDigit
argument_list|(
name|charFound
operator|=
call|(
name|char
call|)
argument_list|(
name|byteBuffer
operator|.
name|get
argument_list|()
operator|&
literal|0xff
argument_list|)
argument_list|)
condition|)
block|{
name|minute
operator|*=
literal|10
expr_stmt|;
name|minute
operator|+=
name|Character
operator|.
name|digit
argument_list|(
name|charFound
argument_list|,
literal|10
argument_list|)
expr_stmt|;
block|}
name|int
name|second
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|Character
operator|.
name|isDigit
argument_list|(
name|charFound
operator|=
call|(
name|char
call|)
argument_list|(
name|byteBuffer
operator|.
name|get
argument_list|()
operator|&
literal|0xff
argument_list|)
argument_list|)
condition|)
block|{
name|second
operator|*=
literal|10
expr_stmt|;
name|second
operator|+=
name|Character
operator|.
name|digit
argument_list|(
name|charFound
argument_list|,
literal|10
argument_list|)
expr_stmt|;
block|}
name|Calendar
name|calendar
init|=
operator|new
name|GregorianCalendar
argument_list|()
decl_stmt|;
name|calendar
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|MONTH
argument_list|,
name|monthValueMap
operator|.
name|get
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|month
argument_list|)
operator|.
name|toLowerCase
argument_list|()
argument_list|)
operator|.
name|ordinal
argument_list|()
argument_list|)
expr_stmt|;
name|calendar
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|DAY_OF_MONTH
argument_list|,
name|day
argument_list|)
expr_stmt|;
name|calendar
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|HOUR_OF_DAY
argument_list|,
name|hour
argument_list|)
expr_stmt|;
name|calendar
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|MINUTE
argument_list|,
name|minute
argument_list|)
expr_stmt|;
name|calendar
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|,
name|second
argument_list|)
expr_stmt|;
return|return
name|calendar
return|;
block|}
block|}
end_class

end_unit

