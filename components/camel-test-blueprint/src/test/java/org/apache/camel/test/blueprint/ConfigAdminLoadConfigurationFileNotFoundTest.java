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
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|ConfigAdminLoadConfigurationFileNotFoundTest
specifier|public
class|class
name|ConfigAdminLoadConfigurationFileNotFoundTest
extends|extends
name|CamelBlueprintTestSupport
block|{
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"org/apache/camel/test/blueprint/configadmin-loadfile.xml"
return|;
block|}
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
try|try
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw IllegalArgumentException, because the config file wasn't found"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"../../src/test/resources/etc/stuff.cfg"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// START SNIPPET: e1
annotation|@
name|Override
DECL|method|loadConfigAdminConfigurationFile ()
specifier|protected
name|String
index|[]
name|loadConfigAdminConfigurationFile
parameter_list|()
block|{
comment|// String[0] = tell Camel the path of the .cfg file to use for OSGi ConfigAdmin in the blueprint XML file
comment|//  this file should exist
comment|// String[1] = tell Camel the persistence-id of the cm:property-placeholder in the blueprint XML file
return|return
operator|new
name|String
index|[]
block|{
literal|"../../src/test/resources/etc/stuff.cfg"
block|,
literal|"stuff"
block|}
return|;
block|}
comment|// END SNIPPET: e1
annotation|@
name|Test
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
comment|// irrelevant
block|}
block|}
end_class

end_unit

