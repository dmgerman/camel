begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.generator.swagger
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|generator
operator|.
name|swagger
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|PatternHelper
import|;
end_import

begin_class
DECL|class|OperationFilter
class|class
name|OperationFilter
block|{
comment|// operation names to include separated by comma (wildcards can be used, eg find*)
DECL|field|includes
specifier|private
name|String
name|includes
decl_stmt|;
DECL|method|getIncludes ()
specifier|public
name|String
name|getIncludes
parameter_list|()
block|{
return|return
name|includes
return|;
block|}
DECL|method|setIncludes (String includes)
specifier|public
name|void
name|setIncludes
parameter_list|(
name|String
name|includes
parameter_list|)
block|{
name|this
operator|.
name|includes
operator|=
name|includes
expr_stmt|;
block|}
DECL|method|accept (String name)
name|boolean
name|accept
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|boolean
name|match
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|includes
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|patterns
init|=
name|includes
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|match
operator|=
name|Arrays
operator|.
name|stream
argument_list|(
name|patterns
argument_list|)
operator|.
name|anyMatch
argument_list|(
name|pattern
lambda|->
name|PatternHelper
operator|.
name|matchPattern
argument_list|(
name|name
argument_list|,
name|pattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|match
return|;
block|}
block|}
end_class

end_unit

