begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|UnsupportedCharsetException
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

begin_comment
comment|/**  * IO helper class.  *  * @version   */
end_comment

begin_class
DECL|class|IOHelper
specifier|public
specifier|final
class|class
name|IOHelper
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|IOHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_BUFFER_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_BUFFER_SIZE
init|=
literal|1024
operator|*
literal|4
decl_stmt|;
DECL|field|UTF8_CHARSET
specifier|private
specifier|static
specifier|final
name|Charset
name|UTF8_CHARSET
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
DECL|method|IOHelper ()
specifier|private
name|IOHelper
parameter_list|()
block|{
comment|// Utility Class
block|}
comment|/**      * Use this function instead of new String(byte[]) to avoid surprises from non-standard default encodings.      */
DECL|method|newStringFromBytes (byte[] bytes)
specifier|public
specifier|static
name|String
name|newStringFromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
name|UTF8_CHARSET
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Impossible failure: Charset.forName(\"UTF-8\") returns invalid name."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Use this function instead of new String(byte[], int, int)       * to avoid surprises from non-standard default encodings.      */
DECL|method|newStringFromBytes (byte[] bytes, int start, int length)
specifier|public
specifier|static
name|String
name|newStringFromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|length
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
name|start
argument_list|,
name|length
argument_list|,
name|UTF8_CHARSET
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Impossible failure: Charset.forName(\"UTF-8\") returns invalid name."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Wraps the passed<code>in</code> into a {@link BufferedInputStream}      * object and returns that. If the passed<code>in</code> is already an      * instance of {@link BufferedInputStream} returns the same passed      *<code>in</code> reference as is (avoiding double wrapping).      *       * @param in the wrapee to be used for the buffering support      * @return the passed<code>in</code> decorated through a      *         {@link BufferedInputStream} object as wrapper      */
DECL|method|buffered (InputStream in)
specifier|public
specifier|static
name|BufferedInputStream
name|buffered
parameter_list|(
name|InputStream
name|in
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|in
argument_list|,
literal|"in"
argument_list|)
expr_stmt|;
return|return
operator|(
name|in
operator|instanceof
name|BufferedInputStream
operator|)
condition|?
operator|(
name|BufferedInputStream
operator|)
name|in
else|:
operator|new
name|BufferedInputStream
argument_list|(
name|in
argument_list|)
return|;
block|}
comment|/**      * Wraps the passed<code>out</code> into a {@link BufferedOutputStream}      * object and returns that. If the passed<code>out</code> is already an      * instance of {@link BufferedOutputStream} returns the same passed      *<code>out</code> reference as is (avoiding double wrapping).      *       * @param out the wrapee to be used for the buffering support      * @return the passed<code>out</code> decorated through a      *         {@link BufferedOutputStream} object as wrapper      */
DECL|method|buffered (OutputStream out)
specifier|public
specifier|static
name|BufferedOutputStream
name|buffered
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|out
argument_list|,
literal|"out"
argument_list|)
expr_stmt|;
return|return
operator|(
name|out
operator|instanceof
name|BufferedOutputStream
operator|)
condition|?
operator|(
name|BufferedOutputStream
operator|)
name|out
else|:
operator|new
name|BufferedOutputStream
argument_list|(
name|out
argument_list|)
return|;
block|}
comment|/**      * Wraps the passed<code>reader</code> into a {@link BufferedReader} object      * and returns that. If the passed<code>reader</code> is already an      * instance of {@link BufferedReader} returns the same passed      *<code>reader</code> reference as is (avoiding double wrapping).      *       * @param reader the wrapee to be used for the buffering support      * @return the passed<code>reader</code> decorated through a      *         {@link BufferedReader} object as wrapper      */
DECL|method|buffered (Reader reader)
specifier|public
specifier|static
name|BufferedReader
name|buffered
parameter_list|(
name|Reader
name|reader
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|reader
argument_list|,
literal|"reader"
argument_list|)
expr_stmt|;
return|return
operator|(
name|reader
operator|instanceof
name|BufferedReader
operator|)
condition|?
operator|(
name|BufferedReader
operator|)
name|reader
else|:
operator|new
name|BufferedReader
argument_list|(
name|reader
argument_list|)
return|;
block|}
comment|/**      * Wraps the passed<code>writer</code> into a {@link BufferedWriter} object      * and returns that. If the passed<code>writer</code> is already an      * instance of {@link BufferedWriter} returns the same passed      *<code>writer</code> reference as is (avoiding double wrapping).      *       * @param writer the wrapee to be used for the buffering support      * @return the passed<code>writer</code> decorated through a      *         {@link BufferedWriter} object as wrapper      */
DECL|method|buffered (Writer writer)
specifier|public
specifier|static
name|BufferedWriter
name|buffered
parameter_list|(
name|Writer
name|writer
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|writer
argument_list|,
literal|"writer"
argument_list|)
expr_stmt|;
return|return
operator|(
name|writer
operator|instanceof
name|BufferedWriter
operator|)
condition|?
operator|(
name|BufferedWriter
operator|)
name|writer
else|:
operator|new
name|BufferedWriter
argument_list|(
name|writer
argument_list|)
return|;
block|}
comment|/**      * A factory method which creates an {@link IOException} from the given      * exception and message      *      * @deprecated IOException support nested exception in Java 1.6. Will be removed in Camel 3.0      */
annotation|@
name|Deprecated
DECL|method|createIOException (Throwable cause)
specifier|public
specifier|static
name|IOException
name|createIOException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
return|return
name|createIOException
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|,
name|cause
argument_list|)
return|;
block|}
comment|/**      * A factory method which creates an {@link IOException} from the given      * exception and message      *      * @deprecated IOException support nested exception in Java 1.6. Will be removed in Camel 3.0      */
annotation|@
name|Deprecated
DECL|method|createIOException (String message, Throwable cause)
specifier|public
specifier|static
name|IOException
name|createIOException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|IOException
name|answer
init|=
operator|new
name|IOException
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|answer
operator|.
name|initCause
argument_list|(
name|cause
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|copy (InputStream input, OutputStream output)
specifier|public
specifier|static
name|int
name|copy
parameter_list|(
name|InputStream
name|input
parameter_list|,
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|copy
argument_list|(
name|input
argument_list|,
name|output
argument_list|,
name|DEFAULT_BUFFER_SIZE
argument_list|)
return|;
block|}
DECL|method|copy (final InputStream input, final OutputStream output, int bufferSize)
specifier|public
specifier|static
name|int
name|copy
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|,
specifier|final
name|OutputStream
name|output
parameter_list|,
name|int
name|bufferSize
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|avail
init|=
name|input
operator|.
name|available
argument_list|()
decl_stmt|;
if|if
condition|(
name|avail
operator|>
literal|262144
condition|)
block|{
name|avail
operator|=
literal|262144
expr_stmt|;
block|}
if|if
condition|(
name|avail
operator|>
name|bufferSize
condition|)
block|{
name|bufferSize
operator|=
name|avail
expr_stmt|;
block|}
specifier|final
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|bufferSize
index|]
decl_stmt|;
name|int
name|n
init|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|int
name|total
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
name|n
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|total
operator|+=
name|n
expr_stmt|;
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
name|output
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|total
return|;
block|}
DECL|method|copyAndCloseInput (InputStream input, OutputStream output)
specifier|public
specifier|static
name|void
name|copyAndCloseInput
parameter_list|(
name|InputStream
name|input
parameter_list|,
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|copyAndCloseInput
argument_list|(
name|input
argument_list|,
name|output
argument_list|,
name|DEFAULT_BUFFER_SIZE
argument_list|)
expr_stmt|;
block|}
DECL|method|copyAndCloseInput (InputStream input, OutputStream output, int bufferSize)
specifier|public
specifier|static
name|void
name|copyAndCloseInput
parameter_list|(
name|InputStream
name|input
parameter_list|,
name|OutputStream
name|output
parameter_list|,
name|int
name|bufferSize
parameter_list|)
throws|throws
name|IOException
block|{
name|copy
argument_list|(
name|input
argument_list|,
name|output
argument_list|,
name|bufferSize
argument_list|)
expr_stmt|;
name|close
argument_list|(
name|input
argument_list|,
literal|null
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
DECL|method|copy (final Reader input, final Writer output, int bufferSize)
specifier|public
specifier|static
name|int
name|copy
parameter_list|(
specifier|final
name|Reader
name|input
parameter_list|,
specifier|final
name|Writer
name|output
parameter_list|,
name|int
name|bufferSize
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|char
index|[]
name|buffer
init|=
operator|new
name|char
index|[
name|bufferSize
index|]
decl_stmt|;
name|int
name|n
init|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|int
name|total
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
name|n
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|total
operator|+=
name|n
expr_stmt|;
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
name|output
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|total
return|;
block|}
comment|/**      * Forces any updates to this channel's file to be written to the storage device that contains it.      *      * @param channel the file channel      * @param name the name of the resource      * @param log the log to use when reporting warnings, will use this class's own {@link Logger} if<tt>log == null</tt>      */
DECL|method|force (FileChannel channel, String name, Logger log)
specifier|public
specifier|static
name|void
name|force
parameter_list|(
name|FileChannel
name|channel
parameter_list|,
name|String
name|name
parameter_list|,
name|Logger
name|log
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|channel
operator|!=
literal|null
condition|)
block|{
name|channel
operator|.
name|force
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|log
operator|==
literal|null
condition|)
block|{
comment|// then fallback to use the own Logger
name|log
operator|=
name|LOG
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot force FileChannel: "
operator|+
name|name
operator|+
literal|". Reason: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot force FileChannel. Reason: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Forces any updates to a FileOutputStream be written to the storage device that contains it.      *      * @param os the file output stream      * @param name the name of the resource      * @param log the log to use when reporting warnings, will use this class's own {@link Logger} if<tt>log == null</tt>      */
DECL|method|force (FileOutputStream os, String name, Logger log)
specifier|public
specifier|static
name|void
name|force
parameter_list|(
name|FileOutputStream
name|os
parameter_list|,
name|String
name|name
parameter_list|,
name|Logger
name|log
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|os
operator|!=
literal|null
condition|)
block|{
name|os
operator|.
name|getFD
argument_list|()
operator|.
name|sync
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|log
operator|==
literal|null
condition|)
block|{
comment|// then fallback to use the own Logger
name|log
operator|=
name|LOG
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot sync FileDescriptor: "
operator|+
name|name
operator|+
literal|". Reason: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot sync FileDescriptor. Reason: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Closes the given writer, logging any closing exceptions to the given log.      * An associated FileOutputStream can optionally be forced to disk.      *      * @param writer the writer to close      * @param os an underlying FileOutputStream that will to be forced to disk according to the the force parameter      * @param name the name of the resource      * @param log the log to use when reporting warnings, will use this class's own {@link Logger} if<tt>log == null</tt>      * @param force forces the FileOutputStream to disk      */
DECL|method|close (Writer writer, FileOutputStream os, String name, Logger log, boolean force)
specifier|public
specifier|static
name|void
name|close
parameter_list|(
name|Writer
name|writer
parameter_list|,
name|FileOutputStream
name|os
parameter_list|,
name|String
name|name
parameter_list|,
name|Logger
name|log
parameter_list|,
name|boolean
name|force
parameter_list|)
block|{
if|if
condition|(
name|writer
operator|!=
literal|null
operator|&&
name|force
condition|)
block|{
comment|// flush the writer prior to syncing the FD
try|try
block|{
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|log
operator|==
literal|null
condition|)
block|{
comment|// then fallback to use the own Logger
name|log
operator|=
name|LOG
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot flush Writer: "
operator|+
name|name
operator|+
literal|". Reason: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot flush Writer. Reason: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|force
argument_list|(
name|os
argument_list|,
name|name
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
name|close
argument_list|(
name|writer
argument_list|,
name|name
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
comment|/**      * Closes the given resource if it is available, logging any closing exceptions to the given log.      *      * @param closeable the object to close      * @param name the name of the resource      * @param log the log to use when reporting closure warnings, will use this class's own {@link Logger} if<tt>log == null</tt>      */
DECL|method|close (Closeable closeable, String name, Logger log)
specifier|public
specifier|static
name|void
name|close
parameter_list|(
name|Closeable
name|closeable
parameter_list|,
name|String
name|name
parameter_list|,
name|Logger
name|log
parameter_list|)
block|{
if|if
condition|(
name|closeable
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|closeable
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
if|if
condition|(
name|log
operator|==
literal|null
condition|)
block|{
comment|// then fallback to use the own Logger
name|log
operator|=
name|LOG
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot close: "
operator|+
name|name
operator|+
literal|". Reason: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot close. Reason: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Closes the given channel if it is available, logging any closing exceptions to the given log.      * The file's channel can optionally be forced to disk.      *      * @param channel the file channel      * @param name the name of the resource      * @param log the log to use when reporting warnings, will use this class's own {@link Logger} if<tt>log == null</tt>      * @param force forces the file channel to disk      */
DECL|method|close (FileChannel channel, String name, Logger log, boolean force)
specifier|public
specifier|static
name|void
name|close
parameter_list|(
name|FileChannel
name|channel
parameter_list|,
name|String
name|name
parameter_list|,
name|Logger
name|log
parameter_list|,
name|boolean
name|force
parameter_list|)
block|{
if|if
condition|(
name|force
condition|)
block|{
name|force
argument_list|(
name|channel
argument_list|,
name|name
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
name|close
argument_list|(
name|channel
argument_list|,
name|name
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
comment|/**      * Closes the given resource if it is available.      *      * @param closeable the object to close      * @param name the name of the resource      */
DECL|method|close (Closeable closeable, String name)
specifier|public
specifier|static
name|void
name|close
parameter_list|(
name|Closeable
name|closeable
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|close
argument_list|(
name|closeable
argument_list|,
name|name
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
comment|/**      * Closes the given resource if it is available.      *      * @param closeable the object to close      */
DECL|method|close (Closeable closeable)
specifier|public
specifier|static
name|void
name|close
parameter_list|(
name|Closeable
name|closeable
parameter_list|)
block|{
name|close
argument_list|(
name|closeable
argument_list|,
literal|null
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
comment|/**      * Closes the given resources if they are available.      *       * @param closeables the objects to close      */
DECL|method|close (Closeable... closeables)
specifier|public
specifier|static
name|void
name|close
parameter_list|(
name|Closeable
modifier|...
name|closeables
parameter_list|)
block|{
for|for
control|(
name|Closeable
name|closeable
range|:
name|closeables
control|)
block|{
name|close
argument_list|(
name|closeable
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|validateCharset (String charset)
specifier|public
specifier|static
name|void
name|validateCharset
parameter_list|(
name|String
name|charset
parameter_list|)
throws|throws
name|UnsupportedCharsetException
block|{
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|Charset
operator|.
name|isSupported
argument_list|(
name|charset
argument_list|)
condition|)
block|{
name|Charset
operator|.
name|forName
argument_list|(
name|charset
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
throw|throw
operator|new
name|UnsupportedCharsetException
argument_list|(
name|charset
argument_list|)
throw|;
block|}
comment|/**      * This method will take off the quotes and double quotes of the charset      */
DECL|method|normalizeCharset (String charset)
specifier|public
specifier|static
name|String
name|normalizeCharset
parameter_list|(
name|String
name|charset
parameter_list|)
block|{
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
name|String
name|answer
init|=
name|charset
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
operator|.
name|startsWith
argument_list|(
literal|"'"
argument_list|)
operator|||
name|answer
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|answer
operator|=
name|answer
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|.
name|endsWith
argument_list|(
literal|"'"
argument_list|)
operator|||
name|answer
operator|.
name|endsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|answer
operator|=
name|answer
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|answer
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
operator|.
name|trim
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|getCharsetName (Exchange exchange)
specifier|public
specifier|static
name|String
name|getCharsetName
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getCharsetName
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Gets the charset name if set as property {@link Exchange#CHARSET_NAME}.      *      * @param exchange  the exchange      * @param useDefault should we fallback and use JVM default charset if no property existed?      * @return the charset, or<tt>null</tt> if no found      */
DECL|method|getCharsetName (Exchange exchange, boolean useDefault)
specifier|public
specifier|static
name|String
name|getCharsetName
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|useDefault
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|String
name|charsetName
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|charsetName
operator|!=
literal|null
condition|)
block|{
return|return
name|IOHelper
operator|.
name|normalizeCharset
argument_list|(
name|charsetName
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|useDefault
condition|)
block|{
return|return
name|getDefaultCharsetName
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|getDefaultCharsetName ()
specifier|private
specifier|static
name|String
name|getDefaultCharsetName
parameter_list|()
block|{
return|return
name|ObjectHelper
operator|.
name|getSystemProperty
argument_list|(
name|Exchange
operator|.
name|DEFAULT_CHARSET_PROPERTY
argument_list|,
literal|"UTF-8"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

