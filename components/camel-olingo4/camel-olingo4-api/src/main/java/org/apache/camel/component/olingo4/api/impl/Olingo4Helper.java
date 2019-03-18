begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo4.api.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo4
operator|.
name|api
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpHeaders
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
name|HttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|format
operator|.
name|ContentType
import|;
end_import

begin_comment
comment|/**  * Helper  */
end_comment

begin_class
DECL|class|Olingo4Helper
specifier|public
specifier|final
class|class
name|Olingo4Helper
block|{
DECL|method|Olingo4Helper ()
specifier|private
name|Olingo4Helper
parameter_list|()
block|{     }
comment|/**      * Gets the content type header in a safe way      */
DECL|method|getContentTypeHeader (HttpResponse response)
specifier|public
specifier|static
name|ContentType
name|getContentTypeHeader
parameter_list|(
name|HttpResponse
name|response
parameter_list|)
block|{
if|if
condition|(
name|response
operator|.
name|containsHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
condition|)
block|{
return|return
name|ContentType
operator|.
name|parse
argument_list|(
name|response
operator|.
name|getFirstHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

