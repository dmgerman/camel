begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Default binding for generic file.  */
end_comment

begin_class
DECL|class|GenericFileDefaultBinding
specifier|public
class|class
name|GenericFileDefaultBinding
parameter_list|<
name|T
parameter_list|>
implements|implements
name|GenericFileBinding
argument_list|<
name|T
argument_list|>
block|{
DECL|field|body
specifier|private
name|Object
name|body
decl_stmt|;
DECL|method|getBody (GenericFile<T> file)
specifier|public
name|Object
name|getBody
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
block|{
return|return
name|body
return|;
block|}
DECL|method|setBody (GenericFile<T> file, Object body)
specifier|public
name|void
name|setBody
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
block|}
DECL|method|loadContent (Exchange exchange, GenericFile<?> file)
specifier|public
name|void
name|loadContent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|GenericFile
argument_list|<
name|?
argument_list|>
name|file
parameter_list|)
throws|throws
name|IOException
block|{
comment|// noop as the body is already loaded
block|}
block|}
end_class

end_unit

