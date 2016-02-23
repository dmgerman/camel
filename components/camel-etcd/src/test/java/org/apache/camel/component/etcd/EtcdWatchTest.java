begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.etcd
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|etcd
package|;
end_package

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|EtcdClient
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
name|Predicate
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
name|junit
operator|.
name|Ignore
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
annotation|@
name|Ignore
argument_list|(
literal|"Etcd must be started manually"
argument_list|)
DECL|class|EtcdWatchTest
specifier|public
class|class
name|EtcdWatchTest
extends|extends
name|EtcdTest
block|{
annotation|@
name|Test
DECL|method|testWatchWithPath ()
specifier|public
name|void
name|testWatchWithPath
parameter_list|()
throws|throws
name|Exception
block|{
name|testWatch
argument_list|(
literal|"mock:watch-with-path"
argument_list|,
literal|"/myKey1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWatchWithConfigPath ()
specifier|public
name|void
name|testWatchWithConfigPath
parameter_list|()
throws|throws
name|Exception
block|{
name|testWatch
argument_list|(
literal|"mock:watch-with-config-path"
argument_list|,
literal|"/myKey2"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWatchRecursive ()
specifier|public
name|void
name|testWatchRecursive
parameter_list|()
throws|throws
name|Exception
block|{
name|testWatch
argument_list|(
literal|"mock:watch-recursive"
argument_list|,
literal|"/recursive/myKey1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWatchWithTimeout ()
specifier|public
name|void
name|testWatchWithTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:watch-with-timeout"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|EtcdConstants
operator|.
name|ETCD_PATH
argument_list|,
literal|"/timeoutKey"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|EtcdConstants
operator|.
name|ETCD_TIMEOUT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessagesMatches
argument_list|(
operator|new
name|Predicate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|==
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testWatch (String mockEndpoint, final String key, boolean updateKey)
specifier|private
name|void
name|testWatch
parameter_list|(
name|String
name|mockEndpoint
parameter_list|,
specifier|final
name|String
name|key
parameter_list|,
name|boolean
name|updateKey
parameter_list|)
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
name|mockEndpoint
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|EtcdConstants
operator|.
name|ETCD_PATH
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessagesMatches
argument_list|(
operator|new
name|Predicate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|startsWith
argument_list|(
name|key
operator|+
literal|"=myValue-"
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|updateKey
condition|)
block|{
name|EtcdClient
name|client
init|=
name|getClient
argument_list|()
decl_stmt|;
name|client
operator|.
name|put
argument_list|(
name|key
argument_list|,
literal|"myValue-1"
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|250
argument_list|)
expr_stmt|;
name|client
operator|.
name|put
argument_list|(
name|key
argument_list|,
literal|"myValue-2"
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"etcd:watch/myKey1"
argument_list|)
operator|.
name|process
argument_list|(
name|NODE_TO_VALUE_IN
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:watch-with-path"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"etcd:watch/recursive?recursive=true"
argument_list|)
operator|.
name|process
argument_list|(
name|NODE_TO_VALUE_IN
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:org.apache.camel.component.etcd?level=INFO"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:watch-recursive"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"etcd:watch/myKey2"
argument_list|)
operator|.
name|process
argument_list|(
name|NODE_TO_VALUE_IN
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:watch-with-config-path"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"etcd:watch/timeoutKey?timeout=250&sendEmptyExchangeOnTimeout=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:watch-with-timeout"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

