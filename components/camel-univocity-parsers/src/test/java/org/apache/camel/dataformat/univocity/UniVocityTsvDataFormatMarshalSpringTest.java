begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.univocity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|univocity
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|EndpointInject
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
name|test
operator|.
name|spring
operator|.
name|junit5
operator|.
name|CamelSpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|univocity
operator|.
name|UniVocityTestHelper
operator|.
name|asMap
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|univocity
operator|.
name|UniVocityTestHelper
operator|.
name|join
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit5
operator|.
name|TestSupport
operator|.
name|assertIsInstanceOf
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * This class tests the marshalling of {@link org.apache.camel.dataformat.univocity.UniVocityTsvDataFormat} using the  * Spring DSL.  */
end_comment

begin_class
DECL|class|UniVocityTsvDataFormatMarshalSpringTest
specifier|public
specifier|final
class|class
name|UniVocityTsvDataFormatMarshalSpringTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|result
name|MockEndpoint
name|result
decl_stmt|;
comment|/**      * Tests that we can marshal TSV with the default configuration.      */
annotation|@
name|Test
DECL|method|shouldMarshalWithDefaultConfiguration ()
specifier|public
name|void
name|shouldMarshalWithDefaultConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:default"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|asMap
argument_list|(
literal|"A"
argument_list|,
literal|"1"
argument_list|,
literal|"B"
argument_list|,
literal|"2"
argument_list|,
literal|"C"
argument_list|,
literal|"3"
argument_list|)
argument_list|,
name|asMap
argument_list|(
literal|"A"
argument_list|,
literal|"one"
argument_list|,
literal|"B"
argument_list|,
literal|"two"
argument_list|,
literal|"C"
argument_list|,
literal|"three"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|String
name|body
init|=
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|join
argument_list|(
literal|"1\t2\t3"
argument_list|,
literal|"one\ttwo\tthree"
argument_list|)
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that we can marshal a single line with TSV.      */
annotation|@
name|Test
DECL|method|shouldMarshalSingleLine ()
specifier|public
name|void
name|shouldMarshalSingleLine
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:default"
argument_list|,
name|asMap
argument_list|(
literal|"A"
argument_list|,
literal|"1"
argument_list|,
literal|"B"
argument_list|,
literal|"2"
argument_list|,
literal|"C"
argument_list|,
literal|"3"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|String
name|body
init|=
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|join
argument_list|(
literal|"1\t2\t3"
argument_list|)
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that the marshalling adds new columns on the fly and keep its order      */
annotation|@
name|Test
DECL|method|shouldMarshalAndAddNewColumns ()
specifier|public
name|void
name|shouldMarshalAndAddNewColumns
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:default"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|asMap
argument_list|(
literal|"A"
argument_list|,
literal|"1"
argument_list|,
literal|"B"
argument_list|,
literal|"2"
argument_list|)
argument_list|,
name|asMap
argument_list|(
literal|"C"
argument_list|,
literal|"three"
argument_list|,
literal|"A"
argument_list|,
literal|"one"
argument_list|,
literal|"B"
argument_list|,
literal|"two"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|String
name|body
init|=
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|join
argument_list|(
literal|"1\t2"
argument_list|,
literal|"one\ttwo\tthree"
argument_list|)
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that we can marshal TSV with specific headers      */
annotation|@
name|Test
DECL|method|shouldMarshalWithSpecificHeaders ()
specifier|public
name|void
name|shouldMarshalWithSpecificHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:header"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|asMap
argument_list|(
literal|"A"
argument_list|,
literal|"1"
argument_list|,
literal|"B"
argument_list|,
literal|"2"
argument_list|,
literal|"C"
argument_list|,
literal|"3"
argument_list|)
argument_list|,
name|asMap
argument_list|(
literal|"A"
argument_list|,
literal|"one"
argument_list|,
literal|"B"
argument_list|,
literal|"two"
argument_list|,
literal|"C"
argument_list|,
literal|"three"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|String
name|body
init|=
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|join
argument_list|(
literal|"1\t3"
argument_list|,
literal|"one\tthree"
argument_list|)
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that we can marshal TSV using and advanced configuration      */
annotation|@
name|Test
DECL|method|shouldMarshalUsingAdvancedConfiguration ()
specifier|public
name|void
name|shouldMarshalUsingAdvancedConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:advanced"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|asMap
argument_list|(
literal|"A"
argument_list|,
literal|null
argument_list|,
literal|"B"
argument_list|,
literal|""
argument_list|,
literal|"C"
argument_list|,
literal|"_"
argument_list|)
argument_list|,
name|asMap
argument_list|(
literal|"A"
argument_list|,
literal|"one"
argument_list|,
literal|"B"
argument_list|,
literal|"two"
argument_list|,
literal|"C"
argument_list|,
literal|"three"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|String
name|body
init|=
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|join
argument_list|(
literal|"N/A\tempty\t_"
argument_list|,
literal|"one\ttwo\tthree"
argument_list|)
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/dataformat/univocity/UniVocityTsvDataFormatMarshalSpringTest.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

