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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
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
name|junit5
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
name|jupiter
operator|.
name|api
operator|.
name|Test
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
name|assertFalse
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
name|assertTrue
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
name|fail
import|;
end_import

begin_comment
comment|/**  * This class tests the unmarshalling of {@link org.apache.camel.dataformat.univocity.UniVocityCsvDataFormat} using the  * Spring DSL.  */
end_comment

begin_class
DECL|class|UniVocityCsvDataFormatUnmarshalTest
specifier|public
specifier|final
class|class
name|UniVocityCsvDataFormatUnmarshalTest
extends|extends
name|CamelTestSupport
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
comment|/**      * Tests that we can unmarshal CSV with the default configuration.      */
annotation|@
name|Test
DECL|method|shouldUnmarshalWithDefaultConfiguration ()
specifier|public
name|void
name|shouldUnmarshalWithDefaultConfiguration
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
name|join
argument_list|(
literal|"A,B,C"
argument_list|,
literal|"1,2,3"
argument_list|,
literal|"one,two,three"
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
name|List
argument_list|<
name|?
argument_list|>
name|body
init|=
name|assertIsInstanceOf
argument_list|(
name|List
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
literal|3
argument_list|,
name|body
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"C"
argument_list|)
argument_list|,
name|body
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"1"
argument_list|,
literal|"2"
argument_list|,
literal|"3"
argument_list|)
argument_list|,
name|body
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"one"
argument_list|,
literal|"two"
argument_list|,
literal|"three"
argument_list|)
argument_list|,
name|body
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that we can unmarshal CSV and produce maps for each row      */
annotation|@
name|Test
DECL|method|shouldUnmarshalAsMap ()
specifier|public
name|void
name|shouldUnmarshalAsMap
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:map"
argument_list|,
name|join
argument_list|(
literal|"A,B,C"
argument_list|,
literal|"1,2,3"
argument_list|,
literal|"one,two,three"
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
name|List
argument_list|<
name|?
argument_list|>
name|body
init|=
name|assertIsInstanceOf
argument_list|(
name|List
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
literal|2
argument_list|,
name|body
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
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
name|body
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
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
argument_list|,
name|body
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that we can unmarshal CSV and produce maps for each row with the given header      */
annotation|@
name|Test
DECL|method|shouldUnmarshalAsMapWithHeaders ()
specifier|public
name|void
name|shouldUnmarshalAsMapWithHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:mapWithHeaders"
argument_list|,
name|join
argument_list|(
literal|"1,2,3"
argument_list|,
literal|"one,two,three"
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
name|List
argument_list|<
name|?
argument_list|>
name|body
init|=
name|assertIsInstanceOf
argument_list|(
name|List
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
literal|2
argument_list|,
name|body
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
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
name|body
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
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
argument_list|,
name|body
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that we can unmarshal CSV and produce an Iterator that lazily reads the input      */
annotation|@
name|Test
DECL|method|shouldUnmarshalUsingIterator ()
specifier|public
name|void
name|shouldUnmarshalUsingIterator
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:lazy"
argument_list|,
name|join
argument_list|(
literal|"A,B,C"
argument_list|,
literal|"1,2,3"
argument_list|,
literal|"one,two,three"
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
name|Iterator
argument_list|<
name|?
argument_list|>
name|body
init|=
name|assertIsInstanceOf
argument_list|(
name|Iterator
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
comment|// Read first line
name|assertTrue
argument_list|(
name|body
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"C"
argument_list|)
argument_list|,
name|body
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
comment|// Try to remove the element
try|try
block|{
name|body
operator|.
name|remove
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
comment|// Success
block|}
comment|// Read all the lines
name|assertTrue
argument_list|(
name|body
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"1"
argument_list|,
literal|"2"
argument_list|,
literal|"3"
argument_list|)
argument_list|,
name|body
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|body
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"one"
argument_list|,
literal|"two"
argument_list|,
literal|"three"
argument_list|)
argument_list|,
name|body
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|body
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
comment|// Try to read one more element
try|try
block|{
name|body
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a NoSuchElementException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{
comment|// Success
block|}
block|}
comment|/**      * Tests that we can unmarshal CSV that has lots of configuration options      */
annotation|@
name|Test
DECL|method|shouldUnmarshalUsingAdvancedConfiguration ()
specifier|public
name|void
name|shouldUnmarshalUsingAdvancedConfiguration
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
name|join
argument_list|(
literal|"!This is comment"
argument_list|,
literal|"!This is comment too"
argument_list|,
literal|"A;B"
argument_list|,
literal|""
argument_list|,
literal|"  ;D  "
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
name|List
argument_list|<
name|?
argument_list|>
name|body
init|=
name|assertIsInstanceOf
argument_list|(
name|List
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
literal|2
argument_list|,
name|body
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|)
argument_list|,
name|body
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"N/A"
argument_list|,
literal|"D  "
argument_list|)
argument_list|,
name|body
operator|.
name|get
argument_list|(
literal|1
argument_list|)
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
comment|// Default reading of CSV
name|tests
operator|.
name|put
argument_list|(
literal|"default"
argument_list|,
operator|new
name|UniVocityCsvDataFormat
argument_list|()
argument_list|)
expr_stmt|;
comment|// Reading CSV as Map
name|tests
operator|.
name|put
argument_list|(
literal|"map"
argument_list|,
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setAsMap
argument_list|(
literal|true
argument_list|)
operator|.
name|setHeaderExtractionEnabled
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// Reading CSV as Map with specific headers
name|tests
operator|.
name|put
argument_list|(
literal|"mapWithHeaders"
argument_list|,
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setAsMap
argument_list|(
literal|true
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
literal|"B"
block|,
literal|"C"
block|}
argument_list|)
argument_list|)
expr_stmt|;
comment|// Reading CSV using an iterator
name|tests
operator|.
name|put
argument_list|(
literal|"lazy"
argument_list|,
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setLazyLoad
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// Reading CSV using advanced configuration
name|tests
operator|.
name|put
argument_list|(
literal|"advanced"
argument_list|,
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setNullValue
argument_list|(
literal|"N/A"
argument_list|)
operator|.
name|setDelimiter
argument_list|(
literal|';'
argument_list|)
operator|.
name|setIgnoreLeadingWhitespaces
argument_list|(
literal|true
argument_list|)
operator|.
name|setIgnoreTrailingWhitespaces
argument_list|(
literal|false
argument_list|)
operator|.
name|setComment
argument_list|(
literal|'!'
argument_list|)
operator|.
name|setSkipEmptyLines
argument_list|(
literal|true
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
name|unmarshal
argument_list|(
name|test
operator|.
name|getValue
argument_list|()
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

