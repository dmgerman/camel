begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

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
name|RandomAccessFile
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
name|FileChannel
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
name|Message
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
name|Producer
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
name|impl
operator|.
name|DefaultProducer
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
name|util
operator|.
name|ExchangeHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A {@link Producer} implementation for File  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|FileProducer
specifier|public
class|class
name|FileProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FileProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|FileEndpoint
name|endpoint
decl_stmt|;
DECL|method|FileProducer (FileEndpoint endpoint)
specifier|public
name|FileProducer
parameter_list|(
name|FileEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|FileEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|FileEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
comment|/**      * @param exchange      * @see org.apache.camel.Processor#process(Exchange)      */
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// TODO is it really worth using a FileExchange as the core type?
name|FileExchange
name|fileExchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|process
argument_list|(
name|fileExchange
argument_list|)
expr_stmt|;
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|exchange
argument_list|,
name|fileExchange
argument_list|)
expr_stmt|;
block|}
DECL|method|process (FileExchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|FileExchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
comment|// lets poll the file
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|configureMessage
argument_list|(
name|endpoint
operator|.
name|getFile
argument_list|()
argument_list|,
name|out
argument_list|)
expr_stmt|;
return|return;
block|}
name|InputStream
name|in
init|=
name|ExchangeHelper
operator|.
name|getMandatoryInBody
argument_list|(
name|exchange
argument_list|,
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|File
name|file
init|=
name|createFileName
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
decl_stmt|;
name|buildDirectory
argument_list|(
name|file
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"About to write to: "
operator|+
name|file
operator|+
literal|" from exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|FileChannel
name|fc
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isAppend
argument_list|()
condition|)
block|{
name|fc
operator|=
operator|new
name|RandomAccessFile
argument_list|(
name|file
argument_list|,
literal|"rw"
argument_list|)
operator|.
name|getChannel
argument_list|()
expr_stmt|;
name|fc
operator|.
name|position
argument_list|(
name|fc
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fc
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
operator|.
name|getChannel
argument_list|()
expr_stmt|;
block|}
name|int
name|size
init|=
name|getEndpoint
argument_list|()
operator|.
name|getBufferSize
argument_list|()
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|size
index|]
decl_stmt|;
name|ByteBuffer
name|byteBuffer
init|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|count
init|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
if|if
condition|(
name|count
operator|<=
literal|0
condition|)
block|{
break|break;
block|}
elseif|else
if|if
condition|(
name|count
operator|<
name|size
condition|)
block|{
name|byteBuffer
operator|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|fc
operator|.
name|write
argument_list|(
name|byteBuffer
argument_list|)
expr_stmt|;
break|break;
block|}
else|else
block|{
name|fc
operator|.
name|write
argument_list|(
name|byteBuffer
argument_list|)
expr_stmt|;
name|byteBuffer
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to close input: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|fc
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|fc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to close output: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/*         ByteBuffer payload = exchange.getIn().getBody(ByteBuffer.class);         if (payload == null) {             InputStream in = ExchangeHelper.getMandatoryInBody(exchange, InputStream.class);             payload = ExchangeHelper.convertToMandatoryType(exchange, ByteBuffer.class, in);         }         payload.flip();         File file = createFileName(exchange);         buildDirectory(file);         if (LOG.isDebugEnabled()) {             LOG.debug("Creating file: " + file);         }         FileChannel fc = null;         try {             if (getEndpoint().isAppend()) {                 fc = new RandomAccessFile(file, "rw").getChannel();                 fc.position(fc.size());             }             else {                 fc = new FileOutputStream(file).getChannel();             }             fc.write(payload);         }         catch (Throwable e) {             LOG.error("Failed to write to File: " + file, e);         }         finally {             if (fc != null) {                 fc.close();             }         }         */
block|}
DECL|method|createFileName (Message message)
specifier|protected
name|File
name|createFileName
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|File
name|answer
decl_stmt|;
name|File
name|endpointFile
init|=
name|endpoint
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|String
name|name
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isIgnoreFileNameHeader
argument_list|()
condition|)
block|{
name|name
operator|=
name|message
operator|.
name|getHeader
argument_list|(
name|FileComponent
operator|.
name|HEADER_FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpointFile
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
operator|new
name|File
argument_list|(
name|endpointFile
argument_list|,
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|answer
operator|=
operator|new
name|File
argument_list|(
name|answer
argument_list|,
name|message
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|File
argument_list|(
name|endpointFile
argument_list|,
name|message
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|endpointFile
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|File
argument_list|(
name|endpointFile
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|buildDirectory (File file)
specifier|private
name|void
name|buildDirectory
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|String
name|dirName
init|=
name|file
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|int
name|index
init|=
name|dirName
operator|.
name|lastIndexOf
argument_list|(
name|File
operator|.
name|separatorChar
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
literal|0
condition|)
block|{
name|dirName
operator|=
name|dirName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|dirName
argument_list|)
decl_stmt|;
name|dir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

