begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tests.partialclasspath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tests
operator|.
name|partialclasspath
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|MyConverter
specifier|public
class|class
name|MyConverter
block|{
annotation|@
name|Converter
DECL|method|fromString (String text)
specifier|public
name|MyBean
name|fromString
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|String
index|[]
name|values
init|=
name|StringHelper
operator|.
name|splitOnCharacter
argument_list|(
name|text
argument_list|,
literal|":"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
return|return
operator|new
name|MyBean
argument_list|(
name|values
index|[
literal|0
index|]
argument_list|,
name|values
index|[
literal|1
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

