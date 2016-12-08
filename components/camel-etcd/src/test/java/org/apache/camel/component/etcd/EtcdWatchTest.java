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
name|mousio
operator|.
name|etcd4j
operator|.
name|responses
operator|.
name|EtcdErrorCode
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|responses
operator|.
name|EtcdException
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
name|Test
import|;
end_import

begin_class
DECL|class|EtcdWatchTest
specifier|public
class|class
name|EtcdWatchTest
extends|extends
name|EtcdTestSupport
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
literal|10
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
literal|10
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
literal|10
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWatchRecovery ()
specifier|public
name|void
name|testWatchRecovery
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
literal|"/myKeyRecovery"
decl_stmt|;
specifier|final
name|EtcdClient
name|client
init|=
name|getClient
argument_list|()
decl_stmt|;
try|try
block|{
comment|// Delete the key if present
name|client
operator|.
name|delete
argument_list|(
name|key
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EtcdException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|e
operator|.
name|isErrorCode
argument_list|(
name|EtcdErrorCode
operator|.
name|KeyNotFound
argument_list|)
condition|)
block|{
throw|throw
name|e
throw|;
block|}
block|}
comment|// Fill the vent backlog (> 1000)
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|2000
condition|;
name|i
operator|++
control|)
block|{
name|client
operator|.
name|put
argument_list|(
name|key
argument_list|,
literal|"v"
operator|+
name|i
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
name|context
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"watchRecovery"
argument_list|)
expr_stmt|;
name|testWatch
argument_list|(
literal|"mock:watch-recovery"
argument_list|,
name|key
argument_list|,
literal|10
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
name|ETCD_NAMESPACE
argument_list|,
name|EtcdNamespace
operator|.
name|watch
operator|.
name|name
argument_list|()
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
name|allMessages
argument_list|()
operator|.
name|body
argument_list|()
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testWatch (String mockEndpoint, final String key, int updates)
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
name|int
name|updates
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|String
index|[]
name|values
init|=
operator|new
name|String
index|[
name|updates
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|updates
condition|;
name|i
operator|++
control|)
block|{
name|values
index|[
name|i
index|]
operator|=
name|key
operator|+
literal|"=myValue-"
operator|+
name|i
expr_stmt|;
block|}
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
name|ETCD_NAMESPACE
argument_list|,
name|EtcdNamespace
operator|.
name|watch
operator|.
name|name
argument_list|()
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
name|expectedBodiesReceived
argument_list|(
name|values
argument_list|)
expr_stmt|;
specifier|final
name|EtcdClient
name|client
init|=
name|getClient
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|updates
condition|;
name|i
operator|++
control|)
block|{
name|client
operator|.
name|put
argument_list|(
name|key
argument_list|,
literal|"myValue-"
operator|+
name|i
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
name|fromF
argument_list|(
literal|"etcd:watch/myKeyRecovery?timeout=%s&fromIndex=%s"
argument_list|,
literal|1000
operator|*
literal|60
operator|*
literal|5
argument_list|,
literal|1
argument_list|)
operator|.
name|id
argument_list|(
literal|"watchRecovery"
argument_list|)
operator|.
name|autoStartup
argument_list|(
literal|false
argument_list|)
operator|.
name|process
argument_list|(
name|NODE_TO_VALUE_IN
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:watch-recovery"
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

