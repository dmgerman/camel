begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo4.api.batch
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
name|batch
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Batch Query part.  */
end_comment

begin_class
DECL|class|Olingo4BatchQueryRequest
specifier|public
class|class
name|Olingo4BatchQueryRequest
extends|extends
name|Olingo4BatchRequest
block|{
DECL|field|queryParams
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParams
decl_stmt|;
DECL|method|getQueryParams ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getQueryParams
parameter_list|()
block|{
return|return
name|queryParams
return|;
block|}
DECL|method|resourcePath (String resourcePath)
specifier|public
specifier|static
name|Olingo4BatchQueryRequestBuilder
name|resourcePath
parameter_list|(
name|String
name|resourcePath
parameter_list|)
block|{
if|if
condition|(
name|resourcePath
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"resourcePath"
argument_list|)
throw|;
block|}
return|return
operator|new
name|Olingo4BatchQueryRequestBuilder
argument_list|()
operator|.
name|resourcePath
argument_list|(
name|resourcePath
argument_list|)
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
operator|new
name|StringBuilder
argument_list|(
literal|"Batch Query Request{ "
argument_list|)
operator|.
name|append
argument_list|(
name|resourceUri
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|resourcePath
argument_list|)
operator|.
name|append
argument_list|(
literal|", headers="
argument_list|)
operator|.
name|append
argument_list|(
name|headers
argument_list|)
operator|.
name|append
argument_list|(
literal|", queryParams="
argument_list|)
operator|.
name|append
argument_list|(
name|queryParams
argument_list|)
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|class|Olingo4BatchQueryRequestBuilder
specifier|public
specifier|static
class|class
name|Olingo4BatchQueryRequestBuilder
block|{
DECL|field|request
specifier|private
name|Olingo4BatchQueryRequest
name|request
init|=
operator|new
name|Olingo4BatchQueryRequest
argument_list|()
decl_stmt|;
DECL|method|build ()
specifier|public
name|Olingo4BatchQueryRequest
name|build
parameter_list|()
block|{
comment|// avoid later NPEs
if|if
condition|(
name|request
operator|.
name|resourcePath
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null resourcePath"
argument_list|)
throw|;
block|}
return|return
name|request
return|;
block|}
DECL|method|resourceUri (String resourceUri)
specifier|public
name|Olingo4BatchQueryRequestBuilder
name|resourceUri
parameter_list|(
name|String
name|resourceUri
parameter_list|)
block|{
name|request
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|resourcePath (String resourcePath)
specifier|public
name|Olingo4BatchQueryRequestBuilder
name|resourcePath
parameter_list|(
name|String
name|resourcePath
parameter_list|)
block|{
name|request
operator|.
name|resourcePath
operator|=
name|resourcePath
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|headers (Map<String, String> headers)
specifier|public
name|Olingo4BatchQueryRequestBuilder
name|headers
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|)
block|{
name|request
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|queryParams (Map<String, String> queryParams)
specifier|public
name|Olingo4BatchQueryRequestBuilder
name|queryParams
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParams
parameter_list|)
block|{
name|request
operator|.
name|queryParams
operator|=
name|queryParams
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
block|}
end_class

end_unit

