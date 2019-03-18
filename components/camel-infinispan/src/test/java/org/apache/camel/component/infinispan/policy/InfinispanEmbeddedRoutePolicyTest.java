begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan.policy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
operator|.
name|policy
package|;
end_package

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|commons
operator|.
name|api
operator|.
name|BasicCacheContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|configuration
operator|.
name|cache
operator|.
name|ConfigurationBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|manager
operator|.
name|DefaultCacheManager
import|;
end_import

begin_class
DECL|class|InfinispanEmbeddedRoutePolicyTest
specifier|public
class|class
name|InfinispanEmbeddedRoutePolicyTest
extends|extends
name|InfinispanRoutePolicyTestBase
block|{
annotation|@
name|Override
DECL|method|createCacheManager ()
specifier|protected
name|BasicCacheContainer
name|createCacheManager
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DefaultCacheManager
argument_list|(
operator|new
name|ConfigurationBuilder
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

