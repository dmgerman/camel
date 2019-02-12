begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|validator
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|SchemaFactory
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
name|ContextTestSupport
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
name|impl
operator|.
name|JndiRegistry
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
DECL|class|CustomSchemaFactoryFeatureTest
specifier|public
class|class
name|CustomSchemaFactoryFeatureTest
extends|extends
name|ContextTestSupport
block|{
comment|// Need to bind the CustomerSchemaFactory
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
name|SchemaFactory
name|mySchemaFactory
init|=
name|SchemaFactory
operator|.
name|newInstance
argument_list|(
name|XMLConstants
operator|.
name|W3C_XML_SCHEMA_NS_URI
argument_list|)
decl_stmt|;
name|mySchemaFactory
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"MySchemaFactory"
argument_list|,
name|mySchemaFactory
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
comment|// just inject the SchemaFactory as we want
annotation|@
name|Test
DECL|method|testCustomSchemaFactory ()
specifier|public
name|void
name|testCustomSchemaFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|ValidatorComponent
name|v
init|=
operator|new
name|ValidatorComponent
argument_list|()
decl_stmt|;
name|v
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|v
operator|.
name|createEndpoint
argument_list|(
literal|"validator:org/apache/camel/component/validator/unsecuredSchema.xsd?schemaFactory=#MySchemaFactory"
argument_list|)
expr_stmt|;
try|try
block|{
name|v
operator|.
name|createEndpoint
argument_list|(
literal|"validator:org/apache/camel/component/validator/unsecuredSchema.xsd"
argument_list|)
expr_stmt|;
comment|// we should get an security exception in JDK 7 with Oracle or Sun JDK
name|String
name|jdkVendor
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.vm.vendor"
argument_list|)
decl_stmt|;
if|if
condition|(
name|jdkVendor
operator|!=
literal|null
operator|&&
operator|(
name|jdkVendor
operator|.
name|indexOf
argument_list|(
literal|"Oracle"
argument_list|)
operator|>
literal|0
operator|||
name|jdkVendor
operator|.
name|indexOf
argument_list|(
literal|"Sun"
argument_list|)
operator|>
literal|0
operator|)
condition|)
block|{
name|fail
argument_list|(
literal|"Expect exception here"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// do nothing here
block|}
block|}
block|}
end_class

end_unit
