begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|validator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|Exchange
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
name|TypeConverters
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|DataFormatDefinition
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
name|RouteContext
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
DECL|class|ValidatorContractTest
specifier|public
class|class
name|ValidatorContractTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testInputTypeOnly ()
specifier|public
name|void
name|testInputTypeOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|addTypeConverters
argument_list|(
operator|new
name|MyTypeConverters
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|validator
argument_list|()
operator|.
name|type
argument_list|(
name|A
operator|.
name|class
argument_list|)
operator|.
name|withUri
argument_list|(
literal|"direct:validator"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|inputType
argument_list|(
name|A
operator|.
name|class
argument_list|,
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:validator"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:validator"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mocka
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:a"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mockv
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:validator"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mocka
operator|.
name|setExpectedCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockv
operator|.
name|setExpectedCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Object
name|answer
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|mocka
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mockv
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exa
init|=
name|mocka
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|A
operator|.
name|class
argument_list|,
name|exa
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|exv
init|=
name|mockv
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|A
operator|.
name|class
argument_list|,
name|exv
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|A
operator|.
name|class
argument_list|,
name|answer
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOutputTypeOnly ()
specifier|public
name|void
name|testOutputTypeOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|addTypeConverters
argument_list|(
operator|new
name|MyTypeConverters
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|validator
argument_list|()
operator|.
name|type
argument_list|(
name|A
operator|.
name|class
argument_list|)
operator|.
name|withUri
argument_list|(
literal|"direct:validator"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|outputType
argument_list|(
name|A
operator|.
name|class
argument_list|,
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:validator"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:validator"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mocka
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:a"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mockv
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:validator"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mocka
operator|.
name|setExpectedCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockv
operator|.
name|setExpectedCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Object
name|answer
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|mocka
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mockv
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exa
init|=
name|mocka
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|exa
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|exv
init|=
name|mockv
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|A
operator|.
name|class
argument_list|,
name|exv
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|A
operator|.
name|class
argument_list|,
name|answer
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyTypeConverters
specifier|public
specifier|static
class|class
name|MyTypeConverters
implements|implements
name|TypeConverters
block|{
annotation|@
name|Converter
DECL|method|toA (String in)
specifier|public
name|A
name|toA
parameter_list|(
name|String
name|in
parameter_list|)
block|{
return|return
operator|new
name|A
argument_list|()
return|;
block|}
block|}
DECL|class|A
specifier|public
specifier|static
class|class
name|A
block|{ }
block|}
end_class

end_unit

