begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|csv
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|regex
operator|.
name|Pattern
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
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|CsvRouteTest
specifier|public
class|class
name|CsvRouteTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CsvRouteTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendMessage ()
specifier|public
name|void
name|testSendMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// START SNIPPET: marshalInput
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|body
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|body
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
comment|// END SNIPPET: marshalInput
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|resultEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|list
control|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|text
init|=
name|in
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Received "
operator|+
name|text
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should be able to convert received body to a string"
argument_list|,
name|text
argument_list|)
expr_stmt|;
comment|// order is not guaranteed with a Map (which was passed in before)
comment|// so we need to check for both combinations
name|assertTrue
argument_list|(
literal|"Text body has wrong value."
argument_list|,
literal|"abc,123"
operator|.
name|equals
argument_list|(
name|text
operator|.
name|trim
argument_list|()
argument_list|)
operator|||
literal|"123,abc"
operator|.
name|equals
argument_list|(
name|text
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testMultipleMessages ()
specifier|public
name|void
name|testMultipleMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:resultMulti"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body1
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|body1
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|body1
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body2
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|body2
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"def"
argument_list|)
expr_stmt|;
name|body2
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|456
argument_list|)
expr_stmt|;
name|body2
operator|.
name|put
argument_list|(
literal|"baz"
argument_list|,
literal|789
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:startMulti"
argument_list|,
name|body1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:startMulti"
argument_list|,
name|body2
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|resultEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|Message
name|in1
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|text1
init|=
name|in1
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Received "
operator|+
name|text1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"First CSV body has wrong value"
argument_list|,
name|Pattern
operator|.
name|matches
argument_list|(
literal|"(abc,123)|(123,abc)"
argument_list|,
name|text1
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Message
name|in2
init|=
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|text2
init|=
name|in2
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Received "
operator|+
name|text2
argument_list|)
expr_stmt|;
comment|// fields should keep the same order from one call to the other
if|if
condition|(
name|text1
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
literal|"abc,123"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"Second CSV body has wrong value"
argument_list|,
literal|"def,456,789"
argument_list|,
name|text2
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"Second CSV body has wrong value"
argument_list|,
literal|"456,def,789"
argument_list|,
name|text2
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testPresetConfig ()
specifier|public
name|void
name|testPresetConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:resultMultiCustom"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body1
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|body1
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|body1
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|body2
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|body2
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"def"
argument_list|)
expr_stmt|;
name|body2
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|456
argument_list|)
expr_stmt|;
name|body2
operator|.
name|put
argument_list|(
literal|"baz"
argument_list|,
literal|789
argument_list|)
expr_stmt|;
name|body2
operator|.
name|put
argument_list|(
literal|"buz"
argument_list|,
literal|"000"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:startMultiCustom"
argument_list|,
name|body1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:startMultiCustom"
argument_list|,
name|body2
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|resultEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|Message
name|in1
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|text1
init|=
name|in1
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Received "
operator|+
name|text1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"First CSV body has wrong value"
argument_list|,
literal|"abc;;123"
argument_list|,
name|text1
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|in2
init|=
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|text2
init|=
name|in2
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Received "
operator|+
name|text2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Second CSV body has wrong value"
argument_list|,
literal|"def;789;456"
argument_list|,
name|text2
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
DECL|method|testUnMarshal ()
specifier|public
name|void
name|testUnMarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:daltons"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// START SNIPPET : unmarshalResult
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|data
init|=
operator|(
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|String
argument_list|>
name|line
range|:
name|data
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s has an IQ of %s and is currently %s"
argument_list|,
name|line
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|line
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|line
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// END SNIPPET : unmarshalResult
block|}
annotation|@
name|Override
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
comment|// START SNIPPET: marshalRoute
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|csv
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: marshalRoute
name|from
argument_list|(
literal|"direct:startMulti"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|csv
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:resultMulti"
argument_list|)
expr_stmt|;
name|CsvDataFormat
name|customCsv
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setDelimiter
argument_list|(
literal|';'
argument_list|)
operator|.
name|setHeader
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"foo"
block|,
literal|"baz"
block|,
literal|"bar"
block|}
argument_list|)
operator|.
name|setSkipHeaderRecord
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:startMultiCustom"
argument_list|)
operator|.
name|marshal
argument_list|(
name|customCsv
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultMultiCustom"
argument_list|)
expr_stmt|;
comment|// START SNIPPET: unmarshalRoute
name|from
argument_list|(
literal|"file:src/test/resources/?fileName=daltons.csv&noop=true"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|csv
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:daltons"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: unmarshalRoute
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

