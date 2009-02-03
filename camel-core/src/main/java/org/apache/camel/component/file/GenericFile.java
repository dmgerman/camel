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
comment|// must normalize path to cater for Windows and other OS
name|newName
operator|=
name|FileUtil
operator|.
name|normalizePath
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|setAbsoluteFileName
argument_list|(
name|getParent
argument_list|()
operator|+
name|File
operator|.
name|separator
operator|+
name|newName
argument_list|)
expr_stmt|;
comment|// relative name is a bit more complex to set as newName itself can contain
comment|// folders we need to consider as well
name|String
name|baseNewName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|newName
operator|.
name|indexOf
argument_list|(
name|File
operator|.
name|separator
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|baseNewName
operator|=
name|newName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|newName
operator|.
name|lastIndexOf
argument_list|(
name|File
operator|.
name|separator
argument_list|)
argument_list|)
expr_stmt|;
name|newName
operator|=
name|newName
operator|.
name|substring
argument_list|(
name|newName
operator|.
name|lastIndexOf
argument_list|(
name|File
operator|.
name|separator
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|relativeFileName
operator|.
name|indexOf
argument_list|(
name|File
operator|.
name|separator
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|relative
init|=
name|relativeFileName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|relativeFileName
operator|.
name|lastIndexOf
argument_list|(
name|File
operator|.
name|separator
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|baseNewName
operator|!=
literal|null
condition|)
block|{
name|setRelativeFileName
argument_list|(
name|relative
operator|+
name|File
operator|.
name|separator
operator|+
name|baseNewName
operator|+
name|File
operator|.
name|separator
operator|+
name|newName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setRelativeFileName
argument_list|(
name|relative
operator|+
name|File
operator|.
name|separator
operator|+
name|newName
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|baseNewName
operator|!=
literal|null
condition|)
block|{
name|setRelativeFileName
argument_list|(
name|baseNewName
operator|+
name|File
operator|.
name|separator
operator|+
name|newName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setRelativeFileName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
block|}
block|}
name|setFileName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
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
name|getAbsoluteFileName
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
name|File
operator|.
name|separator
argument_list|)
operator|>
literal|0
condition|)
block|{
return|return
name|getAbsoluteFileName
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|getAbsoluteFileName
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
name|File
operator|.
name|separator
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|""
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
comment|// must normalize path to cater for Windows and other OS
name|this
operator|.
name|absoluteFileName
operator|=
name|FileUtil
operator|.
name|normalizePath
argument_list|(
name|absoluteFileName
argument_list|)
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
name|canonicalFileName
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
name|relativeFileName
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

