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
name|InputStream
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
name|Expression
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
name|spi
operator|.
name|Language
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
name|camel
operator|.
name|util
operator|.
name|FileUtil
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
name|ObjectHelper
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
comment|/**  * Generic file producer  */
end_comment

begin_class
DECL|class|GenericFileProducer
specifier|public
class|class
name|GenericFileProducer
parameter_list|<
name|T
parameter_list|>
extends|extends
name|DefaultProducer
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|protected
specifier|final
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
decl_stmt|;
DECL|field|operations
specifier|protected
specifier|final
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
decl_stmt|;
DECL|method|GenericFileProducer (GenericFileEndpoint<T> endpoint, GenericFileOperations<T> operations)
specifier|protected
name|GenericFileProducer
parameter_list|(
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
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
name|this
operator|.
name|operations
operator|=
name|operations
expr_stmt|;
block|}
DECL|method|getFileSeparator ()
specifier|protected
name|String
name|getFileSeparator
parameter_list|()
block|{
return|return
name|File
operator|.
name|separator
return|;
block|}
DECL|method|normalizePath (String name)
specifier|protected
name|String
name|normalizePath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|FileUtil
operator|.
name|normalizePath
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
name|fileExchange
init|=
operator|(
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
operator|)
name|endpoint
operator|.
name|createExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|processExchange
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
comment|/**      * Perform the work to process the fileExchange      *      * @param exchange fileExchange      * @throws Exception is thrown if some error      */
DECL|method|processExchange (GenericFileExchange<T> exchange)
specifier|protected
name|void
name|processExchange
parameter_list|(
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Processing "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|String
name|target
init|=
name|createFileName
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|preWriteCheck
argument_list|()
expr_stmt|;
comment|// should we write to a temporary name and then afterwards rename to real target
name|boolean
name|writeAsTempAndRename
init|=
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getTempPrefix
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|tempTarget
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|writeAsTempAndRename
condition|)
block|{
comment|// compute temporary name with the temp prefix
name|tempTarget
operator|=
name|createTempFileName
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
comment|// upload the file
name|writeFile
argument_list|(
name|exchange
argument_list|,
name|tempTarget
operator|!=
literal|null
condition|?
name|tempTarget
else|:
name|target
argument_list|)
expr_stmt|;
comment|// if we did write to a temporary name then rename it to the real
comment|// name after we have written the file
if|if
condition|(
name|tempTarget
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Renaming file: ["
operator|+
name|tempTarget
operator|+
literal|"] to: ["
operator|+
name|target
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
name|boolean
name|renamed
init|=
name|operations
operator|.
name|renameFile
argument_list|(
name|tempTarget
argument_list|,
name|target
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|renamed
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot rename file from: "
operator|+
name|tempTarget
operator|+
literal|" to: "
operator|+
name|target
argument_list|)
throw|;
block|}
block|}
comment|// lets store the name we really used in the header, so end-users
comment|// can retrieve it
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_PRODUCED
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleFailedWrite
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * If we fail writing out a file, we will call this method. This hook is      * provided to disconnect from servers or clean up files we created (if needed).      */
DECL|method|handleFailedWrite (GenericFileExchange<T> exchange, Exception exception)
specifier|protected
name|void
name|handleFailedWrite
parameter_list|(
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
name|exchange
parameter_list|,
name|Exception
name|exception
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
name|exception
throw|;
block|}
comment|/**      * Perform any actions that need to occur before we write such as connecting to an FTP server etc.      */
DECL|method|preWriteCheck ()
specifier|protected
name|void
name|preWriteCheck
parameter_list|()
throws|throws
name|Exception
block|{     }
DECL|method|writeFile (GenericFileExchange<T> exchange, String fileName)
specifier|protected
name|void
name|writeFile
parameter_list|(
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
name|exchange
parameter_list|,
name|String
name|fileName
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
name|InputStream
name|payload
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
comment|// build directory if auto create is enabled
if|if
condition|(
name|endpoint
operator|.
name|isAutoCreate
argument_list|()
condition|)
block|{
comment|// use java.io.File to compute the file path
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|String
name|directory
init|=
name|file
operator|.
name|getParent
argument_list|()
decl_stmt|;
name|boolean
name|absolute
init|=
name|file
operator|.
name|isAbsolute
argument_list|()
decl_stmt|;
if|if
condition|(
name|directory
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|operations
operator|.
name|buildDirectory
argument_list|(
name|directory
argument_list|,
name|absolute
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Cannot build directory ["
operator|+
name|directory
operator|+
literal|"] (could be because of denied permissions)"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// upload
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"About to write ["
operator|+
name|fileName
operator|+
literal|"] to ["
operator|+
name|getEndpoint
argument_list|()
operator|+
literal|"] from exchange ["
operator|+
name|exchange
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
name|boolean
name|success
init|=
name|operations
operator|.
name|storeFile
argument_list|(
name|fileName
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|success
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Error writing file ["
operator|+
name|fileName
operator|+
literal|"]"
argument_list|)
throw|;
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Wrote ["
operator|+
name|fileName
operator|+
literal|"] to ["
operator|+
name|getEndpoint
argument_list|()
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|ObjectHelper
operator|.
name|close
argument_list|(
name|payload
argument_list|,
literal|"Closing payload"
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createFileName (Exchange exchange)
specifier|protected
name|String
name|createFileName
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|answer
decl_stmt|;
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// expression support
name|Expression
name|expression
init|=
name|endpoint
operator|.
name|getFileName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
comment|// the header name can be an expression too, that should override
comment|// whatever configured on the endpoint
if|if
condition|(
name|name
operator|.
name|indexOf
argument_list|(
literal|"${"
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
operator|+
literal|" contains a FileLanguage expression: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
name|Language
name|language
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"file"
argument_list|)
decl_stmt|;
name|expression
operator|=
name|language
operator|.
name|createExpression
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Filename evaluated as expression: "
operator|+
name|expression
argument_list|)
expr_stmt|;
block|}
name|name
operator|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|// flatten name
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
name|endpoint
operator|.
name|isFlatten
argument_list|()
condition|)
block|{
name|int
name|pos
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
name|getFileSeparator
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|==
operator|-
literal|1
condition|)
block|{
name|pos
operator|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
comment|// compute path by adding endpoint starting directory
name|String
name|endpointPath
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDirectory
argument_list|()
decl_stmt|;
comment|// Its a directory so we should use it as a base path for the filename
comment|// If the path isn't empty, we need to add a trailing / if it isn't already there
name|String
name|baseDir
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|endpointPath
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|baseDir
operator|=
name|endpointPath
operator|+
operator|(
name|endpointPath
operator|.
name|endsWith
argument_list|(
name|getFileSeparator
argument_list|()
argument_list|)
condition|?
literal|""
else|:
name|getFileSeparator
argument_list|()
operator|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|baseDir
operator|+
name|name
expr_stmt|;
block|}
else|else
block|{
comment|// use a generated filename if no name provided
name|answer
operator|=
name|baseDir
operator|+
name|endpoint
operator|.
name|getGeneratedFileName
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// must normalize path to cater for Windows and other OS
name|answer
operator|=
name|normalizePath
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createTempFileName (String fileName)
specifier|protected
name|String
name|createTempFileName
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
comment|// must normalize path to cater for Windows and other OS
name|fileName
operator|=
name|normalizePath
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|int
name|path
init|=
name|fileName
operator|.
name|lastIndexOf
argument_list|(
name|getFileSeparator
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|==
operator|-
literal|1
condition|)
block|{
comment|// no path
return|return
name|endpoint
operator|.
name|getTempPrefix
argument_list|()
operator|+
name|fileName
return|;
block|}
else|else
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|sb
operator|.
name|insert
argument_list|(
name|path
operator|+
literal|1
argument_list|,
name|endpoint
operator|.
name|getTempPrefix
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

