begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
operator|.
name|mbean
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|OpenDataException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|OpenType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|SimpleType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularType
import|;
end_import

begin_comment
comment|/**  * Various JMX openmbean types used by Camel.  */
end_comment

begin_class
DECL|class|CamelOpenMBeanTypes
specifier|public
specifier|final
class|class
name|CamelOpenMBeanTypes
block|{
DECL|method|CamelOpenMBeanTypes ()
specifier|private
name|CamelOpenMBeanTypes
parameter_list|()
block|{     }
DECL|method|listTypeConvertersTabularType ()
specifier|public
specifier|static
name|TabularType
name|listTypeConvertersTabularType
parameter_list|()
throws|throws
name|OpenDataException
block|{
name|CompositeType
name|ct
init|=
name|listTypeConvertersCompositeType
argument_list|()
decl_stmt|;
return|return
operator|new
name|TabularType
argument_list|(
literal|"listTypeConverters"
argument_list|,
literal|"Lists all the type converters in the registry (from -> to)"
argument_list|,
name|ct
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"from"
block|,
literal|"to"
block|}
argument_list|)
return|;
block|}
DECL|method|listTypeConvertersCompositeType ()
specifier|public
specifier|static
name|CompositeType
name|listTypeConvertersCompositeType
parameter_list|()
throws|throws
name|OpenDataException
block|{
return|return
operator|new
name|CompositeType
argument_list|(
literal|"types"
argument_list|,
literal|"From/To types"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"from"
block|,
literal|"to"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"From type"
block|,
literal|"To type"
block|}
argument_list|,
operator|new
name|OpenType
index|[]
block|{
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|STRING
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

