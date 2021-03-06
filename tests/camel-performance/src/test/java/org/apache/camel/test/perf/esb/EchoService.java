begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.perf.esb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|perf
operator|.
name|esb
package|;
end_package

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
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|Channels
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|ReadableByteChannel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|WritableByteChannel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServlet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_class
DECL|class|EchoService
specifier|public
class|class
name|EchoService
extends|extends
name|HttpServlet
block|{
DECL|field|delayMillis
specifier|public
specifier|static
specifier|volatile
name|long
name|delayMillis
decl_stmt|;
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|DEFAULT_BUFFER_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_BUFFER_SIZE
init|=
literal|4
decl_stmt|;
DECL|method|doPost (HttpServletRequest request, HttpServletResponse response)
specifier|public
name|void
name|doPost
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
block|{
name|ByteBuffer
name|bb
decl_stmt|;
name|List
argument_list|<
name|ByteBuffer
argument_list|>
name|bbList
init|=
literal|null
decl_stmt|;
try|try
block|{
name|int
name|bufKBytes
init|=
name|DEFAULT_BUFFER_SIZE
decl_stmt|;
name|int
name|delaySecs
init|=
literal|0
decl_stmt|;
name|String
name|soapAction
init|=
name|request
operator|.
name|getHeader
argument_list|(
literal|"SOAPAction"
argument_list|)
decl_stmt|;
if|if
condition|(
name|soapAction
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|soapAction
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|soapAction
operator|=
name|soapAction
operator|.
name|replaceAll
argument_list|(
literal|"\""
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
name|int
name|dotPos
init|=
name|soapAction
operator|.
name|indexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
name|int
name|secondDotPos
init|=
name|dotPos
operator|==
operator|-
literal|1
condition|?
operator|-
literal|1
else|:
name|soapAction
operator|.
name|indexOf
argument_list|(
literal|"."
argument_list|,
name|dotPos
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|secondDotPos
operator|>
literal|0
condition|)
block|{
name|bufKBytes
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|soapAction
operator|.
name|substring
argument_list|(
name|dotPos
operator|+
literal|1
argument_list|,
name|secondDotPos
argument_list|)
argument_list|)
expr_stmt|;
name|delaySecs
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|soapAction
operator|.
name|substring
argument_list|(
name|secondDotPos
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dotPos
operator|>
literal|0
condition|)
block|{
name|bufKBytes
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|soapAction
operator|.
name|substring
argument_list|(
name|dotPos
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|bb
operator|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
name|bufKBytes
operator|*
literal|1024
argument_list|)
expr_stmt|;
name|ReadableByteChannel
name|rbc
init|=
name|Channels
operator|.
name|newChannel
argument_list|(
name|request
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|len
init|=
literal|0
decl_stmt|;
name|int
name|tot
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|len
operator|=
name|rbc
operator|.
name|read
argument_list|(
name|bb
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|tot
operator|+=
name|len
expr_stmt|;
if|if
condition|(
name|tot
operator|>=
name|bb
operator|.
name|capacity
argument_list|()
condition|)
block|{
comment|// --- auto expand logic ---
if|if
condition|(
name|bbList
operator|==
literal|null
condition|)
block|{
name|bbList
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|bb
operator|.
name|flip
argument_list|()
expr_stmt|;
name|bbList
operator|.
name|add
argument_list|(
name|bb
argument_list|)
expr_stmt|;
name|bufKBytes
operator|=
name|bufKBytes
operator|*
literal|2
expr_stmt|;
name|bb
operator|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
name|bufKBytes
operator|*
literal|1024
argument_list|)
expr_stmt|;
block|}
block|}
name|bb
operator|.
name|flip
argument_list|()
expr_stmt|;
comment|// sleep when a "sleep" header exists - but if "port" is also specified, only when it matches
name|String
name|sleep
init|=
name|request
operator|.
name|getHeader
argument_list|(
literal|"sleep"
argument_list|)
decl_stmt|;
if|if
condition|(
name|sleep
operator|!=
literal|null
condition|)
block|{
name|String
name|port
init|=
name|request
operator|.
name|getHeader
argument_list|(
literal|"port"
argument_list|)
decl_stmt|;
if|if
condition|(
name|port
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|request
operator|.
name|getLocalPort
argument_list|()
operator|==
name|Integer
operator|.
name|parseInt
argument_list|(
name|port
argument_list|)
condition|)
block|{
name|Long
name|sleepVal
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|sleep
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Echo Service on port : "
operator|+
name|port
operator|+
literal|" sleeping for : "
operator|+
name|sleepVal
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|sleepVal
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Long
name|sleepVal
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|sleep
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Echo Service on port : "
operator|+
name|request
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|" sleeping for : "
operator|+
name|sleepVal
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|sleepVal
argument_list|)
expr_stmt|;
block|}
block|}
comment|// --- auto expand logic ---
if|if
condition|(
name|bbList
operator|!=
literal|null
condition|)
block|{
name|bbList
operator|.
name|add
argument_list|(
name|bb
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|delaySecs
operator|>
literal|0
operator|&&
name|request
operator|.
name|getLocalPort
argument_list|()
operator|==
literal|9000
condition|)
block|{
comment|// sleep only when running on port 9000
name|Thread
operator|.
name|sleep
argument_list|(
name|delaySecs
operator|*
literal|1000
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|delayMillis
operator|>
literal|0
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|delayMillis
argument_list|)
expr_stmt|;
block|}
name|response
operator|.
name|setContentType
argument_list|(
name|request
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|setHeader
argument_list|(
literal|"port"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|request
operator|.
name|getLocalPort
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|//System.out.println("Reply from Echo service on port : " + request.getLocalPort());
name|OutputStream
name|out
init|=
name|response
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|WritableByteChannel
name|wbc
init|=
name|Channels
operator|.
name|newChannel
argument_list|(
name|out
argument_list|)
decl_stmt|;
if|if
condition|(
name|bbList
operator|==
literal|null
condition|)
block|{
do|do
block|{
name|len
operator|=
name|wbc
operator|.
name|write
argument_list|(
name|bb
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|len
operator|>
literal|0
condition|)
do|;
block|}
else|else
block|{
comment|// --- auto expand logic ---
for|for
control|(
name|ByteBuffer
name|b
range|:
name|bbList
control|)
block|{
do|do
block|{
name|len
operator|=
name|wbc
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|len
operator|>
literal|0
condition|)
do|;
block|}
block|}
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

