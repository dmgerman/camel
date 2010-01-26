begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|osgi
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
name|DefaultCamelContext
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
name|osgi
operator|.
name|test
operator|.
name|MyService
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
name|ApplicationContext
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

begin_class
DECL|class|ServiceRegistryTest
specifier|public
class|class
name|ServiceRegistryTest
extends|extends
name|CamelOsgiTestSupport
block|{
annotation|@
name|Test
DECL|method|camelContextFactoryServiceRegistryTest ()
specifier|public
name|void
name|camelContextFactoryServiceRegistryTest
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContextFactory
name|factory
init|=
operator|new
name|CamelContextFactory
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setBundleContext
argument_list|(
name|getBundleContext
argument_list|()
argument_list|)
expr_stmt|;
name|DefaultCamelContext
name|context
init|=
name|factory
operator|.
name|createContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MyService
name|myService
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|MyService
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|MyService
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"MyService should not be null"
argument_list|,
name|myService
argument_list|)
expr_stmt|;
name|Object
name|service
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|MyService
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"MyService should not be null"
argument_list|,
name|myService
argument_list|)
expr_stmt|;
name|service
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByType
argument_list|(
name|MyService
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"MyService should not be null"
argument_list|,
name|myService
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|camelContextFactoryBeanServiceRegistryTest ()
specifier|public
name|void
name|camelContextFactoryBeanServiceRegistryTest
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContextFactoryBean
name|factoryBean
init|=
operator|new
name|CamelContextFactoryBean
argument_list|()
decl_stmt|;
name|factoryBean
operator|.
name|setBundleContext
argument_list|(
name|getBundleContext
argument_list|()
argument_list|)
expr_stmt|;
name|ApplicationContext
name|applicationContext
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/osgi/camelContext.xml"
argument_list|)
decl_stmt|;
name|factoryBean
operator|.
name|setApplicationContext
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
name|DefaultCamelContext
name|context
init|=
name|factoryBean
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MyService
name|myService
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|MyService
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|MyService
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"MyService should not be null"
argument_list|,
name|myService
argument_list|)
expr_stmt|;
name|Object
name|service
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|MyService
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"MyService should not be null"
argument_list|,
name|myService
argument_list|)
expr_stmt|;
name|service
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByType
argument_list|(
name|MyService
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"MyService should not be null"
argument_list|,
name|myService
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

