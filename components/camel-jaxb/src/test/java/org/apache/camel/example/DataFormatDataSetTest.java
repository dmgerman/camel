begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
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
name|builder
operator|.
name|RouteBuilder
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
name|component
operator|.
name|dataset
operator|.
name|SimpleDataSet
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
name|converter
operator|.
name|jaxb
operator|.
name|JaxbDataFormat
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
name|spi
operator|.
name|DataFormat
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
name|spi
operator|.
name|Registry
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|DataFormatDataSetTest
specifier|public
class|class
name|DataFormatDataSetTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testConcurrentMarshall ()
specifier|public
name|void
name|testConcurrentMarshall
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|bindToRegistry (Registry registry)
specifier|protected
name|void
name|bindToRegistry
parameter_list|(
name|Registry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
name|PurchaseOrder
name|bean
init|=
operator|new
name|PurchaseOrder
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"Beer"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setAmount
argument_list|(
literal|23
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setPrice
argument_list|(
literal|2.5
argument_list|)
expr_stmt|;
name|SimpleDataSet
name|ds
init|=
operator|new
name|SimpleDataSet
argument_list|()
decl_stmt|;
name|ds
operator|.
name|setDefaultBody
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|ds
operator|.
name|setSize
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"beer"
argument_list|,
name|ds
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|DataFormat
name|jaxb
init|=
operator|new
name|JaxbDataFormat
argument_list|(
literal|"org.apache.camel.example"
argument_list|)
decl_stmt|;
comment|// use 5 concurrent threads to do marshalling
name|from
argument_list|(
literal|"dataset:beer"
argument_list|)
operator|.
name|marshal
argument_list|(
name|jaxb
argument_list|)
operator|.
name|to
argument_list|(
literal|"dataset:beer"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

