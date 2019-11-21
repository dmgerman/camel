begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.platform.http.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|platform
operator|.
name|http
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_comment
comment|/**  * An HTTP method.  */
end_comment

begin_enum
DECL|enum|Method
specifier|public
enum|enum
name|Method
block|{
DECL|enumConstant|GET
DECL|enumConstant|HEAD
DECL|enumConstant|POST
DECL|enumConstant|PUT
DECL|enumConstant|DELETE
DECL|enumConstant|TRACE
DECL|enumConstant|OPTIONS
DECL|enumConstant|CONNECT
DECL|enumConstant|PATCH
name|GET
argument_list|(
literal|false
argument_list|)
block|,
name|HEAD
argument_list|(
literal|false
argument_list|)
block|,
name|POST
argument_list|(
literal|true
argument_list|)
block|,
name|PUT
argument_list|(
literal|true
argument_list|)
block|,
name|DELETE
argument_list|(
literal|false
argument_list|)
block|,
name|TRACE
argument_list|(
literal|false
argument_list|)
block|,
name|OPTIONS
argument_list|(
literal|false
argument_list|)
block|,
name|CONNECT
argument_list|(
literal|false
argument_list|)
block|,
name|PATCH
argument_list|(
literal|true
argument_list|)
block|;
DECL|field|ALL
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|Method
argument_list|>
name|ALL
init|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|canHaveBody
specifier|private
specifier|final
name|boolean
name|canHaveBody
decl_stmt|;
DECL|method|Method (boolean canHaveBody)
specifier|private
name|Method
parameter_list|(
name|boolean
name|canHaveBody
parameter_list|)
block|{
name|this
operator|.
name|canHaveBody
operator|=
name|canHaveBody
expr_stmt|;
block|}
DECL|method|getAll ()
specifier|public
specifier|static
name|Set
argument_list|<
name|Method
argument_list|>
name|getAll
parameter_list|()
block|{
return|return
name|ALL
return|;
block|}
comment|/**      * @return {@code true} if HTTP requests with this {@link Method} can have a body; {@code false} otherwise      */
DECL|method|canHaveBody ()
specifier|public
name|boolean
name|canHaveBody
parameter_list|()
block|{
return|return
name|canHaveBody
return|;
block|}
comment|/**      * Parse the given comma separated {@code methodList} to a {@link Set} of {@link Method}s. If {@code methodList} is      * empty or {@code null} returns {@link #ALL}.      *      * @param methodList a comma separated list of HTTP method names.      * @return a {@link Set} of {@link Method}s      */
DECL|method|parseList (String methodList)
specifier|public
specifier|static
name|Set
argument_list|<
name|Method
argument_list|>
name|parseList
parameter_list|(
name|String
name|methodList
parameter_list|)
block|{
if|if
condition|(
name|methodList
operator|==
literal|null
condition|)
block|{
return|return
name|ALL
return|;
block|}
name|methodList
operator|=
name|methodList
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|ROOT
argument_list|)
expr_stmt|;
name|String
index|[]
name|methods
init|=
name|methodList
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|methods
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|ALL
return|;
block|}
elseif|else
if|if
condition|(
name|methods
operator|.
name|length
operator|==
literal|1
condition|)
block|{
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|Method
operator|.
name|valueOf
argument_list|(
name|methods
index|[
literal|0
index|]
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
name|Set
argument_list|<
name|Method
argument_list|>
name|result
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|method
range|:
name|methods
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|Method
operator|.
name|valueOf
argument_list|(
name|method
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ALL
operator|.
name|equals
argument_list|(
name|result
argument_list|)
condition|?
name|ALL
else|:
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|result
argument_list|)
return|;
block|}
block|}
block|}
end_enum

end_unit

