begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
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
name|ContextTestSupport
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
name|TypeConverter
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
DECL|class|FallbackPromoteTest
specifier|public
class|class
name|FallbackPromoteTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|isLoadTypeConverters ()
specifier|protected
name|boolean
name|isLoadTypeConverters
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Test
DECL|method|testFallbackPromote ()
specifier|public
name|void
name|testFallbackPromote
parameter_list|()
throws|throws
name|Exception
block|{
name|MyCoolBean
name|cool
init|=
operator|new
name|MyCoolBean
argument_list|()
decl_stmt|;
name|cool
operator|.
name|setCool
argument_list|(
literal|"Camel rocks"
argument_list|)
expr_stmt|;
name|TypeConverter
name|tc
init|=
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|MyCoolBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"No regular type converters"
argument_list|,
name|tc
argument_list|)
expr_stmt|;
name|String
name|s
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
name|cool
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"This is cool: Camel rocks"
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|cool
operator|.
name|setCool
argument_list|(
literal|"It works"
argument_list|)
expr_stmt|;
name|s
operator|=
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
name|cool
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"This is cool: It works"
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|tc
operator|=
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|MyCoolBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have been promoted"
argument_list|,
name|tc
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

