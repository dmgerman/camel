begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
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
name|List
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
name|LoggingLevel
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
name|Produce
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
name|ProducerTemplate
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
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|simple
operator|.
name|bool
operator|.
name|BooleanExample
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
name|interceptor
operator|.
name|Tracer
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
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|Assert
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
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
DECL|class|BindySimpleCsvBooleanUnmarshallTest
specifier|public
class|class
name|BindySimpleCsvBooleanUnmarshallTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|URI_MOCK_RESULT
specifier|private
specifier|static
specifier|final
name|String
name|URI_MOCK_RESULT
init|=
literal|"mock:result"
decl_stmt|;
DECL|field|URI_MOCK_ERROR
specifier|private
specifier|static
specifier|final
name|String
name|URI_MOCK_ERROR
init|=
literal|"mock:error"
decl_stmt|;
DECL|field|URI_DIRECT_START
specifier|private
specifier|static
specifier|final
name|String
name|URI_DIRECT_START
init|=
literal|"direct:start"
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
name|URI_DIRECT_START
argument_list|)
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
name|URI_MOCK_RESULT
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
name|URI_MOCK_ERROR
argument_list|)
DECL|field|error
specifier|private
name|MockEndpoint
name|error
decl_stmt|;
DECL|field|expected
specifier|private
name|String
name|expected
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testUnMarshallMessageWithBoolean ()
specifier|public
name|void
name|testUnMarshallMessageWithBoolean
parameter_list|()
throws|throws
name|Exception
block|{
comment|// We suppress the firstName field of the first record
name|expected
operator|=
literal|"andrew,true\r\n"
operator|+
literal|"andrew,false\r\n"
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|BooleanExample
argument_list|>
name|examples
init|=
operator|(
name|List
argument_list|<
name|BooleanExample
argument_list|>
operator|)
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
decl_stmt|;
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
name|assertFalse
argument_list|(
name|examples
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|examples
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|,
literal|"andrew"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|examples
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getExist
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|examples
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|examples
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|,
literal|"andrew"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|examples
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getExist
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|examples
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
annotation|@
name|DirtiesContext
DECL|method|testUnMarshallMessageWithBooleanMissingFields ()
specifier|public
name|void
name|testUnMarshallMessageWithBooleanMissingFields
parameter_list|()
throws|throws
name|Exception
block|{
comment|// We suppress the firstName field of the first record
name|expected
operator|=
literal|"andrew,true\r\n"
operator|+
literal|"joseph,false\r\n"
operator|+
literal|"nicholas,\r\n"
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|BooleanExample
argument_list|>
name|examples
init|=
operator|(
name|List
argument_list|<
name|BooleanExample
argument_list|>
operator|)
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
decl_stmt|;
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
name|assertFalse
argument_list|(
name|examples
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|examples
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|,
literal|"andrew"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|examples
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getExist
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|examples
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|examples
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|,
literal|"joseph"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|examples
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getExist
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|examples
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getName
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|examples
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|,
literal|"nicholas"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|examples
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getExist
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|examples
argument_list|)
expr_stmt|;
block|}
DECL|class|ContextConfig
specifier|public
specifier|static
class|class
name|ContextConfig
extends|extends
name|RouteBuilder
block|{
DECL|field|camelDataFormat
name|BindyCsvDataFormat
name|camelDataFormat
init|=
operator|new
name|BindyCsvDataFormat
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|simple
operator|.
name|bool
operator|.
name|BooleanExample
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// from("file://src/test/data?move=./target/done").unmarshal(camelDataFormat).to("mock:result");
name|Tracer
name|tracer
init|=
operator|new
name|Tracer
argument_list|()
decl_stmt|;
name|tracer
operator|.
name|setLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|ERROR
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|setLogName
argument_list|(
literal|"org.apache.camel.bindy"
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|addInterceptStrategy
argument_list|(
name|tracer
argument_list|)
expr_stmt|;
comment|// default should errors go to mock:error
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
name|URI_MOCK_ERROR
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|URI_DIRECT_START
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|camelDataFormat
argument_list|)
operator|.
name|to
argument_list|(
name|URI_MOCK_RESULT
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

