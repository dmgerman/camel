begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.exec
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|exec
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|commons
operator|.
name|io
operator|.
name|IOUtils
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|TypeConverter
operator|.
name|MISS_VALUE
import|;
end_import

begin_comment
comment|/**  * Default converters for {@link ExecResult}. For details how to extend the  * converters check out<a  * href="http://camel.apache.org/type-converter.html">the Camel docs for type  * converters.</a>  */
end_comment

begin_class
annotation|@
name|Converter
argument_list|(
name|generateLoader
operator|=
literal|true
argument_list|)
DECL|class|ExecResultConverter
specifier|public
specifier|final
class|class
name|ExecResultConverter
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
name|ExecResultConverter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|ExecResultConverter ()
specifier|private
name|ExecResultConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|convertToInputStream (ExecResult result)
specifier|public
specifier|static
name|InputStream
name|convertToInputStream
parameter_list|(
name|ExecResult
name|result
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
return|return
name|toInputStream
argument_list|(
name|result
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToByteArray (ExecResult result, Exchange exchange)
specifier|public
specifier|static
name|byte
index|[]
name|convertToByteArray
parameter_list|(
name|ExecResult
name|result
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|IOException
block|{
name|InputStream
name|stream
init|=
name|toInputStream
argument_list|(
name|result
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|stream
argument_list|)
return|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Converter
DECL|method|convertToString (ExecResult result, Exchange exchange)
specifier|public
specifier|static
name|String
name|convertToString
parameter_list|(
name|ExecResult
name|result
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
comment|// special for string, as we want an empty string if no output from stdin / stderr
name|InputStream
name|is
init|=
name|toInputStream
argument_list|(
name|result
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|is
argument_list|)
return|;
block|}
else|else
block|{
comment|// no stdin/stdout, so return an empty string
return|return
literal|""
return|;
block|}
block|}
annotation|@
name|Converter
DECL|method|convertToDocument (ExecResult result, Exchange exchange)
specifier|public
specifier|static
name|Document
name|convertToDocument
parameter_list|(
name|ExecResult
name|result
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
return|return
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|result
argument_list|)
return|;
block|}
comment|/**      * Converts<code>ExecResult</code> to the type<code>T</code>.      *       * @param<T> The type to convert to      * @param type Class instance of the type to which to convert      * @param exchange a Camel exchange. If exchange is<code>null</code>, no      *            conversion will be made      * @param result the exec result      * @return the converted {@link ExecResult}      * @throws FileNotFoundException if there is a file in the execResult, and      *             the file can not be found      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|convertTo (Class<T> type, Exchange exchange, ExecResult result)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|ExecResult
name|result
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
name|InputStream
name|is
init|=
name|toInputStream
argument_list|(
name|result
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|is
argument_list|)
return|;
block|}
else|else
block|{
comment|// use Void to indicate we cannot convert it
comment|// (prevents Camel from using a fallback converter which may convert a String from the instance name)
return|return
operator|(
name|T
operator|)
name|MISS_VALUE
return|;
block|}
block|}
comment|/**      * Returns<code>InputStream</code> object with the<i>output</i> of the      * executable. If there is {@link ExecCommand#getOutFile()}, its content is      * preferred to {@link ExecResult#getStdout()}. If no out file is set, and      * the stdout of the exec result is<code>null</code> returns the stderr of      * the exec result.<br>      * If the output stream is of type<code>ByteArrayInputStream</code>, its      *<code>reset()</code> method is called.      *       * @param execResult ExecResult object to convert to InputStream.      * @return InputStream object with the<i>output</i> of the executable.      *         Returns<code>null</code> if both {@link ExecResult#getStdout()}      *         and {@link ExecResult#getStderr()} are<code>null</code> , or if      *         the<code>execResult</code> is<code>null</code>.      * @throws FileNotFoundException if the {@link ExecCommand#getOutFile()} can      *             not be opened. In this case the out file must have had a not      *<code>null</code> value      */
DECL|method|toInputStream (ExecResult execResult)
specifier|private
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|ExecResult
name|execResult
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
if|if
condition|(
name|execResult
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Received a null ExecResult instance to convert!"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// prefer the out file for output
name|InputStream
name|result
decl_stmt|;
if|if
condition|(
name|execResult
operator|.
name|getCommand
argument_list|()
operator|.
name|getOutFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|FileInputStream
argument_list|(
name|execResult
operator|.
name|getCommand
argument_list|()
operator|.
name|getOutFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if the stdout is null, return the stderr.
if|if
condition|(
name|execResult
operator|.
name|getStdout
argument_list|()
operator|==
literal|null
operator|&&
name|execResult
operator|.
name|getCommand
argument_list|()
operator|.
name|isUseStderrOnEmptyStdout
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"ExecResult has no stdout, will fallback to use stderr."
argument_list|)
expr_stmt|;
name|result
operator|=
name|execResult
operator|.
name|getStderr
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|execResult
operator|.
name|getStdout
argument_list|()
operator|!=
literal|null
condition|?
name|execResult
operator|.
name|getStdout
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
block|}
comment|// reset the stream if it was already read.
name|resetIfByteArrayInputStream
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Resets the stream, only if it's a ByteArrayInputStream.      */
DECL|method|resetIfByteArrayInputStream (InputStream stream)
specifier|private
specifier|static
name|void
name|resetIfByteArrayInputStream
parameter_list|(
name|InputStream
name|stream
parameter_list|)
block|{
if|if
condition|(
name|stream
operator|instanceof
name|ByteArrayInputStream
condition|)
block|{
try|try
block|{
name|stream
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Unable to reset the stream "
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

