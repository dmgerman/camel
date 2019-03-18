begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|container
operator|.
name|test
operator|.
name|api
operator|.
name|OperateOnDeployment
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
DECL|class|PropertiesLocationTest
specifier|public
class|class
name|PropertiesLocationTest
block|{
annotation|@
name|Deployment
argument_list|(
name|name
operator|=
literal|"single-location"
argument_list|)
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
name|SingleLocation
operator|.
name|class
argument_list|)
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
comment|// TODO: reactivate when ARQ-1255 is fixed
comment|/*     @Deployment(name = "multiple-locations")     public static Archive<?> multipleLocationsDeployment() {         return ShrinkWrap.create(JavaArchive.class)             // Camel CDI             .addPackage(CdiCamelExtension.class.getPackage())             // Test classes             .addClass(MultipleLocations.class)             // Bean archive deployment descriptor             .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");     }     */
annotation|@
name|Test
annotation|@
name|OperateOnDeployment
argument_list|(
literal|"single-location"
argument_list|)
DECL|method|resolvePropertyFromLocation (CamelContext context)
specifier|public
name|void
name|resolvePropertyFromLocation
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
literal|"Property from classpath location does not resolve!"
argument_list|,
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
literal|"{{header.message}}"
argument_list|)
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
literal|"message from file"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*     @Test     @OperateOnDeployment("multiple-locations")     public void resolvePropertyFromLocations(CamelContext context) throws Exception {         assertThat("Property from classpath locations does not resolve!", context.resolvePropertyPlaceholders("{{foo.property}}"), is(equalTo("foo.value")));         assertThat("Property from classpath locations does not resolve!", context.resolvePropertyPlaceholders("{{bar.property}}"), is(equalTo("bar.value")));     }     */
block|}
end_class

begin_class
DECL|class|SingleLocation
class|class
name|SingleLocation
block|{
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
return|return
operator|new
name|PropertiesComponent
argument_list|(
literal|"classpath:placeholder.properties"
argument_list|)
return|;
block|}
block|}
end_class

begin_class
DECL|class|MultipleLocations
class|class
name|MultipleLocations
block|{
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
return|return
operator|new
name|PropertiesComponent
argument_list|(
literal|"classpath:foo.properties"
argument_list|,
literal|"classpath:bar.properties"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

