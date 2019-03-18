begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.examples.cluster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|examples
operator|.
name|cluster
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
name|springframework
operator|.
name|context
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
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_class
annotation|@
name|Configuration
DECL|class|ClusterNodeConfiguration
specifier|public
class|class
name|ClusterNodeConfiguration
block|{
annotation|@
name|Bean
DECL|method|routeBuilder ()
specifier|public
name|RouteBuilder
name|routeBuilder
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
throws|throws
name|Exception
block|{
comment|// This route is configured to be local (see application.properties)
comment|// so it will be started regardless of the leadership status if
comment|// this node.
name|from
argument_list|(
literal|"timer:heartbeat?period=10s"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"heartbeat"
argument_list|)
operator|.
name|log
argument_list|(
literal|"HeartBeat route (timer) {{node.id}} ..."
argument_list|)
expr_stmt|;
comment|// This route is configured to be clustered so it will be started
comment|// by the controller only when this node is leader
name|from
argument_list|(
literal|"timer:clustered?period=5s"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"clustered"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Clustered route (timer) {{node.id}} ..."
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

