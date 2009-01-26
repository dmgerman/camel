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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|NewFileBinding
specifier|public
class|class
name|NewFileBinding
implements|implements
name|GenericFileBinding
argument_list|<
name|File
argument_list|>
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
comment|// TODO: comment why I do this
comment|// TODO: consider storing object and only create new if changed
comment|// TODO: Consider callback from changeName to binding so we change
comment|// change it at that time
return|return
operator|new
name|File
argument_list|(
name|file
operator|.
name|getAbsoluteFileName
argument_list|()
argument_list|)
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

