begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.metrics
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|metrics
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_class
DECL|class|MetricsTypeTest
specifier|public
class|class
name|MetricsTypeTest
block|{
annotation|@
name|Test
DECL|method|testGetByName ()
specifier|public
name|void
name|testGetByName
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|MetricsType
name|type
range|:
name|EnumSet
operator|.
name|allOf
argument_list|(
name|MetricsType
operator|.
name|class
argument_list|)
control|)
block|{
name|MetricsType
name|t
init|=
name|MetricsType
operator|.
name|getByName
argument_list|(
name|type
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|t
argument_list|,
name|is
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

