begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jooq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jooq
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_enum
DECL|enum|JooqOperation
specifier|public
enum|enum
name|JooqOperation
block|{
DECL|enumConstant|EXECUTE
name|EXECUTE
argument_list|(
literal|"execute"
argument_list|)
block|,
DECL|enumConstant|FETCH
name|FETCH
argument_list|(
literal|"fetch"
argument_list|)
block|,
DECL|enumConstant|NONE
name|NONE
argument_list|(
literal|""
argument_list|)
block|;
DECL|field|MAP
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|JooqOperation
argument_list|>
name|MAP
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
for|for
control|(
name|JooqOperation
name|myEnum
range|:
name|values
argument_list|()
control|)
block|{
name|MAP
operator|.
name|put
argument_list|(
name|myEnum
operator|.
name|getValue
argument_list|()
argument_list|,
name|myEnum
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|value
specifier|private
name|String
name|value
decl_stmt|;
DECL|method|JooqOperation (String value)
name|JooqOperation
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|getByValue (String value)
specifier|public
specifier|static
name|JooqOperation
name|getByValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|MAP
operator|.
name|get
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|getValue ()
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
block|}
end_enum

end_unit

