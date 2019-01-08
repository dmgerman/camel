begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.bigxml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|bigxml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|builder
operator|.
name|NotifyBuilder
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
name|BeforeClass
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stax
operator|.
name|StAXBuilder
operator|.
name|stax
import|;
end_import

begin_class
DECL|class|StaxTokenizerTest
specifier|public
class|class
name|StaxTokenizerTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|BeforeClass
DECL|method|beforeClass ()
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
throws|throws
name|Exception
block|{
name|TestUtils
operator|.
name|buildTestXml
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|ctx
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
return|return
name|ctx
return|;
block|}
annotation|@
name|Override
DECL|method|getShutdownTimeout ()
specifier|protected
name|int
name|getShutdownTimeout
parameter_list|()
block|{
return|return
literal|300
return|;
block|}
annotation|@
name|Test
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
name|TestUtils
operator|.
name|getNumOfRecords
argument_list|()
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|boolean
name|matches
init|=
name|notify
operator|.
name|matches
argument_list|(
name|TestUtils
operator|.
name|getMaxWaitTime
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Processed XML file with {} records"
argument_list|,
name|TestUtils
operator|.
name|getNumOfRecords
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Test completed"
argument_list|,
name|matches
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
name|from
argument_list|(
literal|"file:"
operator|+
name|TestUtils
operator|.
name|getBasePath
argument_list|()
operator|+
literal|"?readLock=changed&noop=true"
argument_list|)
operator|.
name|split
argument_list|(
name|stax
argument_list|(
name|Record
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|stopOnException
argument_list|()
comment|//.log(LoggingLevel.TRACE, "org.apache.camel.example.bigxml", "${body}")
operator|.
name|to
argument_list|(
literal|"log:org.apache.camel.example.bigxml?level=DEBUG&groupInterval=100&groupDelay=100&groupActiveOnly=false"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

