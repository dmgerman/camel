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
comment|/**  * Generic File. Specific implementations of a file based endpoint need to  * provide a File for transfer.  */
end_comment

begin_class
DECL|class|GenericFile
specifier|public
class|class
name|GenericFile
parameter_list|<
name|T
parameter_list|>
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
name|GenericFile
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpointPath
specifier|private
name|String
name|endpointPath
decl_stmt|;
DECL|field|fileName
specifier|private
name|String
name|fileName
decl_stmt|;
DECL|field|fileNameOnly
specifier|private
name|String
name|fileNameOnly
decl_stmt|;
DECL|field|relativeFilePath
specifier|private
name|String
name|relativeFilePath
decl_stmt|;
DECL|field|absoluteFilePath
specifier|private
name|String
name|absoluteFilePath
decl_stmt|;
DECL|field|fileLength
specifier|private
name|long
name|fileLength
decl_stmt|;
DECL|field|lastModified
specifier|private
name|long
name|lastModified
decl_stmt|;
DECL|field|file
specifier|private
name|T
name|file
decl_stmt|;
DECL|field|binding
specifier|private
name|GenericFileBinding
argument_list|<
name|T
argument_list|>
name|binding
decl_stmt|;
DECL|field|absolute
specifier|private
name|boolean
name|absolute
decl_stmt|;
DECL|method|getFileSeparator ()
specifier|public
name|char
name|getFileSeparator
parameter_list|()
block|{
return|return
name|File
operator|.
name|separatorChar
return|;
block|}
comment|/**      * Creates a copy based on the source      *      * @param source the source      * @return a copy of the source      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|copyFrom (GenericFile<T> source)
specifier|public
name|GenericFile
argument_list|<
name|T
argument_list|>
name|copyFrom
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|source
parameter_list|)
block|{
name|GenericFile
argument_list|<
name|T
argument_list|>
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|source
operator|.
name|getClass
argument_list|()
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|result
operator|.
name|setEndpointPath
argument_list|(
name|source
operator|.
name|getEndpointPath
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setAbsolute
argument_list|(
name|source
operator|.
name|isAbsolute
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setAbsoluteFilePath
argument_list|(
name|source
operator|.
name|getAbsoluteFilePath
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setRelativeFilePath
argument_list|(
name|source
operator|.
name|getRelativeFilePath
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setFileName
argument_list|(
name|source
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setFileNameOnly
argument_list|(
name|source
operator|.
name|getFileNameOnly
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setFileLength
argument_list|(
name|source
operator|.
name|getFileLength
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setLastModified
argument_list|(
name|source
operator|.
name|getLastModified
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setFile
argument_list|(
name|source
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setBody
argument_list|(
name|source
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setBinding
argument_list|(
name|source
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|copyFromPopulateAdditional
argument_list|(
name|source
argument_list|,
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Copies additional information from the source to the result.      *<p/>      * Inherited classes can override this method and copy their specific data.      *      * @param source  the source      * @param result  the result      */
DECL|method|copyFromPopulateAdditional (GenericFile<T> source, GenericFile<T> result)
specifier|public
name|void
name|copyFromPopulateAdditional
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|source
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|result
parameter_list|)
block|{
comment|// noop
block|}
comment|/**      * Bind this GenericFile to an Exchange      */
DECL|method|bindToExchange (Exchange exchange)
specifier|public
name|void
name|bindToExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|FileComponent
operator|.
name|FILE_EXCHANGE_FILE
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|GenericFileMessage
argument_list|<
name|T
argument_list|>
name|in
init|=
operator|new
name|GenericFileMessage
argument_list|<
name|T
argument_list|>
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|populateHeaders
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
comment|/**      * Populates the {@link GenericFileMessage} relevant headers      *      * @param message the message to populate with headers      */
DECL|method|populateHeaders (GenericFileMessage<T> message)
specifier|public
name|void
name|populateHeaders
parameter_list|(
name|GenericFileMessage
argument_list|<
name|T
argument_list|>
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_ONLY
argument_list|,
name|getFileNameOnly
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"CamelFileAbsolute"
argument_list|,
name|isAbsolute
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"CamelFileAbsolutePath"
argument_list|,
name|getAbsoluteFilePath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isAbsolute
argument_list|()
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_PATH
argument_list|,
name|getAbsoluteFilePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we must normalize path according to protocol if we build our own paths
name|String
name|path
init|=
name|normalizePathToProtocol
argument_list|(
name|getEndpointPath
argument_list|()
operator|+
name|File
operator|.
name|separator
operator|+
name|getRelativeFilePath
argument_list|()
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_PATH
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setHeader
argument_list|(
literal|"CamelFileRelativePath"
argument_list|,
name|getRelativeFilePath
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_PARENT
argument_list|,
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getFileLength
argument_list|()
operator|>=
literal|0
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
literal|"CamelFileLength"
argument_list|,
name|getFileLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getLastModified
argument_list|()
operator|>
literal|0
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_LAST_MODIFIED
argument_list|,
operator|new
name|Date
argument_list|(
name|getLastModified
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|isAbsolute (String name)
specifier|protected
name|boolean
name|isAbsolute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|FileUtil
operator|.
name|isAbsolute
argument_list|(
operator|new
name|File
argument_list|(
name|name
argument_list|)
argument_list|)
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
comment|/**      * Changes the name of this remote file. This method alters the absolute and      * relative names as well.      *      * @param newName the new name      */
DECL|method|changeFileName (String newName)
specifier|public
name|void
name|changeFileName
parameter_list|(
name|String
name|newName
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Changing name to: {}"
argument_list|,
name|newName
argument_list|)
expr_stmt|;
comment|// Make sure the newName is normalized.
name|String
name|newFileName
init|=
name|normalizePath
argument_list|(
name|newName
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Normalized endpointPath: {}"
argument_list|,
name|endpointPath
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Normalized newFileName: ()"
argument_list|,
name|newFileName
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|newFileName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|absolute
condition|)
block|{
comment|// for relative then we should avoid having the endpoint path duplicated so clip it
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpointPath
argument_list|)
operator|&&
name|newFileName
operator|.
name|startsWith
argument_list|(
name|endpointPath
argument_list|)
condition|)
block|{
comment|// clip starting endpoint in case it was added
if|if
condition|(
name|endpointPath
operator|.
name|endsWith
argument_list|(
literal|""
operator|+
name|getFileSeparator
argument_list|()
argument_list|)
condition|)
block|{
name|newFileName
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|newFileName
argument_list|,
name|endpointPath
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newFileName
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|newFileName
argument_list|,
name|endpointPath
operator|+
name|getFileSeparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// reconstruct file with clipped name
name|file
operator|=
operator|new
name|File
argument_list|(
name|newFileName
argument_list|)
expr_stmt|;
block|}
block|}
comment|// store the file name only
name|setFileNameOnly
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setFileName
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// relative path
if|if
condition|(
name|file
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setRelativeFilePath
argument_list|(
name|file
operator|.
name|getParent
argument_list|()
operator|+
name|getFileSeparator
argument_list|()
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setRelativeFilePath
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// absolute path
if|if
condition|(
name|isAbsolute
argument_list|(
name|newFileName
argument_list|)
condition|)
block|{
name|setAbsolute
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setAbsoluteFilePath
argument_list|(
name|newFileName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setAbsolute
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// construct a pseudo absolute filename that the file operations uses even for relative only
name|String
name|path
init|=
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|endpointPath
argument_list|)
condition|?
literal|""
else|:
name|endpointPath
operator|+
name|getFileSeparator
argument_list|()
decl_stmt|;
name|setAbsoluteFilePath
argument_list|(
name|path
operator|+
name|getRelativeFilePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"FileNameOnly: {}"
argument_list|,
name|getFileNameOnly
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"FileName: {}"
argument_list|,
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Absolute: {}"
argument_list|,
name|isAbsolute
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Relative path: {}"
argument_list|,
name|getRelativeFilePath
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Absolute path: {}"
argument_list|,
name|getAbsoluteFilePath
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Name changed to: {}"
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getRelativeFilePath ()
specifier|public
name|String
name|getRelativeFilePath
parameter_list|()
block|{
return|return
name|relativeFilePath
return|;
block|}
DECL|method|setRelativeFilePath (String relativeFilePath)
specifier|public
name|void
name|setRelativeFilePath
parameter_list|(
name|String
name|relativeFilePath
parameter_list|)
block|{
name|this
operator|.
name|relativeFilePath
operator|=
name|normalizePathToProtocol
argument_list|(
name|relativeFilePath
argument_list|)
expr_stmt|;
block|}
DECL|method|getFileName ()
specifier|public
name|String
name|getFileName
parameter_list|()
block|{
return|return
name|fileName
return|;
block|}
DECL|method|setFileName (String fileName)
specifier|public
name|void
name|setFileName
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
name|this
operator|.
name|fileName
operator|=
name|normalizePathToProtocol
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
block|}
DECL|method|getFileLength ()
specifier|public
name|long
name|getFileLength
parameter_list|()
block|{
return|return
name|fileLength
return|;
block|}
DECL|method|setFileLength (long fileLength)
specifier|public
name|void
name|setFileLength
parameter_list|(
name|long
name|fileLength
parameter_list|)
block|{
name|this
operator|.
name|fileLength
operator|=
name|fileLength
expr_stmt|;
block|}
DECL|method|getLastModified ()
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
return|return
name|lastModified
return|;
block|}
DECL|method|setLastModified (long lastModified)
specifier|public
name|void
name|setLastModified
parameter_list|(
name|long
name|lastModified
parameter_list|)
block|{
name|this
operator|.
name|lastModified
operator|=
name|lastModified
expr_stmt|;
block|}
DECL|method|getFile ()
specifier|public
name|T
name|getFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
DECL|method|setFile (T file)
specifier|public
name|void
name|setFile
parameter_list|(
name|T
name|file
parameter_list|)
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
DECL|method|getBody ()
specifier|public
name|Object
name|getBody
parameter_list|()
block|{
return|return
name|getBinding
argument_list|()
operator|.
name|getBody
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|setBody (Object os)
specifier|public
name|void
name|setBody
parameter_list|(
name|Object
name|os
parameter_list|)
block|{
name|getBinding
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
DECL|method|getParent ()
specifier|public
name|String
name|getParent
parameter_list|()
block|{
name|String
name|parent
decl_stmt|;
if|if
condition|(
name|isAbsolute
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|getAbsoluteFilePath
argument_list|()
decl_stmt|;
name|File
name|path
init|=
operator|new
name|File
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|parent
operator|=
name|path
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|name
init|=
name|getRelativeFilePath
argument_list|()
decl_stmt|;
name|File
name|path
init|=
operator|new
name|File
argument_list|(
name|endpointPath
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|parent
operator|=
name|path
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
return|return
name|normalizePathToProtocol
argument_list|(
name|parent
argument_list|)
return|;
block|}
DECL|method|getBinding ()
specifier|public
name|GenericFileBinding
argument_list|<
name|T
argument_list|>
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
operator|new
name|GenericFileDefaultBinding
argument_list|<
name|T
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
DECL|method|setBinding (GenericFileBinding<T> binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|GenericFileBinding
argument_list|<
name|T
argument_list|>
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|setAbsoluteFilePath (String absoluteFilePath)
specifier|public
name|void
name|setAbsoluteFilePath
parameter_list|(
name|String
name|absoluteFilePath
parameter_list|)
block|{
name|this
operator|.
name|absoluteFilePath
operator|=
name|normalizePathToProtocol
argument_list|(
name|absoluteFilePath
argument_list|)
expr_stmt|;
block|}
DECL|method|getAbsoluteFilePath ()
specifier|public
name|String
name|getAbsoluteFilePath
parameter_list|()
block|{
return|return
name|absoluteFilePath
return|;
block|}
DECL|method|isAbsolute ()
specifier|public
name|boolean
name|isAbsolute
parameter_list|()
block|{
return|return
name|absolute
return|;
block|}
DECL|method|setAbsolute (boolean absolute)
specifier|public
name|void
name|setAbsolute
parameter_list|(
name|boolean
name|absolute
parameter_list|)
block|{
name|this
operator|.
name|absolute
operator|=
name|absolute
expr_stmt|;
block|}
DECL|method|getEndpointPath ()
specifier|public
name|String
name|getEndpointPath
parameter_list|()
block|{
return|return
name|endpointPath
return|;
block|}
DECL|method|setEndpointPath (String endpointPath)
specifier|public
name|void
name|setEndpointPath
parameter_list|(
name|String
name|endpointPath
parameter_list|)
block|{
name|this
operator|.
name|endpointPath
operator|=
name|normalizePathToProtocol
argument_list|(
name|endpointPath
argument_list|)
expr_stmt|;
block|}
DECL|method|getFileNameOnly ()
specifier|public
name|String
name|getFileNameOnly
parameter_list|()
block|{
return|return
name|fileNameOnly
return|;
block|}
DECL|method|setFileNameOnly (String fileNameOnly)
specifier|public
name|void
name|setFileNameOnly
parameter_list|(
name|String
name|fileNameOnly
parameter_list|)
block|{
name|this
operator|.
name|fileNameOnly
operator|=
name|fileNameOnly
expr_stmt|;
block|}
comment|/**      * Fixes the path separator to be according to the protocol      */
DECL|method|normalizePathToProtocol (String path)
specifier|protected
name|String
name|normalizePathToProtocol
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|path
argument_list|)
condition|)
block|{
return|return
name|path
return|;
block|}
name|path
operator|=
name|path
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
name|getFileSeparator
argument_list|()
argument_list|)
expr_stmt|;
name|path
operator|=
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
name|getFileSeparator
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|path
return|;
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
literal|"GenericFile["
operator|+
operator|(
name|absolute
condition|?
name|absoluteFilePath
else|:
name|relativeFilePath
operator|)
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

