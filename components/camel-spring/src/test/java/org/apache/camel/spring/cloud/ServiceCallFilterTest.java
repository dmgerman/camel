begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|cloud
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
name|Processor
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
name|cloud
operator|.
name|ServiceCallConstants
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
name|SpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
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

begin_class
annotation|@
name|DirtiesContext
DECL|class|ServiceCallFilterTest
specifier|public
class|class
name|ServiceCallFilterTest
extends|extends
name|SpringTestSupport
block|{
annotation|@
name|Test
DECL|method|testServiceFilter ()
specifier|public
name|void
name|testServiceFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|result
decl_stmt|;
name|result
operator|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:start"
argument_list|,
name|emptyProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|assertHeader
argument_list|(
name|result
argument_list|,
name|ServiceCallConstants
operator|.
name|SERVICE_HOST
argument_list|,
literal|"host1"
argument_list|)
expr_stmt|;
name|assertHeader
argument_list|(
name|result
argument_list|,
name|ServiceCallConstants
operator|.
name|SERVICE_PORT
argument_list|,
literal|9093
argument_list|)
expr_stmt|;
name|result
operator|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:start"
argument_list|,
name|emptyProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|assertHeader
argument_list|(
name|result
argument_list|,
name|ServiceCallConstants
operator|.
name|SERVICE_HOST
argument_list|,
literal|"host4"
argument_list|)
expr_stmt|;
name|assertHeader
argument_list|(
name|result
argument_list|,
name|ServiceCallConstants
operator|.
name|SERVICE_PORT
argument_list|,
literal|9094
argument_list|)
expr_stmt|;
block|}
comment|// *********************
comment|// Helpers
comment|// *********************
DECL|method|assertHeader (Exchange exchange, String header, Object expectedValue)
specifier|private
name|void
name|assertHeader
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|expectedValue
parameter_list|)
block|{
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|header
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|expectedValue
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|header
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|emptyProcessor ()
specifier|private
name|Processor
name|emptyProcessor
parameter_list|()
block|{
return|return
name|e
lambda|->
block|{
return|return;
block|}
return|;
block|}
comment|// *********************
comment|// Application Context
comment|// *********************
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/cloud/ServiceCallFilterTest.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

