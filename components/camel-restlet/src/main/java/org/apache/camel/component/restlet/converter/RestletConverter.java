begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|Converter
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
name|StringHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Method
import|;
end_import

begin_comment
comment|/**  *  * @version   */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|RestletConverter
specifier|public
specifier|final
class|class
name|RestletConverter
block|{
DECL|method|RestletConverter ()
specifier|private
name|RestletConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toMethod (String name)
specifier|public
specifier|static
name|Method
name|toMethod
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|Method
operator|.
name|valueOf
argument_list|(
name|name
operator|.
name|toUpperCase
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toMethods (String name)
specifier|public
specifier|static
name|Method
index|[]
name|toMethods
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
index|[]
name|strings
init|=
name|name
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Method
argument_list|>
name|methods
init|=
operator|new
name|ArrayList
argument_list|<
name|Method
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|string
range|:
name|strings
control|)
block|{
name|methods
operator|.
name|add
argument_list|(
name|toMethod
argument_list|(
name|string
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|methods
operator|.
name|toArray
argument_list|(
operator|new
name|Method
index|[
name|methods
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toMediaType (String name)
specifier|public
specifier|static
name|MediaType
name|toMediaType
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|MediaType
operator|.
name|valueOf
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toMediaTypes (final String name)
specifier|public
specifier|static
name|MediaType
index|[]
name|toMediaTypes
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
specifier|final
name|String
index|[]
name|strings
init|=
name|name
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|MediaType
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|strings
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|strings
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|MediaType
name|mediaType
init|=
name|toMediaType
argument_list|(
name|strings
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|mediaType
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|mediaType
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
operator|.
name|toArray
argument_list|(
operator|new
name|MediaType
index|[
name|answer
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

