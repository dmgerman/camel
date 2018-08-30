begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

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
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|CamelContext
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
name|Message
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
name|TestSupport
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
name|impl
operator|.
name|DefaultCamelContext
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
name|impl
operator|.
name|transformer
operator|.
name|DataFormatTransformer
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
name|impl
operator|.
name|transformer
operator|.
name|ProcessorTransformer
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
name|model
operator|.
name|dataformat
operator|.
name|StringDataFormat
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
name|processor
operator|.
name|SendProcessor
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
name|DataType
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
name|Transformer
import|;
end_import

begin_class
DECL|class|TransformerBuilderTest
specifier|public
class|class
name|TransformerBuilderTest
extends|extends
name|TestSupport
block|{
annotation|@
name|Test
DECL|method|testDataFormatTransformer ()
specifier|public
name|void
name|testDataFormatTransformer
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|RouteBuilder
name|builder
init|=
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
name|transformer
argument_list|()
operator|.
name|fromType
argument_list|(
literal|"xml:foo"
argument_list|)
operator|.
name|toType
argument_list|(
literal|"json:bar"
argument_list|)
operator|.
name|withDataFormat
argument_list|(
operator|new
name|StringDataFormat
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:input"
argument_list|)
operator|.
name|log
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|ctx
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|Transformer
name|transformer
init|=
name|ctx
operator|.
name|resolveTransformer
argument_list|(
operator|new
name|DataType
argument_list|(
literal|"xml:foo"
argument_list|)
argument_list|,
operator|new
name|DataType
argument_list|(
literal|"json:bar"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|transformer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DataFormatTransformer
operator|.
name|class
argument_list|,
name|transformer
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|DataFormatTransformer
name|dft
init|=
operator|(
name|DataFormatTransformer
operator|)
name|transformer
decl_stmt|;
name|Field
name|f
init|=
name|DataFormatTransformer
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"dataFormatType"
argument_list|)
decl_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
name|dataFormatType
init|=
name|f
operator|.
name|get
argument_list|(
name|dft
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|StringDataFormat
operator|.
name|class
argument_list|,
name|dataFormatType
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointTransformer ()
specifier|public
name|void
name|testEndpointTransformer
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|RouteBuilder
name|builder
init|=
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
name|transformer
argument_list|()
operator|.
name|fromType
argument_list|(
literal|"json:foo"
argument_list|)
operator|.
name|toType
argument_list|(
literal|"xml:bar"
argument_list|)
operator|.
name|withUri
argument_list|(
literal|"direct:transformer"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:transformer"
argument_list|)
operator|.
name|log
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|ctx
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|Transformer
name|transformer
init|=
name|ctx
operator|.
name|resolveTransformer
argument_list|(
operator|new
name|DataType
argument_list|(
literal|"json:foo"
argument_list|)
argument_list|,
operator|new
name|DataType
argument_list|(
literal|"xml:bar"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|transformer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ProcessorTransformer
operator|.
name|class
argument_list|,
name|transformer
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|ProcessorTransformer
name|pt
init|=
operator|(
name|ProcessorTransformer
operator|)
name|transformer
decl_stmt|;
name|Field
name|f
init|=
name|ProcessorTransformer
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"processor"
argument_list|)
decl_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
name|processor
init|=
name|f
operator|.
name|get
argument_list|(
name|pt
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|processor
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|SendProcessor
name|sp
init|=
operator|(
name|SendProcessor
operator|)
name|processor
decl_stmt|;
name|assertEquals
argument_list|(
literal|"direct://transformer"
argument_list|,
name|sp
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCustomTransformer ()
specifier|public
name|void
name|testCustomTransformer
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|RouteBuilder
name|builder
init|=
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
name|transformer
argument_list|()
operator|.
name|scheme
argument_list|(
literal|"other"
argument_list|)
operator|.
name|withJava
argument_list|(
name|MyTransformer
operator|.
name|class
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:input"
argument_list|)
operator|.
name|log
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|ctx
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|Transformer
name|transformer
init|=
name|ctx
operator|.
name|resolveTransformer
argument_list|(
literal|"other"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|transformer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MyTransformer
operator|.
name|class
argument_list|,
name|transformer
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyTransformer
specifier|public
specifier|static
class|class
name|MyTransformer
extends|extends
name|Transformer
block|{
annotation|@
name|Override
DECL|method|transform (Message message, DataType from, DataType to)
specifier|public
name|void
name|transform
parameter_list|(
name|Message
name|message
parameter_list|,
name|DataType
name|from
parameter_list|,
name|DataType
name|to
parameter_list|)
throws|throws
name|Exception
block|{
name|message
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

