begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|builder
operator|.
name|RouteBuilder
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
DECL|class|SerializationDataFormatTest
specifier|public
class|class
name|SerializationDataFormatTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testSerialization ()
specifier|public
name|void
name|testSerialization
parameter_list|()
throws|throws
name|Exception
block|{
name|MySerialBean
name|bean
init|=
operator|new
name|MySerialBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"Donald"
argument_list|)
expr_stmt|;
name|Object
name|data
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|bean
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|data
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|MySerialBean
name|outBean
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|MySerialBean
operator|.
name|class
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|outBean
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|outBean
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Donald"
argument_list|,
name|outBean
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:unmarshal"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|serialization
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:marshal"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|serialization
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit
