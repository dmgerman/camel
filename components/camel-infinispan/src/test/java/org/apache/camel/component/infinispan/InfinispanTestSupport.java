begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan
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
name|impl
operator|.
name|JndiRegistry
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
name|infinispan
operator|.
name|commons
operator|.
name|api
operator|.
name|BasicCache
import|;
end_import

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
name|commons
operator|.
name|time
operator|.
name|TimeService
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

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|test
operator|.
name|TestingUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|util
operator|.
name|ControlledTimeService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_class
DECL|class|InfinispanTestSupport
specifier|public
class|class
name|InfinispanTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|KEY_ONE
specifier|protected
specifier|static
specifier|final
name|String
name|KEY_ONE
init|=
literal|"keyOne"
decl_stmt|;
DECL|field|VALUE_ONE
specifier|protected
specifier|static
specifier|final
name|String
name|VALUE_ONE
init|=
literal|"valueOne"
decl_stmt|;
DECL|field|KEY_TWO
specifier|protected
specifier|static
specifier|final
name|String
name|KEY_TWO
init|=
literal|"keyTwo"
decl_stmt|;
DECL|field|VALUE_TWO
specifier|protected
specifier|static
specifier|final
name|String
name|VALUE_TWO
init|=
literal|"valueTwo"
decl_stmt|;
DECL|field|basicCacheContainer
specifier|protected
name|BasicCacheContainer
name|basicCacheContainer
decl_stmt|;
DECL|field|ts
specifier|protected
name|ControlledTimeService
name|ts
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|basicCacheContainer
operator|=
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
expr_stmt|;
name|basicCacheContainer
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|basicCacheContainer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"cacheContainer"
argument_list|,
name|basicCacheContainer
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
DECL|method|currentCache ()
specifier|protected
name|BasicCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|currentCache
parameter_list|()
block|{
return|return
name|basicCacheContainer
operator|.
name|getCache
argument_list|()
return|;
block|}
DECL|method|namedCache (String name)
specifier|protected
name|BasicCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|namedCache
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|basicCacheContainer
operator|.
name|getCache
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|injectTimeService ()
specifier|protected
name|void
name|injectTimeService
parameter_list|()
block|{
name|ts
operator|=
operator|new
name|ControlledTimeService
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|TestingUtil
operator|.
name|replaceComponent
argument_list|(
operator|(
name|DefaultCacheManager
operator|)
name|basicCacheContainer
argument_list|,
name|TimeService
operator|.
name|class
argument_list|,
name|ts
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

