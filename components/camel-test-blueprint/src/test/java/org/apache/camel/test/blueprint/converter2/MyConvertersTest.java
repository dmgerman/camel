begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint.converter2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
operator|.
name|converter2
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
name|test
operator|.
name|blueprint
operator|.
name|CamelBlueprintTestSupport
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

begin_class
DECL|class|MyConvertersTest
specifier|public
class|class
name|MyConvertersTest
extends|extends
name|CamelBlueprintTestSupport
block|{
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"org/apache/camel/test/blueprint/converter2/MyConvertersTest.xml"
return|;
block|}
annotation|@
name|Test
DECL|method|testConvertersShouldBeAddedAutomaticByBlueprint ()
specifier|public
name|void
name|testConvertersShouldBeAddedAutomaticByBlueprint
parameter_list|()
throws|throws
name|Exception
block|{
name|Country
name|country
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Country
operator|.
name|class
argument_list|,
literal|"en"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|country
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"England"
argument_list|,
name|country
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|iso
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|country
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|iso
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"en"
argument_list|,
name|iso
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

