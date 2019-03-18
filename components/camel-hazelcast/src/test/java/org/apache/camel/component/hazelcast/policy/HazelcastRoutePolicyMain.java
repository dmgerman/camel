begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.policy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|policy
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
name|main
operator|.
name|Main
import|;
end_import

begin_class
DECL|class|HazelcastRoutePolicyMain
specifier|public
specifier|final
class|class
name|HazelcastRoutePolicyMain
block|{
DECL|method|HazelcastRoutePolicyMain ()
specifier|private
name|HazelcastRoutePolicyMain
parameter_list|()
block|{     }
DECL|method|main (final String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
specifier|final
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|addRouteBuilder
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|HazelcastRoutePolicy
name|policy
init|=
operator|new
name|HazelcastRoutePolicy
argument_list|()
decl_stmt|;
name|policy
operator|.
name|setLockMapName
argument_list|(
literal|"camel:lock:map"
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setLockKey
argument_list|(
literal|"route-policy"
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setLockValue
argument_list|(
name|args
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setTryLockTimeout
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"file:///tmp/camel?DELETE=true"
argument_list|)
operator|.
name|routeId
argument_list|(
name|args
index|[
literal|1
index|]
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"HazelcastRouteID"
argument_list|,
name|constant
argument_list|(
name|args
index|[
literal|1
index|]
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"HazelcastServiceName"
argument_list|,
name|constant
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:org.apache.camel.component.hazelcast?level=INFO&showAll=true"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|main
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

