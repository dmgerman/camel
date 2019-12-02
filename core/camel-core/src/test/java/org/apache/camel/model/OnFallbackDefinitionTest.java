begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
DECL|class|OnFallbackDefinitionTest
specifier|public
class|class
name|OnFallbackDefinitionTest
block|{
annotation|@
name|Test
DECL|method|testLabel ()
specifier|public
name|void
name|testLabel
parameter_list|()
block|{
name|OnFallbackDefinition
name|ofd
init|=
operator|new
name|OnFallbackDefinition
argument_list|()
decl_stmt|;
name|ofd
operator|.
name|addOutput
argument_list|(
operator|new
name|ToDefinition
argument_list|(
literal|"urn:foo1"
argument_list|)
argument_list|)
expr_stmt|;
name|ofd
operator|.
name|addOutput
argument_list|(
operator|new
name|ToDefinition
argument_list|(
literal|"urn:foo2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"onFallback[urn:foo1,urn:foo2]"
argument_list|,
name|ofd
operator|.
name|getLabel
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

