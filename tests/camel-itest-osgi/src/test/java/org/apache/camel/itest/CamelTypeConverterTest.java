begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|itest
operator|.
name|typeconverter
operator|.
name|MyConverter
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
name|support
operator|.
name|DefaultExchange
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
name|karaf
operator|.
name|AbstractFeatureTest
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
name|karaf
operator|.
name|CamelKarafTestSupport
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
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|ProbeBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|TestProbeBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|PaxExam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|InnerClassStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundle
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
name|Constants
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
DECL|class|CamelTypeConverterTest
specifier|public
class|class
name|CamelTypeConverterTest
extends|extends
name|AbstractFeatureTest
block|{
annotation|@
name|Test
DECL|method|testTypeConverterInSameBundleAsCamelRoute ()
specifier|public
name|void
name|testTypeConverterInSameBundleAsCamelRoute
parameter_list|()
throws|throws
name|Exception
block|{
comment|// install the camel blueprint xml file and the Camel converter we use in this test
name|URL
name|blueprintUrl
init|=
name|ObjectHelper
operator|.
name|loadResourceAsURL
argument_list|(
literal|"org/apache/camel/itest/CamelTypeConverterTest.xml"
argument_list|,
name|CamelTypeConverterTest
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|installBlueprintAsBundle
argument_list|(
literal|"CamelTypeConverterTest"
argument_list|,
name|blueprintUrl
argument_list|,
literal|true
argument_list|,
name|bundle
lambda|->
block|{
comment|// install converter
operator|(
operator|(
name|TinyBundle
operator|)
name|bundle
operator|)
operator|.
name|add
argument_list|(
literal|"META-INF/services/org/apache/camel/TypeConverter"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"org.apache.camel.itest.typeconverter.MyConverter"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|MyConverter
operator|.
name|class
argument_list|,
name|InnerClassStrategy
operator|.
name|NONE
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|DYNAMICIMPORT_PACKAGE
argument_list|,
literal|"*"
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
comment|// lookup Camel from OSGi
name|CamelContext
name|camel
init|=
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Pojo
name|pojo
init|=
operator|new
name|Pojo
argument_list|()
decl_stmt|;
name|String
name|pojoName
init|=
literal|"Constantine"
decl_stmt|;
name|pojo
operator|.
name|setName
argument_list|(
name|pojoName
argument_list|)
expr_stmt|;
specifier|final
name|DefaultExchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camel
argument_list|)
decl_stmt|;
specifier|final
name|String
name|string
init|=
name|camel
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|pojo
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"POJO -> String: {}"
argument_list|,
name|string
argument_list|)
expr_stmt|;
specifier|final
name|Pojo
name|copy
init|=
name|camel
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|Pojo
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|string
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"String -> POJO: {}"
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|pojoName
argument_list|,
name|copy
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Configuration
DECL|method|configure ()
specifier|public
name|Option
index|[]
name|configure
parameter_list|()
block|{
return|return
name|CamelKarafTestSupport
operator|.
name|configure
argument_list|(
literal|"camel-test-karaf"
argument_list|)
return|;
block|}
annotation|@
name|ProbeBuilder
DECL|method|probeConfiguration (TestProbeBuilder probe)
specifier|public
name|TestProbeBuilder
name|probeConfiguration
parameter_list|(
name|TestProbeBuilder
name|probe
parameter_list|)
block|{
comment|// Export Pojo class for TypeConverter bundle
name|probe
operator|.
name|setHeader
argument_list|(
name|Constants
operator|.
name|EXPORT_PACKAGE
argument_list|,
literal|"org.apache.camel.itest"
argument_list|)
expr_stmt|;
return|return
name|probe
return|;
block|}
block|}
end_class

end_unit

