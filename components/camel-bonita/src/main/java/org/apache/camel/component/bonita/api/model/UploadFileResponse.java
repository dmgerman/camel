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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonProperty
import|;
end_import

begin_class
DECL|class|UploadFileResponse
specifier|public
class|class
name|UploadFileResponse
block|{
annotation|@
name|JsonProperty
argument_list|(
literal|"filename"
argument_list|)
DECL|field|filename
specifier|private
name|String
name|filename
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"tempPath"
argument_list|)
DECL|field|tempPath
specifier|private
name|String
name|tempPath
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"contentType"
argument_list|)
DECL|field|contentType
specifier|private
name|String
name|contentType
decl_stmt|;
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
DECL|method|getTempPath ()
specifier|public
name|String
name|getTempPath
parameter_list|()
block|{
return|return
name|tempPath
return|;
block|}
DECL|method|setTempPath (String tempPath)
specifier|public
name|void
name|setTempPath
parameter_list|(
name|String
name|tempPath
parameter_list|)
block|{
name|this
operator|.
name|tempPath
operator|=
name|tempPath
expr_stmt|;
block|}
DECL|method|getContentType ()
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|contentType
return|;
block|}
DECL|method|setContentType (String contentType)
specifier|public
name|void
name|setContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
name|this
operator|.
name|contentType
operator|=
name|contentType
expr_stmt|;
block|}
block|}
end_class

end_unit

