begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bonita.api.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bonita
operator|.
name|api
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_class
DECL|class|FileInput
specifier|public
class|class
name|FileInput
implements|implements
name|Serializable
block|{
DECL|field|filename
specifier|private
name|String
name|filename
decl_stmt|;
DECL|field|content
specifier|private
name|byte
index|[]
name|content
decl_stmt|;
DECL|method|FileInput (String filename, byte[] content)
specifier|public
name|FileInput
parameter_list|(
name|String
name|filename
parameter_list|,
name|byte
index|[]
name|content
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|filename
operator|=
name|filename
expr_stmt|;
name|this
operator|.
name|content
operator|=
name|content
expr_stmt|;
block|}
DECL|method|getFilename ()
specifier|public
name|String
name|getFilename
parameter_list|()
block|{
return|return
name|filename
return|;
block|}
DECL|method|setFilename (String filename)
specifier|public
name|void
name|setFilename
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|this
operator|.
name|filename
operator|=
name|filename
expr_stmt|;
block|}
DECL|method|getContent ()
specifier|public
name|byte
index|[]
name|getContent
parameter_list|()
block|{
return|return
name|content
return|;
block|}
DECL|method|setContent (byte[] content)
specifier|public
name|void
name|setContent
parameter_list|(
name|byte
index|[]
name|content
parameter_list|)
block|{
name|this
operator|.
name|content
operator|=
name|content
expr_stmt|;
block|}
block|}
end_class

end_unit

