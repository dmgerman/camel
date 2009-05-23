begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
operator|.
name|FileLanguage
operator|.
name|file
import|;
end_import

begin_comment
comment|/**  * File being processed sync vs async to demonstrate the time difference.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|FileConcurrentAggregateBatchConsumerTest
specifier|public
class|class
name|FileConcurrentAggregateBatchConsumerTest
extends|extends
name|FileConcurrentTest
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FileConcurrentAggregateBatchConsumerTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|testProcessFilesConcurrently ()
specifier|public
name|void
name|testProcessFilesConcurrently
parameter_list|()
throws|throws
name|Exception
block|{
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
name|from
argument_list|(
literal|"file://target/concurrent?delay=60000&initialDelay=2500"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"id"
argument_list|,
name|file
argument_list|(
literal|"${file:onlyname.noext}"
argument_list|)
argument_list|)
operator|.
name|async
argument_list|(
literal|20
argument_list|)
operator|.
name|beanRef
argument_list|(
literal|"business"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"country"
argument_list|)
argument_list|,
operator|new
name|MyBusinessTotal
argument_list|()
argument_list|)
operator|.
name|batchConsumer
argument_list|()
operator|.
name|batchTimeout
argument_list|(
literal|60000
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"2000"
argument_list|,
literal|"2500"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|long
name|delta
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Time taken parallel: "
operator|+
name|delta
argument_list|)
expr_stmt|;
block|}
DECL|method|testProcessFilesSequentiel ()
specifier|public
name|void
name|testProcessFilesSequentiel
parameter_list|()
throws|throws
name|Exception
block|{
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
name|from
argument_list|(
literal|"file://target/concurrent?delay=60000&initialDelay=2500"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"id"
argument_list|,
name|file
argument_list|(
literal|"${file:onlyname.noext}"
argument_list|)
argument_list|)
operator|.
name|beanRef
argument_list|(
literal|"business"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"country"
argument_list|)
argument_list|,
operator|new
name|MyBusinessTotal
argument_list|()
argument_list|)
operator|.
name|batchConsumer
argument_list|()
operator|.
name|batchTimeout
argument_list|(
literal|60000
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"2000"
argument_list|,
literal|"2500"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|long
name|delta
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Time taken sequentiel: "
operator|+
name|delta
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

