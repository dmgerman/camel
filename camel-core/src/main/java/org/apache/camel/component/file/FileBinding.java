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

begin_comment
comment|/**  * File binding with the {@link java.io.File} type.  */
end_comment

begin_class
DECL|class|FileBinding
specifier|public
class|class
name|FileBinding
implements|implements
name|GenericFileBinding
argument_list|<
name|File
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|body
specifier|private
name|File
name|body
decl_stmt|;
DECL|method|getBody (GenericFile<File> file)
specifier|public
name|Object
name|getBody
parameter_list|(
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|)
block|{
comment|// as we use java.io.File itself as the body (not loading its content into a OutputStream etc.)
comment|// we just store a java.io.File handle to the actual file denoted by the
comment|// file.getAbsoluteFileName. We must do this as the original file consumed can be renamed before
comment|// being processed (preMove) and thus it points to an invalid file location.
comment|// GenericFile#getAbsoluteFileName() is always up-to-date and thus we use it to create a file
comment|// handle that is correct
if|if
condition|(
name|body
operator|==
literal|null
operator|||
operator|!
name|file
operator|.
name|getAbsoluteFileName
argument_list|()
operator|.
name|equals
argument_list|(
name|body
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
condition|)
block|{
name|body
operator|=
operator|new
name|File
argument_list|(
name|file
operator|.
name|getAbsoluteFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|body
return|;
block|}
DECL|method|setBody (GenericFile<File> file, Object body)
specifier|public
name|void
name|setBody
parameter_list|(
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit

