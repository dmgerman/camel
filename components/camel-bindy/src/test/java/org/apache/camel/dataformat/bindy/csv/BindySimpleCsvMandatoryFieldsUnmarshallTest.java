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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelExecutionException
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
name|spring
operator|.
name|javaconfig
operator|.
name|SingleRouteCamelConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|config
operator|.
name|java
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|config
operator|.
name|java
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|config
operator|.
name|java
operator|.
name|test
operator|.
name|JavaConfigContextLoader
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
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|Assert
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
name|fail
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
literal|"org.apache.camel.dataformat.bindy.csv.BindySimpleCsvMandatoryFieldsUnmarshallTest$ContextConfig"
argument_list|,
name|loader
operator|=
name|JavaConfigContextLoader
operator|.
name|class
argument_list|)
DECL|class|BindySimpleCsvMandatoryFieldsUnmarshallTest
specifier|public
class|class
name|BindySimpleCsvMandatoryFieldsUnmarshallTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|BindySimpleCsvMandatoryFieldsUnmarshallTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result1"
argument_list|)
DECL|field|resultEndpoint1
specifier|protected
name|MockEndpoint
name|resultEndpoint1
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result2"
argument_list|)
DECL|field|resultEndpoint2
specifier|protected
name|MockEndpoint
name|resultEndpoint2
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start1"
argument_list|)
DECL|field|template1
specifier|protected
name|ProducerTemplate
name|template1
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start2"
argument_list|)
DECL|field|template2
specifier|protected
name|ProducerTemplate
name|template2
decl_stmt|;
DECL|field|header
name|String
name|header
init|=
literal|"order nr,client ref,first name, last name,instrument code,instrument name,order type, instrument type, quantity,currency,date\r\n"
decl_stmt|;
DECL|field|record1
name|String
name|record1
init|=
literal|""
decl_stmt|;
comment|// empty records
DECL|field|record2
name|String
name|record2
init|=
literal|",,blabla,,,,,,,,"
decl_stmt|;
comment|// optional fields
DECL|field|record3
name|String
name|record3
init|=
literal|"1,A1,Charles,Moulliard,ISIN,LU123456789,,,,,"
decl_stmt|;
comment|// mandatory fields present (A1, Charles, Moulliard)
DECL|field|record4
name|String
name|record4
init|=
literal|"1,A1,Charles,,ISIN,LU123456789,,,,,"
decl_stmt|;
comment|// mandatory field missing
DECL|field|record5
name|String
name|record5
init|=
literal|",,,,,,,,,,"
decl_stmt|;
comment|// record with no data
DECL|field|record6
name|String
name|record6
init|=
literal|",,,,,,,,,,,,,,"
decl_stmt|;
comment|// too much data in the record (only 11 are accepted by the model
annotation|@
name|DirtiesContext
annotation|@
name|Test
DECL|method|testEmptyRecord ()
specifier|public
name|void
name|testEmptyRecord
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint1
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template1
operator|.
name|sendBody
argument_list|(
name|record1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|Assert
operator|.
name|isInstanceOf
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
comment|// LOG.info(">> Error : " + e);
block|}
name|resultEndpoint1
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|DirtiesContext
annotation|@
name|Test
DECL|method|testEmptyFields ()
specifier|public
name|void
name|testEmptyFields
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint1
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template1
operator|.
name|sendBody
argument_list|(
name|record2
argument_list|)
expr_stmt|;
name|resultEndpoint1
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|DirtiesContext
annotation|@
name|Test
DECL|method|testOneOptionalField ()
specifier|public
name|void
name|testOneOptionalField
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint1
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template1
operator|.
name|sendBody
argument_list|(
name|record2
argument_list|)
expr_stmt|;
name|resultEndpoint1
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|DirtiesContext
annotation|@
name|Test
DECL|method|testSeveralOptionalFields ()
specifier|public
name|void
name|testSeveralOptionalFields
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint1
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template1
operator|.
name|sendBody
argument_list|(
name|record3
argument_list|)
expr_stmt|;
name|resultEndpoint1
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|DirtiesContext
annotation|@
name|Test
DECL|method|testTooMuchFields ()
specifier|public
name|void
name|testTooMuchFields
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint1
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template1
operator|.
name|sendBody
argument_list|(
name|record6
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
comment|// expected
name|Assert
operator|.
name|isInstanceOf
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|resultEndpoint1
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|DirtiesContext
annotation|@
name|Test
DECL|method|testMandatoryFields ()
specifier|public
name|void
name|testMandatoryFields
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint2
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template2
operator|.
name|sendBody
argument_list|(
name|header
operator|+
name|record3
argument_list|)
expr_stmt|;
name|resultEndpoint2
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|DirtiesContext
annotation|@
name|Test
DECL|method|testMissingMandatoryFields ()
specifier|public
name|void
name|testMissingMandatoryFields
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint2
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
try|try
block|{
name|template2
operator|.
name|sendBody
argument_list|(
name|header
operator|+
name|record4
argument_list|)
expr_stmt|;
name|resultEndpoint2
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
comment|// LOG.info(">> Error : " + e);
block|}
block|}
annotation|@
name|Configuration
DECL|class|ContextConfig
specifier|public
specifier|static
class|class
name|ContextConfig
extends|extends
name|SingleRouteCamelConfiguration
block|{
DECL|field|formatOptional
name|BindyCsvDataFormat
name|formatOptional
init|=
operator|new
name|BindyCsvDataFormat
argument_list|(
literal|"org.apache.camel.dataformat.bindy.model.simple.oneclass"
argument_list|)
decl_stmt|;
DECL|field|formatMandatory
name|BindyCsvDataFormat
name|formatMandatory
init|=
operator|new
name|BindyCsvDataFormat
argument_list|(
literal|"org.apache.camel.dataformat.bindy.model.simple.oneclassmandatory"
argument_list|)
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Bean
DECL|method|route ()
specifier|public
name|RouteBuilder
name|route
parameter_list|()
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
block|{
name|from
argument_list|(
literal|"direct:start1"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|formatOptional
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|formatMandatory
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result2"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
block|}
end_class

end_unit

