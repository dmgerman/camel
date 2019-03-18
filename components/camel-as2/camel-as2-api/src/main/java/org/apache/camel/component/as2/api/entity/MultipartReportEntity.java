begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api.entity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|entity
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|AS2MimeType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|ContentType
import|;
end_import

begin_class
DECL|class|MultipartReportEntity
specifier|public
class|class
name|MultipartReportEntity
extends|extends
name|MultipartMimeEntity
block|{
DECL|method|MultipartReportEntity (String charset, boolean isMainBody, String boundary)
specifier|public
name|MultipartReportEntity
parameter_list|(
name|String
name|charset
parameter_list|,
name|boolean
name|isMainBody
parameter_list|,
name|String
name|boundary
parameter_list|)
throws|throws
name|HttpException
block|{
name|super
argument_list|(
name|ContentType
operator|.
name|create
argument_list|(
name|AS2MimeType
operator|.
name|MULTIPART_REPORT
argument_list|,
name|charset
argument_list|)
argument_list|,
name|isMainBody
argument_list|,
name|boundary
argument_list|)
expr_stmt|;
block|}
DECL|method|MultipartReportEntity ()
specifier|protected
name|MultipartReportEntity
parameter_list|()
block|{      }
block|}
end_class

end_unit

