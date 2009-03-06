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
name|Serializable
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
implements|implements
name|Serializable
block|{
DECL|field|endpointPath
specifier|private
name|String
name|endpointPath
decl_stmt|;
DECL|field|absoluteFileName
specifier|private
name|String
name|absoluteFileName
decl_stmt|;
DECL|field|canonicalFileName
specifier|private
name|String
name|canonicalFileName
decl_stmt|;
DECL|field|relativeFileName
specifier|private
name|String
name|relativeFileName
decl_stmt|;
DECL|field|fileName
specifier|private
name|String
name|fileName
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
annotation|@
name|Override
DECL|method|clone ()
specifier|public
name|GenericFile
argument_list|<
name|T
argument_list|>
name|clone
parameter_list|()
block|{
return|return
name|copyFrom
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Creates a clone based on the source      *      * @param source the source      * @return a clone of the source      */
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
name|setAbsoluteFileName
argument_list|(
name|source
operator|.
name|getAbsoluteFileName
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setCanonicalFileName
argument_list|(
name|source
operator|.
name|getCanonicalFileName
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setRelativeFileName
argument_list|(
name|source
operator|.
name|getRelativeFileName
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
return|return
name|result
return|;
block|}
DECL|method|needToNormalize ()
specifier|public
name|boolean
name|needToNormalize
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getFileSeparator ()
specifier|public
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
name|newName
operator|=
name|needToNormalize
argument_list|()
condition|?
name|FileUtil
operator|.
name|normalizePath
argument_list|(
name|newName
argument_list|)
else|:
name|newName
expr_stmt|;
comment|// is it relative or absolute
name|boolean
name|absolute
init|=
name|isAbsolutePath
argument_list|(
name|newName
argument_list|)
decl_stmt|;
name|boolean
name|nameChangeOnly
init|=
name|newName
operator|.
name|indexOf
argument_list|(
name|getFileSeparator
argument_list|()
argument_list|)
operator|==
operator|-
literal|1
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|newName
argument_list|)
decl_stmt|;
if|if
condition|(
name|absolute
condition|)
block|{
name|setAbsolute
argument_list|(
literal|true
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
try|try
block|{
name|setCanonicalFileName
argument_list|(
name|file
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|setRelativeFileName
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|setAbsoluteFileName
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
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
name|setFileName
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|setCanonicalFileName
argument_list|(
name|file
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
if|if
condition|(
name|nameChangeOnly
condition|)
block|{
name|setRelativeFileName
argument_list|(
name|changeNameOnly
argument_list|(
name|getRelativeFileName
argument_list|()
argument_list|,
name|file
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
name|setRelativeFileName
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
name|setRelativeFileName
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// construct a pseudo absolute filename that the file operations uses
name|setAbsoluteFileName
argument_list|(
name|endpointPath
operator|+
name|getFileSeparator
argument_list|()
operator|+
name|getRelativeFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|changeNameOnly (String path, String name)
specifier|private
name|String
name|changeNameOnly
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|int
name|pos
init|=
name|path
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
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
operator|+
literal|1
argument_list|)
operator|+
name|name
return|;
block|}
else|else
block|{
return|return
name|name
return|;
block|}
block|}
DECL|method|isAbsolutePath (String path)
specifier|private
name|boolean
name|isAbsolutePath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|path
argument_list|)
operator|.
name|isAbsolute
argument_list|()
return|;
block|}
DECL|method|getRelativeFileName ()
specifier|public
name|String
name|getRelativeFileName
parameter_list|()
block|{
return|return
name|relativeFileName
return|;
block|}
DECL|method|setRelativeFileName (String relativeFileName)
specifier|public
name|void
name|setRelativeFileName
parameter_list|(
name|String
name|relativeFileName
parameter_list|)
block|{
name|this
operator|.
name|relativeFileName
operator|=
name|needToNormalize
argument_list|()
condition|?
name|FileUtil
operator|.
name|normalizePath
argument_list|(
name|relativeFileName
argument_list|)
else|:
name|relativeFileName
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
name|fileName
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
if|if
condition|(
name|isAbsolute
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|getAbsoluteFileName
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
return|return
name|path
operator|.
name|getParent
argument_list|()
return|;
block|}
else|else
block|{
name|String
name|name
init|=
name|getRelativeFileName
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
return|return
name|path
operator|.
name|getParent
argument_list|()
return|;
block|}
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
DECL|method|setAbsoluteFileName (String absoluteFileName)
specifier|public
name|void
name|setAbsoluteFileName
parameter_list|(
name|String
name|absoluteFileName
parameter_list|)
block|{
name|this
operator|.
name|absoluteFileName
operator|=
name|needToNormalize
argument_list|()
condition|?
name|FileUtil
operator|.
name|normalizePath
argument_list|(
name|absoluteFileName
argument_list|)
else|:
name|absoluteFileName
expr_stmt|;
block|}
DECL|method|getAbsoluteFileName ()
specifier|public
name|String
name|getAbsoluteFileName
parameter_list|()
block|{
return|return
name|absoluteFileName
return|;
block|}
DECL|method|getCanonicalFileName ()
specifier|public
name|String
name|getCanonicalFileName
parameter_list|()
block|{
return|return
name|canonicalFileName
return|;
block|}
DECL|method|setCanonicalFileName (String canonicalFileName)
specifier|public
name|void
name|setCanonicalFileName
parameter_list|(
name|String
name|canonicalFileName
parameter_list|)
block|{
name|this
operator|.
name|canonicalFileName
operator|=
name|needToNormalize
argument_list|()
condition|?
name|FileUtil
operator|.
name|normalizePath
argument_list|(
name|canonicalFileName
argument_list|)
else|:
name|canonicalFileName
expr_stmt|;
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
name|needToNormalize
argument_list|()
condition|?
name|FileUtil
operator|.
name|normalizePath
argument_list|(
name|endpointPath
argument_list|)
else|:
name|endpointPath
expr_stmt|;
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
name|absoluteFileName
else|:
name|relativeFileName
operator|)
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

