begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.stub.tcp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|stub
operator|.
name|tcp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketException
import|;
end_import

begin_class
DECL|class|SocketStub
specifier|public
class|class
name|SocketStub
extends|extends
name|Socket
block|{
DECL|field|connected
specifier|public
name|boolean
name|connected
init|=
literal|true
decl_stmt|;
DECL|field|inputShutdown
specifier|public
name|boolean
name|inputShutdown
decl_stmt|;
DECL|field|outputShutdown
specifier|public
name|boolean
name|outputShutdown
decl_stmt|;
DECL|field|closed
specifier|public
name|boolean
name|closed
decl_stmt|;
DECL|field|receiveBufferSize
specifier|public
name|int
name|receiveBufferSize
init|=
literal|1024
decl_stmt|;
DECL|field|sendBufferSize
specifier|public
name|int
name|sendBufferSize
init|=
literal|1024
decl_stmt|;
DECL|field|timeout
specifier|public
name|int
name|timeout
init|=
literal|1000
decl_stmt|;
DECL|field|linger
specifier|public
name|boolean
name|linger
decl_stmt|;
DECL|field|lingerTimeout
specifier|public
name|int
name|lingerTimeout
init|=
literal|1024
decl_stmt|;
DECL|field|inputStreamStub
specifier|public
name|SocketInputStreamStub
name|inputStreamStub
init|=
operator|new
name|SocketInputStreamStub
argument_list|()
decl_stmt|;
DECL|field|outputStreamStub
specifier|public
name|SocketOutputStreamStub
name|outputStreamStub
init|=
operator|new
name|SocketOutputStreamStub
argument_list|()
decl_stmt|;
DECL|field|returnNullInputStream
specifier|public
name|boolean
name|returnNullInputStream
decl_stmt|;
DECL|field|returnNullOutputStream
specifier|public
name|boolean
name|returnNullOutputStream
decl_stmt|;
DECL|field|throwExceptionOnClose
specifier|public
name|boolean
name|throwExceptionOnClose
decl_stmt|;
DECL|field|throwExceptionOnShutdownInput
specifier|public
name|boolean
name|throwExceptionOnShutdownInput
decl_stmt|;
DECL|field|throwExceptionOnShutdownOutput
specifier|public
name|boolean
name|throwExceptionOnShutdownOutput
decl_stmt|;
annotation|@
name|Override
DECL|method|getInputStream ()
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|returnNullInputStream
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|isClosed
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SocketException
argument_list|(
literal|"Socket is closed"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|isConnected
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SocketException
argument_list|(
literal|"Socket is not connected"
argument_list|)
throw|;
block|}
if|if
condition|(
name|isOutputShutdown
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SocketException
argument_list|(
literal|"Socket output is shutdown"
argument_list|)
throw|;
block|}
if|if
condition|(
name|inputStreamStub
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Faking getInputStream failure"
argument_list|)
throw|;
block|}
return|return
name|inputStreamStub
return|;
block|}
annotation|@
name|Override
DECL|method|getOutputStream ()
specifier|public
name|OutputStream
name|getOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|returnNullOutputStream
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|isClosed
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SocketException
argument_list|(
literal|"Socket is closed"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|isConnected
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SocketException
argument_list|(
literal|"Socket is not connected"
argument_list|)
throw|;
block|}
if|if
condition|(
name|isOutputShutdown
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SocketException
argument_list|(
literal|"Socket output is shutdown"
argument_list|)
throw|;
block|}
if|if
condition|(
name|outputStreamStub
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Faking getOutputStream failure"
argument_list|)
throw|;
block|}
return|return
name|outputStreamStub
return|;
block|}
annotation|@
name|Override
DECL|method|setSoLinger (boolean on, int linger)
specifier|public
name|void
name|setSoLinger
parameter_list|(
name|boolean
name|on
parameter_list|,
name|int
name|linger
parameter_list|)
throws|throws
name|SocketException
block|{
name|this
operator|.
name|linger
operator|=
name|on
expr_stmt|;
name|this
operator|.
name|lingerTimeout
operator|=
name|linger
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSoLinger ()
specifier|public
name|int
name|getSoLinger
parameter_list|()
throws|throws
name|SocketException
block|{
if|if
condition|(
name|linger
condition|)
block|{
return|return
name|lingerTimeout
return|;
block|}
return|return
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|isConnected ()
specifier|public
name|boolean
name|isConnected
parameter_list|()
block|{
return|return
name|connected
return|;
block|}
annotation|@
name|Override
DECL|method|isInputShutdown ()
specifier|public
name|boolean
name|isInputShutdown
parameter_list|()
block|{
return|return
name|inputShutdown
return|;
block|}
annotation|@
name|Override
DECL|method|isOutputShutdown ()
specifier|public
name|boolean
name|isOutputShutdown
parameter_list|()
block|{
return|return
name|outputShutdown
return|;
block|}
annotation|@
name|Override
DECL|method|isClosed ()
specifier|public
name|boolean
name|isClosed
parameter_list|()
block|{
return|return
name|closed
return|;
block|}
annotation|@
name|Override
DECL|method|shutdownInput ()
specifier|public
name|void
name|shutdownInput
parameter_list|()
throws|throws
name|IOException
block|{
name|inputShutdown
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|throwExceptionOnShutdownInput
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Faking a shutdownInput failure"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|shutdownOutput ()
specifier|public
name|void
name|shutdownOutput
parameter_list|()
throws|throws
name|IOException
block|{
name|outputShutdown
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|throwExceptionOnShutdownOutput
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Faking a shutdownOutput failure"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
specifier|synchronized
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|closed
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|throwExceptionOnClose
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Faking a close failure"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getReceiveBufferSize ()
specifier|public
specifier|synchronized
name|int
name|getReceiveBufferSize
parameter_list|()
throws|throws
name|SocketException
block|{
return|return
name|receiveBufferSize
return|;
block|}
annotation|@
name|Override
DECL|method|setReceiveBufferSize (int size)
specifier|public
specifier|synchronized
name|void
name|setReceiveBufferSize
parameter_list|(
name|int
name|size
parameter_list|)
throws|throws
name|SocketException
block|{
name|this
operator|.
name|receiveBufferSize
operator|=
name|size
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSendBufferSize ()
specifier|public
specifier|synchronized
name|int
name|getSendBufferSize
parameter_list|()
throws|throws
name|SocketException
block|{
return|return
name|sendBufferSize
return|;
block|}
annotation|@
name|Override
DECL|method|setSendBufferSize (int size)
specifier|public
specifier|synchronized
name|void
name|setSendBufferSize
parameter_list|(
name|int
name|size
parameter_list|)
throws|throws
name|SocketException
block|{
name|this
operator|.
name|sendBufferSize
operator|=
name|size
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSoTimeout ()
specifier|public
specifier|synchronized
name|int
name|getSoTimeout
parameter_list|()
throws|throws
name|SocketException
block|{
return|return
name|timeout
return|;
block|}
annotation|@
name|Override
DECL|method|setSoTimeout (int timeout)
specifier|public
specifier|synchronized
name|void
name|setSoTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
throws|throws
name|SocketException
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
block|}
end_class

end_unit

