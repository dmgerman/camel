begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|test
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

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
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|ApplicationScoped
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|NoTypeConversionAvailableException
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
name|ProducerTemplate
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
name|TypeConverter
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
name|cdi
operator|.
name|CdiCamelExtension
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
name|cdi
operator|.
name|Uri
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
name|cdi
operator|.
name|bean
operator|.
name|InjectedTypeConverterRoute
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
name|cdi
operator|.
name|converter
operator|.
name|InjectedTypeConverter
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
name|cdi
operator|.
name|pojo
operator|.
name|TypeConverterInput
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
name|cdi
operator|.
name|pojo
operator|.
name|TypeConverterOutput
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
name|camel
operator|.
name|component
operator|.
name|properties
operator|.
name|PropertiesComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|container
operator|.
name|test
operator|.
name|api
operator|.
name|Deployment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|junit
operator|.
name|Arquillian
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|Archive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|ShrinkWrap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|asset
operator|.
name|EmptyAsset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|spec
operator|.
name|JavaArchive
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
import|import static
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
operator|.
name|assertIsSatisfied
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Arquillian
operator|.
name|class
argument_list|)
DECL|class|InjectedTypeConverterTest
specifier|public
class|class
name|InjectedTypeConverterTest
block|{
annotation|@
name|Deployment
DECL|method|deployment ()
specifier|public
specifier|static
name|Archive
argument_list|<
name|?
argument_list|>
name|deployment
parameter_list|()
block|{
return|return
name|ShrinkWrap
operator|.
name|create
argument_list|(
name|JavaArchive
operator|.
name|class
argument_list|)
comment|// Camel CDI
operator|.
name|addPackage
argument_list|(
name|CdiCamelExtension
operator|.
name|class
operator|.
name|getPackage
argument_list|()
argument_list|)
comment|// Test class
operator|.
name|addClass
argument_list|(
name|InjectedTypeConverterRoute
operator|.
name|class
argument_list|)
comment|// Type converter
operator|.
name|addClass
argument_list|(
name|InjectedTypeConverter
operator|.
name|class
argument_list|)
comment|// No need as Camel CDI automatically registers the type converter bean
comment|//.addAsManifestResource(new StringAsset("org.apache.camel.cdi.se.converter"), ArchivePaths.create("services/org/apache/camel/TypeConverter"))
comment|// Bean archive deployment descriptor
operator|.
name|addAsManifestResource
argument_list|(
name|EmptyAsset
operator|.
name|INSTANCE
argument_list|,
literal|"beans.xml"
argument_list|)
return|;
block|}
annotation|@
name|Produces
annotation|@
name|ApplicationScoped
annotation|@
name|Named
argument_list|(
literal|"properties"
argument_list|)
DECL|method|configuration ()
specifier|private
specifier|static
name|PropertiesComponent
name|configuration
parameter_list|()
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"property1"
argument_list|,
literal|"value 1"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"property2"
argument_list|,
literal|"value 2"
argument_list|)
expr_stmt|;
name|PropertiesComponent
name|component
init|=
operator|new
name|PropertiesComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setInitialProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
return|return
name|component
return|;
block|}
annotation|@
name|Test
DECL|method|sendMessageToInbound (@riR) ProducerTemplate inbound, @Uri(R) MockEndpoint outbound)
specifier|public
name|void
name|sendMessageToInbound
parameter_list|(
annotation|@
name|Uri
argument_list|(
literal|"direct:inbound"
argument_list|)
name|ProducerTemplate
name|inbound
parameter_list|,
annotation|@
name|Uri
argument_list|(
literal|"mock:outbound"
argument_list|)
name|MockEndpoint
name|outbound
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|outbound
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|TypeConverterInput
name|input
init|=
operator|new
name|TypeConverterInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|setProperty
argument_list|(
literal|"property value is [{{property1}}]"
argument_list|)
expr_stmt|;
name|inbound
operator|.
name|sendBody
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertIsSatisfied
argument_list|(
literal|2L
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
name|outbound
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|outbound
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|TypeConverterOutput
operator|.
name|class
argument_list|)
operator|.
name|getProperty
argument_list|()
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
literal|"property value is [value 1]"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|convertWithTypeConverter (TypeConverter converter)
specifier|public
name|void
name|convertWithTypeConverter
parameter_list|(
name|TypeConverter
name|converter
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
block|{
name|TypeConverterInput
name|input
init|=
operator|new
name|TypeConverterInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|setProperty
argument_list|(
literal|"property value is [{{property2}}]"
argument_list|)
expr_stmt|;
name|TypeConverterOutput
name|output
init|=
name|converter
operator|.
name|mandatoryConvertTo
argument_list|(
name|TypeConverterOutput
operator|.
name|class
argument_list|,
name|input
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|output
operator|.
name|getProperty
argument_list|()
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
literal|"property value is [value 2]"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

