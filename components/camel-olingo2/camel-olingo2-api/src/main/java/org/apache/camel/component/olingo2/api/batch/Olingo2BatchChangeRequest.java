begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo2.api.batch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo2
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
comment|/**  * Batch Change part.  */
end_comment

begin_class
DECL|class|Olingo2BatchChangeRequest
specifier|public
class|class
name|Olingo2BatchChangeRequest
extends|extends
name|Olingo2BatchRequest
block|{
DECL|field|contentId
specifier|protected
name|String
name|contentId
decl_stmt|;
DECL|field|operation
specifier|protected
name|Operation
name|operation
decl_stmt|;
DECL|field|body
specifier|protected
name|Object
name|body
decl_stmt|;
DECL|method|getOperation ()
specifier|public
name|Operation
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|getBody ()
specifier|public
name|Object
name|getBody
parameter_list|()
block|{
return|return
name|body
return|;
block|}
DECL|method|getContentId ()
specifier|public
name|String
name|getContentId
parameter_list|()
block|{
return|return
name|contentId
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
literal|"Batch Change Request{ "
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
literal|", contentId="
argument_list|)
operator|.
name|append
argument_list|(
name|contentId
argument_list|)
operator|.
name|append
argument_list|(
literal|", operation="
argument_list|)
operator|.
name|append
argument_list|(
name|operation
argument_list|)
operator|.
name|append
argument_list|(
literal|", body="
argument_list|)
operator|.
name|append
argument_list|(
name|body
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
DECL|method|resourcePath (String resourcePath)
specifier|public
specifier|static
name|Olingo2BatchChangeRequestBuilder
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
name|Olingo2BatchChangeRequestBuilder
argument_list|()
operator|.
name|resourcePath
argument_list|(
name|resourcePath
argument_list|)
return|;
block|}
DECL|class|Olingo2BatchChangeRequestBuilder
specifier|public
specifier|static
class|class
name|Olingo2BatchChangeRequestBuilder
block|{
DECL|field|request
specifier|private
name|Olingo2BatchChangeRequest
name|request
init|=
operator|new
name|Olingo2BatchChangeRequest
argument_list|()
decl_stmt|;
DECL|method|resourcePath (String resourcePath)
specifier|public
name|Olingo2BatchChangeRequestBuilder
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
name|Olingo2BatchChangeRequestBuilder
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
DECL|method|contentId (String contentId)
specifier|public
name|Olingo2BatchChangeRequestBuilder
name|contentId
parameter_list|(
name|String
name|contentId
parameter_list|)
block|{
name|request
operator|.
name|contentId
operator|=
name|contentId
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|operation (Operation operation)
specifier|public
name|Olingo2BatchChangeRequestBuilder
name|operation
parameter_list|(
name|Operation
name|operation
parameter_list|)
block|{
name|request
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|body (Object body)
specifier|public
name|Olingo2BatchChangeRequestBuilder
name|body
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
name|request
operator|.
name|body
operator|=
name|body
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|build ()
specifier|public
name|Olingo2BatchChangeRequest
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
if|if
condition|(
name|request
operator|.
name|operation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null operation"
argument_list|)
throw|;
block|}
if|if
condition|(
name|request
operator|.
name|operation
operator|!=
name|Operation
operator|.
name|DELETE
operator|&&
name|request
operator|.
name|body
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null body"
argument_list|)
throw|;
block|}
return|return
name|request
return|;
block|}
block|}
block|}
end_class

end_unit

