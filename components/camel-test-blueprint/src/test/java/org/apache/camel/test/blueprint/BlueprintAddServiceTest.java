begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|util
operator|.
name|KeyValueHolder
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
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_class
DECL|class|BlueprintAddServiceTest
specifier|public
class|class
name|BlueprintAddServiceTest
extends|extends
name|CamelBlueprintTestSupport
block|{
DECL|field|myService
specifier|private
name|MyService
name|myService
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"org/apache/camel/test/blueprint/BlueprintAddServiceTest.xml"
return|;
block|}
annotation|@
name|Override
DECL|method|addServicesOnStartup (Map<String, KeyValueHolder<Object, Dictionary>> services)
specifier|protected
name|void
name|addServicesOnStartup
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|KeyValueHolder
argument_list|<
name|Object
argument_list|,
name|Dictionary
argument_list|>
argument_list|>
name|services
parameter_list|)
block|{
name|services
operator|.
name|put
argument_list|(
literal|"myService"
argument_list|,
name|asService
argument_list|(
name|myService
argument_list|,
literal|"beer"
argument_list|,
literal|"Carlsberg"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAddService ()
specifier|public
name|void
name|testAddService
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"CamelCamel"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Camel"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|ref
init|=
name|getBundleContext
argument_list|()
operator|.
name|getServiceReference
argument_list|(
literal|"myService"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Carlsberg"
argument_list|,
name|ref
operator|.
name|getProperty
argument_list|(
literal|"beer"
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|service
init|=
name|getBundleContext
argument_list|()
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|myService
argument_list|,
name|service
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

