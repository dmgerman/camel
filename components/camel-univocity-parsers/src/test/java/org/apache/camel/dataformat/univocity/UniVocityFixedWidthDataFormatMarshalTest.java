begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p/>  * http://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
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

begin_comment
comment|/**  * This class tests the marshalling of {@link org.apache.camel.dataformat.univocity.UniVocityFixedWidthDataFormat}.  */
end_comment

begin_class
DECL|class|UniVocityFixedWidthDataFormatMarshalTest
specifier|public
specifier|final
class|class
name|UniVocityFixedWidthDataFormatMarshalTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|result
name|MockEndpoint
name|result
decl_stmt|;
comment|/**      * Tests that we can marshal fixed-width with the default configuration.      */
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
literal|"1  2  3    "
argument_list|,
literal|"onetwothree"
argument_list|)
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that we can marshal a single line with fixed-width.      */
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
literal|"1  2  3    "
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
literal|"1  2  "
argument_list|,
literal|"onetwothree"
argument_list|)
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that we can marshal fixed-width with specific headers      */
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
literal|"1  3    "
argument_list|,
literal|"onethree"
argument_list|)
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that we can marshal fixed-width using and advanced configuration      */
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
literal|"N/A__empty"
argument_list|,
literal|"one__two__"
argument_list|)
argument_list|,
name|body
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
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DataFormat
argument_list|>
name|tests
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Default writing of fixed-width
name|tests
operator|.
name|put
argument_list|(
literal|"default"
argument_list|,
operator|new
name|UniVocityFixedWidthDataFormat
argument_list|()
operator|.
name|setFieldLengths
argument_list|(
operator|new
name|int
index|[]
block|{
literal|3
block|,
literal|3
block|,
literal|5
block|}
argument_list|)
argument_list|)
expr_stmt|;
comment|// Write a fixed-width with specific headers
name|tests
operator|.
name|put
argument_list|(
literal|"header"
argument_list|,
operator|new
name|UniVocityFixedWidthDataFormat
argument_list|()
operator|.
name|setFieldLengths
argument_list|(
operator|new
name|int
index|[]
block|{
literal|3
block|,
literal|5
block|}
argument_list|)
operator|.
name|setHeaders
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"A"
block|,
literal|"C"
block|}
argument_list|)
argument_list|)
expr_stmt|;
comment|// Write a fixed-width with an advanced configuration
name|tests
operator|.
name|put
argument_list|(
literal|"advanced"
argument_list|,
operator|new
name|UniVocityFixedWidthDataFormat
argument_list|()
operator|.
name|setFieldLengths
argument_list|(
operator|new
name|int
index|[]
block|{
literal|5
block|,
literal|5
block|}
argument_list|)
operator|.
name|setNullValue
argument_list|(
literal|"N/A"
argument_list|)
operator|.
name|setEmptyValue
argument_list|(
literal|"empty"
argument_list|)
operator|.
name|setPadding
argument_list|(
literal|'_'
argument_list|)
argument_list|)
expr_stmt|;
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
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DataFormat
argument_list|>
name|test
range|:
name|tests
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|from
argument_list|(
literal|"direct:"
operator|+
name|test
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|marshal
argument_list|(
name|test
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

